import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by BrainMaestro on 25/01/2017.
 */
class BoardTest {
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void move() {
        assertTrue(board.move(PlayerPosition.PLAYER_ONE, 0, 0), "Valid move");
        assertFalse(board.move(PlayerPosition.PLAYER_TWO, 0, 4), "Invalid move - position does not exist on board");
        assertFalse(board.move(PlayerPosition.PLAYER_TWO, 0, 0), "Invalid move - position already occupied");
    }

}