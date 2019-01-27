/*
 * Vikram Siddam
 * Period 3
 * 5/27/17
 * GameFrame.java
 * 
 * The JFrame class that manages the behavior of JPanel class
 */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;

import javax.swing.JFrame;

public class GameFrame extends JFrame implements KeyListener
{
	// class variables storing JPanel instance and other attributes
	private GamePanel gamePanel;
	public static final String gameName = "Ball Boxing";
	public static final int WIDTH = 2050, HEIGHT = 1070;

	/**
	 * Creates a GameFrame
	 */
	public GameFrame()
	{
		super(gameName);

		gamePanel = new GamePanel(); // instance of GamePanel
		add(gamePanel); // add panel to frame
		requestFocus(); // request focus
		addKeyListener(this);
		setSize(WIDTH, HEIGHT); // set dimensions
		setResizable(false); // disable resizability
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close application
														// when frame is closed
		setVisible(true);
		super.toFront(); // bring to front of screen?
	}

	/**
	 * Creates a GameFrame with specified game attributes
	 * 
	 * @param foodFreq
	 *            freqency of panel food
	 * @param knockoutFrames
	 *            number of knockout frames for players
	 */
	public GameFrame(int foodFreq, int knockoutFrames)
	{
		super(gameName);

		gamePanel = new GamePanel(foodFreq, knockoutFrames); // instance of
																// GamePanel
																// with
																// specifications
		add(gamePanel);

		addKeyListener(this);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * @return the GamePanel of this GameFrame
	 */
	public GamePanel getPanel()
	{
		return gamePanel;
	}

	/**
	 * repaints game intermittently
	 */
	public void repaintGame()
	{
		gamePanel.repaint();
	}

	/**
	 * Runs game with user interface in console, informing user on how to play
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		GameFrame frame; // new instance of game
		Scanner scanner = new Scanner(System.in); // instance of scanner to
													// receive input
		System.out.println("Welcome to " + GameFrame.gameName.trim() + " by Vikram!\n"); // welcome
																							// notification
																							// with
																							// game
																							// name
																							// and
																							// my
																							// name
		// Tells user controls
		System.out
				.println("Player 1 uses WASD controls and is cyan.\n" + "Player 2 uses Arrow Key controls and is red.\n"
						+ "Eat food to gain mass, which can be used to knock the other player out of bounds more easily.\n"
						+ "A player which is knocked out of the screen will have their damage bar go up.\n"
						+ "When the damage bar reaches max capacity, the associated player loses.\n\n");

		// Give user time to read instructions
		try
		{
			Thread.sleep(2000);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		// Tells user more information
		System.out.println("Shield (SHIFT for player1, ENTER for player2) can be used to temporarily void knockback\n"
				+ "and increase size.\n"
				+ "When the gray bar at the bottom of the screen runs out, the shield will automatically be diabled \n"
				+ "and cannot be reactivated for the rest of the game.\n\n");

		// Prompts user to play game
		System.out.println("Type the enter key to skip setup, any key to continue with setup");
		String input = scanner.nextLine().trim();

		if (input.equalsIgnoreCase(""))
		{
			frame = new GameFrame();
		} else // setup
		{
			System.out.println("Enter the desired HP of each player as an integer");
			int knockoutFrames = scanner.nextInt();
			System.out.println("Enter the food frequency as an integer");
			int foodFreq = scanner.nextInt();

			frame = new GameFrame(foodFreq, knockoutFrames);

		}
		scanner.close();

		// keep repainting game
		while (!frame.getPanel().isGameOver())
		{
			try
			{
				Thread.sleep(20);
			} catch (Exception e)
			{
				frame.dispose();
			}
			frame.repaintGame();
		}

		// variable to store amount of after-game frames
		int finalCount = 0;

		// keep playing game until counter runs out
		while (finalCount < 100)
		{
			try
			{
				Thread.sleep(100);
			} catch (Exception e)
			{
				frame.dispose();
			}
			frame.repaintGame();
			finalCount++;
		}
		// print game over
		System.out.println("Game Over - " + frame.getPanel().getWinner());
		frame.dispose(); // close game window
	}

	/**
	 * KeyListener method, activated when key pressed
	 * 
	 * @param arg0
	 *            the keyEvent
	 */
	public void keyPressed(KeyEvent arg0)
	{
		gamePanel.keyPressed(arg0);
	}

	/**
	 * KeyListener method, activated when key released
	 * 
	 * @param arg0
	 *            the keyEvent
	 */
	public void keyReleased(KeyEvent arg0)
	{
		gamePanel.keyReleased(arg0);
	}

	/**
	 * KeyListener method, activated when key typed (not used)
	 * 
	 * @param arg0
	 *            the keyEvent
	 */
	public void keyTyped(KeyEvent arg0)
	{
	}
}
