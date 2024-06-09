import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import StratifiedKFold, GridSearchCV
from sklearn.tree import DecisionTreeClassifier, plot_tree
from sklearn.neural_network import MLPClassifier
from sklearn.metrics import roc_curve, accuracy_score, f1_score, precision_score



dataset = pd.read_csv("./student-mat.csv", delimiter=";")

# convertir variables 
dataset["school"] = (dataset["school"] == "GP").astype(int)
dataset["sex"] = (dataset["sex"] == "M").astype(int)
dataset["address"] = (dataset["address"] == "U").astype(int)
dataset["famsize"] = (dataset["famsize"] == "GT3").astype(int)
dataset["Pstatus"] = (dataset["Pstatus"] == "T").astype(int)

job_map = {"teacher":0, "health":1, "services":2, "at_home":3, "other":4}
dataset["Mjob"] = dataset["Mjob"].replace(job_map)
dataset["Fjob"] = dataset["Fjob"].replace(job_map)

reason_map = {"home":0, "reputation":1, "course":2, "other":3}
dataset["reason"] = dataset["reason"].replace(reason_map)

guardian_map = {"mother":0, "father":1, "other":2}
dataset["guardian"] = dataset["guardian"].replace(guardian_map)

dataset["schoolsup"] = (dataset["schoolsup"] == "yes").astype(int)
dataset["famsup"] = (dataset["famsup"] == "yes").astype(int)
dataset["paid"] = (dataset["paid"] == "yes").astype(int)
dataset["activities"] = (dataset["activities"] == "yes").astype(int)
dataset["nursery"] = (dataset["nursery"] == "yes").astype(int)
dataset["higher"] = (dataset["higher"] == "yes").astype(int)
dataset["internet"] = (dataset["internet"] == "yes").astype(int)
dataset["romantic"] = (dataset["romantic"] == "yes").astype(int)

# definir target
target = (dataset["G3"] >= 10).astype(int)

# crea conjunto de valores eliminando el valor target ( G3 )
X = dataset.copy().drop(["G3"], axis="columns")
y = target

skf = StratifiedKFold(n_splits=10, shuffle=True, random_state=21)

param_grid = {
    'max_depth': [3, 5, 7, 10],
    'min_samples_split': [2, 5, 10]
}

# Inicializar GridSearchCV para árbol de decisión
grid_search_dt = GridSearchCV(DecisionTreeClassifier(), param_grid, cv=skf, scoring='accuracy')

dt_acc = [] 
dt_f1s = [] 
dt_prec = [] 
dt_fpr = [] 
dt_tpr = [] 

for train_index, test_index in skf.split(X, y):
    X_train, X_test = X.iloc[train_index], X.iloc[test_index]
    y_train, y_test = y.iloc[train_index], y.iloc[test_index]

    grid_search_dt.fit(X_train, y_train)
    decision_tree = grid_search_dt.best_estimator_
    prediction = decision_tree.predict(X_test)
    dt_proba = decision_tree.predict_proba(X_test)

    dt_acc.append(accuracy_score(y_test, prediction))
    dt_f1s.append(f1_score(y_test, prediction))
    dt_prec.append(precision_score(y_test, prediction))

    fpr, tpr, _ = roc_curve(y_test, prediction)
    dt_fpr.append(fpr)
    dt_tpr.append(tpr)

# exporto el arbol de decision a pdf
plt.figure(figsize=(50, 20))
plot_tree(decision_tree, filled=True, feature_names=X.columns, class_names=["Suspende", "Aprueba"])
plt.savefig("decision_tree.pdf")

# Perceptron multicapa

mlp = MLPClassifier(hidden_layer_sizes=(100, 100), max_iter=1000, alpha=0.0001, solver="adam", random_state=21)
mlp_acc = []
mlp_f1s = []
mlp_prec = []
mlp_fpr = []
mlp_tpr = []

for train_index, test_index in skf.split(X, y):
    X_train, X_test = X.iloc[train_index], X.iloc[test_index]
    y_train, y_test = y.iloc[train_index], y.iloc[test_index]

    scaler = StandardScaler()
    X_train = scaler.fit_transform(X_train)
    X_test = scaler.transform(X_test)

    mlp.fit(X_train, y_train)
    prediction = mlp.predict(X_test)
    mlp_proba = mlp.predict_proba(X_test)

    mlp_acc.append(accuracy_score(y_test, prediction))
    mlp_f1s.append(f1_score(y_test, prediction))
    mlp_prec.append(precision_score(y_test, prediction))

    fpr, tpr, _ = roc_curve(y_test, prediction)
    mlp_fpr.append(fpr)
    mlp_tpr.append(tpr)

# Grafico de curva ROC con la media de los valores obtenidos
plt.figure()
plt.plot([0, 1], [0, 1], linestyle="--", label="Random")
plt.plot(np.mean(dt_fpr, axis=0), np.mean(dt_tpr, axis=0), label="Decision Tree")
plt.plot(np.mean(mlp_fpr, axis=0), np.mean(mlp_tpr, axis=0), label="MLP")
plt.xlabel("Tasa de falsos positivos")
plt.ylabel("Tasa de verdaderos positivos")
plt.legend()
plt.savefig("roc_curve.pdf")

# Imprimir resultados comparando
print("Accuracy: Decision Tree:", {np.mean(dt_acc)}, "MLP:", {np.mean(mlp_acc)})
print("F1 Score: Decision Tree:", {np.mean(dt_f1s)}, "MLP:", {np.mean(mlp_f1s)})
print("Precision: Decision Tree:", {np.mean(dt_prec)}, "MLP:", {np.mean(mlp_prec)})
