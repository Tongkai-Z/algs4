import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
  // key insight: remove an element for an unordered array, we can simply swap
  // the current element with the last element and delete the last element
  // Implement this queue by resizable array
  private static final int INITIAL_CAPACITY = 1;
  private static final int GROWTH_RATE = 2;
  private static final int HALVE_FACTOR = 4;
  private Item[] element;
  private int size;

  public RandomizedQueue() {
    element = (Item[]) new Object[INITIAL_CAPACITY];
    size = 0;
  }

  // is the randomized queue empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // return the number of items on the randomized queue
  public int size() {
    return size;
  }

  // add the item
  public void enqueue(Item e) {
    checkNull(e);
    if (size == element.length) {
      resize(element.length * GROWTH_RATE);
    }
    element[size++] = e;
  }

  // remove and return a random item
  public Item dequeue() {
    // pick a random index
    checkEmpty();
    int ran = StdRandom.uniform(size);
    Item temp = element[ran];
    element[ran] = element[--size];
    element[size] = null;// avoid loitering
    if (size != 0 && element.length / size == HALVE_FACTOR) {
      resize(element.length / GROWTH_RATE);
    }
    return temp;
  }

  // return a random item (but do not remove it)
  public Item sample() {
    checkEmpty();
    int ran = StdRandom.uniform(size);
    return element[ran];
  }

  // return an independent iterator over items in random order
  public Iterator<Item> iterator() {
    return new RandomIterator();
  }

  private class RandomIterator implements Iterator<Item> {
    private int current;
    private int[] visitOrder;

    RandomIterator() {
      visitOrder = StdRandom.permutation(size);
      current = 0;
    }

    public boolean hasNext() {
      return current != size;
    }

    public Item next() {
      if (current == size) {
        throw new NoSuchElementException("No element left");
      }
      return element[visitOrder[current++]];
    }

    public void remove() {
      throw new UnsupportedOperationException("Remove operation is not supported");
    }
  }

  private void resize(int len) {
    Item[] newElement = (Item[]) new Object[len];
    int index = 0;
    for (int i = 0;i < size;i++) {
      newElement[index++] = element[i];
    }
    element = newElement;
  }
  private void checkNull(Item e) {
    if (e == null) {
      throw new IllegalArgumentException("The input shouldn't be null");
    }
  }

  private void checkEmpty() {
    if (size == 0) {
      throw new NoSuchElementException("The RandomQueue is empty");
    }
  }

  // unit testing (required)
  public static void main(String[] args) {
    RandomizedQueue<Integer> test = new RandomizedQueue<>();
    for (int i = 0;i < 10;i++) {
      test.enqueue(i);
    }
    System.out.println(test.sample());
    System.out.println(test.size());
    test.dequeue();
    System.out.println(test.size());
    Iterator<Integer> iter = test.iterator();
    Iterator<Integer> iter2 = test.iterator();
    System.out.println(iter.next());
    System.out.println(iter2.next());
  }
}
