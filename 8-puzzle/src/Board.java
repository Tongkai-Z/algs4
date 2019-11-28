import java.util.ArrayDeque;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Queue;

public class Board {
  private final int[][] board;
  private int first;
  private int second;
  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    int row = tiles.length;
    int col = tiles[0].length;
    board = new int[row][col];
    for (int i = 0;i < row;i++) {
      for (int j = 0;j < col;j++) {
        board[i][j] = tiles[i][j];
      }
    }
  }

  // string representation of this board
  public String toString() {
    String repr = board.length + "\n";
    for (int i = 0;i < board.length;i++) {
      repr += " ";
      for (int j = 0;j < board.length;j++) {
        repr += board[i][j] + "  ";
      }
      repr += "\n";
    }
    return repr;
  }

  // board dimension n
  public int dimension() {
    return board.length;
  }

  // number of tiles out of place
  // The tile at (row, col) should be row * n + col + 1
  public int hamming() {
    int count = 0;
    int n = dimension();
    for (int i = 0;i < n;i++) {
      for (int j = 0;j < n;j++) {
        if (i != n - 1 || j != n - 1) {// skip the last blank tile
          if (i * n + j + 1 != board[i][j]) {
            count++;
          }
        }
      }
    }
    return count;
  }

  // sum of Manhattan distances between tiles and goal
  // For tile x, the final place is at ((x - 1) / n, (x - 1) % n)
  public int manhattan() {
    int sum = 0;
    int n = dimension();
    for (int i = 0;i < n;i++) {
      for (int j = 0;j < n;j++) {
        int curr = board[i][j];
        if (curr != 0) {
          int dRow = (curr - 1) / n - i;
          int dCol = (curr - 1) % n - j;
          dRow = dRow > 0? dRow : -dRow;
          dCol = dCol > 0? dCol : -dCol;
          sum += dRow + dCol;
        }
      }
    }
    return sum;
  }

  // is this board the goal board?
  public boolean isGoal() {
    return hamming() == 0;
  }

  // does this board equal y?
  public boolean equals(Object y) {
    if (y instanceof Board) {
      Board other = (Board) y;
      return toString().equals(other.toString());
    }
    return false;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    Queue<Board> neighbors = new ArrayDeque<>();
    int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    int n = dimension();
    int blankRow = 0;
    int blankCol = 0;
    for (int i = 0;i < n;i++) {
      for (int j = 0;j < n;j++) {
        if (board[i][j] == 0) {
          blankRow = i;
          blankCol = j;
        }
      }
    }
    for (int[] dir : dirs) {
      int neiR = blankRow + dir[0];
      int neiC = blankCol + dir[1];
      if (neiR < n && neiR >= 0 && neiC < n && neiC >= 0) {
        swap(blankRow, blankCol, neiR, neiC);
        neighbors.offer(new Board(board));
        swap(blankRow, blankCol, neiR, neiC);
      }
    }
    return neighbors;
  }

  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    int n = dimension();
    while (first == second || board[first / n][first % n] == 0 || board[second / n][second % n] == 0) {
      first = StdRandom.uniform(n * n - 1);
      second = StdRandom.uniform(n * n - 1);
    }
    swap(first / n, first % n, second / n, second % n);
    Board res = new Board(board);
    swap(first / n, first % n, second / n, second % n);
    return res;
  }

  private void swap(int currR, int currC, int neiR, int neiC) {
    int temp = board[currR][currC];
    board[currR][currC] = board[neiR][neiC];
    board[neiR][neiC] = temp;
  }

  // unit testing (not graded)
  public static void main(String[] args) {
    int[][] tiles1 = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
    int[][] tiles2 = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
    int[][] tiles3 = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
    Board board1 = new Board(tiles1);
    Board board2 = new Board(tiles2);
    Board board3 = new Board(tiles3);
//    System.out.println(board1);
//    System.out.println(board2);
//    System.out.println(board1.dimension());
//    System.out.println(board2.hamming());
//    System.out.println(board2.manhattan());
//    System.out.println(board1.equals(board3));
//    for (Board b : board1.neighbors()) {
//      System.out.println(b);
//    }
    System.out.println(board1.twin());
    System.out.println(board1);
    System.out.println(board1.twin());
    System.out.println(board1.twin());
    System.out.println(board1.twin());
  }
}
