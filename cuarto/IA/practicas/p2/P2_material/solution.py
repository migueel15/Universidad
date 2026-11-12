"""Practice P2. A learned model and a written rule in one system.

Fill in the four places marked TODO_1 to TODO_4. Everything else works.
Run it with:   python3 initial_solution.py
"""

import warnings

import pandas as pd
from sklearn.metrics import mean_absolute_error
from sklearn.neural_network import MLPRegressor
from sklearn.preprocessing import StandardScaler

warnings.filterwarnings("ignore")

DATA = "bikes.csv"
SEED = 42
TRAIN_SHARE = 0.8
TRAINING_BUDGET = 512
HIDDEN = (32, 32)
CAPACITY = 400

# --- Step 1. Load the data, in chronological order --------------------------

table = pd.read_csv(DATA)
target = table["next_hour"].to_numpy()
inputs = table.drop(columns=["next_hour"])

cut = int(len(table) * TRAIN_SHARE)
x_train, x_test = inputs.iloc[:cut], inputs.iloc[cut:]
y_train, y_test = target[:cut], target[cut:]

print("hours in the file: %d" % len(table))
print("training hours: %d" % len(x_train))
print("test hours: %d" % len(x_test))
print("mean demand of the test hours: %.1f bicycles" % y_test.mean())

# --- Step 2. The rule written by a person -----------------------------------
# The simplest rule anyone would write: the next hour will be like this one.

rule_answer = x_test["cnt"].to_numpy()

print("\n--- rule, the next hour is like this one ---")
print("mean absolute error: %.2f bicycles" % mean_absolute_error(y_test, rule_answer))

ratio = (
    pd.DataFrame(
        {
            "hour": x_train["hr"],
            "next": y_train,
            "now": x_train["cnt"],
        }
    )
    .groupby("hour")
    .apply(
        lambda group: group["next"].mean() / max(group["now"].mean(), 1),
        include_groups=False,
    )
)

better_answer = x_test["cnt"].to_numpy() * x_test["hr"].map(ratio).to_numpy()

print("\n--- rule, corrected by the hour of the day ---")
print("mean absolute error: %.2f bicycles" % mean_absolute_error(y_test, better_answer))

ratio_two = (
    pd.DataFrame(
        {
            "hour": x_train["hr"],
            "work": x_train["workingday"],
            "next": y_train,
            "now": x_train["cnt"],
        }
    )
    .groupby(["hour", "work"])
    .apply(
        lambda group: group["next"].mean() / max(group["now"].mean(), 1),
        include_groups=False,
    )
)
test_pairs = pd.MultiIndex.from_frame(x_test[["hr", "workingday"]])
better_answer_two = x_test["cnt"].to_numpy() * test_pairs.map(ratio_two).to_numpy()

print("\n--- rule, corrected by hour and working day ---")
print(
    "mean absolute error: %.2f bicycles"
    % mean_absolute_error(y_test, better_answer_two)
)

# --- Step 3. The network ----------------------------------------------------

scaler = StandardScaler().fit(x_train)
x_train_scaled, x_test_scaled = scaler.transform(x_train), scaler.transform(x_test)

network = MLPRegressor(
    hidden_layer_sizes=HIDDEN, max_iter=TRAINING_BUDGET, random_state=SEED
)
network.fit(x_train_scaled, y_train)
network_answer = network.predict(x_test_scaled)

print("\n--- network trained for %d epochs ---" % TRAINING_BUDGET)
print(
    "mean absolute error: %.2f bicycles" % mean_absolute_error(y_test, network_answer)
)

# --- Step 4. The two of them in one system ----------------------------------

print("\n--- model with a person for the busy hours ---")

automatic = network_answer <= CAPACITY

print("hours answered by the model: %.2f %%" % (100.0 * automatic.mean()))
print("hours sent to a person: %.2f %%" % (100.0 * (~automatic).mean()))
print(
    "mean absolute error of the automatic hours: %.2f bicycles"
    % mean_absolute_error(y_test[automatic], network_answer[automatic])
)

# --- Step 5. Everything side by side ----------------------------------------

print("\n--- comparison ---")

print("answer                                     MAE")
print(
    "rule, next hour like this one              %.2f"
    % mean_absolute_error(y_test, rule_answer)
)
print(
    "rule corrected by the hour                 %.2f"
    % mean_absolute_error(y_test, better_answer)
)
print(
    "rule corrected by hour and working day     %.2f"
    % mean_absolute_error(y_test, better_answer_two)
)
print(
    "network                                    %.2f"
    % mean_absolute_error(y_test, network_answer)
)
