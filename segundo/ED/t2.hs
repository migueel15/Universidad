import Test.QuickCheck

len :: [a] -> Int
len [] = 0
len (x : xs) = 1 + len xs

prop1 xs xy = len xs + len xy == len (xs ++ xy)

sorted :: (Ord a) => [a] -> Bool
sorted [] = True
sorted [x] = True
sorted (x : y : zs) = x <= y && sorted (y : zs)

data Direction = North | South | East | West

directionToInt :: Direction -> Int
directionToInt North = 0
directionToInt South = 1
directionToInt East = 2
directionToInt West = 3

instance Eq Direction where
  North == North = True
  South == South = True
  East == East = True
  West == West = True
  _ == _ = False

instance Ord Direction where
  x <= y = directionToInt x <= directionToInt y

instance Show Direction where
  show North = "North"
  show South = "South"
  show West = "West"
  show East = "East"

-- data SecuenciaEnteros = Vacia | Nodo Int SecuenciaEnteros deriving (Show)

-- sec1 :: SecuenciaEnteros
-- sec1 = Vacia

-- sec2 :: SecuenciaEnteros
-- sec2 = Nodo 10 Vacia

-- sec3 = Nodo 20 sec2

-- longSec :: SecuenciaEnteros -> Int
-- longSec Vacia = 0
-- longSec (Nodo x ys) = 1 + longSec ys

data Sec a = Vacia | Nodo a (Sec a) deriving (Show)

sec2 :: Sec Int
sec2 = Nodo 10 (Nodo 20 (Nodo 30 Vacia))

sec3 :: Sec Char
sec3 = Nodo 'a' (Nodo 'b' Vacia)

longSec :: Sec a -> Int
longSec Vacia = 0
longSec (Nodo x ys) = 1 + longSec ys

sumaSec :: (Num a) => Sec a -> a
sumaSec Vacia = 0
sumaSec (Nodo x ys) = x + sumaSec ys

concatSec :: Sec a -> Sec a -> Sec a
concatSec Vacia ys = ys
concatSec (Nodo x xs) ys = Nodo x (concatSec xs ys)