
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int size;

    /* construct an empty deque*/
    public Deque() {

    }

    /* is the deque empty?*/
    public boolean isEmpty() {
        return first == null;
    }

    /* return the number of items on the deque*/
    public int size() {
        return size;
    }

    private void incrementSize() {
        size++;
    }

    private void decrementSize() {
        size--;
    }

    private void throwExceptionIfItemNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
    }

    /* add the item to the front*/
    public void addFirst(Item item) {
        throwExceptionIfItemNull(item);
        Node<Item> oldFirst = first;
        first = new Node<>();
        first.item = item;
        first.next = oldFirst;

        if (oldFirst == null) {
            last = first;
        } else {
            oldFirst.previous = first;
        }

        incrementSize();
    }

    /*  add the item to the end*/
    public void addLast(Item item) {
        throwExceptionIfItemNull(item);
        Node<Item> oldLast = last;
        last = new Node<>();
        last.item = item;
        last.previous = oldLast;

        /* if this is the only node*/
        if (oldLast == null) {
            first = last;
        } else {
            oldLast.next = last;
        }
        incrementSize();
    }

    /*  remove and return the item from the front*/
    public Item removeFirst() {
        throwExceptionIfListEmpty();
        Item item = first.item;
        first = first.next;

        if (first == null) {
            last = null;
        } else {
            first.previous = null;
        }
        decrementSize();

        return item;
    }

    private void throwExceptionIfListEmpty() {
        if (first == null) {
            throw new NoSuchElementException("Queue empty");
        }
    }

    /*  remove and return the item from the end*/
    public Item removeLast() {
        throwExceptionIfListEmpty();
        Item item = last.item;
        last = last.previous;

        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        decrementSize();

        return item;
    }

    /*  return an iterator over items in order from front to end*/
    public Iterator<Item> iterator() {
        return new DequeueIterator<>(first);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        Node<Item> el = first;
        while (el != null) {
            s.append(el.item);
            el = el.next;
        }

        return s.toString();
    }

    private static <T> void addF(Deque<T> deque, T t) {
        deque.addFirst(t);
        System.out.printf("addFirst %s, result: %s\n", t, deque.toString());
    }

    private static <T> void addL(Deque<T> deque, T t) {
        deque.addLast(t);
        System.out.printf("addLast %s, result: %s\n", t, deque.toString());
    }

    private static <T> void removeF(Deque<T> deque) {
        T t = deque.removeFirst();
        System.out.printf("removeFirst %s, result: %s\n", t, deque.toString());
    }

    private static <T> void removeL(Deque<T> deque) {
        T t = deque.removeLast();
        System.out.printf("removeLast %s, result: %s\n", t, deque.toString());
    }

    public static void main(String[] args) {
        Deque<String> deck = new Deque<>();

        addF(deck, "a");
        addL(deck, "b");
        addF(deck, "c");
        addL(deck, "d");
        addF(deck, "e");
        addF(deck, "f");

        for (String val : deck) {
            System.out.printf("Iterator val: %s\n", val);
        }

        removeF(deck);
        removeL(deck);
        removeF(deck);
        removeL(deck);
        removeF(deck);
    }

    private class Node<I> {
        private I item;
        private Node<I> next;
        private Node<I> previous;
    }

    private class DequeueIterator<T> implements Iterator<T> {
        private Node<T> currentNode;

        public DequeueIterator(Node<T> currentNode) {
            this.currentNode = currentNode;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (currentNode == null) {
                throw new NoSuchElementException("No more elements");
            }
            T item = currentNode.item;
            currentNode = currentNode.next;

            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove method not supported");
        }
    }
}
