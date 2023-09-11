.data
    vector: .word 9,5,4,10,7, -1
    res: .word 9

.text
.global main
main: 
    ldr r0, =vector
    push {lr}
    bl contperfect
    pop {lr}
    ldr r4,=res
    str r0,[r4]
    bx lr
    contperfect:
        mov r1, r0
        mov r3, #0  @ contador de numeros perfectos
        loopcontar:
            ldr r0, [r1], #4

            cmp r0, #0
            blt fincontar

            push {r1, r2, r3, lr}
            bl URperfect
            pop {r1, r2, r3, lr}

            cmp r0, #1
            addeq r3, #1

            b loopcontar

            fincontar:
                mov r0, r3
                bx lr

URperfect:
    cmp r0, #0
    beq es

    mov r1, #1
    mov r2, #3

    loop:
        cmp r0, r1
        beq es
        blt no

        add r1, r1, r2
        add r2, r2, #2
    b loop

    es:
        mov r0, #1
        bx lr
    
    no:
        mov r0, #0
        bx lr