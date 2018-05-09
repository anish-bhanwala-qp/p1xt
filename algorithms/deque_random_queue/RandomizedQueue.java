import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int itemCount;
    private int currentIndex;

    /* construct an empty randomized queue*/
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        currentIndex = -1;
    }

    /* is the randomized queue empty?*/
    public boolean isEmpty() {
        return itemCount == 0;
    }

    /* return the number of items on the randomized queue*/
    public int size() {
        return itemCount;
    }

    private void doubleArrayCapacity() {
        createNewArray(items.length * 2);
    }

    private void halfArrayCapacity() {
        createNewArray(items.length / 2);
    }

    private void createNewArray(int capacity) {
        Item[] newArray = (Item[]) new Object[capacity];
        for (int i = 0; i < itemCount; i++) {
            newArray[i] = items[i];
        }

        items = newArray;
    }

    private void throwExceptionIfItemNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
    }

    /* add the item*/
    public void enqueue(Item item) {
        throwExceptionIfItemNull(item);

        if (isArrayFull()) {
            doubleArrayCapacity();
        }

        items[++currentIndex] = item;
        itemCount++;
    }

    private boolean isArrayFull() {
        return items.length == currentIndex + 1;
    }

    private boolean isArray75PercentCapacity() {
        if (itemCount <= 1) {
            return false;
        }
        return items.length / 4 == (currentIndex + 1);
    }

    /* remove and return a random item*/
    public Item dequeue() {
        throwExceptionIfEmpty();

        int randomIndex = getRandomIndex();
        Item val = items[randomIndex];

        /* replace randomIndex element with currentIndex*/
        items[randomIndex] = items[currentIndex];
        items[currentIndex] = null;
        --currentIndex;

        if (isArray75PercentCapacity()) {
            halfArrayCapacity();
        }

        itemCount--;

        return val;
    }

    private void throwExceptionIfEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
    }

    /* return a random item (but do not remove it)*/
    public Item sample() {
        throwExceptionIfEmpty();

        return items[getRandomIndex()];
    }

    private int getRandomIndex() {
        if (currentIndex == 0) {
            return 0;
        }
        return StdRandom.uniform(currentIndex + 1);
    }

    /* return an independent iterator over items in random order*/
    public Iterator<Item> iterator() {
        Item[] arrayCopy = (Item[]) new Object[itemCount];
        for (int i = 0; i < itemCount; i++) {
            arrayCopy[i] = items[i];
        }

        return new MyIterator<>(arrayCopy);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item i : items) {
            s.append(i).append(" ");
        }

        return s.toString();
    }

    private static <T> void enq(RandomizedQueue<T> randomizedQueue, T t) {
        randomizedQueue.enqueue(t);
        System.out.printf("enqueue %s, result: %s\n", t, randomizedQueue.toString());
    }

    private static <T> void deq(RandomizedQueue<T> randomizedQueue) {
        T t = randomizedQueue.dequeue();
        System.out.printf("dequeue %s, result: %s\n", t, randomizedQueue.toString());
    }

    public static void main(String[] args) {
        RandomizedQueue<String> randomQueue = new RandomizedQueue<>();

        enq(randomQueue, "a");
        enq(randomQueue, "b");
        enq(randomQueue, "c");
        enq(randomQueue, "d");
        enq(randomQueue, "e");
        enq(randomQueue, "f");

        for (String val : randomQueue) {
            System.out.printf("Iterator val: %s\n", val);
        }

        deq(randomQueue);
        deq(randomQueue);
        deq(randomQueue);
        deq(randomQueue);
        deq(randomQueue);
        deq(randomQueue);
    }

    private class MyIterator<Item> implements Iterator<Item> {
        private int maxIndex;
        private Item[] items;

        public MyIterator(Item[] items) {
            this.maxIndex = items.length - 1;
            this.items = items;
        }

        private int getRandomIndex() {
            if (maxIndex == 0) {
                return 0;
            }
            return StdRandom.uniform(maxIndex + 1);
        }

        @Override
        public boolean hasNext() {
            return maxIndex >= 0;
        }

        @Override
        public Item next() {
            if (maxIndex < 0) {
                throw new NoSuchElementException();
            }
            int randomIndex = getRandomIndex();
            Item val = items[randomIndex];

            /* replace randomIndex element with currentIndex*/
            items[randomIndex] = items[maxIndex];
            maxIndex--;

            return val;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}