package stack;

public interface Stack<E> {
    void push(E elem);

    E top();

    void pop();

    boolean isEmpty();

    int size();
}
