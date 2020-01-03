import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {
  private Set<Point2D> points;
  // construct an empty set of points
  public PointSET() {
    points = new TreeSet<>();
  }

  // is the set empty?
  public boolean isEmpty() {
    return points.isEmpty();
  }

  // number of points in the set
  public int size() {
    return points.size();
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    points.add(p);
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    return points.contains(p);
  }

  // draw all points to standard draw
  public void draw() {
    for (Point2D p : points) {
      p.draw();
    }
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    List<Point2D> res = new ArrayList<>();
    for (Point2D p : points) {
      if (rect.contains(p)) {
        res.add(p);
      }
    }
    return res;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    Double min = null;
    Point2D res = null;
    for (Point2D other : points) {
      Double dis = p.distanceTo(other);
      if (min == null || dis < min) {
        min = dis;
        res = other;
      }
    }
    return res;
  }

  // unit testing of the methods (optional)
  public static void main(String[] args) {

  }
}
