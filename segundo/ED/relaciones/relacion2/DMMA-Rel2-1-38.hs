-------------------------------------------------------------------------------
-- Estructuras de Datos. 2º Curso. ETSI Informática. UMA
--
-- Titulación: Grado en Ingeniería del Software.
-- Alumno: DORADO MALDONADO, MIGUEL ÁNGEL
-- Fecha de entrega: DIA | MES | AÑO
--
-- Relación de Ejercicios 2. Ejercicios resueltos: ......
--
-------------------------------------------------------------------------------

import GHC.Float (divideDouble)
import Test.QuickCheck

-- 1
-- data Direction = North | South | East | West deriving(Eq, Ord, Enum, Show)

-- 1 a
infixl 1 <<

(<<) :: Direction -> Direction -> Bool
(<<) x y = fromEnum x < fromEnum y

-- p_menor x y = (x < y) == (x << y)
-- instance Arbitrary Direction where
--   arbitrary = do
--     n <- choose(0, 3)
--     return $ toEnum n
-- 1 b
data Direction = North | South | East | West deriving (Eq, Enum, Show)

instance Ord Direction where
  x <= y = x << y

-- 2
máximoYresto :: (Ord a) => [a] -> (a, [a])
-- 2 a y b Orden de la lista devuelta puede ser arbitrario
máximoYresto (x : xs) = (val, filter (\v -> v /= val) (x : xs))
  where
    val = (foldr max x (xs))

-- 3
reparte :: [a] -> ([a], [a])
reparte [x] = ([x], [])
reparte (x : y : zs) = (x : l1, y : l2) where (l1, l2) = reparte zs

-- 4
distintos :: (Eq a) => [a] -> Bool
distintos [] = True
distintos (x : xs) = all (/= x) xs && distintos xs

-- 5
-- a
replicate' :: Int -> a -> [a]
replicate' 0 _ = []
-- replicate' x y = y : replicate' (x - 1) y
replicate' x y = [y | n <- [1 .. x]]

-- b
p_replicate' n x =
  n >= 0
    && n
      <= 1000
        ==> length (filter (== x) xs)
      == n
    && length (filter (/= x) xs) == 0
  where
    xs = replicate' n x

-- 6
divideA :: Int -> Int -> Bool
divideA x y = mod x y == 0

divisores :: Int -> [Int]
divisores x = [divisor | divisor <- [1 .. x], divideA x divisor]

-- para numeros negativos tambien
divideA' :: Int -> Int -> Bool
divideA' x y
  | y < 0 = divideA x (y * (-1))
  | y == 0 = False
  | otherwise = divideA x y

divisores' :: Int -> [Int]
divisores' x = [divisor | divisor <- [(-x) .. x], divideA' x divisor]

-- 7