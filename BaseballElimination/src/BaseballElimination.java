import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {
  private String[] teams;// the team name of each index
  private Map<String, Integer> index;
  private Map<String, List<String>> cert;//cache for certification
  private int[] w;//w[i] denotes the wins of of team i
  private int[] r;//r[i] denotes the rest game team i need to play
  private int[] l;
  private int[][] g;//g[i][j] denotes the number of games rest between team i and j

  public BaseballElimination(String filename) {
    //read all the data from the file
    In in = new In(filename);
    int count = in.readInt();
    in.readLine();//advance the pointer
    int curr = 0;
    teams = new String[count];
    cert = new HashMap<>();
    w = new int[count];
    r = new int[count];
    l = new int[count];
    g = new int[count][count];
    index = new HashMap<>();
    while (in.hasNextLine()) {
      String line = in.readLine();
      String[] info = line.split("\\s+");
      teams[curr] = info[0];
      index.put(info[0], curr);
      w[curr] = Integer.parseInt(info[1]);
      l[curr] = Integer.parseInt(info[2]);
      r[curr] = Integer.parseInt(info[3]);
      // read the games
      for (int i = 4;i < info.length;i++) {
        g[curr][i - 4] = Integer.parseInt(info[i]);
      }
      curr++;
    }
  }

  public int numberOfTeams() {
    return teams.length;
  }

  public Iterable<String> teams() {
    return Arrays.asList(teams);
  }

  public int wins(String team) {
    checkTeam(team);
    return w[index.get(team)];
  }

  public int losses(String team) {
    checkTeam(team);
    return l[index.get(team)];

  }

  public int remaining(String team) {
    checkTeam(team);
    return r[index.get(team)];
  }

  public int against(String team1, String team2) {
    checkTeam(team1);
    checkTeam(team2);
    return g[index.get(team1)][index.get(team2)];
  }

  public boolean isEliminated(String team) {
    checkTeam(team);
    //trivial elimination
    int curr = index.get(team);
    int maxWin = w[curr] + r[curr];
    for (int i = 0;i < teams.length;i++) {
      if (w[i] > maxWin) {
        cert.put(team, Arrays.asList(teams[i]));
        return true;
      }
    }
    // maxflow elimination
    // initialize the FlowNetwork based on all the teams and games except the input one
    // number of vertices teams + games between other teams + s + t
    int num = numberOfTeams();
    // Simplify: keep the current team in the graph, and also the games, but set the capacity to zero
    int vertexNum = num * (num - 1) / 2 + num + 2;
    FlowNetwork fn = new FlowNetwork(vertexNum);
    // encode the games and teams
    // teams already encoded from 0 -> num - 1
    // we only need the upperright half of the game matrix
    // with the coordinate (0,1) (0,2)...(2, 3)...(3, 4)...
    // ith row has (num - 1 - i) games
    // (i, j): the (j - i)th element at ith row
    // (i, j) encoding: num - 1 + i * num - (i^2 + 3i)/2 + j
    // s: vertexNum - 2, t: vertexNum - 1
    // add the edge to the graph
    int saturation = 0;
    for (int i = 0;i < g.length;i++) {
      for (int j = 0;j < g[0].length;j++) {
        if (j > i && i != curr && j != curr) {//skip the curr team games
          int node = num - 1 + i * num - (i * i + 3 * i) / 2 + j;
          //connect game node to s
          FlowEdge e1 = new FlowEdge(vertexNum - 2, node, g[i][j], 0);
          fn.addEdge(e1);
          saturation += g[i][j];
          //connect game node to two teams
          FlowEdge e2 = new FlowEdge(node, i, Double.POSITIVE_INFINITY, 0);
          FlowEdge e3 = new FlowEdge(node, j, Double.POSITIVE_INFINITY, 0);
          fn.addEdge(e2);
          fn.addEdge(e3);
        }
      }
    }
    // connect team node to t
    for (int i = 0;i < teams.length;i++) {
      if (i != curr) {
        int winMax = w[curr] + r[curr] - w[i];
        FlowEdge e = new FlowEdge(i, vertexNum - 1, winMax, 0);
        fn.addEdge(e);
      }
    }
    FordFulkerson ff = new FordFulkerson(fn, vertexNum - 2, vertexNum - 1);
    // check saturation
    if (ff.value() < saturation) {
      List<String> proof = new ArrayList<>();
      for (int i = 0;i < teams.length;i++) {
        if (i != curr && ff.inCut(i)) {
          proof.add(teams[i]);
        }
      }
      cert.put(team, proof);
      return true;
    }
    return false;
  }

  public Iterable<String> certificateOfElimination(String team) {
    if (cert.containsKey(team)) {
      return cert.get(team);
    }
    if (isEliminated(team)) {
      return cert.get(team);
    }
    return null;
  }

  private void checkTeam(String team) {
    if (!index.containsKey(team)) {
      throw new IllegalArgumentException("team is not valid");
    }
  }

  public static void main(String[] args) {
    BaseballElimination division = new BaseballElimination(args[0]);
    for (String team : division.teams()) {
      if (division.isEliminated(team)) {
        StdOut.print(team + " is eliminated by the subset R = { ");
        for (String t : division.certificateOfElimination(team)) {
          StdOut.print(t + " ");
        }
        StdOut.println("}");
      }
      else {
        StdOut.println(team + " is not eliminated");
      }
    }
  }

}
