import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

df = pd.read_csv("nba.csv", index_col="Name")
df["Weight"].plot(label="weight")

df2 = df[["Team","Number"]]
df3 = df.iloc[0:3,:]
first = df.loc["Avery Bradley"]
second = df.loc["R.J. Hunter"]
