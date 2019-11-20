import java.util.Iterator;
import java.util.NoSuchElementException;

// Memory: 48N + 32 + 16 + 4 + 16 = 48N + 68
public class Deque<Item> implements Iterable<Item> {
  private Node head;
  private Node tail;
  private int size;

  // Memory analysis: object overhead + inner class extra + ref * 3 = 48
  private class Node {
    Item e;
    Node prev;
    Node next;
    Node (Item e) {
      this.e = e;
    }
  }
  // construct an empty deque
  public Deque() {
    size = 0;
  }

  // is the deque empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // return the number of items on the deque
  public int size() {
    return size;
  }

  // add the item to the front
  // handle both the prev and next pointers
  public void addFirst(Item e) {
    checkNull(e);
    Node curr = new Node(e);
    if (size == 0) {
      head = curr;
      tail = curr;
    } else {
      Node temp = head;
      head = curr;
      head.next = temp;
      temp.prev = head;
    }
    size++;
  }

  // add the item to the back
  // handle both the prev and next pointers
  public void addLast(Item e) {
    checkNull(e);
    Node curr = new Node(e);
    if (size == 0) {
      head = curr;
      tail = curr;
    } else {
      Node temp = tail;
      tail = curr;
      tail.prev = temp;
      temp.next = tail;
    }
    size++;
  }

  // remove and return the item from the front
  public Item removeFirst() {
    checkEmpty();
    Item temp = head.e;
    head = head.next;
    if (head != null) {
      head.prev = null;
    }
    size--;
    return temp;
  }

  // remove and return the item from the back
  public Item removeLast() {
    checkEmpty();
    Item temp = tail.e;
    tail = tail.prev;
    if (tail != null) {
      tail.next = null;
    }
    size--;
    return temp;
  }

  // return an iterator over items in order from front to back
  public Iterator<Item> iterator(){
    return new myIterator();
  }

  // Memory: object overhead(16 byte) + inner class extra(8 byte) + ref = 32
  private class myIterator implements Iterator<Item> {
    private Node current = head;

    public boolean hasNext() {
      return current != null;
    }

    public Item next() {
      if (current == null) {
        throw new NoSuchElementException("No element left");
      }
      Item temp = current.e;
      current = current.next;
      return temp;
    }

    public void remove() {
      throw new UnsupportedOperationException("Remove operation is not supported");
    }
  }

  private void checkNull(Item e) {
    if (e == null) {
      throw new IllegalArgumentException("The input shouldn't be null");
    }
  }

  private void checkEmpty() {
    if (size == 0) {
      throw new NoSuchElementException("The deque is empty");
    }
  }

  // unit testing (required)
  public static void main(String[] args) {
    Deque<Integer> test = new Deque<>();
    test.addFirst(1);
    test.addFirst(2);
    test.addFirst(3);
    test.addFirst(4);
    test.addFirst(5);
    test.addFirst(6);
    for (int i : test) {
      System.out.print(i);
    }
    System.out.println();
    System.out.println(test.size());
    test.addLast(1);
    test.addLast(2);
    test.addLast(3);
    for (int i : test) {
      System.out.print(i);
    }
    System.out.println();
    System.out.println(test.size());
    test.removeLast();
    test.removeLast();
    test.removeLast();
    for (int i : test) {
      System.out.print(i);
    }
    System.out.println();
    System.out.println(test.size());
    test.removeLast();
    test.removeLast();
    test.removeLast();
    for (int i : test) {
      System.out.print(i);
    }
    System.out.println();
    System.out.println(test.size());
    test.removeFirst();
    test.removeFirst();
    test.removeFirst();
    for (int i : test) {
      System.out.print(i);
    }
    System.out.println();
    System.out.println(test.size());
    test.addFirst(1);
    for (int i : test) {
      System.out.print(i);
    }
  }
}
