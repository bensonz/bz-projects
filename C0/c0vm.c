#include <assert.h>
#include <stdio.h>
#include <limits.h>
#include <stdlib.h>

#include "lib/xalloc.h"
#include "lib/contracts.h"
#include "lib/stacks.h"
#include "lib/c0vm.h"
#include "lib/c0vm_c0ffi.h"
#include "lib/c0vm_abort.h"

/* call stack frames */
typedef struct frame * frame;
struct frame {
  c0_value *V; /* local variables */
  stack S;     /* operand stack */
  ubyte *P;    /* function body */
  size_t pc;   /* return address */
};

/* TODO: implement execute function */
int execute(struct bc0_file *bc0) {

  /* Variables used for bytecode interpreter. You will need to initialize
     these appropriately. */

  /* callStack to hold frames when functions are called */
  stack callStack = stack_new();
  (void) callStack;
  /* initial program is the "main" function, function 0 (which must exist) */
  struct function_info *main_fn = bc0->function_pool;
  struct native_info *main_native = bc0->native_pool;
  /* array to hold local variables for function */
  c0_value *V = xcalloc(main_fn -> num_vars, sizeof(c0_value));
  (void) V;
  /* stack for operands for computations */
  stack S = stack_new();
  /* array of (unsigned) bytes that make up the program */
  ubyte *P = main_fn -> code;
  /* program counter that holds "address" of next bytecode to interpret from
     program P */
  size_t pc = 0;

  while (true) {

#ifdef DEBUG
    fprintf(stderr,
            "Opcode %x -- Operand stack size: %zu -- Program counter: %zu\n",
            P[pc], stack_size(S), pc);
#endif

    switch (P[pc]) {

      /* GENERAL INSTRUCTIONS: Implement the following cases for each of the
         possible bytecodes.  Read the instructions in the assignment to see
         how far you should go before you test your code at each stage.  Do
         not try to write all of the code below at once!  Remember to update
         the program counter (pc) for each case, depending on the number of
         bytes needed for each bytecode operation.  See PROG_HINTS.txt for
         a few more hints.

         IMPORTANT NOTE: For each case, the case should end with a break
         statement to prevent the execution from continuing on into the
         next case.  See the POP case for an example.  To introduce new
         local variables in a case, use curly braces to create a new block.
         See the DUP case for an example.

         See C_IDIOMS.txt for further information on idioms you may find
         useful.
      */

    /* Additional stack operation: */

    case POP: {
      pc++;
      pop(S);
      break;
    }

    case DUP: {
      pc++;
      c0_value v = pop(S);
      push(S,v);
      push(S,v);
      break;
    }

    case SWAP: {
      pc++;
      c0_value v1 = pop(S);
      c0_value v2 = pop(S);
      push(S,v1);
      push(S,v2);
      break;
    }

    /* Returning from a function */

    case RETURN:{
      if (stack_empty(callStack)){
        c0_value v = pop(S);
        stack_free(S,NULL);
        stack_free(callStack,NULL);
        free(V);
        int32_t value = INT(v);
        return value;
      }
      else{
        c0_value v = pop(S);
        frame nextFunc = pop(callStack);
        stack_free(S,NULL);
        S = nextFunc->S;
        free(V);
        V = nextFunc->V;
        P = nextFunc->P;
        pc = nextFunc->pc;
        free(nextFunc);
        push(S,v);
      }
      break;
    }

    /* Arithmetic and Logical operations */

    case IADD:{
      pc++;
      uint32_t x = INT(pop(S));
      uint32_t y = INT(pop(S));
      c0_value t = VAL((int32_t)(x+y));
      push(S,t);
      break;
    }

    case ISUB:{
      pc++;
      uint32_t x = INT(pop(S));
      uint32_t y = INT(pop(S));
      c0_value t = VAL((int32_t)(y-x));
      push(S,t);
      break;
    }

    case IMUL:{
      pc++;
      uint32_t x = INT(pop(S));
      uint32_t y = INT(pop(S));
      c0_value t = VAL((int32_t)(y*x));
      push(S,t);
      break;
    }

    case IDIV:{
      pc++;
      int32_t x = INT(pop(S));
      int32_t y = INT(pop(S));
      if (x == 0) {
        c0_arith_error("Error: Division by zero");
      }
      if (y == -2147483648 && x == -1) {
        c0_arith_error("Error: Overflow");
      }
      c0_value t = VAL((int32_t)(y / x));
      push(S,t);
      break;
    }

    case IREM:{
      pc++;
      int32_t x = INT(pop(S));
      int32_t y = INT(pop(S));
      if (x == 0) {
        c0_arith_error("Error: Division by zero");
      }
      if (y == -2147483648 && x == -1) {
        c0_arith_error("Error: Overflow");
      }
      c0_value t = VAL(y % x);
      push(S,t);
      break;
    }

    case IAND:{
      pc++;
      uint32_t x = INT(pop(S));
      uint32_t y = INT(pop(S));
      c0_value t = VAL((int32_t)(x & y));
      push(S,t);
      break;
    }

    case IOR:{
      pc++;
      uint32_t x = INT(pop(S));
      uint32_t y = INT(pop(S));
      c0_value t = VAL((int32_t)(x | y));
      push(S,t);
      break;
    }

    case IXOR:{
      pc++;
      uint32_t x = INT(pop(S));
      uint32_t y = INT(pop(S));
      c0_value t = VAL((int32_t)(x ^ y));
      push(S,t);
      break;
    }

    case ISHL:{
      pc++;
      int32_t x = INT(pop(S));
      int32_t y = INT(pop(S));
      if (x < 0) {
        c0_arith_error("Error: y < 0");
      }
      if (x > 31) {
        c0_arith_error("Error: Divison by zero");
      }
      c0_value shl = VAL(y << x);
      push(S,shl);
      break;
    }

    case ISHR:{
      pc++;
      int32_t x = INT(pop(S));
      int32_t y = INT(pop(S));
      if (x < 0) {
        c0_arith_error("Error: y < 0");
      }
      if (x > 31) {
        c0_arith_error("Error: Divison by zero");
      }
      if (y < 0) {
        assert((y>> x) < 0);
      }
      c0_value shr = VAL(y >> x);
      push(S,shr);
      break;
    }


    /* Pushing small constants */

    case BIPUSH:{
      byte c = P[pc+1];
      push(S,VAL((int32_t) c));
      pc = pc+2;
      break;
    }

    /* Operations on local variables */

    case VLOAD:{
      ubyte loc = P[pc+1];
      c0_value var = V[loc];
      push(S,var);
      pc = pc+2;
      break;
    }

    case VSTORE:{
      ubyte loc = P[pc+1];
      c0_value var = pop(S);
      V[(int32_t)loc] = var;
      pc = pc+2;
      break;
    }

    case ACONST_NULL:{
      pc = pc+1;
      c0_value k = NULL;
      push(S,k);
      break;
    }

    case ILDC:{
      uint16_t c1 = (uint16_t) P[pc+1];
      uint16_t c2 = (uint16_t) P[pc+2];
      pc += 3;
      int val = bc0->int_pool[(c1<<8)|c2];
      push(S,VAL(val));
      break;
    }

    case ALDC:{
      uint16_t c1 = (uint16_t) P[pc+1];
      uint16_t c2 = (uint16_t) P[pc+2];
      pc = pc+3;
      c0_value val = (c0_value)(&bc0->string_pool[(c1<<8)|c2]);
      push(S,val);
      break;
    }


    /* Control flow operations */

    case NOP:{
      pc++;
      break;
    }

    case IF_CMPEQ:{
      uint16_t o1 = (uint16_t)P[pc+1];
      uint16_t o2 = (uint16_t)P[pc+2];
      c0_value v1 = pop(S);
      c0_value v2 = pop(S);
      if (v1 == v2){
        pc = pc+(int16_t)(o1<<8|o2);
      }
      else{
        pc +=3;
      }
      break;
    }

    case IF_CMPNE:{
      uint16_t o1 = (uint16_t)P[pc+1];
      uint16_t o2 = (uint16_t)P[pc+2];
      c0_value v1 = pop(S);
      c0_value v2 = pop(S);
      if (v1 != v2){
        pc = pc+(int16_t)(o1<<8|o2);
      }
      else{
        pc +=3;
      }
      break;
    }

    case IF_ICMPLT:{
      uint16_t o1 = (uint16_t)P[pc+1];
      uint16_t o2 = (uint16_t)P[pc+2];
      int32_t x = INT(pop(S));
      int32_t y = INT(pop(S));
      if (y < x){
        pc = pc+(int16_t)(o1<<8|o2);
      }
      else{
        pc +=3;
      }
      break;
    }

    case IF_ICMPGE:{
      uint16_t o1 = (uint16_t)P[pc+1];
      uint16_t o2 = (uint16_t)P[pc+2];
      c0_value v1 = pop(S);
      c0_value v2 = pop(S);
      if (INT(v2) >= INT(v1)){
        pc = pc+(int16_t)(o1<<8|o2);
      }
      else{
        pc +=3;
      }
      break;
    }

    case IF_ICMPGT:{
      uint16_t o1 = (uint16_t)P[pc+1];
      uint16_t o2 = (uint16_t)P[pc+2];
      c0_value v1 = pop(S);
      c0_value v2 = pop(S);
      if (INT(v2) > INT(v1)){
        pc = pc+(int16_t)(o1<<8|o2);
      }
      else{
        pc +=3;
      }
      break;
    }

    case IF_ICMPLE:{
      uint16_t o1 = (uint16_t)P[pc+1];
      uint16_t o2 = (uint16_t)P[pc+2];
      int32_t v1 = INT(pop(S));
      int32_t v2 = INT(pop(S));
      if (v2 <= v1){
        pc = pc+(int16_t)(o1<<8|o2);
      }
      else{
        pc +=3;
      }
      break;
    }

    case GOTO:{
      uint16_t o1 = (uint16_t)P[pc+1];
      uint16_t o2 = (uint16_t)P[pc+2];
      pc = pc+(int16_t)(o1<<8|o2);
      break;
    }

    case ATHROW:{
      pc++;
      c0_value a = pop(S);
      c0_user_error(a);
      break;
    }

    case ASSERT:{
      pc++;
      c0_value a = pop(S);
      int32_t x = INT(pop(S));
      if (x == 0){
        c0_assertion_failure(a);
      }
      break;
    }

    /* Function call operations: */

    case INVOKESTATIC:{
      uint16_t c1 = (uint16_t)P[pc+1];
      uint16_t c2 = (uint16_t)P[pc+2];
      frame st = xmalloc(sizeof(struct frame));
      st->V = V;
      st->S = S;
      st->P = P;
      st->pc = pc+3;
      push(callStack,st);
      struct function_info fi = main_fn[(c1<<8)|c2];
      uint16_t args = fi.num_args;
      uint16_t vars = fi.num_vars;
      c0_value* newV = xcalloc(vars,sizeof(c0_value));
      for (int i = args-1; i >= 0;i--){
        newV[i] = pop(S);
      }
      S = stack_new();
      V = newV;
      P = fi.code;
      pc = 0;
      break;
    }

    case INVOKENATIVE:{
      uint16_t c1 = (uint16_t)P[pc+1];
      uint16_t c2 = (uint16_t)P[pc+2];
      struct native_info IDK = main_native[(c1<<8)|c2];
      uint16_t index = IDK.function_table_index;
      c0_value (*g)(c0_value*) = native_function_table[index];
      uint32_t args = IDK.num_args;
      c0_value* newV = xcalloc(args,sizeof(c0_value));
      for(int i = IDK.num_args-1;i>=0;i--){
        newV[i] = pop(S);
      }
      pc+=3;
      push(S,(*g)(newV));
      free(newV);
      break;
    }

    /* Memory allocation operations: */

    case NEW:{
      ubyte s = P[pc+1];
      c0_value a = xcalloc(1,s);
      push(S,a);
      pc+=2;
      break;
    }

    case NEWARRAY:{
      int32_t n = INT(pop(S));
      if (n<0){c0_memory_error("Error: array too small HINT: >0");}
      ubyte s = P[pc+1];
      struct c0_array_header *yolo = xcalloc(1,sizeof(struct c0_array_header));
      yolo->elems = xcalloc(n,s);
      yolo->count = n;
      yolo->elt_size = s;
      push(S,(c0_value)yolo);
      pc+=2;
      break;
    }

    case ARRAYLENGTH:{
      c0_array my_array = pop(S);
      if (my_array == NULL){c0_memory_error("SHITTT");}
      int32_t size = my_array ->count;
      push(S,VAL(size));
      pc++;
      break;
    }


    /* Memory access operations: */

    case AADDF:{
      c0_value address = pop(S);
      if(address == NULL){c0_memory_error("INVALID ADDRESS");}
      ubyte f = P[pc+1];
      char* newADD = (char*)address + f;
      push(S,newADD);
      pc+=2;
      break;
    }

    case AADDS:{
      int32_t i = INT(pop(S));
      c0_array array = (c0_array)pop(S);
      if (array == NULL){c0_memory_error("LOL MEMORY Problem");}
      if (!(0<=i && i< array->count)){c0_memory_error("PROBLEMS");}
      char* k = (char*)array->elems + array->elt_size*i;
      push(S,k);
      pc++;
      break;
    }

    case IMLOAD:{
      c0_value address = pop(S);
      if (address == NULL){c0_memory_error("IMLOAD PROBLEM");}
      int32_t* x = (int32_t*) address;
      push(S,VAL(*x));
      pc++;
      break;
    }

    case IMSTORE:{
      int32_t x = INT(pop(S));
      c0_value address = pop(S);
      if (address == NULL){c0_memory_error("IMSTORE PROBLEM");}
      *(int32_t*) address = x;
      pc++;
      break;
    }

    case AMLOAD:{
      c0_value address = pop(S);
      if (address == NULL){c0_memory_error("AMLOAD PROBLEM");}
      c0_value b = *(c0_value*) address;
      push(S,b);
      pc++;
      break;
    }

    case AMSTORE:{
      c0_value b = pop(S);
      c0_value address = pop(S);
      if(address == NULL){c0_memory_error("AMSTORE PROBLEM");}
      *(c0_value*)address = b;
      pc++;
      break;
    }

    case CMLOAD:{
      c0_value a = pop(S);
      if (a == NULL){
        c0_memory_error("CMLOAD PROBLEM");
      }
      int32_t x = (int32_t)(*(char*) a);
      push(S,VAL(x));
      pc++;
      break;
    }

    case CMSTORE:{
      int32_t x = INT(pop(S));
      c0_value a = pop(S);
      if (a == NULL){c0_memory_error("CMSTORE Problem");}
      *(char*)a = x & 0x7f;
      pc++;
      break;
    }

    default:
      fprintf(stderr, "invalid opcode: 0x%02x\n", P[pc]);
      abort();
    }
  }

  /* cannot get here from infinite loop */
  assert(false);
}

