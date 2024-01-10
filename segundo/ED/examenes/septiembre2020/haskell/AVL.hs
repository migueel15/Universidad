{------------------------------------------------------------------------------
 - Student's name:
 -
 - Student's group:
 -----------------------------------------------------------------------------}

module AVL
  ( Weight,
    Capacity,
    AVL (..),
    Bin,
    emptyBin,
    remainingCapacity,
    addObject,
    maxRemainingCapacity,
    height,
    nodeWithHeight,
    node,
    rotateLeft,
    addNewBin,
    addFirst,
    addAll,
    toList,
    linearBinPacking,
    seqToList,
    addAllFold,
  )
where

type Capacity = Int

type Weight = Int

data Bin = B Capacity [Weight]

data AVL = Empty | Node Bin Int Capacity AVL AVL deriving (Show)

emptyBin :: Capacity -> Bin
emptyBin c = B c []

remainingCapacity :: Bin -> Capacity
remainingCapacity (B c lista) = c

addObject :: Weight -> Bin -> Bin
addObject peso (B c lista)
  | peso > c = error "peso mayor a cantidad restante"
  | otherwise = B (c - peso) (peso : lista)

maxRemainingCapacity :: AVL -> Capacity
maxRemainingCapacity (Node bin h cap nl nr) = cap

height :: AVL -> Int
height (Node bin h cap nl nr) = h

nodeWithHeight :: Bin -> Int -> AVL -> AVL -> AVL
nodeWithHeight bin@(B c lista) altura nl nr = Node bin altura (maxcap) nl nr
  where
    maxcap = max (maxRemainingCapacity nl) (maxRemainingCapacity nr)

node :: Bin -> AVL -> AVL -> AVL
node _ _ _ = undefined

rotateLeft :: Bin -> AVL -> AVL -> AVL
rotateLeft _ _ _ = undefined

addNewBin :: Bin -> AVL -> AVL
addNewBin _ _ = undefined

addFirst :: Capacity -> Weight -> AVL -> AVL
addFirst _ _ _ = undefined

addAll :: Capacity -> [Weight] -> AVL
addAll _ _ = undefined

toList :: AVL -> [Bin]
toList _ = undefined

{-
	SOLO PARA ALUMNOS SIN EVALUACION CONTINUA
  ONLY FOR STUDENTS WITHOUT CONTINUOUS ASSESSMENT
 -}

data Sequence = SEmpty | SNode Bin Sequence deriving (Show)

linearBinPacking :: Capacity -> [Weight] -> Sequence
linearBinPacking _ _ = undefined

seqToList :: Sequence -> [Bin]
seqToList _ = undefined

addAllFold :: [Weight] -> Capacity -> AVL
addAllFold _ _ = undefined

{- No modificar. Do not edit -}

objects :: Bin -> [Weight]
objects (B _ os) = reverse os

instance Show Bin where
  show b@(B c os) = "Bin(" ++ show c ++ "," ++ show (objects b) ++ ")"
