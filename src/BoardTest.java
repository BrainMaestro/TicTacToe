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

    @Test
    void checkWinner() {
        board.move(PlayerPosition.PLAYER_TWO, 0, 0);
        assertFalse(board.checkWinner(PlayerPosition.PLAYER_TWO), "Not enough moves to win");

        board.move(PlayerPosition.PLAYER_TWO, 0, 1);
        board.move(PlayerPosition.PLAYER_TWO, 2, 0);
        board.move(PlayerPosition.PLAYER_TWO, 2, 1);
        board.move(PlayerPosition.PLAYER_TWO, 1, 2);
        assertFalse(board.checkWinner(PlayerPosition.PLAYER_TWO), "Not enough correct moves to win");

        board = new Board(); // clear board
        board.move(PlayerPosition.PLAYER_ONE, 0, 0); // for win
        board.move(PlayerPosition.PLAYER_ONE, 0, 1); // for win
        board.move(PlayerPosition.PLAYER_ONE, 1, 0);
        board.move(PlayerPosition.PLAYER_ONE, 1, 2);
        board.move(PlayerPosition.PLAYER_ONE, 2, 1);
        board.move(PlayerPosition.PLAYER_ONE, 0, 2); // for win
        assertTrue(board.checkWinner(PlayerPosition.PLAYER_ONE), "Horizontal win");

        board = new Board(); // clear board
        board.move(PlayerPosition.PLAYER_ONE, 0, 0);
        board.move(PlayerPosition.PLAYER_ONE, 1, 1); // for win
        board.move(PlayerPosition.PLAYER_ONE, 0, 1); // for win
        board.move(PlayerPosition.PLAYER_ONE, 2, 1); // for win
        board.move(PlayerPosition.PLAYER_ONE, 0, 2);
        assertTrue(board.checkWinner(PlayerPosition.PLAYER_ONE), "Vertical win");

        board = new Board(); // clear board
        board.move(PlayerPosition.PLAYER_ONE, 0, 0); // for win
        board.move(PlayerPosition.PLAYER_ONE, 1, 1); // for win
        board.move(PlayerPosition.PLAYER_ONE, 2, 1);
        board.move(PlayerPosition.PLAYER_ONE, 0, 1);
        board.move(PlayerPosition.PLAYER_ONE, 1, 2);
        board.move(PlayerPosition.PLAYER_ONE, 2, 2); // for win
        assertTrue(board.checkWinner(PlayerPosition.PLAYER_ONE), "Diagonal win");
    }

    @Test
    void isFull() {
        board.move(PlayerPosition.PLAYER_ONE, 0, 0);
        board.move(PlayerPosition.PLAYER_ONE, 1, 0);
        board.move(PlayerPosition.PLAYER_ONE, 2, 0);
        board.move(PlayerPosition.PLAYER_ONE, 0, 1);
        board.move(PlayerPosition.PLAYER_ONE, 1, 1);
        board.move(PlayerPosition.PLAYER_ONE, 2, 1);
        board.move(PlayerPosition.PLAYER_ONE, 0, 2);
        board.move(PlayerPosition.PLAYER_ONE, 1, 2);
        board.move(PlayerPosition.PLAYER_ONE, 2, 2);
        assertTrue(board.isFull());
    }

}