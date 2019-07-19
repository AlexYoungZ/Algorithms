import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private int numOfSeg;
    private LineSegment[] lines;

    public FastCollinearPoints(Point[] points) {

        numOfSeg = 0;
        lines = new LineSegment[2];

        cornerTest(points);

        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);

        for (int i = 0; i < sortedPoints.length; i++) {
            collinearFinder(sortedPoints, sortedPoints[i]);
        }
    }

    private void collinearFinder(Point[] points, Point origin) {

        Point[] slopeSortedPoints = points.clone();
        Arrays.sort(slopeSortedPoints, origin.slopeOrder());

        double slope;
        int last = 1;
        int collinearNum = 2;

        while (last < slopeSortedPoints.length - 1) {

            slope = origin.slopeTo(slopeSortedPoints[last]);
            collinearNum = 2;
            last++;

            while (slope == origin.slopeTo(slopeSortedPoints[last])) {
                collinearNum++;
                last++;
                if (last > slopeSortedPoints.length - 1)
                    break;
            }
        }

        if (collinearNum >= 4) {
            Point[] copy = new Point[collinearNum];
            copy[0] = origin;

            for (int i = 1; i < collinearNum; i++) {
                copy[i] = slopeSortedPoints[last - collinearNum + i];
            }

            Arrays.sort(copy);

            if (copy[0].compareTo(origin) == 0) {
                lines[numOfSeg] = new LineSegment(origin, slopeSortedPoints[last - 1]);
                numOfSeg++;
                if (numOfSeg == lines.length) {
                    resize(2 * lines.length                );
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

        // tired today later

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
            temp[i] = lines[i];
        }
        lines = temp;
    }

    public int numberOfSegments() {
        return numOfSeg;
    }

    public LineSegment[] segments() {

        LineSegment[] lines = new LineSegment[numOfSeg];
        for (int i = 0; i < numOfSeg; i++) {
            lines[i] = lines[i];
        }
        return lines;
    }


    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();



    }
}
