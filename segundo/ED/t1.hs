import Data.Char
import Text.Printf (IsChar (toChar))

twice :: Integer -> Integer
twice x = x * 2

next :: Char -> Char
next x = chr (ord x + 1)