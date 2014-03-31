#include "lib/boardutil.h"
#include "lib/contracts.h"
#include "lib/ht.h"
#include "lib/pq.h"
#include "lib/queues.h"
#include "lib/stacks.h"
#include "lib/xalloc.h"

struct board
{
  bitarray c_b;
  queue moves;
};
typedef struct board board;

struct one_move
{
  uint8_t row;
  uint8_t col;
};
typedef struct one_move one_move;

ht_key elem_key(ht_elem e){
  REQUIRES (e != NULL);
  return e;
}

bool key_equal(ht_key k1, ht_key k2){
  REQUIRES (k1 != NULL);
  REQUIRES (k2 != NULL);
  board* k3 = (board*) k1;
  board* k4 = (board*) k2;
  return (k3->c_b == k4->c_b);
}

size_t key_hash(ht_key k){
  board* m = (board*) k;
  size_t b = (size_t) m->c_b;
  return b;
}

void board_free(board* b){
  while(!queue_empty(b->moves)){
    one_move* m =deq(b->moves);
    if( m!= NULL){
      free(m);
    }
  }
  if (b->moves != NULL){
    queue_free(b->moves,NULL);
    }
  free(b);
}

void elem_free(ht_elem e){
  board_free(e);
}


void free_all(queue possible_moves, queue pb, ht boards)
{
  while (!queue_empty(possible_moves)){
    one_move* m = deq(possible_moves);
    free(m);
  }
  queue_free(possible_moves,NULL);
  queue_free(pb,NULL);
  ht_free(boards);
}

void get_possible_moves(queue* possible_moves,uint8_t width, uint8_t height){
  for (uint8_t i = 0; i < width; i++){
    for (uint8_t j = 0; j < height; j ++){
      one_move* new = xmalloc(sizeof(one_move));
      new->row = j;
      new->col = i;
      enq(*possible_moves,new);
    }
  }
}

void queue_copy(queue a, queue original){
  queue ha = queue_new();
  while(!queue_empty(original)){
    enq(ha,deq(original));
  }
  while(!queue_empty(ha)){
    one_move* b = deq(ha);
    one_move* c = xmalloc(sizeof(one_move));
    *c = *b;
    enq(a,c);
    enq(original,b);
  }
  queue_free(ha,NULL);
}

board* new_board(board* old_board,
        one_move* move,
        uint8_t width,
        uint8_t height)
{
  uint8_t index = get_index(move->row,move->col, width, height);
  int up = -1 ; int left = -1 ;
  int right= -1 ; int down= -1 ;
  board* b = xmalloc(sizeof(board));
  bitarray new = bitarray_new();
  // has an up, not on the top row
  if (index >= width){ up = index - width;}
  // has a left, not at the leftmost column
  if (index % width != 0){left = index -1;}
  // has a right, not at the rightmost column
  if ((index+1) % width != 0){right = index + 1;}
  // has a down, not at the bottom column
  if (index + width < width*height){down = index + width;}
  new = bitarray_flip(old_board->c_b,index);
  if (up >= 0){new = bitarray_flip(new,up);}
  if (left >= 0){new = bitarray_flip(new, left);}
  if (right >= 0){new = bitarray_flip(new,right);}
  if (down >= 0){new = bitarray_flip(new, down);}
  b->c_b = new;
  b->moves = queue_new();
  queue_copy(b->moves,old_board->moves);
  return b;
}

bool solve(queue pb, ht boards_pointer,
           queue pmp,uint8_t width,uint8_t height)
{
  while (!(queue_empty(pb))){
    board* old_board = deq(pb);
    if (queue_empty(pmp)){get_possible_moves(&pmp,width,height);}
    while (!queue_empty(pmp)){
      one_move* move = deq(pmp);
      board* current_board = new_board(old_board, move, width, height);
      if (ht_lookup(boards_pointer, elem_key(current_board)) == NULL){
        enq(current_board->moves,move);
        enq(pb,current_board);
        ht_insert(boards_pointer,current_board);
        if (current_board->c_b == bitarray_new()){
          return true;
        }
      }
      else{
        board_free(current_board);
        free(move);
      }
    }
  }
  return !queue_empty(pb);
}

void print_sol(board* b){
  queue ha = queue_new();
  while(!(queue_empty(b->moves))){
    enq(ha,deq(b->moves));
  }
  while(!(queue_empty(ha))){
    one_move* m = deq(ha);
    fprintf(stdout,"%d:%d \n",m->row,m->col);
    free(m);
  }
  queue_free(ha,NULL);
}


int main(int argc, char **argv)
{
  if (argc != 2){
    fprintf(stderr, "usage: lightsout <board name> \n");
    return 1;
  }
  char* board_filename = argv[1];
  bitarray array = bitarray_new();
  uint8_t width = 0;
  uint8_t height= 0;
  if (!(file_read(board_filename,&array,&width,&height))){
    fprintf(stderr, "cannot read file yo\n");
    return 1;
  }
  /* hash tables contains pointers to struct of board
   * queue contains pointers to stuct of one_move
   */
  if (array == bitarray_new()){return 0;}
  ht boards = ht_new (BITARRAY_LIMIT,elem_key,key_equal,key_hash,
                      elem_free);
  queue possible_moves = queue_new();
  queue pb = queue_new();
  board* n_b = xmalloc(sizeof(board));
  n_b -> c_b = array;
  n_b -> moves = queue_new();
  enq(pb,n_b);
  ht_insert(boards,n_b);
  get_possible_moves(&possible_moves, width, height);
  if(solve(pb, boards, possible_moves,width,height)){
    board* solution = deq(pb);
    while (!queue_empty(pb)){
      solution = deq(pb);
    }
    print_sol(solution);
    free_all(possible_moves,pb,boards);
    return 0;
  }
  else {
    free_all(possible_moves,pb,boards);
    return 1;
  }
}
