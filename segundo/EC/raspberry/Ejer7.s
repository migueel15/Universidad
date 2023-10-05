	.set GPBASE,  0x3F200000
	.set GPFSEL0, 0x00
	.set GPSET0,  0x1c
	.set GPCLR0,  0x28
	.set GPLEV0,  0x34
	.set STBASE,  0x3F300000
	.set STCLO,   0x04

.text
	ldr r0, =GPBASE
	ldr r7, =STBASE
	
	@Configuración puertos
	ldr r1, =0b00000000000000000001000000000000 @salida altavoz, entrada btn1 y btn2
	str r1, [r0, #GPSEL0]

	ldr r2, =0b00000000000000000000000000000100 @btn1 pulsado
	ldr r3, =0b00000000000000000000000000001000 @btn2 pulsado
	ldr r6, =0b00000000000000000000000000010000 @posicion altavoz

bucle: 
	ldr r1, [r0, #GPLEV0]
	tst r1, r2
	beq sonido1
	tst r1, r3
	beq sonido2
	b bucle

sonido1:
	ldr r1, =3816 @sleep fecuencia Do
	str r6, [r0, #GPSET0]
	bl espera
	str r6, [r0, #GPCLR0]
	bl espera

sonido2:
	ldr r1, =2557 @sleep fecuencia Sol
	str r6, [r0, #GPSET0]
	bl espera
	str r6, [r0, #GPCLR0]
	bl espera

espera:
	push {r4, r5}
	ldr r4, [r7, #STCLO]
	add r4, r1
ret1:
	ldr r5, [r7, #STCLO]
	cmp r5, r4
	blo ret1
	pop {r4, r5}
	bx lr
