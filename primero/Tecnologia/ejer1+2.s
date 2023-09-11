.data
res: .word 0
.text
main:
mov r1, #2
mov r2, #3
bl maximo
mov r1, r0
mov r2, #4
bl maximo
ldr r1, =res
str r0, [r1]
mov lr, #0
bx lr
@ variable para almacenar resultado
@ primer valor a comparar en r1
@ segundo valor a comparar en r2
@ llamada a la función maximo
@ el resultado en r0 lo pasamos a r1
@ tercer valor a comparar en r2
@ llamada a la función maximo
@ cargamos en r1 dir. de variable res.
@ almacenamos resultado en res
@ el main va a parar en la posición lr = 0
@ terminamos programa principal (main)
maximo:
cmp r1, r2
movgt r0, r1
movle r0, r2
bx lr
@ declaración función maximo
@ comparamos registros r1 y r2
@ si r1>r2, pasamos a r0 el valor de r1
@ si r1<=r2, pasamos a r0 el valor de r2
@ saltamos a la dirección almacenada en
@ registro lr (r14)
