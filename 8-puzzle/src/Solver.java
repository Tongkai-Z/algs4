import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayDeque;


public class Solver {
  private Node sol;

  private class Node {
    Board board;
    Node previous;
    int moves;
    int manhattan;
    int priority;

    public Node(Board board, int moves) {
      this.board = board;
      this.moves = moves;
      manhattan = board.manhattan();
      priority = manhattan + moves;
    }
  }
  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException();
    }
    MinPQ<Node> minHeap = new MinPQ<>((n1, n2) -> n1.priority - n2.priority);
    MinPQ<Node> twinMinHeap = new MinPQ<>((n1, n2) -> n1.priority - n2.priority);
    minHeap.insert(new Node(initial, 0));
    twinMinHeap.insert(new Node(initial.twin(), 0));
    while (!minHeap.isEmpty() && !twinMinHeap.isEmpty()) {
      Node curr = minHeap.delMin();
      Node twinCurr = twinMinHeap.delMin();
      if (curr.board.isGoal()) {
        sol = curr;
        break;
      }
      if (twinCurr.board.isGoal()) {
        break;
      }
      for (Board nei : curr.board.neighbors()) {
        Node next = new Node(nei, curr.moves + 1);
        next.previous = curr;
        if (curr.previous == null || !next.board.equals(curr.previous.board)) {
          minHeap.insert(next);
        }
      }
      for (Board nei : twinCurr.board.neighbors()) {
        Node next = new Node(nei, twinCurr.moves + 1);
        next.previous = twinCurr;
        if (twinCurr.previous == null || !next.board.equals(twinCurr.previous.board)) {
          twinMinHeap.insert(next);
        }
      }
    }
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return sol != null;
  }

  // min number of moves to solve initial board
  public int moves() {
    return sol == null? -1 : sol.moves;
  }

  // sequence of boards in a shortest solution
  public Iterable<Board> solution() {
    if (sol == null) {
      return null;
    }
    ArrayDeque<Board> stack = new ArrayDeque<>();
    Node curr = sol;
    while (curr != null) {
      stack.offerFirst(curr.board);
      curr = curr.previous;
    }
    return stack;
  }

  // test client (see below)
  public static void main(String[] args){
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
        StdOut.println(board);
    }
  }
}
