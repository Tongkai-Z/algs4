import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
  private static final int R = 256;

  public static void encode() {
    // initialization
    char[] sq = new char[R];
    for (int i = 0;i < R;i++) {
      sq[i] = (char) i;
    }
    while (!BinaryStdIn.isEmpty()) {
      char curr = BinaryStdIn.readChar();
      // find it in the sq O(nR)
      int index = search(sq, curr);
      if (index != -1 ) {
        move(sq, index);//move to front O(nR)
      }
      BinaryStdOut.write(index, 8);
    }
    BinaryStdOut.close();
  }

  public static void decode() {
    // initialization
    char[] sq = new char[R];
    for (int i = 0;i < R;i++) {
      sq[i] = (char) i;
    }
    while (!BinaryStdIn.isEmpty()) {
      int index = BinaryStdIn.readInt(8);
      BinaryStdOut.write(sq[index]);
      move(sq, index);
    }
    BinaryStdOut.close();
  }

  private static int search(char[] sq, char curr) {
    for (int i = 0;i < R;i++) {
      if (sq[i] == curr) {
        return i;
      }
    }
    return -1;
  }

  private static void move(char[] sq, int index) {
    char tmp = sq[index];
    for (int i = index;i > 0;i--) {
      sq[i] = sq[i - 1];
    }
    sq[0] = tmp;
  }

  public static void main(String[] args) {
    if (args[0].equals("-")) {
      encode();
    } else if (args[0].equals("+")) {
      decode();
    }
  }
}
