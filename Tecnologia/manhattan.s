.data
  res: .word 0

.text
main:
  mov r0, #0 @xi
  mov r1, #1 @yi
  mov r2, #2 @xj
  mov r3, #3 @yj

  push {lr}
  bl Manhattan
  pop {lr}

  ldr r2, =res
  str r0, [r2]
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
