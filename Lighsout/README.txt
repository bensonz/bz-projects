Lights Out

CODE UPDATE 1: Fixed incorrect makefile line for bitarray-test
               Fixed overly strict preconditions on pq/stack/queue free.
               Fixed multiple errors in is_valid_boardsize
               Add "make clean" target to Makefile
CODE UPDATE 2: Try to address some integer casting issues in boardutil.c
               Add #include "bitarray.h" to boardutil.h
CODE UPDATE 3: Add _BOARDUTIL_H_ header guards to boardutil.h

==========================================================

Compiling your bitarray unit tests
   % make bitarray-test
   % ./bitarray-test

Compiling and running your lights out solver (with -DDEBUG)
   % make
   % ./lightsout-d board/board0.txt

Compiling and running your lights out solver (without -DDEBUG)
   % make
   % ./lightsout board/board0.txt 

==========================================================

