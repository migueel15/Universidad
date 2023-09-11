.data
data:	.word 3, 2, 1, 0, 4, 32, 0, 0, 25, -3, -6, 1, 23, -1, -4,-1
.text
main:	
	lw $15,16($0)
	lw $2,20($0)
	lw $3,20($0)
	nop
	addi $4, $15, 0 
	nop
	nop
	getBBPos:
	loop:
		beq $4, 0, addValores
		nop
		nop
		nop
		addi $4, $4, -1
		addi $3, $3, 4
		
	j loop
	nop
	nop
addValores:
	addi $4, $15, 0 # Contador
	nop
	nop
	nop
	loopSuma:	
		beq $4, 0, exit
		addi $4, $4, -1
		lw $8, data($2)
		lw $9, data($3)
		nop
		nop
		nop
		sub $6, $8, $9
		addi $1, $0, 1
		nop
		nop
		slt $10, $6, $0
		nop
		nop
		beq $10, 1, absoluto
		addi $1, $0, 0
		nop
		nop
		nop
		addValor:
		add $12, $12, $6

	j loopSuma
	addi $2, $2, 4
	addi $3 $3, 4

		
absoluto:
	sub $6, $0, $6
	j addValor
	nop
	nop

exit:
	sw $12, 0xC($0)
	nop
	nop
	nop
	syscall
