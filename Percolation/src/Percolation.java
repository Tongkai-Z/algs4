import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private final WeightedQuickUnionUF uf;
  private final WeightedQuickUnionUF ufFull;
  private final boolean[] open;
  private int openSites;
  private final int n;
  private final int top;
  private final int bottom;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("Invalid input");
    }
    this.n = n;
    uf = new WeightedQuickUnionUF(n * n + 2);
    ufFull = new WeightedQuickUnionUF(n * n + 1);
    open = new boolean[n * n + 1];
    openSites = 0;
    top = 0;
    bottom = n * n + 1;
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    checkValidity(row, col);
    int index = (row - 1) * n + col;
    if (!open[index]) {// it's blocked
      // Check its four neighbors, union the open neighbors
      // If the site is in the first or last row, union it with the visual points
      open[index] = true;
      openSites++;
      if (row == 1) {
        uf.union(top, index);
        ufFull.union(top, index);
      }
      if (row == n) {
        uf.union(bottom, index);
      }
      unionSite(index, row + 1, col);
      unionSite(index, row - 1, col);
      unionSite(index, row, col + 1);
      unionSite(index, row, col - 1);
    }
  }

  private void unionSite(int index, int row, int col) {
    try {
      if (isOpen(row, col)) {
        int indexNei = (row - 1) * n + col;
        uf.union(index, indexNei);
        ufFull.union(index, indexNei);
      }
    } catch (IllegalArgumentException e) {

    }
  }
  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    checkValidity(row, col);
    int index = (row - 1) * n + col;
    return open[index];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    checkValidity(row, col);
    int index = (row - 1) * n + col;
    return isOpen(row, col) && ufFull.connected(index, top);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return openSites;
  }

  // does the system percolate?
  public boolean percolates() {
    return uf.connected(top, bottom);
  }

  private void checkValidity(int row, int col) {
    if (row < 1 || row > n || col < 1 || col > n) {
      throw new IllegalArgumentException("Invalid input");
    }
  }

  // test client (optional)
  public static void main(String[] args) {
  }
}
