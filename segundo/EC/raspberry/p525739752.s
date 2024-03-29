	.set GPBASE,  0x3F200000
	.set GPFSEL0, 0x00
	.set GPSET0,  0x1c
	.set GPCLR0,  0x28
	.set STBASE,  0x3F003000
	.set STCLO,   0x04

.text
	ldr r0, =0b11010011
	msr cpsr_c, r0
	ldr sp, =0x08000000
	
	ldr r0, =GPBASE
	ldr r1, =0b00000000000000000001000000000000
	str r1, [r0, #GPFSEL0]
	ldr r1, =0b00000000000000000000000000010000
	
	ldr r2, =1706
	ldr r3, =STBASE
bucle:
	bl espera
	str r1, [r0, #GPSET0]
	bl espera
	str r1, [r0, #GPCLR0]
	b bucle

espera:
	push {r4, r5}
	ldr r4, [r3, #STCLO]
	add r4, r2
ret1:
	ldr r5, [r3, #STCLO]
	cmp r5, r4
	blo ret1
	pop {r4, r5}
	bx lr