import Test.QuickCheck

len :: [a] -> Int
len [] = 0
len (x : xs) = 1 + len xs

prop1 xs xy = len xs + len xy == len (xs ++ xy)

sorted :: (Ord a) => [a] -> Bool
sorted [] = True
sorted [x] = True
sorted (x : y : zs) = x <= y && sorted (y : zs)