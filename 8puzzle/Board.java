/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {

    private final int[][] board;
    private final int n; // initialize the dimension of the board

    // Corner cases.
    // You may assume that the constructor receives an n-by-n array containing
    // the n2 integers between 0 and n2 − 1, where 0 represents the blank square
    // Can I assume that the puzzle inputs
    // (arguments to the Board constructor and input to Solver) are valid?
    // Yes, though it never hurts to include some basic error checking.

    public Board(int[][] blocks) {

        // for two dimensions arrays, a.length = M；a[0].length = N;
        n = blocks.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = blocks[i][j];
            }
        }
    }

    private int[][] boardCopy() {
        int[][] copy = new int[dimension()][dimension()];
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    private int goalBoard(int x, int y) {  // interesting
        if (x + y != 2 * (dimension() - 1)) {
            return dimension() * x + y + 1;
        }
        else {
            return 0;
        }
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int ham = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (i + j != 2 * (dimension() - 1) && board[i][j] != goalBoard(i, j)) {
                    ham++;
                }
            }
        }
        return ham;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (i + j != 2 * (dimension() - 1) && board[i][j] != goalBoard(i, j)) {
                    distance = distance + manhattanDistance(i, j);
                }
            }
        }
        return distance;
    }

    // compute the manhattan distance of board[x][y]
    private int manhattanDistance(int x, int y) {
        int distance = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] == goalBoard(x, y)) {
                    distance = Math.abs(i - x) + Math.abs(j - y);
                    // StdOut.println("board " + x + ", " + y + " manhattanDistance is " + distance);
                    break;
                }
            }
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    // (the blank square is not a block)
    //  You will use it to determine whether a puzzle is solvable:
    //  exactly one of a board and its twin are solvable
    // it's different from neighbor
    public Board twin() {
        int[][] copy = boardCopy(); // Board is immutable, need array

        int i = 0;
        int j = 0;
        int p = 1;
        int q = 1;
        if (board[i][j] == 0) {
            j = 1;
        }
        else if (board[p][q] == 0) {
            q = 0;
        }

        exchange(copy, i, j, p, q);
        Board twin = new Board(copy);
        return twin;
    }

    private void exchange(int[][] a, int i, int j, int p, int q) {

        // exchange a[i][j] between a[p][q]
        int number = a[i][j];
        a[i][j] = a[p][q];
        a[p][q] = number;
    }

    // How do I implement equals()?
    // Java has some arcane rules for implementing equals(),
    // discussed on p. 103 of Algorithms, 4th edition.
    // Note that the argument to equals() is required to be Object.
    // You can also inspect Date.java or Transaction.java for online examples.
    public boolean equals(Object y) {

        if (y == null) return false;
        if (y == this) return true;
        if (y.getClass() != this.getClass()) return false;
        Board yBoard = (Board) y;
        if (yBoard.n != this.n) return false;
        else return Arrays.deepEquals(this.board, yBoard.board);
    }

    // neighbors are those that can be reached in one move,
    // that is ,exchange 0 block with its all neighbors
    // How do I return an Iterable<Board>?
    // Add the items you want to a Stack<Board> or Queue<Board> and return that.
    // Of course, your client code should not depend on
    // whether the iterable returned is a stack or queue
    // (because it could be some any iterable).
    public Iterable<Board> neighbors() {

        int x = 0;
        int y = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (board[i][j] == 0) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        Queue<Board> neighborBoard = new Queue<>();
        neighborProcess(neighborBoard, x, y);
        return neighborBoard;
    }

    private void neighborProcess(Queue<Board> neighborBoard, int x, int y) {

        if (x != 0) {
            int[][] copy = boardCopy();
            exchange(copy, x, y, x - 1, y);
            neighborBoard.enqueue(new Board(copy));
        }
        if (x != dimension() - 1) {
            int[][] copy = boardCopy();
            exchange(copy, x, y, x + 1, y);
            neighborBoard.enqueue(new Board(copy));
        }
        if (y != 0) {
            int[][] copy = boardCopy();
            exchange(copy, x, y, x, y - 1);
            neighborBoard.enqueue(new Board(copy));
        }
        if (y != dimension() - 1) {
            int[][] copy = boardCopy();
            exchange(copy, x, y, x, y + 1);
            neighborBoard.enqueue(new Board(copy));
        }
    }

    // Be sure to include the board dimension and use 0 for the blank square.
    // Use String.format() to format strings—it works like StdOut.printf(),
    // but returns the string instead of printing it to standard output.
    // For reference, our implementation is below,
    // but yours may vary depending on your choice of instance variables.

    // string representation of this board
    // (in the output format specified below)
    public String toString() {

        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

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
        StdOut.println("board is " + initial);
        StdOut.println("neighbors are ");

        for (Board b : initial.neighbors()) {
            StdOut.println(b);
        }
    }
}
