module Demos.Trie.DictionaryStringTrieDemo where

import DataStructures.Trie.DictionaryStringTrie

sample = sampleTrie             -- sampleTrie1, sampleTrie2, sampleTrie3, sampleTrie4

-- Prelude> pretty sample
{-
Node Nothing
  b -> Node Nothing
         a -> Node Nothing
                t -> Node (Just 0)
         e -> Node (Just 1)
                d -> Node (Just 2)
  c -> Node Nothing
         a -> Node Nothing
                t -> Node (Just 3)
  t -> Node Nothing
         o -> Node (Just 4)
                e -> Node (Just 5)
-}

-- Prelude> size sample
-- 6


-- Prelude> search "bed" sample
-- Just 2
-- Prelude> search "cat" sample
-- Just 3
-- Prelude> search "how" sample
-- Nothing


-- Prelude> let sampleTrie' = insert "bat" 0 $ insert "be" 1 $ insert "bed" 2 $ insert "cat" 3 $ insert "to" 4 $ insert "toe" 5 $ empty
-- Prelude> pretty sampleTrie'
-- Same as sampleTrie

-- Prelude> strings sample
-- ["bat","be","bed"","cat","to","toe"]

-- Prelude>  pretty $ fromList ["how","thin","that","how","how","thin","hey"]
{-
Node Nothing
  h -> Node Nothing
         e -> Node Nothing
                y -> Node (Just 1)
         o -> Node Nothing
                w -> Node (Just 3)
  t -> Node Nothing
         h -> Node Nothing
                a -> Node Nothing
                       t -> Node (Just 1)
                i -> Node Nothing
                       n -> Node (Just 2)
-}