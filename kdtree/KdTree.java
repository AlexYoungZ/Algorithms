/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    // number of Nodes
    private int n;
    private Node root;

    private static class Node {
        private Point2D point;
        private RectHV rectangle;
        private Node left;
        private Node right;
    }

    public KdTree() {
        n = 0;
        root = new Node();
        root.rectangle = new RectHV(0, 0, 1, 1);
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (n == 0) {
            root.point = p;
            n++;
        }
        else if (!contains(p)) {
            insert(root, p, 'x');
        }
    }

    // Each node corresponds to an axis-aligned rectangle in the unit square,
    // which encloses all of the points in its subtree.
    // The root corresponds to the unit square;
    // the left and right children of the root
    // corresponds to the two rectangles split by the x-coordinate of the point at the root; and so forth.
    private void insert(Node searchNode, Point2D p, char ori) {

        if (ori == 'x') {
            if (searchNode.point.x() > p.x()) {
                if (searchNode.left != null) {
                    insert(searchNode.left, p, 'y');
                }
                else {
                    Node node = new Node();
                    node.point = p;
                    node.rectangle = new RectHV(searchNode.rectangle.xmin(),
                                                searchNode.rectangle.ymin(),
                                                searchNode.point.x(),
                                                searchNode.rectangle.ymax());
                    n++;
                    searchNode.left = node;
                }
            }
            else {
                if (searchNode.right != null) {
                    insert(searchNode.right, p, 'y');
                }
                else {
                    Node node = new Node();
                    node.point = p;
                    node.rectangle = new RectHV(searchNode.point.x(),
                                                searchNode.rectangle.ymin(),
                                                searchNode.rectangle.xmax(),
                                                searchNode.rectangle.ymax());
                    n++;
                    searchNode.right = node;
                }
            }
        }
        else {
            if (searchNode.point.y() > p.y()) {
                if (searchNode.left != null) {
                    insert(searchNode.left, p, 'x');
                }
                else {
                    Node node = new Node();
                    node.point = p;
                    node.rectangle = new RectHV(searchNode.rectangle.xmin(),
                                                searchNode.rectangle.ymin(),
                                                searchNode.rectangle.xmax(),
                                                searchNode.point.y());
                    n++;
                    searchNode.left = node;
                }
            }
            else {
                if (searchNode.right != null) {
                    insert(searchNode.right, p, 'x');
                }
                else {
                    Node node = new Node();
                    node.point = p;
                    node.rectangle = new RectHV(searchNode.rectangle.xmin(),
                                                searchNode.point.y(),
                                                searchNode.rectangle.xmax(),
                                                searchNode.rectangle.ymax());
                    n++;
                    searchNode.right = node;
                }
            }
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root.point == null) return false;
        return contains(root, p, 'x');
    }

    private boolean contains(Node searchNode, Point2D p, char ori) {
        if (searchNode == null) return false;
        if (searchNode.point.equals(p)) return true;
        if (ori == 'x') {
            if (searchNode.point.x() > p.x()) {
                return contains(searchNode.left, p, 'y');
            }
            else {
                return contains(searchNode.right, p, 'y');
            }
        }
        else {
            if (searchNode.point.y() > p.y()) {
                return contains(searchNode.left, p, 'x');
            }
            else return contains(searchNode.right, p, 'x');
        }
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node.point != null) {
            node.point.draw();
        }
        else if (node.left != null) {
            draw(node.left);
        }
        else {
            if (node.right != null) {
                draw(node.right);
            }
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (n == 0) return null;
        Queue<Point2D> insidePoints = new Queue<>();
        rangeSearch(root, rect, insidePoints);
        return insidePoints;
    }

    private void rangeSearch(Node searchNode, RectHV rect, Queue<Point2D> queue) {
        if (searchNode.rectangle.intersects(rect)) {
            if (rect.contains(searchNode.point)) queue.enqueue(searchNode.point);
            if (searchNode.left != null) rangeSearch(searchNode.left, rect, queue);
            if (searchNode.right != null) rangeSearch(searchNode.right, rect, queue);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root.point == null) return null;
        Node minNode = new Node();
        minNode.point = root.point;
        nearest(root, p, 'x', minNode);
        return minNode.point;
    }

    // when there are two possible subtrees to go down,
    // you always choose the subtree that is on the same side of
    // the splitting line as the query point as the first subtree to explore
    private void nearest(Node searchNode, Point2D point, char ori, Node minNode) {
        if (searchNode != null &&
                searchNode.rectangle.distanceSquaredTo(point) <
                        minNode.point.distanceSquaredTo(point)) {
            minNode.point = searchNode.point;
        }
        if (ori == 'x') {
            if (point.x() < searchNode.point.x()) {
                nearest(searchNode.left, point, 'y', minNode);
                nearest(searchNode.right, point, 'y', minNode);
            }
            else {
                nearest(searchNode.right, point, 'y', minNode);
                nearest(searchNode.left, point, 'y', minNode);
            }
        }
        if (ori == 'y') {
            if (point.y() < searchNode.point.y()) {
                nearest(searchNode.left, point, 'x', minNode);
                nearest(searchNode.right, point, 'x', minNode);
            }
            else {
                nearest(searchNode.right, point, 'x', minNode);
                nearest(searchNode.left, point, 'x', minNode);
            }
        }
    }

    public static void main(String[] args) {
        KdTree test = new KdTree();
        test.insert(new Point2D(0.7, 0.2));
        test.insert(new Point2D(0.5, 0.4));
        test.insert(new Point2D(0.2, 0.3));
        test.insert(new Point2D(0.4, 0.7));
        test.insert(new Point2D(0.9, 0.6));
        StdOut.println(test.nearest(new Point2D(0.606, 0.623)));

    }
}
