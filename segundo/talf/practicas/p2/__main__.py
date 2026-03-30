from python.automata import finite_automaton
from python.util import load_representation

# ej1
# automata = load_representation("libs/python/automata/finiteautomata", "cadena a afd")
# finite_automaton(automata, "a")
# finite_automaton(automata, "bb")
# finite_automaton(automata, "aaa")


# ej2
automata = load_representation("libs/python/automata/finiteautomata", "cadena a afnd")

finite_automaton(automata, "a")
finite_automaton(automata, "bb")
finite_automaton(automata, "aaa")
