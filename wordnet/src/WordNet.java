import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {
  // note that in wordnet a word can appear in more than one synsets
  private Map<String, List<Integer>> st;// noun to index
  private Map<Integer, String> synsetIndex;//index to synset
  private Digraph g;
  private SAP sap;

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
    In in = new In(synsets);
    st = new HashMap<>();
    synsetIndex = new HashMap<>();
    while (in.hasNextLine()) {
      String[] a = in.readLine().split(",");
      String[] nouns = a[1].split(" ");
      int index = Integer.parseInt(a[0]);// index is unique
      synsetIndex.put(index, a[1]);
      for (String word : nouns) {
        List<Integer> lst = st.getOrDefault(word, new ArrayList<>());
        lst.add(index);
        st.put(word, lst);
      }
    }
    // build the graph
    g = new Digraph(synsetIndex.size());
    in = new In(hypernyms);
    // initialize the adj list
    while (in.hasNextLine()) {
      String[] edges = in.readLine().split(",");
      int tail = Integer.parseInt(edges[0]);
      for (int i = 1;i < edges.length;i++) {
        g.addEdge(tail, Integer.parseInt(edges[i]));
      }
    }
    checkCyclic();
    sap = new SAP(g);
  }

  // use dfs to check whether the digraph is cyclic or not
  private void checkCyclic() {
    DirectedCycle dc = new DirectedCycle(g);
    if (dc.hasCycle()) {
      throw new IllegalArgumentException("Cycle detected");
    }
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return st.keySet();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    return st.get(word) != null;
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    checkNoun(nounA);
    checkNoun(nounB);
    List<Integer> setA = st.get(nounA);
    List<Integer> setB = st.get(nounB);
    return sap.length(setA, setB);
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    checkNoun(nounA);
    checkNoun(nounB);
    return synsetIndex.get(sap.ancestor(st.get(nounA), st.get(nounB)));
  }

  private void checkNoun(String noun) {
    if (!isNoun(noun)) {
      throw new IllegalArgumentException(noun + " is not valid");
    }
  }

  // do unit testing of this class
  public static void main(String[] args) {
    WordNet test = new WordNet("./data/synsets.txt", "./data/hypernyms.txt");
    System.out.println(test.st.size());
    System.out.println(test.synsetIndex.size());
    System.out.println(test.sap("miracle", "increase"));
  }
}