import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class SAP {
  private Digraph G;

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    this.G = G;
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    BreadthFirstDirectedPaths bpV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bpW = new BreadthFirstDirectedPaths(G, w);
    int minDis = -1;
    for (int i = 0;i < G.V();i++) {
      if (bpV.hasPathTo(i) && bpW.hasPathTo(i)) {
        int sum = bpV.distTo(i) + bpW.distTo(i);
        if (minDis == -1 || minDis > sum) {
          minDis = sum;
        }
      }
    }
    return minDis;
  }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    BreadthFirstDirectedPaths bpV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bpW = new BreadthFirstDirectedPaths(G, w);
    int minDis = -1;
    int ca = -1;
    for (int i = 0;i < G.V();i++) {
      if (bpV.hasPathTo(i) && bpW.hasPathTo(i)) {
        int sum = bpV.distTo(i) + bpW.distTo(i);
        if (minDis == -1 || minDis > sum) {
          minDis = sum;
          ca = i;
        }
      }
    }
    return ca;
  }

  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    BreadthFirstDirectedPaths bpV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bpW = new BreadthFirstDirectedPaths(G, w);
    int minDis = -1;
    for (int i = 0;i < G.V();i++) {
      if (bpV.hasPathTo(i) && bpW.hasPathTo(i)) {
        int sum = bpV.distTo(i) + bpW.distTo(i);
        if (minDis == -1 || minDis > sum) {
          minDis = sum;
        }
      }
    }
    return minDis;
  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    BreadthFirstDirectedPaths bpV = new BreadthFirstDirectedPaths(G, v);
    BreadthFirstDirectedPaths bpW = new BreadthFirstDirectedPaths(G, w);
    int minDis = -1;
    int ca = -1;
    for (int i = 0;i < G.V();i++) {
      if (bpV.hasPathTo(i) && bpW.hasPathTo(i)) {
        int sum = bpV.distTo(i) + bpW.distTo(i);
        if (minDis == -1 || minDis > sum) {
          minDis = sum;
          ca = i;
        }
      }
    }
    return ca;
  }

  // do unit testing of this class
  public static void main(String[] args) {
    In in = new In("./data/digraph1.txt");
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    System.out.println(sap.length(7, 2));
    System.out.println(sap.ancestor(7, 2));
  }
}
