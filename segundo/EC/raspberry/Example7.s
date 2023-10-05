.include  "inter.inc"
.text
	ADDEXC 0x18, irq_handler
	ADDEXC 0x1c, fiq_handler
	mov    r0, #0b11010001
	msr    cpsr_c, r0		@ FIQ mode, init stack
	mov    sp, #0x4000
	mov    r0, #0b11010010
	msr    cpsr_c, r0		@ IRQ mode, init stack
	mov    sp, #0x8000
	mov    r0, #0b11010011
	msr    cpsr_c, r0		@ SVC mode, init stack
	mov    sp, #0x8000000
	ldr    r0, =GPBASE
	mov    r1, #0b00001000000000000000000000000000
        str    r1, [r0, #GPFSEL0]	@ Configure GPIO9
	mov    r1, #0b00000000000000000000000000000100
        str    r1, [r0, #GPFEN0]	@ Enable falling edge interruptions through GPIO2
        ldr    r0, =INTBASE
	mov    r1, #0b00000000000100000000000000000000
	str    r1, [r0, #INTENIRQ2]	@ Allow interruptions from any GPIO pin
	ldr    r0, =STBASE
	ldr    r1, [r0, #STCLO]
	add    r1, #0x400000
	str    r1, [r0, #STC1]		@ 4.19 seconds
	ldr    r0, =INTBASE
	mov    r1, #0b00000010
	str    r1, [r0, #INTENIRQ1]	@ Enable interrupt at C1
 	mov    r1, #0b10110100
	str    r1, [r0, #INTFIQCON]	@ Enable FIQ for GPIO_int3
	mov    r0, #0b00010011
	msr    cpsr_c, r0		@ SVC mode, IRQ and FIQ enabled
buc:	b      buc

fiq_handler:
	push   {r0, r1, r2}
        ldr    r0, =GPBASE
	ldr    r1, =onoff
	ldr    r2, [r1]			@ Load variable
	eors   r2, #1			@ Xor with 1 to test if it is on or off
	str    r2, [r1]
	mov    r1, #0b00000000000000000000001000000000
	streq  r1, [r0, #GPCLR0]	@ Turn on if variable is 1
	strne  r1, [r0, #GPSET0]	@ Turn off if variable is 1
	mov    r1, #0b00000000000000000000000000000100	
	str    r1, [r0, #GPEDS0]	@ Clear GPIO2 event
	pop    {r0, r1, r2}
        subs   pc, lr, #4

irq_handler:
	push   {r0, r1, r2}
	ldr    r0, =GPBASE
	ldr    r1, =onoff
	ldr    r2, [r1]			@ Load variable
	eors   r2, #1			@ Xor with 1 to test if it is on or off
	str    r2, [r1]
	mov    r1, #0b00000000000000000000001000000000
	strne  r1, [r0, #GPSET0]	@ Turn on if variable is 1
	streq  r1, [r0, #GPCLR0]	@ Turn off if variable is 1
	ldr    r0, =STBASE
        mov    r1, #0b0010
        str    r1, [r0, #STCS]		@ Clear timer interrupt
	ldr    r1, [r0, #STCLO]
	add    r1, #0x400000
	str    r1, [r0, #STC1]		@ 4.19 seconds
	pop    {r0, r1}
	subs   pc, lr, #4

onoff: .word  0				@ Variable stored after the program code
