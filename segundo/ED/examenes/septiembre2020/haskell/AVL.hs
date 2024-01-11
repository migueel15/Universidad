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
maxRemainingCapacity Empty = 0
maxRemainingCapacity (Node bin h cap nl nr) = cap

height :: AVL -> Int
height Empty = 0
height (Node bin h cap nl nr) = h

nodeWithHeight :: Bin -> Int -> AVL -> AVL -> AVL
nodeWithHeight bin@(B c lista) altura nl nr = Node bin altura (maxcap) nl nr
  where
    maxcap = max (max (maxRemainingCapacity nl) (maxRemainingCapacity nr)) c

node :: Bin -> AVL -> AVL -> AVL
node bin@(B c lista) nl nr = Node bin getaltura getcapacidad nl nr
  where
    getaltura = max (height nl) (height nr) + 1
    getcapacidad = max (max (maxRemainingCapacity nl) (maxRemainingCapacity nr)) c

rotateLeft :: Bin -> AVL -> AVL -> AVL
rotateLeft c@(B cc listac) l@(Node bl hl cl nll nrl) (Node bx hx cx r1 r2) = raiz
  where
    nodoizq = node c l r1
    raiz = node bx nodoizq r2

addNewBin :: Bin -> AVL -> AVL
addNewBin bin@(B c lista) Empty = Node bin c 1 Empty Empty
addNewBin bin@(B bc lista) (Node x@(B xc listac) h c nl nr)
  | height nr - height nl > 1 = rotateLeft x nl (addNewBin bin nr)
  | otherwise = Node x h c nl (addNewBin bin nr)

addFirst :: Capacity -> Weight -> AVL -> AVL
addFirst cap wg Empty = addNewBin (B (cap - wg) [wg]) Empty
addFirst cap wg avl@(Node bin h c nl nr)
  | c < wg = addNewBin (B (cap - wg) [wg]) avl
  | maxRemainingCapacity nl >= wg = Node bin h c (addFirst cap wg nl) nr
  | maxRemainingCapacity avl >= wg = Node (addObject wg bin) h (c - wg) nl nr
  | otherwise = Node bin h c nl (addFirst cap wg nr)

addAll :: Capacity -> [Weight] -> AVL
addAll cap lista = foldr (\wg ini -> addFirst cap wg ini) Empty lista

toList :: AVL -> [Bin]
toList Empty = []
toList (Node bin h c nl nr) = toList nl ++ [bin] ++ toList nr

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
