-- =====================================================
-- ============ Puntuación máxima 1.25 =================
-- =====================================================
-- Una Ruleta es una estructura de datos que permite simular el movimiento de una
-- "ruleta mecánica";

-- consideremos n objetos colocados en forma circular y un puntero que "apunta" a la
-- posición de uno de ellos cualquiera (elemento destacado).
-- La ruleta al principio está colocada señalando cierto objeto destacado.
module DataStructures.Roulette.QueueRoulette
  ( empty, -- :: Roulette a
    isEmpty, -- :: Roulette a -> Bool
    sign, -- :: Roulette a ->  a
    turn, -- :: Integer ->  Roulette a ->  Roulette a
    delete, -- :: Roulette a ->  Roulette a
    insert, -- :: a  ->  Roulette a ->  Roulette a
    mapRoulette, -- :: (a -> b) -> Roulette a -> Roulette b
    listToRoulette, -- :: [a] -> Roulette a
    rouletteToList, -- :: Roulette a -> [a]
  )
where

import Data.List (intercalate)
import DataStructures.Queue.LinearQueue qualified as Q
import Test.QuickCheck

-- Vamos a implementar la ruleta con una cola. Además llevaremos la cuenta de los
-- elementos que contiene la ruleta.
-- INVARIANTE: El objeto destacado siempre será la cima de la cola
-- Los siguientes elementos de la ruleta en sentido horario serán los
-- sucesivos elementos de la cola

data Roulette a = R (Q.Queue a) Integer deriving (Eq)

-- La ruleta que contiene los numeros del 1 al 10 en sentido horario
-- y está señalando al 3 será representado por el dato
-- R LinearQueue(3,4,5,6,7,8,9,10,1,2) 10
-- Su show será QueueRoulette:10(3,4,5,6,7,8,9,10,1,2)
--                    x
--                    3
--                2       4
--              1           5
--             10           6
--                9       7
--                    8
-- NOTA: Todas las operaciones con la cola deben cualificarse con Q: Q.Queue, Q.enqueue, Q.first, etc.
sample1 = R (foldl (flip Q.enqueue) Q.empty [3, 4, 5, 6, 7, 8, 9, 10, 1, 2]) 10

-- ===========================================================
-- Ejercicio 1 (0.05 ptos.)
-- Crea una ruleta vacia
empty :: Roulette a
empty = R Q.empty 0

-- ===========================================================
-- Ejercicio 2 (0.05 ptos.)
-- Determina si una ruleta está vacia
isEmpty :: Roulette a -> Bool
isEmpty (R q r) = Q.isEmpty q

-- isEmpty (R q r) = if Q.isEmpty q then True else False

-- ===========================================================
-- Ejercicio 3 (0.10 ptos.)
-- devuelve el dato apuntado
sign :: Roulette a -> a
sign (R q _)
  | Q.isEmpty q = error "Ruleta vacia"
  | otherwise = Q.first q

{-
Prelude (QueueRoulette.hs)> sign sample1
3
-}
-- ===========================================================
-- Ejercicio 4 (0.20 ptos.)
-- turn gira la ruleta un determinado número de elementos
-- turn n r      moverá (avanzará) el puntero de la ruleta r en sentido horario n posiciones
-- turn (-n) r   moverá el puntero de la ruleta r en sentido antihorario n posiciones
-- n puede ser cualquier número entero
turn :: Integer -> Roulette a -> Roulette a
turn n r@(R q tam)
  | n >= 0 = turnMod (mod n tam) r
  | n < 0 = turn (n + tam) r

turnMod :: Integer -> Roulette a -> Roulette a
turnMod n r@(R q tam)
  | n == 0 = r
  | otherwise = turnMod (n - 1) (R (Q.enqueue (Q.first q) (Q.dequeue q)) tam)

{-
turn 12 sample1
QueueRoulette:10(5,6,7,8,9,10,1,2,3,4)
Prelude (QueueRoulette.hs)> turn (-12) sample1
QueueRoulette:10(1,2,3,4,5,6,7,8,9,10)
-}
-- ===========================================================
-- Ejercicio 5 (0.10 ptos.)
-- elimina el elemento situado en la posición del puntero y coloca el puntero
-- en la siguiente posición en sentido horario
delete :: Roulette a -> Roulette a
delete r@(R q tam)
  | Q.isEmpty q = r
  | otherwise = R (Q.dequeue q) (tam - 1)

{-
delete sample1
QueueRoulette:9(4,5,6,7,8,9,10,1,2)
-}
-- ===========================================================
-- Ejercicio 6 (0.15 ptos.)
-- inserta el elemento en la posición del puntero y corre el resto en sentido horario
-- El insertado pasa a ser el destacado
insert :: a -> Roulette a -> Roulette a
insert num r@(R q tam) = turn (-1) (R (Q.enqueue num q) (tam + 1))

{-
Prelude (QueueRoulette.hs)> insert 20 sample1
QueueRoulette:11(20,3,4,5,6,7,8,9,10,1,2)
Prelude (QueueRoulette.hs)> delete $ insert 20 sample1
QueueRoulette:10(3,4,5,6,7,8,9,10,1,2)
Prelude (QueueRoulette.hs)> insert 2 empty
QueueRoulette:1(2)
Prelude (QueueRoulette.hs)> delete $ insert 2 empty
QueueRoulette:0()
-}
-- ===========================================================
-- Ejercicio 7 (0.15 ptos.)
-- genera una ruleta con los objetos de la lista situados en orden horario y con el puntero apuntando al primero
listToRoulette :: [a] -> Roulette a
listToRoulette xs = listAux xs empty

listAux :: [a] -> Roulette a -> Roulette a
listAux [] r@(R q tam) = r
listAux (x : xs) r@(R q tam) = listAux xs (R (Q.enqueue x q) (tam + 1))

-- ===========================================================
-- Ejercicio 8 (0.15 ptos.)
-- genera una lista con los elementos de una ruleta. El primero será el apuntado por el
-- puntero y luego irán los elementos en sentido horario
rouletteToList :: Roulette a -> [a]
rouletteToList r@(R q tam)
  | tam == 0 = []
  | otherwise = Q.first q : rouletteToList (delete r)

-- ===========================================================
-- Ejercicio 9 (0.20 ptos.)
-- mapRoulette toma una función de a->b y se la aplica a todos los elementos
-- de la ruleta quedando la ruleta en la misma posición
mapRoulette :: (a -> b) -> Roulette a -> Roulette b
mapRoulette f r@(R q tam) = listToRoulette (map f (rouletteToList r))

-- ===========================================================
-- Ejercicio 9 (0.10 ptos.)
-- probar con quickCheck que para cualquier n y cualquier ruleta girar n a la derecha
-- y luego n a la izquierda produce la misma ruleta. Las ruletas son Arbitray por lo
-- que pueden aparecer como argumentos de una propiedad
p1 :: Integer -> Roulette Integer -> Bool
p1 n r@(R q tam)
  | Q.isEmpty q = True
  | otherwise = turn (-n) (turn n r) == r

-- =============================================================================
-- ========================= NO TOCAR DE AQUÍ PARA ABAJO =======================
instance (Show a) => Show (Roulette a) where
  show (R q size) = "QueueRoulette:" ++ show size ++ "(" ++ (intercalate "," (aux q)) ++ ")"
    where
      aux q1
        | Q.isEmpty q1 = []
        | otherwise = show x : aux q'
        where
          x = Q.first q1
          q' = Q.dequeue q1

instance (Arbitrary a) => Arbitrary (Roulette a) where
  arbitrary = do
    xs <- listOf arbitrary
    return (foldr insert empty xs)