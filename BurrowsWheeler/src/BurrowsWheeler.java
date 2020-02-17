import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BurrowsWheeler {
  private static final int R = 256;

  //Time O(n) excluding the building circular suffix array
  public static void transform() {
    StringBuilder sb = new StringBuilder();
    while (!BinaryStdIn.isEmpty()) {
      sb.append(BinaryStdIn.readChar());
    }
    BinaryStdIn.close();
    String s = sb.toString();
    CircularSuffixArray csa = new CircularSuffixArray(s);
    // t[]: the substring t[i] is the last char of ith substring in sorted array which is the index[i]th substring in original array
    // the last char of the kth substring in the original array is s.charAt((k - 1 + len) % len)
    // t[i] = s.charAt((index[i] - 1 + len) % len)
    int len = s.length();
    char[] T = new char[len];
    int first = 0;
    for (int i = 0;i < len;i++) {
      int index = csa.index(i);
      if (index == 0) {
        first = i;
      }
      T[i] = s.charAt((index - 1 + len) % len);
    }
    BinaryStdOut.write(first);
    for (char c : T) {
      BinaryStdOut.write(c);
    }
    BinaryStdOut.close();
  }

  //Time O(n + R)
  public static void inverseTransform() {
    int first = BinaryStdIn.readInt();
    List<Character> T = new ArrayList<>();
    while (!BinaryStdIn.isEmpty()) {
      char curr = BinaryStdIn.readChar();
      T.add(curr);
    }
    char[] H = new char[T.size()];
    int k = 0;
    for (char c : T) {
      H[k++] = c;
    }
    H = radixSort(H);//O(n + R)
    int[] next = new int[T.size()];
    // H[i] = T[j] then next[i] = j, if all char are unique
    // duplicate char maintain their relative order in H and T
    // we can use a queue or list here
    Map<Character, List<Integer>> map = new HashMap<>();
    for (int i = 0;i < T.size();i++) {
      List<Integer> lst = map.getOrDefault(T.get(i), new LinkedList<>());
      lst.add(i);//lst is sorted
      map.put(T.get(i), lst);
    }
    for (int i = 0;i < H.length;i++) {
      List<Integer> lst = map.get(H[i]);
      next[i] = lst.get(0);
      lst.remove(0);
    }
    //reconstruct the original string
    for(int i = 0;i < next.length;i++){
      BinaryStdOut.write(H[first]);
      first = next[first];
    }
    BinaryStdOut.close();
  }

  private static char[] radixSort(char[] array) {
    int[] count = new int[R + 1];
    for (char c : array) {
      count[c + 1]++;
    }
    for (int i = 1;i <= R;i++) {
      count[i] = count[i] + count[i - 1];
    }
    char[] tmp = new char[array.length];
    for (char c : array) {
      tmp[count[c]++] = c;
    }
    return tmp;
  }

  public static void main(String[] args) {
    if (args[0].equals("-")) {
      transform();
    } else if (args[0].equals("+")) {
      inverseTransform();
    }
  }
}
