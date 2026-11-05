build:
	mpicc -Wall -O2 -o P4_ga_ring P4_ga_ring.c 
	mpirun --oversubscribe -np 4 ./P4_ga_ring 1 
	mpirun --oversubscribe -np 4 ./P4_ga_ring 2
