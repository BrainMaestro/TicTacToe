import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by BrainMaestro on 25/01/2017.
 */
public class Board {
    private static final int DEFAULT_POSITIONS = 9;
    private boolean[][] grid;
    private Map<PlayerPosition, Set<Point>> moves;

    public Board() {
        this(DEFAULT_POSITIONS);
    }

    public Board(int positions) {
        // a perfect square is required to make a symmetric NxN board
        if (!isPerfectSquare(positions)) {
            positions = DEFAULT_POSITIONS;
        }

        int root = (int) Math.sqrt(positions);
        grid = new boolean[root][root];
        moves = new HashMap<>();
        for (PlayerPosition playerPosition: PlayerPosition.values()) {
            moves.put(playerPosition, new HashSet<>());
        }
    }

    public boolean move(PlayerPosition playerPosition, int x, int y) {
        if (x < 0 || x > grid.length || y < 0 || y > grid.length) {
            return false;
        }

        // already occupied
        if (grid[x][y]) {
            return false;
        }

        grid[x][y] = true;
        moves.get(playerPosition).add(new Point(x, y));

        return true;
    }

    private boolean isPerfectSquare(int num) {
        int root = (int) Math.sqrt(num);

        return num == root * root;
    }
}
