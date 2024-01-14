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
----------------------------------------------

module Kruskal (kruskal, kruskals) where

import DataStructures.Dictionary.AVLDictionary qualified as D
import DataStructures.Graph.DictionaryWeightedGraph
import DataStructures.PriorityQueue.LinearPriorityQueue qualified as Q

fromJust :: Maybe a -> a
fromJust (Just a) = a

mkPQueue :: (Ord a) => [a] -> Q.PQueue a
mkPQueue xs = foldr Q.enqueue Q.empty xs

llenar xs = mkPQueue xs

kruskal :: (Ord a, Ord w) => WeightedGraph a w -> [WeightedEdge a w]
kruskal g = aux cola first (dicIni (vertices g))
  where
    cola = llenar (edges g)

    dicIni :: (Ord a) => [a] -> D.Dictionary a a
    dicIni [x] = D.insert x x (D.empty)
    dicIni (x : xs) = D.insert x x (dicIni xs)

    first = Q.first cola
    cola' = Q.dequeue cola

    representante :: (Ord a) => a -> D.Dictionary a a -> a
    representante x dic
      | x /= fromJust (D.valueOf x dic) = representante (fromJust (D.valueOf x dic)) dic
      | otherwise = x

    src :: (Ord a, Ord w) => WeightedEdge a w -> a
    src (WE a w b) = a
    dst :: (Ord a, Ord w) => WeightedEdge a w -> a
    dst (WE a w b) = b

    aux :: (Ord a, Ord w) => Q.PQueue (WeightedEdge a w) -> WeightedEdge a w -> D.Dictionary a a -> [WeightedEdge a w]
    aux cola primeroCola dicVertices
      | Q.isEmpty cola && representante (src primeroCola) dicVertices /= representante (dst primeroCola) dicVertices = [primeroCola]
      | Q.isEmpty cola && representante (src primeroCola) dicVertices == representante (dst primeroCola) dicVertices = []
      | not (Q.isEmpty cola) && representante (src primeroCola) dicVertices /= representante (dst primeroCola) dicVertices = primeroCola : aux cola'' first' (D.insert (dst primeroCola) (src primeroCola) dicVertices)
      | not (Q.isEmpty cola) && representante (src primeroCola) dicVertices == representante (dst primeroCola) dicVertices = aux cola'' first' dicVertices
      | otherwise = aux cola'' first' dicVertices
      where
        first' = Q.first cola
        cola'' = Q.dequeue cola

-- Solo para evaluación continua / only for part time students
kruskals :: (Ord a, Ord w) => WeightedGraph a w -> [[WeightedEdge a w]]
kruskals = undefined
