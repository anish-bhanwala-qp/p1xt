import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.Stack;

public class FastCollinearPoints {
    private Point[] points;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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

        // Sort points by coordinates before starting
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
        LineSegment[] segments = new LineSegment[segmentStack.size()];
        int i = 0;
        for (LineSegment segment : segmentStack) {
            segments[i++] = segment;
        }
        return segments;
    }

    private void calculateSegments() {
        int count;
        for (int i = 0; i < points.length; i++) {
            // calculate slope to point i with all elements
            SlopeAndPoint[] slopeToArray = new SlopeAndPoint[points.length - 1];
            int k = 0;
            for (int j = 0; j < points.length; j++) {            
                if (i != j) {
                    slopeToArray[k++] = new SlopeAndPoint(points[i].slopeTo(points[j]), points[j], j);                        
                }         
            }

            // sort array by slopes
            Merge.sort(slopeToArray);                         

            count = 1;
            int found = 0;
            double slope;
            for (int j = 1; j < slopeToArray.length; j++) {
                slope = slopeToArray[j - 1].slope;
                if (Double.compare(slope, slopeToArray[j].slope) == 0) {
                    // These two points have slope matching to line already matched with previous
                    // points
                    // skip this
                    if (slopeToArray[j - 1].index < i) {
                        count = 1;
                        while (j < slopeToArray.length && Double.compare(slope, slopeToArray[j].slope) == 0) {
                            j++;
                        }
                    } else {
                        count++;
                        // If this is last element in array, check if we already have 3 points on a line
                        if (j + 1 == slopeToArray.length && count > 2) {
                            segmentStack.push(new LineSegment(points[i], slopeToArray[j].point));
                            found++;
                        }
                    }
                } else {
                    if (count > 2) {
                        segmentStack.push(new LineSegment(points[i], slopeToArray[j - 1].point));
                        found++;
                    }
                    count = 1;
                }
            }
        }
    }

    private class SlopeAndPoint implements Comparable<SlopeAndPoint> {
        public double slope;
        private Point point;
        private int index;

        public SlopeAndPoint(double slope, Point point, int index) {
            this.slope = slope;
            this.point = point;
            this.index = index;
        }

        @Override
        public int compareTo(SlopeAndPoint slopeAndPoint) {
            return Double.compare(this.slope, slopeAndPoint.slope);
        }
    }
}
