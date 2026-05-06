from python.automata import pushdown_automaton

pruebas = [
    ("011", "accept"),
    ("001111", "accept"),
    ("000111111", "accept"),
    ("01", "blocked"),
    ("00111", "blocked"),
    ("1111", "blocked"),
]

for cadena, esperado in pruebas:
    print("Cadena:", cadena)
    resultado = pushdown_automaton("ej1_0n1_2n", cadena, desired_output=esperado)
    print("Resultado:", resultado)
    print("Esperado:", esperado)
    print()
