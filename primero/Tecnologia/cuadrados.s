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
  bxeq lr

  push {r0, lr}
  sub r0, r0 ,#1
  bl cuad
  pop {r1, lr}
  mul r2, r1, r1
  add r0, r0, r2
  bx lr
