	.set GPBASE,  0x3F200000
	.set GPFSEL0, 0x00
	.set GPFSEL2, 0x08
	.set GPSET0,  0x1c
	.set GPLEV0,  0x34
	.set GPCLR0,  0x28
.text
	ldr r0, =GPBASE
	ldr r1,     =0b00000000001000000000000001000000
	str r1, [r0, #GPFSEL2]
	ldr r1,     =0b00001000010000000000000000000000
	str r1, [r0, #GPSET0]

	ldr r3, =0b00000000000000000000000000001000
	ldr r4, =0b00000000000000000000000000000100

bucle:
	ldr r1, [r0, #GPLEV0]
	tst r1, r3
	beq boton1 
	tst r1, r4
	beq boton2
	b bucle
	
infi:   b   infi

boton1: 
	ldr r6, =0b00001000000000000000000000000000
	str r6, [r0, #GPSET0]
	ldr r1, =0b00000000010000000000000000000000
	str r1, [r0, #GPCLR0]
	b bucle
	
	
boton2: 
	ldr r6, =0b00000000010000000000000000000000
	str r6, [r0, #GPSET0]
	ldr r1, =0b00001000000000000000000000000000
	str r1, [r0, #GPCLR0]
	b bucle