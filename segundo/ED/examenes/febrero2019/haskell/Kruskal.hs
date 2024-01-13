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

kruskal :: (Ord a, Ord w) => WeightedGraph a w -> [WeightedEdge a w]
kruskal = undefined

-- Solo para evaluación continua / only for part time students
kruskals :: (Ord a, Ord w) => WeightedGraph a w -> [[WeightedEdge a w]]
kruskals = undefined
