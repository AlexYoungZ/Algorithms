import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Write a program BruteCollinearPoints.java that examines 4 points at a time and checks whether they all lie on
 * the same line segment, printing out any such line segments to standard output and drawing them
 * using standard drawing. To check whether the 4 points p, q, r, and s are collinear, check whether
 * the slopes between p and q, between p and r, and between p and s are all equal.
 * <p>
 * Each program should take the name of an input file as a command-line argument, read the input
 * file (in the format specified below), print to standard output the line segments discovered (in
 * the format specified below), and draw to standard draw the line segments discovered (in the
 * format specified below).
 * <p>
 * Read the points from an input file in the following format: An integer N, followed by N pairs of
 * integers (x, y), each between 0 and 32,767.
 * <p>
 * Print to standard output the line segments that your program discovers, one per line. Print each
 * line segment as an ordered sequence of its constituent points, separated by " -> ".
 * <p>
 * Also, draw the points using draw() and draw the line segments using drawTo(). Your programs
 * should call draw() once for each point in the input file and it should call drawTo() once for
 * each line segment discovered. Before drawing, use StdDraw.setXscale(0, 32768) and
 * StdDraw.setYscale(0, 32768) to rescale the coordinate system.
 */


public class BruteCollinearPoints {
    private int numOfSeg;
    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {

        numOfSeg = 0;
        lineSegments = new LineSegment[2];

        cornerTest(points);

        Point[] pointcopy = points.clone();
        Arrays.sort(pointcopy);

        double slope1;

        for (int i = 0; i < pointcopy.length - 3; i++) {
            for (int j = i + 1; j < pointcopy.length - 2; j++) {
                slope1 = pointcopy[i].slopeTo(pointcopy[j]);
                for (int k = j + 1; k < pointcopy.length - 1; k++) {
                    for (int l = k + 1; l < pointcopy.length; l++) {
                        if (slope1 == pointcopy[k].slopeTo(pointcopy[l])
                                && slope1 == pointcopy[j].slopeTo(pointcopy[k])) {
                            lineSegments[numOfSeg] = new LineSegment(pointcopy[i], pointcopy[l]);
                            numOfSeg++;
                            if (numOfSeg == lineSegments.length) {
                                resize(2 * lineSegments.length);
                            }
                        }

                    }
                }
            }
        }
    }

    // Corner cases. Throw a java.lang.IllegalArgumentException if the argument to the constructor is null,
    // if any point in the array is null, or if the argument to the constructor contains a repeated point.
    private void cornerTest(Point[] points) {

        if (points == null) throw new IllegalArgumentException();

        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException();
        }

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);

        int N = points.length;
        for (int i = 0; i < N - 1; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void resize(int max) {
        LineSegment[] temp = new LineSegment[max];
        for (int i = 0; i < numOfSeg; i++) {
            temp[i] = lineSegments[i];
        }
        lineSegments = temp;
    }

    public int numberOfSegments() {
        return numOfSeg;
    }

    public LineSegment[] segments() {

        LineSegment[] lines = new LineSegment[numOfSeg];
        for (int i = 0; i < numOfSeg; i++) {
            lines[i] = lineSegments[i];
        }
        return lines;
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        int n = in.readInt();
        StdOut.println(n);
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

        StdDraw.show();

    }
}

