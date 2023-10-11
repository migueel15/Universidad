.include "inter.inc"

.text
	mov r0, #0
	ADDEXC 0x18, irq_handler
	ldr r0, =0b11010010
	msr cpsr_c, r0
	ldr sp, =0x08000
	
	ldr r0, =0b11010011
	msr cpsr_c, r0
	ldr sp, =0x08000000
	ldr r0, =GPBASE
	ldr r1, =0b00000000000000000000000001000000
	str r1, [r0, #GPFSEL2]
	
	ldr r0, =STBASE
	ldr r1, [r0, #STCLO]
	add r1, #0x300000
	str r1, [r0, #STC3]
	
	ldr r0, =INTBASE
	ldr r1, =0b1000
	str r1, [r0, #INTENIRQ1]
	
	ldr r0, =0b01010011
	msr cpsr_c, r0
	
	bucle: b bucle
	
	irq_handler:
		push {r0, r1}
		ldr r0, =GPBASE
		ldr r1, =0b00000000010000000000000000000000
		str r1, [r0, #GPSET0]
		pop {r0, r1}
		subs pc, lr, #4