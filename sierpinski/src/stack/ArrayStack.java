package stack;

import java.util.NoSuchElementException;

public class ArrayStack<E> implements Stack<E> {
    private final int initial_capacity = 10;
    private Object[] elems = new Object[initial_capacity];

    @Override
    public void push(E elem) {
        int currentCapacity = elems.length;
        if (elems[currentCapacity - 1] != null) {
            resize(currentCapacity * 2);
        }
        elems[size()] = elem;
    }

    @Override
    public E top() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty stack");
        }
        @SuppressWarnings("unchecked")
        E elem = (E) elems[size() - 1];
        return elem;
    }

    @Override
    public void pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Empty stack");
        }
        int lastPos = size() - 1; //Obtenemos el size del Stack

        @SuppressWarnings("unchecked")
        E res = (E) elems[lastPos];
        int currentCapacity = elems.length;
        elems[lastPos] = null; //Eliminamos el elemento
        if (lastPos < (currentCapacity / 2)) {
            int size = currentCapacity - ((currentCapacity - lastPos) / 2);
            if (size > initial_capacity) {
                resize(size);
            }
        }
    }

    private void resize(int size) {
        Object[] elems = new Object[size];

        for (int i = 0; i < size(); i++) {
            elems[i] = this.elems[i];
        }
        this.elems = elems;
    }

    @Override
    public boolean isEmpty() {
        for (Object elem : elems) {
            if (elem != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        int counter = 0;
        for (Object elem : elems) {
            if (elem != null) {
                counter += 1;
            } else {
                break;
            }
        }
        return counter;
    }

    int capacity() {
        return elems.length;
    }
}
