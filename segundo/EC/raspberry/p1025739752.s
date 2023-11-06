.include "inter.inc"
onoff: .word 0
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
	ldr r1, =0b00001000000000000000000000000000
	ldr r2, =0b00000000001000000000000000001001
	ldr r3, =0b00000000001000000000000001000000
	
	str r1, [r0, #GPFSEL0]
	str r2, [r0, #GPFSEL1]
	str r3, [r0, #GPFSEL2]
	
	ldr r0, =STBASE
	ldr r1, [r0, #STCLO]
	add r1, #0x50000
	str r1, [r0, #STC3]
	
	ldr r0, =INTBASE
	ldr r1, =0b1000
	str r1, [r0, #INTENIRQ1]
	
	ldr r0, =0b01010011
	msr cpsr_c, r0
	
	bucle: b bucle
	
	irq_handler:
		push {r0, r1, r2, r3, r4}
		ldr r3, =onoff
		ldr r4, [r3]
		eors r4, #1
		str r4, [r3]
		
		ldr r0, =GPBASE
		ldr r1, =0b00001000010000100000111000000000
		strne r1, [r0, #GPSET0]
		streq r1, [r0, #GPCLR0]

		ldr r0, =STBASE
		ldr r1, =0b1000
		str r1, [r0, #STCS]
		
		ldr r0, =STBASE
		ldr r1, [r0, #STCLO]
		add r1, #0x50000
		str r1, [r0, #STC3]
		

		pop {r0, r1, r2, r3, r4}
		subs pc, lr, #4