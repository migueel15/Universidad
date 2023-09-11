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
	lw $15,data($1)
	
	# Pos memoria vector A en $2
	addi $2, $2, 0x14
	lw $2,data($2)
	add $3, $3, $2

	getBBPos:
	add $4, $4, $15
	loop:
		beq $4, 0, addValores
		add $3, $3, 4
		addi $4, $4, -1
	j loop
addValores:
	add $4, $4, $15 # Contador
	loopSuma:	
		beq $4, 0, exit
		sub $4, $4, 1
		
		lw $8, data($2)
		lw $9, data($3)
		
		sub $6, $8, $9
		slt $10, $6, $5
		beq $10, 1, absoluto
		
		add $7, $7, $6
		addValor:
		add $12, $12, $7
		sub $6, $6, $6
		sub $7, $7, $7
		add $2, $2, 4
		add $3 $3, 4	
	j loopSuma

		
absoluto:
	sub $7, $6, $6 
	sub $7, $7, $6
	j addValor

exit:
	sw $12, 0xC($1)
	syscall