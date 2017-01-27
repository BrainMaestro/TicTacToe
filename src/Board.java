import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by BrainMaestro on 25/01/2017.
 */
public class Board {
    private static final int DEFAULT_POSITIONS = 9;
    private boolean[][] grid;
    private HashMap<PlayerPosition, TreeSet<Point>> moves;
    private Comparator<Point> pointComparator;
    private int positionsLeft;

    private enum WinLine {
        HORIZONTAL, VERTICAL, DIAGONAL
    }

    public Board() {
        this(DEFAULT_POSITIONS);
    }

    public Board(int positions) {
        // a perfect square is required to make a symmetric NxN board
        if (!isPerfectSquare(positions)) {
            positions = DEFAULT_POSITIONS;
        }

        positionsLeft = positions;
        int root = (int) Math.sqrt(positions);
        grid = new boolean[root][root];
        moves = new HashMap<>();
        for (PlayerPosition playerPosition: PlayerPosition.values()) {
            moves.put(playerPosition, new TreeSet<>(getPointComparator()));
        }
    }

    public boolean move(PlayerPosition playerPosition, int x, int y) {
        if (x < 0 || x > grid.length || y < 0 || y > grid.length || isFull()) {
            return false;
        }

        // already occupied
        if (grid[x][y]) {
            return false;
        }

        grid[x][y] = true;
        moves.get(playerPosition).add(new Point(x, y));
        positionsLeft--;

        return true;
    }

    public boolean isFull() {
        return positionsLeft == 0;
    }

    public boolean checkWinner(PlayerPosition playerPosition) {
        TreeSet<Point> playerPoints = moves.get(playerPosition);
        if (playerPoints.size() < grid.length) {
            return false;
        }

        for (Point[] points: combination(playerPoints.toArray(new Point[0]))) {
            if (hasStraight(points)) {
                return true;
            }
        }

        return false;
    }

    private boolean isPerfectSquare(int num) {
        int root = (int) Math.sqrt(num);

        return num == root * root;
    }

    private Comparator<Point> getPointComparator() {
        if (pointComparator != null) {
            return pointComparator;
        }

        pointComparator = (point1, point2) -> {
            if (point1.equals(point2)) {
                return 0;
            }

            if (point1.getX() < point2.getX()) {
                return -1;
            }

            return 1;
        };

        return pointComparator;
    }

    /**
     * Checks if a straight line that leads to a win exists among the moves a player has made
     *
     * @param points
     * @return
     */
    private boolean hasStraight(Point[] points) {
        for (WinLine lineType: WinLine.values()) {
            int x = -1; // x value for comparison
            int y = -1; // y value for comparison
            int d = 0; // diagonal value to adjust x and y values
            boolean failed = false;
            for (Point point: points) {
                if (x == -1 && y == -1) {
                    x = point.x;
                    y = point.y;

                    // a diagonal can either lean backward *\* or forward */*
                    // the initial point values tells us which way
                    if (x == 0 && y == 0) {
                        d = 1;
                    } else if (x == grid.length && y == 0) {
                        d = -1;
                    }

                    continue;
                }

                switch (lineType) {
                    case HORIZONTAL:
                        x += 1; break;
                    case VERTICAL:
                        y += 1; break;
                    case DIAGONAL:
                        x += d; y += d; break;
                }

                if (point.x != x || point.y != y) {
                    failed = true;
                    break;
                }
            }

            if (!failed) {
                return true;
            }
        }

        return false;
    }

    /**
     * Create a combination of all possible points that could lead to a win
     *
     * @param playerPoints
     * @return
     */
    private List<Point[]> combination(Point[] playerPoints) {
        int[] combination = new int[grid.length];
        int N = playerPoints.length;
        List<Point[]> pointList = new ArrayList<>();

        int r = 0;
        int index = 0;

        while(r >= 0){
            // possible indexes for 1st position "r=0" are "0,1,2" --> "A,B,C"
            // possible indexes for 2nd position "r=1" are "1,2,3" --> "B,C,D"

            // for r = 0 ==> index < (4+ (0 - 2)) = 2
            if(index <= (N + (r - grid.length))){
                combination[r] = index;

                // if we are at the last position print and increase the index
                if (r == grid.length - 1) {
                    Point[] points = new Point[combination.length];

                    for (int i = 0; i < combination.length; i++) {
                        points[i] = playerPoints[combination[i]];
                    }

                    pointList.add(points);
                    index++;
                } else {
                    // select index for next position
                    index = combination[r] + 1;
                    r++;
                }
            } else {
                r--;
                if (r > 0)
                    index = combination[r] + 1;
                else
                    index = combination[0] + 1;
            }
        }

        return pointList;
    }
}
