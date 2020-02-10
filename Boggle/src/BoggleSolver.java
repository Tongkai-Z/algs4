import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
  // directions for dps
  private static int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {-1, -1}, {1, -1}, {-1, 1}, {1, 1}};
  private AZTrie trie;//using 26-way trie to optimize the performance of this algorithm
  // Initializes the data structure using the given array of strings as the dictionary.
  // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary)  {
    // use 26-way trie + DFS + visited
    // use value 1 to denote that this word is in the dictionary
    trie = new AZTrie();
    for (String w : dictionary) {
      trie.put(w, 1);
    }
  }

  // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    // if the prefix formed by the simple path is not a valid prefix in the dictionary
    // then there is no need to expend the path.
    Set<String> res = new HashSet<>();
    int r = board.rows();
    int c = board.cols();
    StringBuilder sb = new StringBuilder();
    boolean[][] visited = new boolean[r][c];
    for (int i = 0;i < r;i++) {
      for (int j = 0;j < c;j++) {
        dfs(board, i, j, sb, res, visited);
      }
    }
    return res;
  }

  // Returns the score of the given word if it is in the dictionary, zero otherwise.
  // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    if (trie.contains(word)) {
      switch (word.length()) {
        case 3:
        case 4:
          return 1;
        case 5:
          return 2;
        case 6:
          return 3;
        case 7:
          return 5;
        default:
          return 11;
      }
    }
    return 0;
  }

  private void dfs(BoggleBoard board, int r, int c, StringBuilder prefix, Set<String> res, boolean[][] visited) {
    if (visited[r][c]) {
      return;
    }
    int m = board.rows();
    int n = board.cols();
    // note that this visited is the path visited
    visited[r][c] = true;
    // check the word
    char curr = board.getLetter(r, c);
    //System.out.println("curr char" + curr);
    int del = 1;
    if (curr == 'Q') {//Qu case
      prefix.append("QU");
      del = 2;
    } else {
      prefix.append(curr);
    }
    String word = prefix.toString();
    //System.out.println(word);
    if (trie.contains(word) && word.length() > 2) {
      res.add(word);
    }
    // Iterable<String> p = trie.keysWithPrefix(word);bottleneck worse case n, n is the size of the dictionary
    if (trie.isPrefix(word)) {//self-implemented linear method to check if this word is a prefix of the words in the dic
      // go to the next level
      for (int[] dir : dirs) {
        int neiR = r + dir[0];
        int neiC = c + dir[1];
        if (neiR >= 0 && neiR < m && neiC >= 0 && neiC < n) {
          dfs(board, neiR, neiC, prefix, res, visited);
        }
      }
    }
    // inverse the change of the path
    while (del > 0) {
      prefix.deleteCharAt(prefix.length() - 1);
      del--;
    }
    visited[r][c] = false;
  }

  public static void main(String[] args) {
    In in = new In(args[0]);
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);
    BoggleBoard board = new BoggleBoard(args[1]);
    int score = 0;
    for (String word : solver.getAllValidWords(board)) {
      StdOut.println(word);
      score += solver.scoreOf(word);
    }
    StdOut.println("Score = " + score);
  }
}
