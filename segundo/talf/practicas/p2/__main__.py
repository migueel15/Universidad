from python.automata import finite_automaton
from python.util import load_representation

# ej1
# automaton = load_representation(
#     "libs/python/automata/finiteautomata", "solo cadena a afd"
# )
# finite_automaton(automaton, "a")
# finite_automaton(automaton, "ab")
# finite_automaton(automaton, "b")


# ej2
automaton = load_representation(
    "libs/python/automata/finiteautomata", "solo cadena a afnd"
)
finite_automaton(automaton, "a")
finite_automaton(automaton, "ab")
finite_automaton(automaton, "b")
