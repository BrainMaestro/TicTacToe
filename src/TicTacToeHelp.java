import java.util.ArrayList;
import java.util.Random;

public class TicTacToeHelp {

	private String board[] = new String[9];
	
	public TicTacToeHelp() {
		
		for ( int i = 0; i < board.length; i++ ) {
			
			board[i] = " ";
		}
	}
	
	public void makeMove( String[] board, String letter, int num ) {
		
		board[num] = letter;
	}
	
	public boolean isWinner( String[] bo, String le ) {
		
		return ((bo[6].equals(le) && bo[7].equals(le) && bo[8].equals(le)) ||
			    (bo[3].equals(le) && bo[4].equals(le) && bo[5].equals(le)) || 
			    (bo[0].equals(le) && bo[1].equals(le) && bo[2].equals(le)) || 
			    (bo[6].equals(le) && bo[3].equals(le) && bo[0].equals(le)) || 
			    (bo[7].equals(le) && bo[4].equals(le) && bo[1].equals(le)) || 
			    (bo[8].equals(le) && bo[5].equals(le) && bo[2].equals(le)) || 
			    (bo[6].equals(le) && bo[4].equals(le) && bo[2].equals(le)) || 
			    (bo[8].equals(le) && bo[4].equals(le) && bo[0].equals(le)) ); 

	}
	
	public String[] getBoardCopy( String board[] ) {
		
		String dupeBoard[] = board.clone();
		return dupeBoard;
	}
	
	public boolean isSpaceFree( String board[], int move ) {
		
		return board[move] == " ";
	}
	
	public int chooseRandomMoveFromList( String[] board, int[] movesList ) {
		
		Random gen = new Random();
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		
		for( int i : movesList ) {
			
			if ( isSpaceFree( board, i ) )
				possibleMoves.add( i );
		}
		
		if ( possibleMoves.size() != 0 )
			return possibleMoves.get( gen.nextInt( possibleMoves.size() ) );
		else 
			return -1;
	}
	
	public int getComputerMove( String[] board, String computerLetter ) {
		
		String playerLetter;
		
		if ( computerLetter == "X" )
			playerLetter = "O";
		else
			playerLetter = "X";
		
		for ( int i = 0; i < board.length; i++ ) {
			
			String[] copy = getBoardCopy(board);
			if ( isSpaceFree(copy, i) ) {
				
				makeMove(copy, computerLetter, i);
				if ( isWinner(copy, computerLetter) )
					return i;
			}	
		}
		
		for ( int i = 0; i < board.length; i++ ) {
			
			String[] copy = getBoardCopy(board);
			if ( isSpaceFree(copy, i) ) {
				
				makeMove(copy, playerLetter, i);
				if ( isWinner(copy, playerLetter) )
					return i;
			}	
		}
		
		int[] corners = {0, 2, 6, 8};
		int move = chooseRandomMoveFromList(board, corners);
		if ( move != -1 )
			return move;
		
		if ( isSpaceFree(board, 4))
			return 4;
		
		int[] sides = {1, 3, 5 ,7};
		return chooseRandomMoveFromList(board, sides);
	}
	
	
}
