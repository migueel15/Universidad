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
	str r1, [r0, #STC3]
	
	ldr r0, =INTBASE
	ldr r1, =0b1000
	str r1, [r0, #INTENIRQ1]
	
	ldr r0, =0b01010011
	msr cpsr_c, r0
	

	bucle: 
		ldr r5, =0
		ldr r0, =GPBASE
		
		ldr r1, =0b00001000000000000000000000000000
		ldr r2, =0b00001000010000100000111000000000
		str r2, [r0, #GPCLR0]
		str r1, [r0, #GPSET0]
		add r5, #1
		bl espera
		
		ldr r1, =0b00000000010000000000000000000000
		ldr r2, =0b00001000010000100000111000000000
		str r2, [r0, #GPCLR0]
		str r1, [r0, #GPSET0]
		add r5, #1
		bl espera
		
		ldr r1, =0b00000000000000100000000000000000
		ldr r2, =0b00001000010000100000111000000000
		str r2, [r0, #GPCLR0]
		str r1, [r0, #GPSET0]
		add r5, #1
		bl espera
		
		ldr r1, =0b00000000000000000000100000000000
		ldr r2, =0b00001000010000100000111000000000
		str r2, [r0, #GPCLR0]
		str r1, [r0, #GPSET0]
		add r5, #1
		bl espera
		
		ldr r1, =0b00000000000000000000010000000000
		ldr r2, =0b00001000010000100000111000000000
		str r2, [r0, #GPCLR0]
		str r1, [r0, #GPSET0]
		add r5, #1
		bl espera
		
		ldr r1, =0b00000000000000000000001000000000
		ldr r2, =0b00001000010000100000111000000000
		str r2, [r0, #GPCLR0]
		str r1, [r0, #GPSET0]
		add r5, #1
		bl espera
		
		b bucle
	
	irq_handler:
		push {r0, r1, r2, r3, r4, r5, r6, r7}
		ldr r0, =GPBASE
		ldr r2, =STBASE
		ldr r1, [r2, #STCS]
		cmp r1, #0b1000
		beq sonido

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
		ldr r2, =1136
		add r1, r2
		str r1, [r0, #STC3]
		b fin

		fin:
		pop {r0, r1, r2, r3, r4, r5, r6, r7}
		subs pc, lr, #4
		
		
	espera:
		push {r2, r3, r4, r7}
		ldr r2, =200000
		ldr r3, =STBASE
		ldr r4, [r3, #STCLO]
		add r4, r2
	ret1:
		ldr r7, [r3, #STCLO]
		cmp r7, r4
		blo ret1
		pop {r2, r3, r4, r7}
		bx lr