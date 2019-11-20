import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
  public static void main(String[] args) {
    RandomizedQueue<String> r = new RandomizedQueue<>();
    int k = Integer.parseInt(args[0]);
    int count = 0;
    // Implement reservoir sampling algorithm to reduce space and make it online
    while (!StdIn.isEmpty()) {
      count++;
      if (r.size() < k) {
        r.enqueue(StdIn.readString());
      } else if (StdRandom.uniform() < (k + 0.0) / count) {
        r.dequeue();
        r.enqueue(StdIn.readString());
      }
    }
    for (String s : r) {
      StdOut.println(s);
    }
  }
}
