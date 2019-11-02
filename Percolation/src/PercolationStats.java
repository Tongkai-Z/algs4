import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
  private double[] stats;
  private int trials;
  private double mean;
  private double stddev;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException("Invalid input");
    }
    this.trials = trials;
    stats = new double[trials];
    for (int i = 0;i < trials;i++) {
      Percolation curr = new Percolation(n);
      while (!curr.percolates()) {
        int row = StdRandom.uniform(n) + 1;
        int col = StdRandom.uniform(n) + 1;
        curr.open(row, col);
      }
      stats[i] = (curr.numberOfOpenSites() + 0.0) / (n * n);
    }

  }

  // sample mean of percolation threshold
  public double mean() {
    mean = StdStats.mean(stats);
    return mean;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    stddev = StdStats.stddev(stats);
    return stddev;
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    double i = Math.sqrt(trials);
    return mean - 1.96 * stddev/i;
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    double i = Math.sqrt(trials);
    return mean + 1.96 * stddev/i;
  }

  // test client (see below)
  public static void main(String[] args) {
    int n = 2;
    int t = 100000;
    Stopwatch time = new Stopwatch();
    PercolationStats test = new PercolationStats(n, t);
    System.out.println(time.elapsedTime());
    System.out.println(test.mean());
    System.out.println(test.stddev());
    System.out.println("[" + test.confidenceLo() + ", " + test.confidenceHi() + "]");
  }

}
