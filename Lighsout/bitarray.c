// bitarray.c
#include <stdbool.h>
#include <stdint.h>
#include "lib/bitarray.h"
#include "lib/contracts.h"

bitarray bitarray_new(){
	birarray new = 0;
	return new;
}

bool bitarray_get(bitarray n, uint8_t i){
	ASSERT ( 0<= i && i<= BITARRAY_LIMIT);
	n = n >> i;
	n = n&0x1;
	return n == 0x1;
}

bitarray bitarray_flip(bitarray n, uint8_t i){
	ASSERT ( 0<= i && i<= BITARRAY_LIMIT);
	bitarray b = bitarray_new();
	uint8_t shift = BITARRAY_LIMIT - i;
	if (bitarray_get(n,i)) {b = n>>shift; b| 0x1; b<<shift;}
	else if (! bitarray_get(n,i)) {b = b>>shift; b|0x0; b<<shift;}
	return n&b;
}