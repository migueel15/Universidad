import Control.Arrow (Arrow (first))
import Data.Sequence (Seq (Empty))
import DataStructures.Stack.LinearStack
import Test.QuickCheck

-- ejercicio 1
wellBalanced :: String -> Bool
wellBalanced xs = wellBalanced' xs empty

wellBalanced' :: String -> Stack Char -> Bool
wellBalanced' [] s = isEmpty s
wellBalanced' (x : xs) s
  | x == '[' = wellBalanced' xs (push '[' s)
  | x == '{' = wellBalanced' xs (push '{' s)
  | x == '(' = wellBalanced' xs (push '(' s)
  | x == ']' && top s == '[' = wellBalanced' xs (pop s)
  | x == '}' && top s == '{' = wellBalanced' xs (pop s)
  | x == ')' && top s == '(' = wellBalanced' xs (pop s)
  | otherwise = wellBalanced' xs s

-- ejercicio 4