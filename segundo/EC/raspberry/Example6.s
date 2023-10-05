	.include  "inter.inc"
.text
	mov     r0, #0
	ADDEXC  0x18, irq_handler
	mov     r0, #0b11010010   
	msr     cpsr_c, r0		@ IRQ mode, init stack
	mov     sp, #0x8000
	mov     r0, #0b11010011
	msr     cpsr_c, r0		@ SVC mode, init stack
	mov     sp, #0x8000000
	ldr     r0, =GPBASE
	mov     r1, #0b00001000000000000000000000000000
	str     r1, [r0, #GPFSEL0]	@ Configure GPIO9
	mov     r1, #0b00000000000000000000000000000100
	str     r1, [r0, #GPFEN0]	@ Enable falling edge interruptions through GPIO2
	ldr     r0, =INTBASE
	mov     r1, #0b00000000000100000000000000000000
	str     r1, [r0, #INTENIRQ2]	@ Allow interruptions from any GPIO pin
	mov     r0, #0b01010011
	msr     cpsr_c, r0		@ SVC mode, IRQ enabled
buc:	b       buc

irq_handler:
       	push    {r0, r1, r2}
       	ldr     r0, =GPBASE
	ldr     r2, [r0, #GPEDS0]	@ Check GPIO2 was pressed
	ands    r2, #0b00000000000000000000000000000100
	movne   r1, #0b00000000000000000000001000000000
	strne   r1, [r0, #GPSET0]	@ Turn on red led if GPIO2 was pressed
	movne   r1, #0b00000000000000000000000000000100	
	strne   r1, [r0, #GPEDS0]	@ Clear GPIO2 event
	pop     {r0, r1, r2}
       	subs    pc, lr, #4
