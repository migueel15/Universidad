.data
    numel: .word 8
    vector: .word 8,-3,4,-7,9,-7,6,-1
    resultado: .word 2
.text
.global main
main:
    ldr r0, =numel
    ldr r0, [r0]    @ contador
    ldr r2, =vector
    ldr r4, =resultado

    cmp r0, #0
    beq tamcero

    ldr r1, [r2], #4
    mov r5, r1  @ min
    mov r6, r1  @ max
    sub r0, r0, #1

    loop:
    cmp r0, #0
    beq savedata

    ldr r1, [r2], #4

    cmp r1, r5
    movlt r5, r1

    cmp r1, r6
    movgt r6, r1
    
    sub r0, r0, #1

    b loop

    savedata:
        sub r7, r6, r5
        str r7, [r4]
        bx lr

    tamcero:
        mov r0, #0
        str r0, [r4]
        bx lr