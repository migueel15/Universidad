-------------------------------------------------------------------------------
-- Estructuras de Datos. 2º Curso. ETSI Informática. UMA
--
-- Titulación: Grado en Ingeniería del Software.
-- Alumno: DORADO MALDONADO, MIGUEL ÁNGEL
-- Fecha de entrega: DIA | MES | AÑO
--
-- Relación de Ejercicios 1. Ejercicios resueltos: ..........
--
-------------------------------------------------------------------------------

import Data.Bifunctor (Bifunctor (first))
import GHC.Driver.CmdLine (Err (errMsg))
import Test.QuickCheck

-- 1
-- 1 a
esTerna :: Integer -> Integer -> Integer -> Bool
esTerna x y z = (x ^ 2 + y ^ 2 == z ^ 2) && (x > 0) && (y > 0) && (z > 0)

-- 1 b
terna :: Integer -> Integer -> (Integer, Integer, Integer)
terna x y = (x ^ 2 - y ^ 2, 2 * x * y, x ^ 2 + y ^ 2)

-- 1 c
pTernas x y = x > 0 && y > 0 && x > y ==> esTerna l1 l2 h
  where
    (l1, l2, h) = terna x y

-- 2
intercambia :: (a, b) -> (b, a)
intercambia (x, y) = (y, x)

-- 3
-- 3 a
ordena2 :: (Ord a) => (a, a) -> (a, a)
ordena2 (x, y) = if x < y then (x, y) else (y, x)

p1_ordena2 x y = enOrden (ordena2 (x, y))
  where
    enOrden (x, y) = x <= y

p2_ordena2 x y = mismosElementos (x, y) (ordena2 (x, y))
  where
    mismosElementos (x, y) (z, v) = (x == z && y == v) || (x == v && y == z)

-- 3 b
ordena3 :: (Ord a) => (a, a, a) -> (a, a, a)
ordena3 (x, y, z)
  | x > y = ordena3 (y, x, z)
  | y > z = ordena3 (x, z, y)
  | otherwise = (x, y, z)

-- 3 c
p1_ordena3 x y z = enOrden (ordena3 (x, y, z))
  where
    enOrden (x, y, z) = x <= y && y <= z

-- 4
-- 4 a
max2 :: (Ord a) => a -> a -> a
max2 x y = if x >= y then x else y

-- 4 b
p1_max2 x y = max2 x y == x || max2 x y == y

p2_max2 x y = max2 x y >= x || max2 x y >= y

p3_max2 x y = x >= y ==> max2 x y == x

p4_max2 x y = y >= x ==> max2 x y == y

-- 5
-- 5 a
entre :: (Ord a) => a -> (a, a) -> Bool
entre x (y, z) = x >= y && x <= z

-- 6
iguales3 :: (Eq a) => (a, a, a) -> Bool
iguales3 (x, y, z) = x == y && y == z

-- 7
-- 7 a
type TotalSegundos = Integer

type Horas = Integer

type Minutos = Integer

type Segundos = Integer

descomponer :: TotalSegundos -> (Horas, Minutos, Segundos)
descomponer x = (horas, minutos, segundos)
  where
    horas = div x 3600
    restoSeg = mod x 3600
    minutos = div restoSeg 60
    segundos = mod restoSeg 60

-- 7 b
p1_descomponer x =
  x
    >= 0
      ==> h
      * 3600
      + m
        * 60
      + s
    == x
    && entre m (0, 59)
    && entre s (0, 59)
  where
    (h, m, s) = descomponer x

-- 8
unEuro :: Double
unEuro = 166.386

-- 8 a
pesetasAEuros :: Double -> Double
pesetasAEuros x = x / unEuro

-- 8 b
eurosAPesetas :: Double -> Double
eurosAPesetas x = x * unEuro

-- 8 c
p1_inversas x = eurosAPesetas (pesetasAEuros x) == x

-- 9
infix 4 ≃

(≃) :: Double -> Double -> Bool
x ≃ y = abs (x - y) < epsilon
  where
    epsilon = 1 / 1000

-- 9 b
p1_inversas_aprox x = eurosAPesetas (pesetasAEuros x) ≃ x

-- 10
-- 10 a
raíces :: Double -> Double -> Double -> (Double, Double)
raíces a b c
  | (b ^ 2 - 4 * a * c) >= 0 = (p, s)
  | otherwise = error "Raices no reales"
  where
    p = ((-b) + sqrt (b ^ 2 - 4 * a * c)) / 2
    s = ((-b) - sqrt (b ^ 2 - 4 * a * c)) / 2

-- 10 b
p1_raices a b c = esRaiz r1 && esRaiz r2
  where
    (r1, r2) = raíces a b c
    esRaiz r = a * r ^ 2 + b * r + c ≃ 0

-- p2_raices a b c = a /= 0 && esRaíces a b c
--   where
--     esRaíces a b c = esRaíz r1 && esRaíz r2
--     (r1, r2) = raíces a b c
--     esRaíz r = a * r ^ 2 + b * r + c ≃ 0

-- 11
esMultiplo :: (Integral a) => a -> a -> Bool
esMultiplo a b = mod a b == 0

-- 12
infixl 1 ==>>

(==>>) :: Bool -> Bool -> Bool
False ==>> x = True
x ==>> False = False

-- 13
esBisiesto :: Integer -> Bool
esBisiesto x
  | mod x 100 == 0 && mod x 400 == 0 = True
  | mod x 4 == 0 && mod x 100 /= 0 = True
  | otherwise = False

-- 14
-- 14 a
potencia :: Integer -> Integer -> Integer
potencia x y = x * potencia x (y - 1)