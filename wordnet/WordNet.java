/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {

    private HashMap<Integer, String[]> synsetMap;
    private HashMap<String, ArrayList<Integer>> nounID;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        // read synset
        In in1 = new In(synsets);
        String synLine = in1.readLine();

        synsetMap = new HashMap<Integer, String[]>();
        nounID = new HashMap<String, ArrayList<Integer>>();

        // Use the readLine() method in our In library to read in the data one line at a time.
        // Use the split() method in Java's String library to divide a line into fields.
        // You can find an example using split() in Domain.java.
        // Use Integer.parseInt() to convert string id numbers into integers.
        // Do I need to store the glosses? No, you won't use them on this assignment.
        String aux;
        int number;
        while (synLine != null) {
            number = Integer.parseInt(synLine.split(",")[0]);
            aux = synLine.split(",")[1];
            synsetMap.put(number, aux.split(" "));
            synLine = in1.readLine();
        }

        // constructor takes a digraph (not necessarily a DAG)
        Digraph hypDigraph = new Digraph(synsetMap.size());

        boolean[] notRoot = new boolean[synsetMap.size()];
        In in2 = new In(hypernyms);
        String hypLine = in2.readLine();
        int ver;
        while (hypLine != null) {
            ver = Integer.parseInt(hypLine.split(",")[0]);
            notRoot[ver] = true;
            for (String s : hypLine.split(",")) {
                if (Integer.parseInt(s) != ver) {
                    hypDigraph.addEdge(ver, Integer.parseInt(s));
                }
            }
            hypLine = in2.readLine();
        }

        sap = new SAP(hypDigraph);

        DirectedCycle directedCycle = new DirectedCycle(hypDigraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException("the graph has cycle");
        }

        int rootNumber = 0;
        for (boolean b : notRoot) {
            if (!b) {
                rootNumber++;
            }
        }
        if (rootNumber > 1) throw new IllegalArgumentException();

        nounID = new HashMap<String, ArrayList<Integer>>();
        for (int i = 0; i < synsetMap.size(); i++) {
            for (String noun : synsetMap.get(i)) {
                if (!nounID.containsKey(noun)) {
                    ArrayList<Integer> index = new ArrayList<>();
                    index.add(i);
                    nounID.put(noun, index);
                }
                else {
                    nounID.get(noun).add(i);
                }
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounID.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounID.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        return sap.length(nounID.get(nounA), nounID.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        int synNumber = sap.ancestor(nounID.get(nounA), nounID.get(nounB));
        // ancestor = ancestor + " " + s;
        // Builds a 'String' object using the '+' operator in a loop,
        // which can take time quadratic in the length of the resulting string.
        // Instead, use 'StringBuilder'.
        StringBuilder ancestor = new StringBuilder();
        for (String s : synsetMap.get(synNumber)) {
            ancestor.append(s + " ");
        }
        return ancestor.toString();
    }
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
