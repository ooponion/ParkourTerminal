package parkourterminal.data.inputdata.intf;

public class IndexedStack<T> {
    private final Object[] elements;
    private final int capacity;
    private int size;

    public IndexedStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.elements = new Object[capacity];
        this.size = 0;
    }

    public void push(T obj) {
        if (capacity == 1) {
            elements[0] = obj;
            if (size < 1) {
                size = 1;
            }
            return;
        }
        int end = size < capacity ? size : capacity - 1;
        for (int i = end; i > 0; i--) {
            elements[i] = elements[i - 1];
        }
        elements[0] = obj;
        if (size < capacity) {
            size++;
        }
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return (T) elements[index];
    }

    public int size() {
        return size;
    }
}