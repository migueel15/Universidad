/******************************************************************************
 * Student's name:
 * Student's group:
 * Data Structures. Grado en Informática. UMA.
******************************************************************************/

package dataStructures.vector;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SparseVector<T> implements Iterable<T> {

    protected interface Tree<T> {
        T get(int sz, int i);
        Tree<T> set(int sz, int i, T x);
    }

    // Unif Implementation

    protected static class Unif<T> implements Tree<T> {

        private T elem;

        public Unif(T e) {
            elem = e;
        }

        @Override
        public T get(int sz, int i) {
            // TODO
            return this.elem;
        }

        @Override
        public Tree<T> set(int sz, int i, T x) {
            // TODO
            if(sz == 1){
                return new Unif<>(x);
            }else{
                int lim = (sz/2)-1;
                Node<T> tree;
                if(i <= lim){
                    tree = new Node<>(set(sz/2,i,x),new Unif<>(this.elem));
                }else{
                    tree = new Node<>(new Unif<>(this.elem),set(sz/2,i-(lim+1),x));
                }
                return tree;
            }
        }

        @Override
        public String toString() {
            return "Unif(" + elem + ")";
        }
    }

    // Node Implementation

    protected static class Node<T> implements Tree<T> {

        private Tree<T> left, right;

        public Node(Tree<T> l, Tree<T> r) {
            left = l;
            right = r;
        }

        @Override
        public T get(int sz, int i) {
            // TODO
            int lim = (sz/2)-1;
            if(i <= lim){
                return left.get(lim,i);
            }else{
                return right.get(lim,i-sz/2);
            }
        }

        @Override
        public Tree<T> set(int sz, int i, T x) {
            // TODO
            int lim = (sz/2)-1;
            if(sz <= lim){
                left = left.set(lim,i,x);
            }else{
                right = right.set(lim,i-sz/2,x);
            }
            simplify();
            return this;
        }

        protected Tree<T> simplify() {
            // TODO
            if(this.left instanceof Unif<?> && this.right instanceof Unif<?> && left.get(1,0).equals(right.get(1,0))){
                return (Unif<T>) left;
            }else{
                return this;
            }
        }

        @Override
        public String toString() {
            return "Node(" + left + ", " + right + ")";
        }
    }

    // SparseVector Implementation

    private int size;
    private Tree<T> root;

    public SparseVector(int n, T elem) {
        // TODO
        if(n < 0){
            throw new VectorException("N menor que 0");
        }
        this.size = (int) Math.pow(2,n);
        this.root = new Unif<>(elem);
    }

    public int size() {
        // TODO
        return this.size;
    }

    public T get(int i) {
        // TODO
        return root.get(this.size,i);
    }

    public void set(int i, T x) {
        // TODO
        root = root.set(this.size,i,x);
    }

    @Override
    public Iterator<T> iterator() {
        // TODO
        return new SparseVectorIterator<>();
    }

    private class SparseVectorIterator<T> implements Iterator<T>{
        int current;

        public SparseVectorIterator(){
            current = 0;
        }

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public T next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            T x = (T) root.get(size,current);
            current++;
            return x;
        }
    }

    @Override
    public String toString() {
        return "SparseVector(" + size + "," + root + ")";
    }
}
