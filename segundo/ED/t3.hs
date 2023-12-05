import DataStructures.Stack.LinearStack qualified as DS
import Library.Lib qualified as L

data Stack a = Empty | Node a (Stack a) deriving (Show)

top :: Stack a -> a
top Empty = error "error stack"
top (Node x xs) = x

push :: a -> Stack a -> Stack a
push x xs = Node x xs

pop :: Stack a -> Stack a
pop Empty = error "error pop en pila vacia"
pop (Node x xs) = xs

isEmpty :: Stack a -> Bool
isEmpty Empty = True
isEmpty (Node x xs) = False

s1 :: Stack Int
s1 = Node 1 (Node 2 (Node 3 Empty))
