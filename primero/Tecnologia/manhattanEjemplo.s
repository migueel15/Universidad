.data
  res: .word 0
  a: .word 0
  b: .word 1
  D: .word 8

  nump: .word 4
  vecX: .word 2,7,5,4
  vecY: .word 3,2,5,3

  pathlength: .word 0
  closepanX: .word 0
  closepanY: .word 0

.text
main:
  ldr r0, =nump
  ldr r4, [r0]
  ldr r5, =vecX
  ldr r6, =vecY

  @ Posicion de la casa
  ldr r8, =a
  ldr r8, [r8]
  ldr r9, =b
  ldr r9, [r9]

  @ Punteros de resultados
  ldr r10, =pathlength
  ldr r11, =closepanX
  ldr r12, =closepanY
  
  push {lr}
  bl Pathlength
  bl Closepan
  pop {lr}
bx lr

abs:
  mov r1, #0
  mov r2, r0
  cmp r2, r1
  mov r3, #0
  sublt r0, r1, r0
  mov pc, lr

Manhattan:
  push {lr}
  @ Valor absoluto i
  sub r0, r0, r2
  push {r1-r3}
  bl abs
  pop {r1-r3}
  mov r2, r0

  @ Valor absoluto j
  sub r0, r1, r3
  push {r1-r3}
  bl abs
  pop {r1-r3}

  add r0, r0, r2
  pop {lr}
bx lr

Pathlength:
  @ Guarda en =pathlength la distancia total
  push {r2, r4, r5, r6, r7, r8, lr}

  @ Distancia de casa a primer punto
  mov r0, r8
  mov r1, r9
  ldr r2, [r5], #4
  ldr r3, [r6], #4
  bl Manhattan
  mov r7, r0
  mov r0, r2
  mov r1, r3

  loop:
    cmp r4, #1
    ble ExitPathLength

    ldr r2, [r5], #4
    ldr r3, [r6], #4
    push {r2, lr}
    bl Manhattan
    pop {r2, lr}

    add r7, r0
    mov r0, r2
    mov r1, r3

    sub r4, r4, #1
  b loop
bx lr

ExitPathLength:
  mov r2, r8
  mov r3, r9
  push {lr}
  bl Manhattan
  pop {lr}
  add r7, r7, r0
  str r7, [r10]
  pop {r2, r4, r5, r6, r7, r8, lr}
bx lr

Closepan:
  @ r10, r12 (x,y) menor
  @ r7 distancia anterior
  
  push {r4, r5, r6, r7, r10, r12, lr}
  cmp r4, #1
  blt ExitClosepan
  mov r0, r8
  mov r1, r9
  ldr r2, [r5], #4
  ldr r3, [r6], #4
  mov r10, r2
  mov r12, r3
  push {lr, r3}
  bl Manhattan
  pop {lr, r3}
  mov r7, r0

  loopClosepan:
    cmp r4, #2
    blt ExitClosepan
    mov r0, r8
    mov r1, r9
    ldr r2, [r5], #4
    ldr r3, [r6], #4
    push {lr, r3}
    bl Manhattan
    pop {lr, r3}

    cmp r7, r0
    movgt r7, r0
    movgt r10, r2
    movgt r12, r3

    sub r4, r4, #1
  b loopClosepan  

  ExitClosepan:
    mov r0, r10
    mov r1, r12
    pop {r4, r5, r6, r7, r10, r12, lr}
    str r0, [r11]
    str r1, [r12]
  bx lr
