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