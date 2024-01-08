package demos.trie;

import dataStructures.list.ArrayList;
import dataStructures.list.List;
import dataStructures.trie.DictionaryStringTrie;

public class DictionaryStringTrieDemo {
    public static void main(String[] args) {
        DictionaryStringTrie<Integer> sampleTrie = DictionaryStringTrie.sampleTrie(); //sampleTrie1, sampleTrie2, sampleTrie3, sampleTrie4
        System.out.println("SampleTrie");
        System.out.println(sampleTrie);
        System.out.println("Size = " + sampleTrie.size()); // 6
        System.out.println("Value of bed = " + sampleTrie.search("bed")); // 2
        System.out.println("Value of cat = " + sampleTrie.search("cat")); // 3
        System.out.println("Value of how = " + sampleTrie.search("how")); // null

        DictionaryStringTrie<Integer> trie = new DictionaryStringTrie<>();
        trie.insert("bat",0);
        trie.insert("be",1);
        trie.insert("bed",2);
        trie.insert("cat",3);
        trie.insert("to",4);
        trie.insert("toe",5);
        System.out.println("\nSampleTrie built with constructor and insert");
        System.out.println(trie);  // same as sampleTrie

        System.out.println("Strings = " +sampleTrie.strings());

        List<String> list = new ArrayList<>();
        list.append("how");
        list.append("thin");
        list.append("that");
        list.append("how");
        list.append("how");
        list.append("thin");
        list.append("hey");
        System.out.println("fromList " +  list);
        System.out.println(DictionaryStringTrie.fromList(list));
    }
}
/* Output:
SampleTrie
Node null
	b -> Node null
		a -> Node null
			t -> Node 0
		e -> Node 1
			d -> Node 2
	c -> Node null
		a -> Node null
			t -> Node 3
	t -> Node null
		o -> Node 4
			e -> Node 5

Size = 6
Value of bed = 2
Value of cat = 3
Value of how = null

SampleTrie built with constructor and insert
Node null
	b -> Node null
		a -> Node null
			t -> Node 0
		e -> Node 1
			d -> Node 2
	c -> Node null
		a -> Node null
			t -> Node 3
	t -> Node null
		o -> Node 4
			e -> Node 5

Strings = ArrayList(bat, be, bed, cat, to, toe)
fromList ArrayList(how, thin, that, how, how, thin, hey)
Node null
	h -> Node null
		e -> Node null
			y -> Node 1
		o -> Node null
			w -> Node 3
	t -> Node null
		h -> Node null
			a -> Node null
				t -> Node 1
			i -> Node null
				n -> Node 2
*/