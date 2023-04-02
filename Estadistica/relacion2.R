# 1


# Ejercicio 4
densidad <- c(43,55,40,52,39,33,50,33,44,21)
velocidad <- c(27,23.8,30.7,24,34.8,41.4,27,40.4,31.7,51.2)
df <- data.frame(densidad, velocidad)

plot(densidad, velocidad)

cov <- mean(densidad * velocidad) - mean(densidad) * mean(velocidad)
dX <- sqrt(mean(densidad^2) - mean(densidad)^2)
dY <- sqrt(mean(velocidad^2) - mean(velocidad)^2)

r <- cov/(dX*dY)

# Ejercicio 8
x <- c(1,2,3,4,5)
y <- c(3,4.5,7,10,15)
plot(x,y)
mod1 <- lm(y ~ x)
coef1 <- mod1$coefficients
z <- log(y)
mod2 <- lm(z ~ x)
coef2 <- exp(mod2$coefficients)
summary(mod2)$r.squared

yp1 <- predict.lm(mod1)
yp2 <- exp(predict.lm(mod2))

MSE1 <- mean((y-yp1)^2)
MSE2 <- mean((y-yp2)^2)

varY <- mean(y^2)-mean(y)^2

R21 <- 1 - MSE1/varY
R22 <- 1- MSE2/varY
