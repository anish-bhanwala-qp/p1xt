import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.Stack;

public class BruteCollinearPoints {
    private Point[] points;
    private LineSegment[] segments;
    private Stack<LineSegment> segmentStack;

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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segmentStack) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points argument cannot be null");
        }

        // Check if any argument is null
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException(String.format("Point at position %d is null", i));
            }
        }

        this.segmentStack = new Stack<LineSegment>();
        this.points = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = points[i];
        }

        //Sort points by coordinates before starting
        Merge.sort(this.points);

        for (int i = 1; i < this.points.length; i++) {
            if (this.points[i].compareTo(this.points[i - 1]) == 0) {
                throw new IllegalArgumentException("Any two points cannot be equal");
            }
        }
      
        calculateSegments();
    }

    // the number of line segments
    public int numberOfSegments() {
        return segmentStack.size();
    }

    // the line segments
    public LineSegment[] segments() {
        segments = new LineSegment[segmentStack.size()];
        int i = 0;
        for (LineSegment segment : segmentStack) {
            segments[i++] = segment;     
        }
        return segments;
    }

    private void calculateSegments() {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {                
                findCollinearSegmentForPointIndex(i, j);
            }
        }
    }

    private boolean findCollinearSegmentForPointIndex(int i, int j) {
        double slope1 = points[i].slopeTo(points[j]);
        double slope2, slope3;        
        for (int k = j + 1; k < points.length; k++) {
            slope2 = points[i].slopeTo(points[k]);
            if (Double.compare(slope1, slope2) != 0) {
                continue;
            }
            for (int l = k + 1; l < points.length; l++) {
                slope3 = points[i].slopeTo(points[l]);
                if (Double.compare(slope1, slope3) != 0) {
                    continue;
                }
                // System.out.printf("Segment found %d, %d, %d, %d, %.0f, %.0f, %.0f\n", 
                //     i, j, k, l, slope1, slope2, slope3);
                // System.out.printf("Points %s, %s, %s, %s\n", 
                //     points[i], points[j], points[k], points[l]);                
                segmentStack.push(new LineSegment(points[i], points[l]));
                return true;
            }
        }

        return false;
    }
}