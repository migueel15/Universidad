.data
numel: .word 4
datos: .word 7,9,0,4

sumaPerfect: .word 0
nrPerfect: .word 0
maxPerfect: .word 0

.text
main:
ldr r4, =numel
ldr r4, [r4]
ldr r5, =datos

mov r6, #0 @sum CPs
mov r7, #0 @num CP
mov r8, #0 @max CP

loopMain:
cmp r4, #1
blt guardarValores

ldr r3, [r5], #4
mov r0, r3

push {lr}
bl URperfect
pop {lr}

cmp r0, #1
addeq r7, #1
addeq r6, r6, r3
movne r3, r8 @ Evita sobreescribir r8 en caso de que el numero no sea CP y sea mayor
cmp r3, r8
movgt r8, r4
sub r4, r4, #1
b loopMain

guardarValores:
ldr r10, =sumaPerfect
str r6, [r10]
ldr r11, =nrPerfect
str r7, [r11]
ldr r12, =maxPerfect
str r8, [r12]
bx lr

URperfect:
cmp r0, #0
beq es

mov r1, #1 @ suma de impares
mov r2, #3 @ segundo impar

loop:
cmp r0, r1
beq es
blt no

add r1, r1, r2
add r2, r2, #2 @ siguiente impar
b loop

es:
mov r0, #1
bx lr

no:
mov r0, #0
bx lr
