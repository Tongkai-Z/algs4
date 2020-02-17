import java.util.Arrays;

public class CircularSuffixArray {
  private int[] index;

  private static class CircularSuffix {
    private String s;
    private int start;
    private CircularSuffix(String s, int start) {
      this.s = s;
      this.start = start;
    }
    private char charAt(int index) {
      return s.charAt((index + start) % s.length());
    }
  }
  // time constraint nlogn, n is the length of the string
  // space constraint n + R
  // three-way string quick sort
  public CircularSuffixArray(String s) {
    if (s == null) {
      throw new IllegalArgumentException("Input can't be null");
    }
    index = new int[s.length()];
    // initialization
    CircularSuffix[] suffixes = new CircularSuffix[s.length()];
    for (int i = 0;i < suffixes.length;i++) {
      suffixes[i] = new CircularSuffix(s, i);
    }
    // three-way string quick sort NlnN
    quick3string(suffixes);
    for (int i = 0;i < index.length;i++) {
      index[i] = suffixes[i].start;
    }

  }

  public int length() {
    return index.length;
  }

  public int index(int i ) {
    if (i < 0 || i >= index.length) {
      throw new IllegalArgumentException("Index is out of bound");
    }
    return index[i];
  }

  private void quick3string(CircularSuffix[] cs) {
    quick3string(cs, 0, cs.length - 1, 0);
  }

  private void quick3string(CircularSuffix[] cs, int left, int right, int curr) {
    if (left >= right || curr == length()) {
      return;
    }
    char pivot = cs[left].charAt(curr);
    int i = left;//cs[i] = pivot.curr, cs[i - 1] < pivot.curr
    int j = right;//cs[j - 1] > pivot.curr
    int k = left + 1;
    while (k <= j) {
      CircularSuffix t = cs[k];
      if (t.charAt(curr) < pivot) {
        swap(cs, i, k);
        i++;
        k++;
      } else if (t.charAt(curr) > pivot) {
        swap(cs, j, k);
        j--;
      } else {
        k++;
      }
    }
    quick3string(cs, left, i - 1, curr);
    quick3string(cs, i, j, curr + 1);
    quick3string(cs, j + 1, right, curr);
  }

  private void swap(CircularSuffix[] cs, int i, int j) {
    CircularSuffix tmp = cs[i];
    cs[i] = cs[j];
    cs[j] = tmp;
  }

  public static void main(String[] args) {
    CircularSuffixArray test = new CircularSuffixArray("ABRACADABRA!");
    System.out.println(Arrays.toString(test.index));
  }
}
