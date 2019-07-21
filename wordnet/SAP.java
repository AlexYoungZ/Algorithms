/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)

    // How can I make the data type SAP immutable?
    // You can (and should) save the associated digraph in an instance variable.
    // However, because our Digraph data type is mutable,
    // you must first make a defensive copy by calling the copy constructor.
    // public Digraph(Digraph G)
    // Initializes a new digraph that is a deep copy of the specified digraph.
    // [[https://algs4.cs.princeton.edu/42digraph/Digraph.java.html
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        digraph = new Digraph(G);
    }

    private void cornerTest(int i) {
        if (i < 0 || i >= digraph.V()) {
            throw new IllegalArgumentException();
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        cornerTest(v);
        cornerTest(w);
        // if (bfsV.hasPathTo(w)) return bfsV.distTo(w);
        // a vertex is considered an ancestor of itself.
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);

        int length = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (length == -1) {
                    length = bfsV.distTo(i) + bfsW.distTo(i);
                }
                else {
                    length = Math.min(bfsV.distTo(i) + bfsW.distTo(i), length);
                }
            }
        }
        return length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        cornerTest(v);
        cornerTest(w);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);

        int length = -1;
        int ancestor = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (length == -1) {
                    length = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestor = i;
                }
                else if (bfsV.distTo(i) + bfsW.distTo(i) < length) {
                    length = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w;
    // -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        for (Integer i : v) {
            if (i == null) throw new IllegalArgumentException();
            cornerTest(i);
        }
        for (Integer i : w) {
            if (i == null) throw new IllegalArgumentException();
            cornerTest(i);
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        // To improve the method length(Iterable<Integer> v, Iterable<Integer> w)
        // time-taking proportional to  E + V?,
        // The key is using the constructor in BreadthFirstDirectedPaths
        // that takes an iterable of sources instead of using a single source
        int length = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (length == -1) {
                    length = bfsV.distTo(i) + bfsW.distTo(i);
                }
                else {
                    length = Math.min(bfsV.distTo(i) + bfsW.distTo(i), length);
                }
            }
        }
        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        for (Integer i : v) {
            if (i == null) throw new IllegalArgumentException();
            cornerTest(i);
        }
        for (Integer i : w) {
            if (i == null) throw new IllegalArgumentException();
            cornerTest(i);
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int length = -1;
        int ancestor = -1;

        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (length == -1) {
                    length = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestor = i;
                }
                else if (bfsV.distTo(i) + bfsW.distTo(i) < length) {
                    length = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestor = i;
                }
            }
        }
        return ancestor;
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

    }
}
