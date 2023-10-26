module Library.Lib (f1, f3) where

import Data.Char

f1 :: Integer -> Integer -> Integer
f1 x y = f2 x + 2 * y

f2 :: Integer -> Integer
f2 x = x + 2 * 4

f3 :: Char -> Char
f3 c = chr (1 + ord c)