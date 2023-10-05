	.include "inter.inc"
.text
	mov    r0, #0
	ADDEXC 0x18, irq_handler
	mov     r0, #0b11010010   
	msr     cpsr_c, r0		@ IRQ mode, init stack
	mov     sp, #0x8000
	mov     r0, #0b11010011
	msr     cpsr_c, r0		@ SVC mode, init stack
	mov     sp, #0x8000000
	ldr    r0, =GPBASE
	ldr    r1, =0b00001000000000000000000000000000
	str    r1, [r0, #GPFSEL0]   @ Configure GPIO9
	ldr    r0, =STBASE
	ldr    r1, [r0, #STCLO]
	add    r1, #0x400000        @ 4.19 seconds
	str    r1, [r0, #STC1]
	ldr    r0, =INTBASE         @ Enable interrupt at C1
	mov    r1, #0b0010
	str    r1, [r0, #INTENIRQ1]
	mov    r0, #0b01010011      @ SVC mode, IRQ enabled
	msr    cpsr_c, r0
buc:	b      buc

irq_handler:
	push   {r0, r1}
	ldr    r0, =GPBASE
	mov    r1, #0b00000000000000000000001000000000
	str    r1, [r0, #GPSET0]    @ Turn on GPIO9
	pop    {r0, r1}
	subs   pc, lr, #4
