import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {

  private List<LineSegment> lines;


  /**
   * This algorithm use nested for loop the check if four points are collinear or not
   *
   * @param points the point list
   * @throws IllegalArgumentException if the input is null or has duplicate points
   */
  public BruteCollinearPoints(Point[] points) {
    isInvalid(points);
    Arrays.sort(points);
    lines = new ArrayList<>();
    int len = points.length;
    for (int i = 0; i < len - 3; i++) {
      for (int j = i + 1; j < len - 2; j++) {
        for (int m = j + 1; m < len - 1; m++) {
          for (int n = m + 1; n < len; n++) {
            if (collinear(points[i], points[j], points[m], points[n])) {
              lines.add(new LineSegment(points[i], points[n]));
            }
          }
        }
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
        if (points[i] == null || points[j] == null || points[i] == points[j]) {
          throw new IllegalArgumentException("Invalid input");
        }
      }
    }
  }

  /**
   * This method checks if four points are collinear or not based on their slopes
   *
   * @param p1 point1
   * @param p2 point2
   * @param p3 point3
   * @param p4 point4
   * @return true if they are collinear, false otherwise
   */
  private boolean collinear(Point p1, Point p2, Point p3, Point p4) {
    double slope = p1.slopeTo(p2);
    if (slope != p1.slopeTo(p3)) {
      return false;
    }
    return slope == p1.slopeTo(p4);
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
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }
}
