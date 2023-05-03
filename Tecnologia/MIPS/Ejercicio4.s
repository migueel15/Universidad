.data
vectors:	.word 25, -3, -6, 1, 23, -1, -4, -1
numel:	.word 4
res:	.word 0
.text
main:	
	lw $6,numel($0)
	# pos resultado (a3)
	# pos sumaActual (a4)
	# pos A (a0)
	# pos B (a1)
geta1Pos:
	add $a2, $6, 0
	loop:
		beq $a2, 0, addV
		add $a1, $a1,4
		addi $a2, $a2, -1
	b loop
addV:
	lw $8, vectors($0)
	lw $9, vectors($a1)
	
	loopAdd:
		sub $s3, $s3, $s3
		
		
	
	
exit:	sw $11, res($0)
		li	$2, 10
		syscall