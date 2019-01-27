/**
 * Vikram Siddam
 * Period 3
 * 5/25/17
 * GamePanel.java
 * 
 * The JPanel class to coordinate the behavior of all other game objects
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GamePanel extends JPanel
{
	// class variables for players, foodmanager, and basic attributes
	// Based on framework, multiple players can be added, but for ease of use
	// only two will be used.
	private Player player1;
	private Player player2;
	private FoodManager manager;
	public final static int WIDTH = GameFrame.WIDTH, HEIGHT = GameFrame.HEIGHT;
	private int foodFrequency;
	private int player1KnockoutCounter, player2KnockoutCounter;
	private int maxKnockoutFrames;
	public static final int bufferFrameCount = 0; // variable that delays
													// buffer time, just for fun
	private int bufferFrames;

	/**
	 * Creates a new GamePanel class with certain foodFrequency and
	 * maxKnockoutFrames(aka health)
	 * 
	 * @param foodFreq
	 *            frequency of food for FoodManager
	 * @param knockoutFrames
	 *            amount of frames a player can be knocked out before game is
	 *            over
	 */
	GamePanel(int foodFreq, int knockoutFrames)
	{
		setSize(WIDTH, HEIGHT); // set dimensions for frame
		setBackground(Color.BLACK);
		requestFocus(true); // request focus
		foodFrequency = foodFreq; // set instance variables
		maxKnockoutFrames = knockoutFrames;

		// create instances of players and FoodManager based on specifications.
		player1 = new Player(10, 10, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SHIFT,
				Color.CYAN);
		player2 = new Player(WIDTH - 10 - Player.initialMass, HEIGHT - 10 - Player.initialMass, KeyEvent.VK_UP,
				KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER, Color.RED);
		manager = new FoodManager(foodFrequency);
		player1KnockoutCounter = 0; // start knockout counters at 0
		player2KnockoutCounter = 0;
		bufferFrames = 0;

		setVisible(true); // set visible
	}

	/**
	 * Default constructor
	 */
	GamePanel()
	{
		setSize(WIDTH, HEIGHT);
		setBackground(Color.BLACK);
		foodFrequency = 50;
		maxKnockoutFrames = 1000;

		player1 = new Player(10, 10, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SHIFT,
				Color.CYAN);
		player2 = new Player(WIDTH - 3 * Player.initialMass, HEIGHT - 2 * Player.initialMass, KeyEvent.VK_UP,
				KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER, Color.RED);
		manager = new FoodManager(foodFrequency);
		player1KnockoutCounter = 0;
		player2KnockoutCounter = 0;
		bufferFrames = 0;
		setVisible(true);
	}

	/**
	 * Notifies players that key has been pressed
	 * 
	 * @param e
	 *            the KeyEvent from the KeyListener
	 */
	public void keyPressed(KeyEvent e)
	{
		player1.keyPressed(e);
		player2.keyPressed(e);
	}

	/**
	 * Determines whether game should end or not
	 * 
	 * @return whether game is over
	 */
	public boolean isGameOver()
	{
		if (player1.isKnockedOut()) // increments knockout counter
			player1KnockoutCounter++;
		if (player2.isKnockedOut())
			player2KnockoutCounter++;

		// determines whether a player has exceeded knockout count
		return player1KnockoutCounter > maxKnockoutFrames || player2KnockoutCounter > maxKnockoutFrames;
	}

	/**
	 * Determines the winner of the game
	 * 
	 * @return the text to be displayed
	 */
	public final String getWinner()
	{
		String winner = "";
		if (player1KnockoutCounter > maxKnockoutFrames && player2KnockoutCounter > maxKnockoutFrames)
			return "Tie";

		else if (player1KnockoutCounter > maxKnockoutFrames)
			winner = "player2";
		else if (player2KnockoutCounter > maxKnockoutFrames)
			winner = "player1";

		return winner + " wins";
	}

	/**
	 * Method that is automatically called every time frame/panel is updated
	 * Manages behavior of game objects every time.
	 * 
	 * @param g
	 *            the graphics component
	 */
	public void paint(Graphics g)
	{
		if (bufferFrames++ >= bufferFrameCount)
		{
			super.paint(g); // clear panel
			bufferFrames = 0;
		}

		// draw health bars
		drawHealthBars(g);
		drawShieldBars(g);

		// add mass of collided foods
		player1.addMass(manager.collided(player1));
		player2.addMass(manager.collided(player2));

		// check collisions
		if (player1.getX() < player2.getX())
			player1.collided(player2);
		else
			player2.collided(player1);

		// draw and move game objects
		player1.draw(g);
		player2.draw(g);
		player1.move();
		player2.move();
		manager.draw(g);
		manager.move();

		// draw user interface components
		if (player1.isKnockedOut())
			drawArrow(player1, g);
		if (player2.isKnockedOut())
			drawArrow(player2, g);
		if (isGameOver())
			drawGameOver(g);
	}

	/**
	 * Draws health bars on graphics component
	 * 
	 * @param g
	 *            the graphics component
	 */
	private void drawHealthBars(Graphics g)
	{
		Color ogColor = g.getColor();
		int barWidth = 10;

		g.setColor(new Color(player1.getColor().getRed(), player1.getColor().getGreen(), player1.getColor().getBlue(),
				50));
		g.fillRect(0, 0, WIDTH / 2, barWidth);
		g.setColor(player1.getColor());
		if (player1KnockoutCounter / maxKnockoutFrames >= 1)
			g.fillRect(0, 0, WIDTH / 2, barWidth);

		else
			g.fillRect(0, 0, (int) (((double) player1KnockoutCounter / maxKnockoutFrames) * WIDTH / 2), barWidth);

		g.setColor(new Color(player2.getColor().getRed(), player2.getColor().getGreen(), player2.getColor().getBlue(),
				50));
		g.fillRect(WIDTH / 2, 0, WIDTH, barWidth);
		g.setColor(player2.getColor());
		if (player2KnockoutCounter / maxKnockoutFrames >= 1)
			g.fillRect(WIDTH / 2, 0, WIDTH, barWidth);
		else
			g.fillRect(WIDTH - (int) (((double) player2KnockoutCounter / maxKnockoutFrames) * WIDTH / 2), 0, WIDTH,
					barWidth);
		g.setColor(ogColor);
	}

	/**
	 * Draws Game Over notification
	 * 
	 * @param g
	 *            the graphics component
	 */
	private void drawGameOver(Graphics g)
	{
		JLabel text = new JLabel("GAME OVER - " + getWinner(), SwingConstants.CENTER);
		text.setBounds(WIDTH / 2 - 500, HEIGHT / 2, 1000, 50);
		text.setFont(new Font("TimesRoman", Font.PLAIN, 50));

		this.add(text);

	}

	/**
	 * Draws shield bars
	 * 
	 * @param g
	 *            the graphics component
	 */
	private void drawShieldBars(Graphics g)
	{
		Color ogColor = g.getColor();
		int barWidth = 50;
		g.setColor(Color.GRAY);

		g.fillRect(0, HEIGHT - barWidth, (int) (((double) player1.getShieldTicks() / Player.MAX_SHIELD) * WIDTH / 2),
				barWidth);
		g.fillRect(WIDTH - (int) (((double) player2.getShieldTicks() / Player.MAX_SHIELD) * WIDTH / 2),
				HEIGHT - barWidth, WIDTH, barWidth);

	}

	/**
	 * Draws arrow towards an out-of-bounds player to help them find their way
	 * back into the ring
	 * 
	 * @param player
	 *            the player
	 * @param g
	 *            the graphics component
	 */
	private void drawArrow(Player player, Graphics g)
	{
		Color ogColor = g.getColor(); // store original component
		int arrowX1 = WIDTH / 2; // origin of arrow
		int arrowY1 = HEIGHT / 2;
		// determines angle of arrow to player
		double angle = Math.atan(((double) (arrowY1 - player.getY()) / (arrowX1 - player.getX())));

		// determines length of arrow depending on how far away player is from
		// origin
		double arrowLength = .01
				* Math.sqrt(Math.pow(arrowX1 - player.getX(), 2) + Math.pow(arrowY1 - player.getY(), 2));

		// determine where connecting point of arrow is (adjusting negativity
		// based on inverse tan inaccuracies)
		int arrowX2;
		if (player.getX() - arrowX1 > 0)
			arrowX2 = arrowX1 + (int) (arrowLength * Math.cos(angle));
		else
			arrowX2 = arrowX1 - (int) (arrowLength * Math.cos(angle));

		int arrowY2;
		if (player.getX() - arrowX1 > 0)
			arrowY2 = arrowY1 + (int) (arrowLength * Math.sin(angle));
		else
			arrowY2 = arrowY1 - (int) (arrowLength * Math.sin(angle));

		// draw line between points
		g.setColor(player.getColor());
		g.drawLine(arrowX1, arrowY1, arrowX2, arrowY2);

		g.setColor(ogColor);
	}

	/**
	 * Notifies player that key has been released
	 * 
	 * @param e
	 *            the KeyEvent from the KeyListener
	 */
	public void keyReleased(KeyEvent e)
	{
		player1.keyReleased(e);
		player2.keyReleased(e);
	}
}
