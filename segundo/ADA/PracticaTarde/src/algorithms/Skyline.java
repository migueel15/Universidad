package algorithms;

import analyzer.Algorithm;

import java.util.ArrayList;
import java.util.Comparator;

public class Skyline implements Algorithm {
    long n;
    ArrayList<Point> points, solution;

    @Override
    public String getName() {
        return "Skyline";
    }

    @Override
    public void init(long n) {
        this.n = n;
        reset();
    }

    @Override
    public void reset() {
        points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            points.add(new Point((int) (Math.random() * 100), (int) (Math.random() * 100)));
        }
    }

    @Override
    public String getSolution() {
        String solText = "";
        for (Point point : this.solution) {
            solText += "(" + point.x + ", " + point.y + ")\n";
        }
        return solText;
    }

    @Override
    public String getComplexity() {
        return "n*log(n)";
    }

    @Override
    public long getMaxSize() {
        return 100;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10000 ; i++) {
            solution = produceSubSkyLines(points);
        }
    }

    public ArrayList<Point> produceSubSkyLines(ArrayList<Point> list) {
        // part where function exits flashback
        int size = list.size();
        if (size == 1) {
            return list;
        } else if (size == 2) {
            if (list.get(0).dominates(list.get(1))) {
                list.remove(1);
            } else {
                if (list.get(1).dominates(list.get(0))) {
                    list.remove(0);
                }
            }
            return list;
        }

        // recursive part of the function
        ArrayList<Point> leftHalf = new ArrayList<>();
        ArrayList<Point> rightHalf = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() / 2) {
                leftHalf.add(list.get(i));
            } else {
                rightHalf.add(list.get(i));
            }
        }
        ArrayList<Point> leftSubSkyLine = produceSubSkyLines(leftHalf);
        ArrayList<Point> rightSubSkyLine = produceSubSkyLines(rightHalf);

        // skyline is produced
        return produceFinalSkyLine(leftSubSkyLine, rightSubSkyLine);
    }

    public ArrayList<Point> produceFinalSkyLine(ArrayList<Point> left, ArrayList<Point> right) {
        // dominated points of ArrayList left are removed
        for (int i = 0; i < left.size() - 1; i++) {
            if (left.get(i).x == left.get(i + 1).x && left.get(i).y > left.get(i + 1).y) {
                left.remove(i);
                i--;
            }
        }

        // minimum y-value is found
        int min = left.get(0).y;
        for (int i = 1; i < left.size(); i++) {
            if (min > left.get(i).y) {
                min = left.get(i).y;
                if (min == 1) {
                    i = left.size();
                }
            }
        }

        // dominated points of ArrayList right are removed
        for (int i = 0; i < right.size(); i++) {
            if (right.get(i).y >= min) {
                right.remove(i);
                i--;
            }
        }

        // final skyline found and returned
        left.addAll(right);
        return left;
    }

    public static class Point {

        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean dominates(Point p1) {
            // checks if p1 is dominated
            return ((this.x < p1.x && this.y <= p1.y) || (this.x <= p1.x && this.y < p1.y));
        }
    }

    class XComparator implements Comparator<Point> {

        @Override
        public int compare(Point a, Point b) {
            return Integer.compare(a.x, b.x);
        }
    }
}
