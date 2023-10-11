	.set GPBASE,  0x3F200000
	.set GPFSEL0, 0x00
	.set GPFSEL2, 0x08
	.set GPSET0,  0x1c
	.set GPCLR0,  0x28

.text
	ldr r0, =GPBASE
	ldr r1, =0b00000000000000000000000001000000
	str r1, [r0, #GPFSEL2]
	ldr r1, =0b00000000010000000000000000000000
bucle:
	str r1, [r0, #GPSET0]
	str r1, [r0, #GPCLR0]
	b bucle
	
infi:   b   infi
