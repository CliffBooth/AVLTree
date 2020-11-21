package avlTree;

import java.lang.reflect.Array;
import java.util.*;

public class AVLTree<T extends Comparable<T>> implements Set<T> {

    private static class Node<T> {
        final T value;
        int height;
        Node<T> left;
        Node<T> right;

        Node(T value) {
            this.value = value;
            height = 1;
        }
    }

    private Node<T> root;
    private int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private Node<T> find(T t) {
        if (root == null)
            return null;
        return find(root, t);
    }

    private Node<T> find(Node<T> node, T value) {
        int comparison = value.compareTo(node.value);
        if (comparison == 0)
            return node;
        if (comparison < 0) {
            if (node.left == null)
                return node;
            return find(node.left, value);
        }
        if (node.right == null)
            return node;
        return find(node.right, value);
    }

    @Override
    public boolean contains(Object o) {
        T t = (T) o;
        Node<T> node = find(t);
        if (node == null)
            return false;
        return node.value == t;
    }

    private int balanceFactor(Node<T> node) {
        if (node.left == null && node.right == null)
            return 0;
        if (node.right == null)
            return -node.left.height;
        if (node.left == null)
            return node.right.height;
        return node.right.height - node.left.height;
    }

    private Node<T> rotateRight(Node<T> node) {
        Node<T> child = node.left;
        node.left = child.right;
        child.right = node;
        node.height = calculateHeight(node);
        child.height = calculateHeight(child);
        return child;
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node<T> child = node.right;
        node.right = child.left;
        child.left = node;
        node.height = calculateHeight(node);
        child.height = calculateHeight(child);
        return child;
    }

    private int calculateHeight(Node<T> node) {
        if (node.left == null && node.right == null) {
            return 1;
        }
        if (node.left == null) {
            return node.right.height + 1;
        }
        if (node.right == null) {
            return node.left.height + 1;
        }
        return Math.max(node.left.height, node.right.height) + 1;
    }

    private Node<T> balance(Node<T> node) {
        int balanceFactor = balanceFactor(node);
        if (balanceFactor < -1) {
            if (balanceFactor(node.left) > 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }
        if (balanceFactor > 1) {
            if (balanceFactor(node.right) < 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }
        return node;
    }

    boolean elementFound;

    @Override
    public boolean add(T t) {
        elementFound = false;
        root = add(root, t);
        if (elementFound)
            return false;
        size++;
        return true;
    }

    private Node<T> add(Node<T> node, T value) {
        if (node == null) {
            return new Node<>(value);
        }
        int comparison = value.compareTo(node.value);
        if (comparison == 0) {
            elementFound = true;
            return node;
        }
        if (comparison < 0)
            node.left = add(node.left, value);
        else
            node.right = add(node.right, value);
        node.height = calculateHeight(node);
        return balance(node);
    }

    @Override
    public boolean remove(Object o) {
        elementFound = false;
        T t = (T) o;
        root = remove(root, t);
        if (!elementFound) {
            return false;
        }
        size--;
        return true;
    }

    private Node<T> remove(Node<T> node, T value) {
        if (node == null)
            return null;
        int comparison = value.compareTo(node.value);
        if (comparison < 0)
            node.left = remove(node.left, value);
        else if (comparison > 0)
            node.right = remove(node.right, value);
        else {
            elementFound = true;
            if (node.left == null || node.right == null) {
                if (node.left == null)
                    node = node.right;
                else
                    node = node.left;
            } else {
                Node<T> left = node.left;
                Node<T> right = node.right;
                Node<T> minNode = node.right;
                while (minNode.left != null)
                    minNode = minNode.left;
                node = new Node<>(minNode.value);
                node.left = left;
                node.right = remove(right, node.value);
            }
        }
        if (node == null)
            return null;
        node.height = calculateHeight(node);
        return balance(node);
    }

    @Override
    public Iterator<T> iterator() {
        return new AVLTreeIterator();
    }

    public class AVLTreeIterator implements Iterator<T> {
        private final List<Node<T>> list;
        private Node<T> current;
        private int index;

        public AVLTreeIterator() {
            index = 0;
            list = new ArrayList<>();
            fillList(root);
        }

        private void fillList(Node<T> node) {
            if (node == null)
                return;
            fillList(node.left);
            list.add(node);
            fillList(node.right);
        }

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public T next() {
            if (index >= list.size()) {
                throw new NoSuchElementException();
            }
            current = list.get(index);
            index++;
            return current.value;
        }

        @Override
        public void remove() {
            if (current == null)
                throw new IllegalStateException();
            AVLTree.this.remove(current.value);
            current = null;
        }
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Iterator<T> iterator = iterator();
        for (int i = 0; i < size; i++) {
            array[i] = iterator.next();
        }
        return array;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean modified = false;
        for (T t : c)
            if (add(t))
                modified = true;
        return modified;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        if (size > c.size()) {
            for (Object e : c)
                modified = remove(e);
        } else {
            for (Iterator<T> i = iterator(); i.hasNext(); ) {
                if (c.contains(i.next())) {
                    i.remove();
                    modified = true;
                }
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (Iterator<T> i = iterator(); i.hasNext(); ) {
            if (!c.contains(i.next())) {
                i.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    @Override
    public <E> E[] toArray(E[] a) {
        E[] array;
        if (a.length >= size)
            array = a;
        else
            array = (E[]) Array.newInstance(a.getClass().getComponentType(), size);
        Iterator<T> it = iterator();
        for (int i = 0; i < size; i++) {
            array[i] = (E) it.next();
        }
        return array;
    }


    List<T> list;

    @Override
    public String toString() {
        if (root == null)
            return "[]";
        list = new ArrayList<>();
        fillList(root);
        return list.toString();
    }

    private void fillList(Node<T> node) {
        list.add(node.value);
        if (node.left != null)
            fillList(node.left);
        if (node.right != null)
            fillList(node.right);
    }

    public int getRootBalance() {
        if (root == null)
            return 0;
        return balanceFactor(root);
    }

    public int getRootHeight() {
        if (root == null)
            return 0;
        return calculateHeight(root);
    }

}
