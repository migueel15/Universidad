"""Practice P1. A first machine learning pipeline.

Fill in the four places marked TODO_1 to TODO_4. Everything else works.
Run it with:   python3 initial_solution.py
"""

import pandas as pd
from sklearn.dummy import DummyClassifier
from sklearn.metrics import accuracy_score, confusion_matrix, f1_score
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier

DATA = "creditcard_subset.csv"
SEED = 42
TEST_SHARE = 0.2
DEPTH = 5

# --- Step 1. Load the data --------------------------------------------------

table = pd.read_csv(DATA)
inputs = table.drop(columns=["Class"])
labels = table["Class"]

print("transactions: %d" % len(table))
print("frauds: %d" % labels.sum())
print("frauds over total: %.3f %%" % (100.0 * labels.mean()))

x_train, x_test, y_train, y_test = train_test_split(
    inputs, labels, test_size=TEST_SHARE, random_state=SEED, stratify=labels
)

print("training transactions: %d" % len(x_train))
print("test transactions: %d" % len(x_test))
print("frauds inside the test set: %d" % y_test.sum())

# --- Step 2. A model that learns nothing ------------------------------------

trivial = DummyClassifier(strategy="most_frequent")
trivial.fit(x_train, y_train)
trivial_answer = trivial.predict(x_test)

print("\n--- model that always answers not fraud ---")
print("accuracy: %.4f" % accuracy_score(y_test, trivial_answer))

# TODO_1. Calcula el F1 y la matriz de confusión para el primer modelo
print("F1: %.4f" % f1_score(y_test, trivial_answer))
print("confusion matrix:")
print(confusion_matrix(y_test, trivial_answer))

# --- Step 3. A model that does learn ----------------------------------------

print("\n--- decision tree of depth %d ---" % DEPTH)

# TODO_2. Genera un arbol de decision con el conjunto de datos de entrenamiento y predice la salida para el conjunto de testeo.
tree = DecisionTreeClassifier(max_depth=DEPTH, random_state=SEED)
tree.fit(x_train, y_train)
tree_answer = tree.predict(x_test)

# TODO_3. Muestra los datos obtenidos del nuevo modelo. Se añade el score f1
print("accuracy: %.4f" % accuracy_score(y_test, tree_answer))
print("F1: %.4f" % f1_score(y_test, tree_answer))
print("confusion matrix:")
print(confusion_matrix(y_test, tree_answer))

# --- Step 4. The two of them side by side -----------------------------------

print("\n--- comparison ---")

# TODO_4
print("model accuracy F1")
print(
    "always not fraud %.4f %.4f"
    % (accuracy_score(y_test, trivial_answer), f1_score(y_test, trivial_answer))
)
print(
    "decision tree %.4f %.4f"
    % (accuracy_score(y_test, tree_answer), f1_score(y_test, tree_answer))
)
