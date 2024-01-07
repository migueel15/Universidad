module WellBalanced where

import DataStructures.Stack.LinearStack qualified as S

-- >>> wellBalanced "v(hg(jij)hags{ss[dd]dd})"
-- True

-- >>> wellBalanced "ff(h([sds)sds]ss)hags"
-- False

-- >>> wellBalanced ""
-- True

wellBalanced :: String -> Bool
wellBalanced str = wellBalanced' str S.empty

wellBalanced' :: String -> S.Stack Char -> Bool
wellBalanced' [] stack = S.isEmpty stack
wellBalanced' (x : xs) stack
  | x == '(' || x == '{' || x == '[' = wellBalanced' xs (S.push x stack)
  | x == ')' && '(' == S.top stack || x == '}' && '{' == S.top stack || x == ']' && '[' == S.top stack = wellBalanced' xs (S.pop stack)
  | otherwise = wellBalanced' xs stack
