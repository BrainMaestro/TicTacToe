package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TicTacToe extends JFrame implements ActionListener {
	
	private String userLetter = "X";
	private String computerLetter = "O";
	private JTextField positions[];
	private JLabel info;
	private Listener mouse;
	private JMenu game, options, help;
	private ArrayList<Integer> totalPlays;
	private ArrayList<Integer> userPlays;
	private ArrayList<Integer> computerPlays;
	private Integer[][] wins = { {1, 2, 3}, {1, 4, 7}, {1, 5, 9}, {2, 5, 8},
			{3, 5, 7}, {3, 6, 9}, {4, 5, 6}, {7, 8, 9} };
	/*
	 * Keeps track of number of games played, wins, losses and draws respectively
	 */
	private int[] statistics = { 0, 0, 0, 0 };
	private boolean competitive;
	
	public static void main( String[] args ) {
		
		TicTacToe gui = new TicTacToe();
		gui.setVisible( true );
	}
	
	public TicTacToe() {
		
		super( "Tic-Tac-Toe" );
		setLayout( new BorderLayout() );
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		addWindowListener( new CheckOnExit() );
		setSize( 300, 300 );
		setLocation( 240, 200 );
		setResizable( false );
		
		info = new JLabel( "Start a new game" );
		info.setHorizontalAlignment( JLabel.CENTER );
		add( info, BorderLayout.NORTH );
		
		JPanel board = new JPanel();
		GridLayout grid = new GridLayout( 3, 3 );
		grid.setHgap( 2 ); grid.setVgap( 2 );
		board.setLayout( grid );
		board.setBackground( Color.BLACK );
		
		positions = new JTextField[ 9 ];
		Font f = new Font( "Comic Sans MS", Font.PLAIN, 80 );
		mouse = new Listener();
		
		for ( int i = 0; i < positions.length; i++ ) {
			
			positions[i] = new JTextField( " ", 1 );
			positions[i].setHorizontalAlignment( JTextField.CENTER );
			positions[i].setEditable( false );
			positions[i].setFont( f );
			positions[i].setName( String.valueOf(i));
			board.add( positions[i] );
		}
		
		add( board, BorderLayout.CENTER );
		totalPlays = new ArrayList<Integer>(9);
		userPlays = new ArrayList<Integer>(5);
		computerPlays = new ArrayList<Integer>(5); 
		
		JMenuItem newGame = new JMenuItem( "New Game" );
		JMenuItem reset = new JMenuItem( "Reset" );
		JMenuItem stats = new JMenuItem( "Statistics" );
		JMenuItem easy = new JMenuItem( "Easy" );
		JMenuItem normal = new JMenuItem( "Normal" );
		JMenuItem x = new JMenuItem( "X" );
		JMenuItem o = new JMenuItem( "O" );
		JMenuItem about = new JMenuItem( "About" );
		JMenuItem exit = new JMenuItem( "Exit" );
		
		ButtonGroup difficulty = new ButtonGroup();
		difficulty.add( easy );
		difficulty.add( normal );
		easy.isSelected();
		
		game = new JMenu( "Game" );
		game.add( newGame );
		game.add( reset );
		game.addSeparator();
		game.add( stats );
		game.addSeparator();
		game.add( exit );
		
		options = new JMenu( "Options" );
		options.add( x );
		options.add( o );
		x.isSelected();
		x.setEnabled( false );
		options.addSeparator();
		options.add( easy );
		options.add( normal );
		easy.isSelected();
		easy.setEnabled( false );
		
		help = new JMenu( "Help" );
		help.add( about );
		
		JMenuBar bar = new JMenuBar();
		bar.add( game );
		bar.add( options );
		bar.add( help );
		setJMenuBar( bar );
		
		Handler h[] = new Handler[2];
		h[0] = new Handler( x );
		h[1] = new Handler( easy );
		
		newGame.addActionListener( this );
		reset.addActionListener( this );
		stats.addActionListener( this );
		x.addActionListener( h[0] );
		o.addActionListener( h[0] );
		easy.addActionListener( h[1] );
		normal.addActionListener( h[1] );
		about.addActionListener( this );
		exit.addActionListener( this );
	}
	
	private class Handler implements ActionListener {

		JMenuItem prev;
		
		public Handler( JMenuItem prev ) {
			
			this.prev = prev;
		}
		
		public void actionPerformed(ActionEvent e) {
			
			JMenuItem n = (JMenuItem)e.getSource();
			String action = e.getActionCommand();
			n.setEnabled( false );
			if ( prev != null )
				prev.setEnabled( true );
			
			prev = n;
			
			if ( action.equals( "X" ) || action.equals( "O" ) ) {
				
				userLetter = action;
				computerLetter = ( userLetter.equals( "X" ) ) ? "O" : "X";
				
			}
			else if ( action.equals( "Easy" ) || action.equals( "Normal" ) )
				competitive = ( action.equals( "Normal" ) ) ? true : false;
			
		}
	}
	
	private class Listener extends MouseAdapter {

		boolean played = false;
		
		public void mouseReleased(MouseEvent e) {
			
			options.setEnabled( false );
			JTextField field = (JTextField) e.getSource();
			
			if ( totalPlays.size() < 9 && field.getText().equals( " " ) ) {
				
				field.setText( userLetter );
				int num = Integer.parseInt( field.getName() );
				info.setText( "Player " + userLetter + 
						" just played at position " + num );
				userPlays.add( num );
				totalPlays.add( num );
				played = true;
				if ( check() )
					return;
			}
			
			if ( played ) {
				
				computerPlay();
				played = false;
			}
		}	
	}
	
	private class CheckOnExit extends WindowAdapter {

		public void windowClosing( WindowEvent e ) {
			
			int i = JOptionPane.showConfirmDialog( TicTacToe.this, 
					"Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE );
			if ( i == 0 )
				System.exit( 0 );
		}
	}
	
	private boolean check() {
		
		if ( totalPlays.size() >= 5 ) {
			
			if ( evaluate() ) {
				
				int i = JOptionPane.showConfirmDialog( TicTacToe.this, 
						"Do you want to continue playing?", "Game Finished",
						JOptionPane.YES_NO_OPTION );
				removeMouseListeners();
				if ( i == 0 )
					newGame();
				return true;
			}
		}
		
		return false;
	}
	
	private int computerDecision() {
		
		TicTacToeHelp t = new TicTacToeHelp();
		String[] board = new String[9];
		
		for( int i = 0; i < board.length; i++ ) {
			
			if ( userPlays.contains(i) )
				board[i] = userLetter;
			else if ( computerPlays.contains(i) )
				board[i] = computerLetter;
			else
				board[i] = " ";
		}
		
		return t.getComputerMove(board, computerLetter);
	}
	
	private void computerPlay( ) {
		
		while ( true ) {
			
			int num;
			Random gen = new Random();
			
			if ( competitive )
				num = computerDecision();
			else 
				num = gen.nextInt( 9 );
						
			if ( totalPlays.size() >= 9  )
				break;
			
			if ( positions[num].getText().equals( " " ) ) {
				
				computerPlays.add( num );
				totalPlays.add( num );
				
				/*
				 * This final variable is needed because of the innner Thread
				 * class
				 */
				final int n = num;
				
				/*
				 * The sole purpose of this new thread is to make slowing down
				 * the AI's actions possible
				 */
				Thread t = new Thread() {
					
					public void run() {
						
						try {
							Thread.sleep( 600 );
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						positions[n].setText( computerLetter );
						info.setText( "Player " + computerLetter + 
								" just played at position " + n );
						check();
					}
				};
				
				t.start();
				break;
			}
		}
	}
	
	private boolean evaluate() {
		
		HashSet<Integer> separateWin = new HashSet<Integer>(3);
		
		for ( Integer[] i: wins ) {
			
			for( int j: i )
				separateWin.add( j - 1 );
			
			if ( userPlays.containsAll( separateWin ) ) {
				
				info.setText( "You just won the game! Congratulations!" );
				statistics[0]++; statistics[1]++;
				return true;
			}
			else if ( computerPlays.containsAll( separateWin ) ) {
				
				info.setText( "The AI just won the game! Sorry :(" );
				statistics[0]++; statistics[2]++;
				return true;
			}
			
			separateWin.clear();
		}
		
		if ( totalPlays.size() == 9  ) {
			
			info.setText( "The game is a draw" );
			statistics[0]++; statistics[3]++;
			return true;
		}
		
		return false;
	}
	
	/**
	 * This method is called when either the AI or the user has won the match
	 */
	private void removeMouseListeners() {
		
		for ( int i = 0; i < positions.length; i++ ) {
			
			positions[i].removeMouseListener( mouse );
		}
	}

 	public void actionPerformed(ActionEvent e) {
		
		String action = e.getActionCommand();
		if ( action.equals( "New Game" ) ) {
			
			newGame();
		}
		else if ( action.equals( "Statistics" ) ) {
	
			JOptionPane.showMessageDialog( TicTacToe.this, 
					String.format( "You have played  %d games%nWon   %d%nLost" +
							"    %d%nDrew  %d", statistics[0], statistics[1],
							statistics[2], statistics[3] ) );
		}
		else if ( action.equals( "Reset" ) ) {
			
			this.dispose();
			TicTacToe gui = new TicTacToe();
			gui.setVisible( true );
		}
		else if ( action.equals( "About" ) ) {
			
			String info = "Written By Ezinwa Okpoechi\n\n" +
					"Email:      ezimaestro@gmail.com\nPhone:    08063023430";
			JOptionPane.showMessageDialog( TicTacToe.this, info, "About", 
					JOptionPane.INFORMATION_MESSAGE );
		}
		else if ( action.equals( "Exit" ) ) {
			
			int i = JOptionPane.showConfirmDialog( TicTacToe.this, 
					"Do you want to exit?", "Exit", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE );
			if ( i == 0 )
				System.exit( 0 );
		}
			
		else
			info.setText( "Unexpected Error" );
				
	}
 	
 	private void newGame() {
 		
 		options.setEnabled( true );
		for ( int i = 0; i < positions.length; i++ ) {
			
			positions[i].setText( " " );
			positions[i].addMouseListener( mouse );
		}
		
		info.setText( " " );
		totalPlays.clear();
		userPlays.clear();
		computerPlays.clear();
 	}

}
