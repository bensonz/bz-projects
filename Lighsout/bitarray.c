#include "lib/bitarray.h"
#include "lib/contracts.h"

bitarray bitarray_new(){
  return 0;
}

bool bitarray_get(bitarray n, uint8_t i)
{
  REQUIRES (i< BITARRAY_LIMIT);
  bitarray k = n >> i;
  return k&1;
}

bitarray bitarray_flip(bitarray n, uint8_t i)
{
  REQUIRES (i< BITARRAY_LIMIT);
  bitarray shift = 1 << i;
  return n ^ shift;
}

