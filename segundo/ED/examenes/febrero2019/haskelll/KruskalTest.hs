module KruskalTest(g1,g2) where

import Kruskal
import DataStructures.Graph.DictionaryWeightedGraph


-------------------------------------------------------------------------------    
-- Some examples
-------------------------------------------------------------------------------    
 
g1 :: WeightedGraph Char Int
g1 = mkWeightedGraphEdges ['a','b','c','d','e']
                            [ WE 'a' 3 'b', WE 'a' 7 'd'
                            , WE 'b' 4 'c', WE 'b' 2 'd'
                            , WE 'c' 5 'd', WE 'c' 6 'e'
                            , WE 'd' 5 'e'
                            ]
-- kruskal g1
-- [WE 'd' 5 'e',WE 'b' 4 'c',WE 'a' 3 'b',WE 'b' 2 'd']

g2 :: WeightedGraph Char Int
g2 = mkWeightedGraphEdges ['a','b','c','d','e','f','g']
                            [ WE 'a' 7 'b', WE 'a' 5 'd'
                            , WE 'b' 9 'd', WE 'b' 8 'c', WE 'b' 7 'e'
                            , WE 'c' 5 'e'
                            , WE 'd' 15 'e', WE 'd' 6 'f'
                            , WE 'e' 8 'f', WE 'e' 9 'g'
                            , WE 'f' 11 'g'
                            ]
-- kruskal g2
-- [WE 'e' 9 'g',WE 'a' 7 'b',WE 'b' 7 'e',WE 'd' 6 'f',WE 'a' 5 'd',WE 'c' 5 'e']

g3 :: WeightedGraph Int Int
g3 = mkWeightedGraphEdges [1,2,3,4,5]
                            [ WE 1 16 2, WE 1 28 3, WE 1 50 5
                            , WE 2 38 4, WE 2 26 5
                            , WE 3 60 4, WE 3 32 5
                            , WE 4 56 5
                            ]
-- kruskal g3
-- [WE 2 38 4,WE 1 28 3,WE 2 26 5,WE 1 16 2]

g4 :: WeightedGraph Int Int
g4 = mkWeightedGraphEdges [1,2,3,4,5]
                            [ WE 1 13 2, WE 1 11 3, WE 1 78 5
                            , WE 2 12 4, WE 2 32 5
                            , WE 3 14 4, WE 3 44 5
                            , WE 4 93 5
                            ]
-- kruskal g4
-- [WE 2 32 5,WE 1 13 2,WE 2 12 4,WE 1 11 3]
