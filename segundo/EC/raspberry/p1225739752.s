.include "inter.inc"
onoff: .word 0
.text
	mov r0, #0
	ADDEXC 0x18, irq_handler
	ldr r0, =0b11010010
	msr cpsr_c, r0
	ldr sp, =0x8000
	
	ldr r0, =0b11010011
	msr cpsr_c, r0
	ldr sp, =0x8000000
	ldr r0, =GPBASE

	ldr r1, =0b00000000001000000000000001000000
	str r1, [r0, #GPFSEL2]
	
	ldr r1, =0b00000000000000000000000000001100
	str r1, [r0, #GPFEN0]

	ldr r0, =INTBASE
	ldr r1, =0b00000000000100000000000000000000
	str r1, [r0, #INTENIRQ2]
	
	ldr r0, =0b01010011
	msr cpsr_c, r0
	
	ldr r0, =GPBASE
	ldr r1, =0b00001000010000000000000000000000
	str r1, [r0, #GPSET0]
	
	bucle: 
		b bucle
	
	irq_handler:
		push {r0, r1, r2, r3, r4}
		
		ldr r1, [r0, #GPEDS0]
		ands r1, #0b00000000000000000000000000000100
		bne boton1
		
		ldr r1, [r0, #GPEDS0]
		ands r1, #0b00000000000000000000000000001000
		bne boton2

		boton1:
		ldr r1, =0b00000000010000000000000000000000
		ldr r2, =0b00001000000000000000000000000000
		str r1, [r0, #GPSET0]
		str r2, [r0, #GPCLR0]
		ldr r1, =0b00000000000000000000000000000100
		str r1, [r0, #GPEDS0]
		b fin

		
		boton2:
		ldr r1, =0b00001000000000000000000000000000
		ldr r2, =0b00000000010000000000000000000000
		str r1, [r0, #GPSET0]
		str r2, [r0, #GPCLR0]
		ldr r1, =0b00000000000000000000000000001000
		str r1, [r0, #GPEDS0]
		b fin

		fin:
		pop {r0, r1, r2, r3, r4}
		subs pc, lr, #4