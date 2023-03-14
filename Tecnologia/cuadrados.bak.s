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
  bl cuad                   
  pop {lr}
  str r0, [r4]
bx lr                  

cuad:
  cmp r0, #0
  beq exitcuad

  mul r2, r0, r0
  add r3, r3, r2

  sub r0, r0 ,#1
  push {lr}
  bl cuad
  pop {lr}

exitcuad:
  mov r0, r3
  bx lr
