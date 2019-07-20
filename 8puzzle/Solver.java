/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Stack<Board> solutions;
    private boolean isSolvable;
    private int totalMoves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        //  The constructor should throw a java.lang.IllegalArgumentException if passed a null argument.
        if (initial == null) throw new IllegalArgumentException();

        solutions = new Stack<Board>();

        MinPQ<SearchNode> searchPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinSearchPQ = new MinPQ<>();

        SearchNode current = new SearchNode(initial, null);
        SearchNode twinCurrent = new SearchNode(initial.twin(), null);

        while (!current.board.isGoal() && !twinCurrent.board.isGoal()) {

            for (Board b : current.board.neighbors()) {
                if (current.predecessor == null || !b.equals(current.predecessor.board)) {
                    SearchNode node = new SearchNode(b, current);
                    searchPQ.insert(node);
                }
            }
            current = searchPQ.delMin();

            for (Board b : twinCurrent.board.neighbors()) {
                if (twinCurrent.predecessor == null || !b.equals(twinCurrent.predecessor.board)) {
                    twinSearchPQ.insert(new SearchNode(b, twinCurrent));
                }
            }
            twinCurrent = twinSearchPQ.delMin();
        }

        if (current.board.isGoal()) {
            isSolvable = true;
            totalMoves = current.move;
            while (current != null) {
                solutions.push(current.board);
                current = current.predecessor;
            }
        }
        else {
            isSolvable = false;
            totalMoves = -1;
            solutions = null;
        }



    }

    private class SearchNode implements Comparable<SearchNode> {

        private final Board board;
        private final SearchNode predecessor;
        private final int move;
        private final int priority;

        SearchNode(Board board, SearchNode predecessor) {
            this.board = board;
            this.predecessor = predecessor;
            this.priority = moves() + board.manhattan();
            if (predecessor != null) {
                this.move = predecessor.move + 1;
            }
            else {
                this.move = 0;
            }
        }

        public int compareTo(SearchNode a) {

            if (this.priority > a.priority) return 1;
            if (this.priority == a.priority) {
                return Integer.compare(this.priority - this.move, a.priority - a.move);
            }
            else {
                return -1;
            }
        }
    }

    // is the initial board solvable?
    // the current API requires you to detect infeasiblity in Solver
    // by using two synchronized A* searches
    // (e.g., using two priority queues).
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return totalMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solutions;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable) {
            StdOut.println("No solution possible");
        }
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }

    }
}
