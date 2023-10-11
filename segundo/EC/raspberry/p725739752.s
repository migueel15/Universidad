.include "inter.inc"

.text
	ldr r0, =0b11010011
	msr cpsr_c, r0
	ldr sp, =0x08000000
	
	ldr r0, =GPBASE
	ldr r7, =STBASE
	
	ldr r1, =0b00000000000000000001000000000000
	str r1, [r0, #GPFSEL0]

	ldr r2, =0b00000000000000000000000000000100
	ldr r3, =0b00000000000000000000000000001000
	ldr r6, =0b00000000000000000000000000010000

bucle: 
	ldr r1, [r0, #GPLEV0]
	tst r1, r2
	beq sonido1
	tst r1, r3
	beq sonido2
	b bucle

sonido1:
	ldr r1, =1908
	bl espera
	str r6, [r0, #GPSET0]
	bl espera
	str r6, [r0, #GPCLR0]
	b bucle

sonido2:
	ldr r1, =1279
	str r6, [r0, #GPSET0]
	bl espera
	str r6, [r0, #GPCLR0]
	bl espera
	b bucle

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
