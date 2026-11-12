import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.neural_network import MLPRegressor
from sklearn.metrics import mean_absolute_error

# Load the dataset
df = pd.read_csv('bikes.csv')

# Define features and target
X = df.drop(columns=['next_hour'])
y = df['next_hour']

# Split the data while maintaining chronological order
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, shuffle=False, random_state=42)

# Scale the features
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# Train the MLPRegressor
mlp = MLPRegressor(hidden_layer_sizes=(32, 32), max_iter=512, random_state=42)
mlp.fit(X_train_scaled, y_train)

# Predict and evaluate
y_pred = mlp.predict(X_test_scaled)
mae = mean_absolute_error(y_test, y_pred)
print(mae)
