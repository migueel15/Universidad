.data
data:	.word 3, 2, 1, 0, 4, 32, 0, 0, 25, -3, -6, 1, 23, -1, -4,-1
.text
main:	
	# Resultado pos 0xC
	# n elementos 0x10
	# dir vector AA 0x14
	
	# Registro resultado
	
	# $1 n
	addi $1, $1, 0x10
	nop
	nop
	lw $15,data($1)
	
	# Pos memoria vector A en $2
	addi $2, $2, 0x14
	nop
	nop
	lw $2,data($2)
	nop
	nop
	add $3, $3, $2
	getBBPos:
	add $4, $4, $15
	nop
	nop
	loop:
		beq $4, 0, addValores
		nop
		add $3, $3, 4
		sub $4, $4, 1
	j loop
	nop
	nop
	nop
addValores:
	add $4, $4, $15 # Contador
	nop
	nop
	loopSuma:	
		beq $4, 0, exit
		sub $4, $4, 1
		lw $8, data($2)
		lw $9, data($3)
		nop
		nop
		sub $6, $8, $9
		nop
		nop
		slt $10, $6, $5
		nop
		nop
		nop
		beq $10, 1, absoluto
		add $7, $7, $6
		nop
		nop
		addValor:
		add $12, $12, $7
		add $6, $0, $0
		add $7, $0, $0
		add $2, $2, 4
		add $3 $3, 4
	j loopSuma
	nop
	nop
	nop

		
absoluto:
	j addValor
	sub $7, $0, $6
	nop
	nop

exit:
	sw $12, 0xC($0)
	nop
	nop
	nop
	syscall
