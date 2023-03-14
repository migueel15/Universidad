.data
minumr:     .word 8
res:        .word 0

.text
.global main

main:
ldr r1, =minumr
ldr r0, [r1]
ldr r4, =res

push {lr}
bl fib
pop {lr}

str r0, [r4]
bx lr

fib:
  cmp r0, #0
  bxeq lr

  mov r1, #1 @f
  mov r2, #0 @a
  mov r3, #1 @b
 loop:
  cmp r0, #2
  movlt r0, r1
  bxlt lr

  add r1, r2, r3
  mov r2, r3
  mov r3, r1

  sub r0, r0, #1
  b loop
