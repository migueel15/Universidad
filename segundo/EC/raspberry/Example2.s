        .set  GPBASE,   0x3F200000
        .set  GPFSEL0,  0x00
        .set  GPSET0,   0x1c
	.set  GPLEV0,   0x34
    .set    GPCLR0,         0x28
.text
        ldr   r0, =GPBASE
/* guia bits         xx999888777666555444333222111000*/
        mov   r1, #0b00001000000000000000000000000000
        str   r1, [r0, #GPFSEL0]  @ Configura como salida GPIO9 y como entrada GPIO2 y GPIO3
	mov   r2, #0b00000000000000000000000000000100
bucle:
	ldr   r3, [r0, #GPLEV0]
	tst   r3, r2
	bne   bucle
/* guia bits            10987654321098765432109876543210*/
        mov   r1, #0b00000000000000000000001000000000
        str   r1, [r0, #GPSET0]   @ Enciende GPIO9
        
infi:	b     infi
