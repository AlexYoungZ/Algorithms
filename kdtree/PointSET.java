/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> pointset;

    public PointSET() {
        pointset = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return pointset.isEmpty();
    }

    public int size() {
        return pointset.size();
    }

    // Your implementation should support insert() and contains()
    // in time proportional to the logarithm of the number of points
    // in the set in the worst case;

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // Throw a java.lang.IllegalArgumentException if any argument is null.
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) {
            pointset.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return pointset.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : pointset) {
            p.draw();
        }
    }

    // support nearest() and range() in time proportional to the number of points in the set.
    public Iterable<Point2D> range(RectHV rect) {
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> pointInside = new Queue<>();
        for (Point2D p : pointset) {
            if (rect.contains(p)) {
                pointInside.enqueue(p);
            }
        }
        return pointInside;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Point2D minPoint = pointset.first();
        double minDistance = p.distanceSquaredTo(minPoint);
        for (Point2D q : pointset) {
            if (p.distanceSquaredTo(q) < minDistance) {
                minPoint = q;
                minDistance = p.distanceSquaredTo(minPoint);
            }
        }
        return minPoint;
    }

    public static void main(String[] args) {

    }
}
