#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include "strbuf.h"
#include "lib/contracts.h"
#include "lib/xalloc.h"

/*
 * String Buffer Library
 *
 * 15-122 Principles of Imperative Computation
 * This version exposes the externals, requires
 * discipline from client! */

/*** Interface ***/
//struct strbuf {
//  size_t limit;   /* limit > 0, bytes allocated for buf */
//  size_t len;     /* len < limit */
//  char *buf;      /* buf != NULL, buf[len] == '\0', strlen(buf) == len */
//};
bool is_strbuf(struct strbuf *sb);

struct strbuf *strbuf_new(size_t init_limit);
char *strbuf_dealloc(struct strbuf *sb);
char *strbuf_str(struct strbuf *sb);

void strbuf_add(struct strbuf *sb, char *str, size_t len);
void strbuf_addstr(struct strbuf *sb, char *str);

/*** Implementation ***/
bool is_strbuf(struct strbuf* sb)
{
  // if sb points to null, not valid
  if (sb == NULL){return false;}
  if (sb->limit < 1){return false;}
  if (sb->len+1> sb->limit){return false;}
  if (sb->buf == NULL){return false;}
  if (sb->buf [ sb->len ] != '\0'){return false;}
  // go inside the buf and check
  for (size_t i = 0; i < sb->len; i++){
    if (sb->buf [ i ] == '\0'){
      if (i != sb->len) {return false;}
    }
  }
  //still have to check length(sb->buf) == sb->limit
  if (strlen(sb->buf)!=sb->len){return false;}
  return true;
}

struct strbuf* strbuf_new(size_t initial_limit)
{
  REQUIRES(initial_limit >0);
  struct strbuf* sb = xcalloc(1,sizeof(struct strbuf));
  sb->buf = xcalloc(initial_limit,sizeof(char));
  sb->buf[0] = '\0';
  sb->len = 0;
  sb->limit = initial_limit;
  ENSURES(is_strbuf(sb));
  return sb;
}

char* strbuf_str(struct strbuf* sb)
{
  REQUIRES (is_strbuf(sb));
  char* new = xcalloc(sb->len+1,sizeof(char));
  new = strcpy(new,sb->buf);
  ENSURES (is_strbuf(sb));
  ENSURES (strlen(new) == strlen(sb->buf));
  ENSURES (new[sb->len] == '\0');
  return new;
}

void strbuf_add(struct strbuf* sb, char* str, size_t str_len)
{
  REQUIRES (is_strbuf(sb));
  REQUIRES (str != NULL);
  REQUIRES (strlen(str) == str_len);
  //total length is the string length add up
  size_t t_len = sb->len + str_len;
  //with the new string, if we need more space, make space
  //sb->limite -1 because the last one must be nul, so one space is taken
  if (t_len > sb->limit-1){
    sb->limit = 2*(t_len+1);//enlarge our new sb->limit
    char* new_buf = xcalloc(sb->limit,sizeof(char));
    //then move all the char in sb->buf and str into new_buf
    for (size_t i = 0;i < sb->len;i++){
        new_buf[i] = sb->buf[i];
    }
    //free the memory used, and assign the pointer to our new buf
    free(sb->buf);
    sb->buf = new_buf;
  }
  //if the str can be added, just add it
  for (size_t i = sb->len; i < t_len; i++){
    sb->buf[i] = str[i-sb->len];
  }
  sb->len = t_len;
  sb->buf[sb->len] = '\0';
  ENSURES(is_strbuf(sb));
}

void strbuf_addstr(struct strbuf* sb, char* str)
{
  REQUIRES(is_strbuf(sb));
  REQUIRES(str != NULL);
  size_t i = strlen(str);
  strbuf_add(sb,str,i);
  ENSURES(is_strbuf(sb));
}

char* strbuf_dealloc(struct strbuf *sb){
  REQUIRES(is_strbuf (sb));
  char* new = sb->buf;
  free(sb);
  return new;
}
