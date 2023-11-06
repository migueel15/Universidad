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
	ldr r1, =0b00001000000000000001000000000000
	ldr r2, =0b00000000001000000000000000001001
	ldr r3, =0b00000000001000000000000001000000
	
	str r1, [r0, #GPFSEL0]
	str r2, [r0, #GPFSEL1]
	str r3, [r0, #GPFSEL2]
	
	ldr r0, =STBASE
	ldr r1, [r0, #STCLO]
	add r1, #0x20000
	str r1, [r0, #STC1]
	str r1, [r0, #STC3]
	
	ldr r0, =INTBASE
	ldr r1, =0b1010
	str r1, [r0, #INTENIRQ1]
	
	ldr r0, =0b01010011
	msr cpsr_c, r0
	ldr r5, =0

	bucle: 
		b bucle
	
	irq_handler:
		push {r0, r1, r2, r3, r4, r6, r7}
		ldr r0, =GPBASE
		ldr r2, =STBASE
		ldr r1, [r2, #STCS]
		cmp r1, #0b1000
		beq sonido

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
		ldr r1, =0b0010
		str r1, [r0, #STCS]
		
		ldr r1, [r0, #STCLO]
		add r1, #0x20000
		str r1, [r0, #STC1]
		
		add r5, #1
		b fin
		
		sonido:
		ldr r0, =GPBASE
		ldr r1, =0b00000000000000000000000000010000
		ldr r6, =onoff
		ldr r7, [r6]	
		cmp r7, #0

		streq r1, [r0, #GPSET0]
		strne r1, [r0, #GPCLR0]

		eors r7, #1
		str r7, [r6]

		ldr r0, =STBASE
		ldr r1, =0b1000
		str r1, [r0, #STCS]
		ldr r1, [r0, #STCLO]
		add r1, #0x1136
		str r1, [r0, #STC3]
		b fin

		fin:
		pop {r1, r0, r2, r3, r4, r6, r7}
		subs pc, lr, #4