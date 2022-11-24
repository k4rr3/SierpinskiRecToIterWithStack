package stack;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ArrayStackTest {
    Object[] objects = new Object[]{"A", "B", "C", "D", "E", "F", "G"};
    ArrayStack<Object> arr = new ArrayStack<Object>();

    @Test
    public <E> void createStackAndCheckElem() {

        @SuppressWarnings("unchecked")
        E elem = (E) objects[0];
        arr.push(elem);
        assertEquals("A", arr.top());
    }

    @Test
    public <E> void addAnElementAndDeleteIt() {
        @SuppressWarnings("unchecked")
        E elem = (E) objects[0];
        @SuppressWarnings("unchecked")
        E elem1 = (E) objects[1];

        arr.push(elem);
        arr.push(elem1);
        arr.pop();
        assertEquals("A", arr.top());
    }

    @Test
    public <E> void getElementOfAnEmptyStack() {
        try {
            arr.top();
        } catch (NoSuchElementException noSuchElementException) {
            System.out.println("NoSuchElementException is successfully thrown when getting the top element");
        }

    }

    @Test
    public <E> void deleteElementOnEmptyStack() {
        try {
            arr.pop();
        } catch (NoSuchElementException noSuchElementException) {
            System.out.println("NoSuchElementException is successfully thrown when popping an element");
        }

    }

    @Test
    public <E> void checkFilledStack() {
        @SuppressWarnings("unchecked")
        E elem = (E) objects[0];
        int initial_capacity = arr.capacity();
        for (int i = 0; i < initial_capacity; i++)
            arr.push(elem);
        @SuppressWarnings("unchecked")
        E elem2 = (E) "FINAL";
        arr.push(elem2);
        assertEquals(arr.top(), "FINAL");
        assertEquals(initial_capacity, arr.capacity() / 2);
    }

    @Test
    public <E> void fillAndPopElementByElement() {
        for (Object object : objects) {
            arr.push(object);
        }
        assertEquals(arr.top(), "G");
        arr.pop();
        assertEquals(arr.top(), "F");
        arr.pop();
        assertEquals(arr.top(), "E");
        arr.pop();
        assertEquals(arr.top(), "D");
        arr.pop();
        assertEquals(arr.top(), "C");
        arr.pop();
        assertEquals(arr.top(), "B");
        arr.pop();
        assertEquals(arr.top(), "A");
    }

    @Test
    public <E> void checkPopResizing() {
        for (int i = 0; i< arr.capacity(); i++){
            arr.push(Integer.toString(i));
        }
        int size = arr.capacity();
        for (int i = 0; i < 5; i++){
            arr.pop();
        }
        assertEquals(size, arr.capacity());
    }

    @Test
    public <E> void validEmptyStack() {
        assertEquals(arr.size(), 0);
        assertTrue(arr.isEmpty());
    }

}