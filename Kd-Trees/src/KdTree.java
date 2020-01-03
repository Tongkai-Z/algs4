import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;
import java.util.List;

public class KdTree {
  private int size;
  private Node root;

  // Node class doesn't depend on the object associated with the outer class
  // Static inner class to decrease the depth of the module
  private static class Node {
    private Point2D p;
    private RectHV rect;
    private Node lb;
    private Node rt;
    Node(Point2D p) {
      this.p = p;
    }
  }

  // construct an empty set of points
  public KdTree() {
    size = 0;
    root = null;
  }

  // is the set empty?
  public boolean isEmpty() {
    return size == 0;
  }

  // number of points in the set
  public int size() {
    return size;
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    double[] rec = {0, 0, 1, 1};// xmin, ymin, xmax, ymax for the node rectangle
    root = insert(root, p, true, rec);
  }

  private Node insert(Node root, Point2D p, boolean useX, double[] rec) {
    if (root == null) {
      Node newNode = new Node(p);
      RectHV r = new RectHV(rec[0], rec[1], rec[2], rec[3]);
      newNode.rect = r;
      size++;
      return newNode;
    } else {
      Point2D curr = root.p;
      if (!curr.equals(p)) {//dedup
        double diff = useX ? curr.x() - p.x() : curr.y() - p.y();
        int maxIndex = useX ? 2 : 3;
        int minIndex = useX ? 0 : 1;
        if (diff > 0) {
          rec[maxIndex] = useX? curr.x() : curr.y();
          root.lb = insert(root.lb, p, !useX, rec);
        } else {
          rec[minIndex] = useX? curr.x() : curr.y();
          root.rt = insert(root.rt, p, !useX, rec);
        }
      }
      return root;
    }
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    return contains(root, p, true);
  }

  private boolean contains(Node root, Point2D p, boolean useX) {
    if (root == null) {
      return false;
    }
    Point2D curr = root.p;
    if (curr.equals(p)) {
      return true;
    }
    double diff = useX? curr.x() - p.x() : curr.y() - p.y();
    if (diff > 0) {
      return contains(root.lb, p, !useX);
    } else {
      return contains(root.rt, p, !useX);
    }
  }

  // draw all points to standard draw
  public void draw() {
    draw(root, true);
  }

  private void draw(Node root, boolean useX) {
    if (root == null) {
      return;
    }
    Point2D curr = root.p;
    RectHV rec = root.rect;
    // draw the point
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    curr.draw();
    //useX draw vertical line which is the same height as the rectangle
    StdDraw.setPenRadius();
    if (useX) {
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.line(curr.x(), rec.ymin(), curr.x(), rec.ymax());
    } else {
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.line(rec.xmin(), curr.y(), rec.xmax(), curr.y());
    }
    draw(root.lb, !useX);
    draw(root.rt, !useX);
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    List<Point2D> res = new ArrayList<>();
    range(root, rect, res);
    return res;
  }

  private void range(Node root, RectHV rect, List<Point2D> res) {
    if (rect == null || root == null || !root.rect.intersects(rect)) {
      return;
    }
    Point2D curr = root.p;
    if (rect.contains(curr)) {
      res.add(curr);
    }
    range(root.lb, rect, res);
    range(root.rt, rect, res);
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    if (p == null) {
      return null;
    }
    Point2D[] min = new Point2D[1];
    nearest(root, p, true, min);
    return min[0];
  }

  private void nearest(Node root, Point2D p, boolean useX, Point2D[] min) {
    if (root == null) {
      return;
    }
    Point2D curr = root.p;
    RectHV currR = root.rect;
    //pruning
    if (min[0] != null && p.distanceSquaredTo(min[0]) <= currR.distanceSquaredTo(p)) {
      return;
    }
    // update min
    if (min[0] == null || p.distanceSquaredTo(curr) < p.distanceSquaredTo(min[0])) {
      min[0] = curr;
    }
    double diff = useX? curr.x() - p.x() : curr.y() - p.y();
    if (diff > 0) {
      nearest(root.lb, p, !useX, min);
      nearest(root.rt, p, !useX, min);
    } else {
      nearest(root.rt, p, !useX, min);
      nearest(root.lb, p, !useX, min);
    }
  }

  // unit testing of the methods (optional)
  public static void main(String[] args) {
    KdTree test = new KdTree();
    test.insert(new Point2D(0.5, 0.5));
    test.insert(new Point2D(0.4, 0.7));
    test.insert(new Point2D(0.3, 0.4));
    test.insert(new Point2D(0.1, 0.8));
    test.insert(new Point2D(0.1, 0.8));
    //test.draw();
    System.out.println(test.nearest(new Point2D(0.3, 0.3)));
    System.out.println(test.contains(new Point2D(0.5, 0.5)));
    System.out.println(test.contains(new Point2D(0.5, 0.6)));
    System.out.println(test.size());
  }
}
