import Data.List (intercalate)

data Stack a = Empty | Node a (Stack a)

empty :: Stack a
empty = Empty

push :: a -> Stack a -> Stack a
push x s = Node x s

pop :: Stack a -> Stack a
pop Empty = Empty
pop (Node n s) = s

instance (Show a) => Show (Stack a) where
  show s = "Stack(" ++ intercalate "," (aux s) ++ ")"
    where
      aux Empty = []
      aux (Node x s) = show x : aux s