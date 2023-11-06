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
	add r1, #0x100000
	str r1, [r0, #STC3]
	
	ldr r0, =INTBASE
	ldr r1, =0b1000
	str r1, [r0, #INTENIRQ1]
	
	ldr r0, =0b01010011
	msr cpsr_c, r0
	ldr r5, =0
	bucle: 
		
		b bucle
	
	irq_handler:
		push {r1, r0, r2, r3, r4}
#                  0b00001000010000100000111000000000 todos
		ldr r0, =GPBASE
		cmp r5, #6
		moveq r5, #0
		
		cmp r5, #0
		ldr r1, =0b00001000000000000000000000000000
		streq r1, [r0, #GPSET0]
		strne r1, [r0, #GPCLR0]
		
		cmp r5, #1
		ldr r1, =0b00000000010000000000000000000000
		streq r1, [r0, #GPSET0]
		strne r1, [r0, #GPCLR0]

		cmp r5, #2
		ldr r1, =0b00000000000000100000000000000000
		streq r1, [r0, #GPSET0]
		strne r1, [r0, #GPCLR0]
		
		cmp r5, #3
		ldr r1, =0b00000000000000000000100000000000
		streq r1, [r0, #GPSET0]
		strne r1, [r0, #GPCLR0]
		
		cmp r5, #4
		ldr r1, =0b00000000000000000000010000000000
		streq r1, [r0, #GPSET0]
		strne r1, [r0, #GPCLR0]
		
		cmp r5, #5
		ldr r1, =0b00000000000000000000001000000000
		streq r1, [r0, #GPSET0]
		strne r1, [r0, #GPCLR0]
		
		ldr r0, =STBASE
		ldr r1, =0b1000
		str r1, [r0, #STCS]
		
		ldr r0, =STBASE
		ldr r1, [r0, #STCLO]
		add r1, #0x100000
		str r1, [r0, #STC3]
		
		add r5, #1
		
		pop {r1, r0, r2, r3, r4}
		subs pc, lr, #4