

# Importamos la libreria que utilizaremos para crear un tibble. Este es similar
# a read.csv pero añade nuevas funcionalidades.
library(readr)
# Los parametroa col_types
data <- read_csv("21425.csv", col_types=cols(.default=col_double(), sexo=col_factor(),
                                             dietaEsp=col_factor(), nivEstPad=col_factor(),
                                             nivEstudios=col_factor(), nivIngresos=col_factor()))

data$IMC <- data$peso/data$altura^2
