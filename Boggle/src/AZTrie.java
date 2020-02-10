public class AZTrie {
  private static final int R = 26;
  private Node root;
  private int n;

  private static class Node {
    private Integer val;
    private Node[] next = new Node[R];
  }

  public AZTrie() {
    root = new Node();
  }

  public Integer get(String key) {
    if (key == null) {
      return null;
    }
    return get(root, key, 0);
  }

  private Integer get(Node root, String key, int index) {
    if (root == null) {
      return null;
    }
    if (index == key.length()) {
      return root.val;
    }
    root = root.next[key.charAt(index) - 'A'];
    return get(root, key, index + 1);
  }

  public boolean contains(String key) {
    return get(key) != null;
  }

  public void put(String key, Integer val) {
    if (key == null) {
      return;
    }
    if (val == null) {
      delete(root, key, 0);
    }
    put(root, key, val, 0);
    n++;
  }

  private Node put(Node root, String key, Integer val, int index) {
    if (root == null) {
      root = new Node();
    }
    if (index == key.length()) {
      root.val = val;
    } else {
      Node next = root.next[key.charAt(index) - 'A'];
      root.next[key.charAt(index) - 'A'] = put(next, key, val, index + 1);
    }
    return root;
  }

  private Node delete(Node root, String key, int index) {
    if (root == null) {
      return null;//no need to delete
    }
    if (index == key.length()) {
      if (root.val != null) {
        n--;
        root.val = null;
      }
    } else {
      Node next = root.next[key.charAt(index) - 'A'];
      root.next[key.charAt(index) - 'A'] = delete(next, key, index + 1);
    }
    boolean empty = true;
    for (Node next : root.next) {
      if (next != null) {
        empty = false;
        break;
      }
    }
    return empty? null : root;
  }

  public int size() {
    return n;
  }

  public boolean isEmpty() {
    return n == 0;
  }

  public boolean isPrefix(String prefix) {
    Node curr = root;
    for (int i = 0;i < prefix.length();i++) {
      if (curr == null) {
        return false;
      }
      curr = curr.next[prefix.charAt(i) - 'A'];
    }
    return true;
  }

  public static void main(String[] args) {
    AZTrie test = new AZTrie();
    test.put("SUNSHINE", 1);
    test.put("SUNSHINES", 1);
    test.put("RISINGSUN", 1);
    System.out.println(test.size());
    System.out.println(test.contains("SUNS"));
    System.out.println(test.isPrefix("SUNS"));
  }
}
