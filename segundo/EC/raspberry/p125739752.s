	.set GPBASE,  0x3F200000
	.set GPFSEL0, 0x00
	.set GPFSEL2, 0x08
	.set GPSET0,  0x1c
.text
	ldr r0, =GPBASE
	mov r1,  #0b00000000001000000000000000000000
	str r1, [r0, #GPFSEL2]
	mov r1, #0b00001000000000000000000000000000
	str r1, [r0, #GPSET0]
infi:   b   infi
