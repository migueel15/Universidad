-------------------------------------------------------------------------------
-- Solution to Josephus' problem using a queue
--
-- Data Structures. Grado en Informática. UMA.
-- Pepe Gallardo, 2012
-------------------------------------------------------------------------------

module Demos.Queue.Josephus where

import DataStructures.Queue.TwoListsQueue

mkQueue :: Int -> Queue Int
mkQueue n = foldr enqueue empty [n-1,n-2..0]

skip :: Int -> Queue a -> Queue a
skip n q
   | n==0 || isEmpty q = q
   | n>0               = skip (n-1) (enqueue x q')
   where
     x = first q
     q' = dequeue q

josephus :: Int -> Int -> [Int]
josephus n m = execute $ mkQueue n
 where
  execute q
   | isEmpty q = []
   | otherwise = x : execute q''
   where
     q' = skip m q
     x = first q'
     q'' = dequeue q'
