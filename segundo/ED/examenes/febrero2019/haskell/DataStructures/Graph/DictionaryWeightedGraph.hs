----------------------------------------------
-- Estructuras de Datos.  2018/19
-- 2º Curso del Grado en Ingeniería [Informática | del Software | de Computadores].
-- Escuela Técnica Superior de Ingeniería en Informática. UMA
--
-- Examen 4 de febrero de 2019
--
-- ALUMNO/NAME: Miguel Angel Dorado Maldonado
-- GRADO/STUDIES: Software A
-- NÚM. MÁQUINA/MACHINE NUMBER: xxx
--
-- Weighted Graph implemented by using a dictionary from
-- sources to another dictionary from destinations to weights
----------------------------------------------

module DataStructures.Graph.DictionaryWeightedGraph
  ( WeightedGraph,
    WeightedEdge (WE),
    empty,
    isEmpty,
    mkWeightedGraphEdges,
    addVertex,
    addEdge,
    vertices,
    numVertices,
    edges,
    numEdges,
    successors,
  )
where

import Data.List (intercalate, nub)
import DataStructures.Dictionary.AVLDictionary qualified as D

data WeightedEdge a w = WE a w a deriving (Show)

instance (Eq a, Eq w) => Eq (WeightedEdge a w) where
  WE u w v == WE u' w' v' =
    (u == u' && v == v' || u == v' && v == u')
      && w == w'

instance (Eq a, Ord w) => Ord (WeightedEdge a w) where
  compare (WE _ w _) (WE _ w' _) = compare w w'

data WeightedGraph a w = WG (D.Dictionary a (D.Dictionary a w))

empty :: WeightedGraph a w
empty = WG D.empty

addVertex :: (Ord a) => WeightedGraph a w -> a -> WeightedGraph a w
addVertex (WG dict) key = WG (D.insert key D.empty dict)

addEdge :: (Ord a, Show a) => WeightedGraph a w -> a -> a -> w -> WeightedGraph a w
addEdge (WG dict) src dst wg
  | D.isDefinedAt src dict = WG (D.insert src (D.insert dst wg (toDict (D.valueOf src dict))) dict)
  | otherwise = error "No existe el vertice"

toDict :: Maybe a -> a
toDict (Just a) = a

edges :: (Eq a, Eq w) => WeightedGraph a w -> [WeightedEdge a w]
edges (WG dic) = [a | b <- aux (D.keys dic) (D.values dic), a <- b]
  where
    aux [] [] = []
    aux [x] [y] = [aux' x (D.keys y) (D.values y)]
    aux (x : xs) (y : ys) = aux' x (D.keys y) (D.values y) : aux xs ys

    aux' x [] [] = []
    aux' x [y] [z] = [WE x z y]
    aux' x (y : ys) (z : zs) = WE x z y : aux' x ys zs

successors :: (Ord a, Show a) => WeightedGraph a w -> a -> [(a, w)]
successors (WG dict) v
  | D.isDefinedAt v dict = aux (D.keys dict) (D.values dict) v
  where
    aux [x] [y] a
      | a /= x = []
      | otherwise = aux' (D.keys y) (D.values y)
    aux (x : xs) (y : ys) a
      | a /= x = aux xs ys a
      | otherwise = aux' (D.keys y) (D.values y)
    aux' [x] [] = []
    aux' [x] [y] = [(x, y)]
    aux' (x : xs) (y : ys) = (x, y) : aux' xs ys

-- NO EDITAR A PARTIR DE AQUÍ
-- DON'T EDIT ANYTHING BELOW THIS COMMENT

vertices :: WeightedGraph a w -> [a]
vertices (WG d) = D.keys d

isEmpty :: WeightedGraph a w -> Bool
isEmpty (WG d) = D.isEmpty d

mkWeightedGraphEdges :: (Ord a, Show a) => [a] -> [WeightedEdge a w] -> WeightedGraph a w
mkWeightedGraphEdges vs es = wg'
  where
    wg = foldl addVertex empty vs
    wg' = foldr (\(WE u w v) wg -> addEdge wg u v w) wg es

numVertices :: WeightedGraph a w -> Int
numVertices = length . vertices

numEdges :: (Eq a, Eq w) => WeightedGraph a w -> Int
numEdges = length . edges

instance (Eq a, Show a, Eq w, Show w) => Show (WeightedGraph a w) where
  show wg = "DictionaryWeightedGraph(" ++ vs ++ ", " ++ as ++ ")"
    where
      vs = "(" ++ intercalate ", " (map show (vertices wg)) ++ ")"
      as = "(" ++ intercalate ", " (map showEdge (edges wg)) ++ ")"
      showEdge (WE x w y) = intercalate "-" [show x, show w, show y]
