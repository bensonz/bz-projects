C0VM

UPDATE 1: Previous code had both -ftrapv and -fwrapv. Fixed to only
   include -fwrapv.

Useful information:
   C_IDOMS.txt            - Describes some of the goofy C stuff in the VM
   PROG_HINTS.txt         - Some hints on the tricky RETURN case
   c0vm-ref.c             - Bytecode implementation reference

Files you won't modify:
   lib/contracts.h        - Contracts for C
   lib/xalloc.{c,h}       - NULL-checking allocation
   lib/stacks.{c,h}       - Stacks (with O(1) stack_size!)
   lib/c0vm_abort.{c,h}   - Functions for your C0VM to report runtime errors
   lib/c0vm_c0ffi.{c,h}   - Interface to the library functions
   lib/c0vm.h             - Header file, contains C0VM types and functions
   lib/read_program.{c,h} - Reading in C0 bytecode from a file
   c0vm_main.c            - Main function - loads bytecode, handles return

Files you will modify:
   c0vm.c                 - Executes C0 bytecode

==========================================================

Compiling a C0 program to create a .bc0 file (produces tests/mid.bc0):
   % cc0 -b tests/mid.c0

Compiling and running your C0VM implementaiton (with -DDEBUG)
   % make
   % ./c0vmd tests/mid.bc0

Compiling and running your C0VM implementation (without -DDEBUG)
   % make
   % ./c0vm tests/mid.bc0

Using the testvm script to automate C0VM/cc0 comparison 
(runs the non-DDEBUG version)
   % ./testvm tests/mid.c0

==========================================================
On autolab (final): https://autolab.cs.cmu.edu/15122-f13/c0vm

