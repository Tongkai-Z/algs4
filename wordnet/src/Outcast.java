import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
  private WordNet wordnet;

  // constructor takes a WordNet object
  public Outcast(WordNet wordnet) {
    this.wordnet = wordnet;
  }

  // given an array of WordNet nouns, return an outcast
  public String outcast(String[] nouns) {
    int max = Integer.MIN_VALUE;
    String out = null;
    // use dynamic programming to optimize
    int len = nouns.length;
    int[][] cache = new int[len][len];
    for (int i = 0;i < nouns.length;i++) {
      int disSum = dis(nouns, i, cache);
      if (disSum > max) {
        max = disSum;
        out = nouns[i];
      }
    }
    return out;
  }

  private int dis(String[] nouns, int index, int[][] cache) {
    int sum = 0;
    for (int i = 0;i < nouns.length;i++) {
      if (i != index) {
        if (cache[index][i] != 0) {
          sum += cache[index][i];
        } else {
          cache[i][index] = wordnet.distance(nouns[i], nouns[index]);
          sum += cache[i][index];
        }
      }
    }
    return sum;
  }

  // see test client below
  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
      In in = new In(args[t]);
      String[] nouns = in.readAllStrings();
      StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
  }
}
