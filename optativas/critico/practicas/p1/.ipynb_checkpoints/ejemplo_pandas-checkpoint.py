# https://www.geeksforgeeks.org/python-pandas-dataframe/
import pandas as pd
import matplotlib.pyplot as plt

# making data frame from csv file
df = pd.read_csv("https://media.geeksforgeeks.org/wp-content/uploads/nba.csv", index_col ="Name")

print(df.head())

print(df.describe())

df['Weight'].plot(label='weight')
plt.legend()
plt.show()

input('select two columns')
df2=df[['Team', 'Number']]
print(df2)

input('rows 0 to 2, all columns')
df3 = df.iloc[0:3, :] # También se puede hacer con df3 = df[0:3][:] iloc se puede usar para filas y columnas
print(df3.head())

input('retrieving row by loc method')
first = df.loc["Avery Bradley"]
second = df.loc["R.J. Hunter"]
  
print(first, "\n\n\n", second)

input('iterating rows and specific column')
for i in range(len(df)):
  print(df.iat[i,0])

input('sort')
print(df.sort_values(by='Name', ascending = True))

input('filtering')
print(df[df["Age"] > 30])

input('New column')
df['FromKentucky'] = df["College"] == "Kentucky"
print(df)

input('statistical')
res = df[["Age", "Salary"]].mean()
print(res)

print(df[["Age"]].min())
print(df[["Age"]].max())
