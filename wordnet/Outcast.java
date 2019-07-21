/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordNet;

    public Outcast(WordNet wordnet) {
        wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    // Assume that argument to outcast() contains only valid wordnet nouns
    // (and that it contains at least two such nouns).
    public String outcast(String[] nouns) {
        int maxDistance = -1;
        String outcast = "";
        int distance;
        for (String a : nouns) {
            distance = 0;
            for (String b : nouns) {
                distance = distance + wordNet.distance(a, b);
            }
            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = a;
            }
        }
        return outcast;
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
