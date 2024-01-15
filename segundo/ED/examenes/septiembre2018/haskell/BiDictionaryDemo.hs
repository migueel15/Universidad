-- | Data Structures. BSc in Computer Science. UMA.
-- | September 2018

module BiDictionaryDemo where

import           AVLBiDictionary as B
import           DataStructures.Dictionary.AVLDictionary as D

list2Dic :: (Ord a, Ord b) => [(a,b)] -> BiDictionary a b
list2Dic xs = foldr (uncurry B.insert) B.empty xs

bd1 :: BiDictionary Integer Integer
bd1 =  list2Dic [(1,4), (2,3), (3,2), (4,5), (5,7), (7,1)]

bd2 :: BiDictionary Integer Char
bd2 =  list2Dic [(1,'a'), (4,'d'), (2,'b'), (7,'c')]

bd3 :: BiDictionary Char String
bd3 =  list2Dic [('a',"l1"), ('d',"l2"), ('b',"l3"), ('c',"l4")]


{-

-- Examples


-- | Exercise a. empty, isEmpty, size

>>> B.empty
BiDictionary()()

>>> B.isEmpty B.empty
True

>>> B.isEmpty (B.insert 1 2 B.empty)
False

>>> size empty
0

>>> B.size bd1
6


-- | Exercise b. insert

>>> (B.insert 1 2 B.empty)
BiDictionary(1->2)(2->1)

>>> (B.insert 1 8 (B.insert 3 5 (B.insert 1 2 B.empty)))
BiDictionary(1->8,3->5)(5->3,8->1)


-- | Exercise c. keyOf

>>> B.keyOf 2 B.empty
Nothing

>>> B.keyOf 8 (B.insert 1 8 (B.insert 3 5 (B.insert 1 2 B.empty)))
Just 1


-- | Exercise d. valueOf

>>> B.valueOf 2 B.empty
Nothing

>>> B.valueOf 1 (B.insert 1 'a' (B.insert 3 'b' (B.insert 1 'b' B.empty)))
Just 'a'


-- | Exercise e. deleteByKey

>>> B.deleteByKey 1 B.empty
BiDictionary()()

>>> B.deleteByKey 1 (B.insert 1 'a' (B.insert 3 'b' (B.insert 1 'b' B.empty)))
BiDictionary(3->'b')('b'->3)


-- | Exercise f. deleteByValue

>>> B.deleteByValue 1 B.empty
BiDictionary()()

>>> B.deleteByValue 'b' (B.insert 1 'a' (B.insert 3 'b' (B.insert 1 'b' B.empty)))
BiDictionary(1->'a')('a'->1)


-- | Exercise g. toBiDictionary

>>> B.toBiDictionary (D.insert 2 'b' (D.insert 1 'a' D.empty))
BiDictionary(1->'a',2->'b')('a'->1,'b'->2)


-- | Exercise h. compose


>>> compose bd2 bd3
BiDictionary(1->"l1",2->"l3",4->"l2",7->"l4")("l1"->1,"l2"->4,"l3"->2,"l4"->7)


-- | Exercise i. isPermutation

>>> B.isPermutation bd1
True

>>> B.isPermutation (B.insert 1 8 (B.insert 3 2 (B.insert 2 1 B.empty)))
False


-- | Exercise j. orbitOf

>>> B.orbitOf 1 bd1
[1, 4, 5, 7]


-- | Exercise k. cyclesOf

>>> B.cyclesOf bd1
[[1,4,5,7],[2,3]]




-}
