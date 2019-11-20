import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FastCollinearPoints {

  private List<LineSegment> lines;

  public FastCollinearPoints(Point[] points) {
    isInvalid(points);
    int len = points.length;
    lines = new ArrayList<>();
    // sort the array first to simplify the process of finding the two ends of the segment
    Arrays.sort(points);
    Point[] copy = new Point[len];
    for (int i = 0; i < len - 3; i++) {
      Point curr = points[i];
      int index = 0;
      while (index < len) {
        copy[index] = points[index];
        index++;
      }
      // bottleneck: time complexity nlogn
      Arrays.sort(copy, curr.slopeOrder());
      // create a slope array to store the slopes
      double[] slope = new double[len];
      for (int k = 0; k < len; k++) {
        slope[k] = curr.slopeTo(copy[k]);
      }
      // no duplicate element allowed so the first element is the point i itself
      int j = 1;
      // O(n)
      while (j < len - 2) {
        int m = j + 1;
        while (m < len && slope[m] == slope[j]) {
          m++;
        }
        // if current point is the lowest in the natural order, we add it to the result
        if (m - j > 2 && curr.compareTo(copy[j]) < 0) {
          LineSegment line = new LineSegment(curr, copy[m - 1]);
          lines.add(line);
        }
        j = m;
      }
    }
  }


  /**
   * return number of line segments formed by the list of points
   *
   * @return number of line segments
   */
  public int numberOfSegments() {
    return lines.size();
  }

  /**
   * return the segments list
   *
   * @return the segments list
   */
  public LineSegment[] segments() {
    LineSegment[] res = new LineSegment[lines.size()];
    int index = 0;
    for (LineSegment line : lines) {
      res[index++] = line;
    }
    return res;
  }

  /**
   * This method examines if the input points is valid or not
   *
   * @param points point list
   * @throws IllegalArgumentException if the input is null or has duplicate points
   */
  private void isInvalid(Point[] points) {
    if (points == null) {
      throw new IllegalArgumentException("Invalid input");
    }
    for (int i = 0; i < points.length; i++) {
      for (int j = i + 1; j < points.length; j++) {
        if (points[i] == null || points[j] == null
            || points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY) {
          throw new IllegalArgumentException("Invalid input");
        }
      }
    }
  }

  public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
