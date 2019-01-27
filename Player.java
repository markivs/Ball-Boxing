/**
 * Vikram Siddam
 * Period 3
 * 5/25/17
 * Player.java
 * 
 * A class to represent a player
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Player
{
	// class variables to keep track of position, velocity, acceleration, color
	// etc. of the player
	private int x, y;
	public static final int initialMass = 50;
	private double vx, vy, accX, accY;
	private boolean accUp, accDown, accLeft, accRight, shield;
	private int mass;
	private final int UP, DOWN, LEFT, RIGHT, SHIELD;
	private Color color;
	private int shieldTicks;
	private int colX, colY;
	public static final double FRICTION = 0.001;
	public static final int SIZE_INCREASE = 100;
	public final static int MAX_SHIELD = 1000;

	/**
	 * Creates a new Player with specified attributes
	 * 
	 * @param up
	 *            keycode for up
	 * @param down
	 *            keycode for down
	 * @param left
	 *            keycode for left
	 * @param right
	 *            keycode for right
	 * @param attack
	 *            keycode for attack
	 * @param c
	 *            initial color of player
	 */
	public Player(int startX, int startY, int up, int down, int left, int right, int shield, Color c)
	{
		UP = up;
		DOWN = down;
		LEFT = left;
		RIGHT = right;
		SHIELD = shield;
		color = c;
		x = startX;
		y = startY;
		accX = 0;
		accY = 0;
		mass = initialMass;
		shieldTicks = MAX_SHIELD;
	}

	/**
	 * @return the x position (top left corner)
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @return the y position (top left corner)
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * @return the mass (also represents diameter) of player
	 */
	public int getMass()
	{
		return mass;
	}

	/**
	 * @return velocity in x direction
	 */
	public double getVX()
	{
		return vx;
	}

	/**
	 * @return velocity in y direction
	 */
	public double getVY()
	{
		return vy;
	}

	/**
	 * Checks whether player has collided with another player and implements
	 * collision logic accordingly.
	 * 
	 * @param other
	 *            the other player
	 * @return whether the player has collided
	 */
	public boolean collided(Player other)
	{
		boolean collisionOccured = false; // keep track of whether collision has
											// occured
		int x1 = x + mass / 2; // position of this player, according to center
								// (for easier calculations)
		int y1 = y + mass / 2;
		int x2 = other.getX() + other.getMass() / 2; // position of other
														// player, according to
														// center
		int y2 = other.getY() + other.getMass() / 2;
		int radius1 = mass / 2; // radius of this player
		int radius2 = other.getMass() / 2; // radius of other player

		double dx = x2 - x1; // distance between player's centers
		double dy = y2 - y1;
		int radii = radius1 + radius2; // sum of radii
		// if collision occured (based on triangle b/w centers (takes into
		// account some degree of error that can result from high velocities)
		if ((dx * dx) + (dy * dy) < radii * radii && (dx * dx) + (dy * dy) < radii * radii - mass / 10)
		{
			collisionOccured = true; // collision has occured

			double xVelocity = other.getVX() - vx; // determine differences
													// between velocities of
													// both players
			double yVelocity = other.getVY() - vy;
			double dotProduct = x * vx + y * vy; // helpful for some
													// calculations
			if (!shield)
			{

				// First, find the normalized vector n from the center of
				// circle1 to the center of circle2

				double dxn = dx / (radius1 + radius2);
				double dyn = dy / (radius1 + radius2);

				// Find the length of the component of each of the movement
				// vectors along n.
				// a1 = v1 . n
				// a2 = v2 . n
				double a1 = radius1;
				double a2 = radius2;

				// determine the force of collision (scaled down because of some
				// error in mass)
				double optimizedP = 0.2 * ((a1 - a2)) / (mass + other.mass);

				// Calculate new velocities, v', the new movement vector of both
				// circles
				// v1' = v1 - optimizedP * m2 * n
				double newVelX1;
				if (dx < 0)
					newVelX1 = other.getVX() + optimizedP * other.getMass() * dxn;
				else
					newVelX1 = other.getVX() - optimizedP * other.getMass() * dxn;

				double newVelY1;
				if (dy < 0)
					newVelY1 = other.getVY() + optimizedP * other.getMass() * dyn;
				else
					newVelY1 = other.getVY() - optimizedP * other.getMass() * dyn;

				double newVelX2;
				if (dx < 0)
					newVelX2 = vx + optimizedP * mass * dxn;
				else
					newVelX2 = vx - optimizedP * mass * dxn;

				double newVelY2;
				if (dy < 0)
					newVelY2 = vy + optimizedP * mass * dyn;
				else
					newVelY2 = vy - optimizedP * mass * dyn;

				if (!shield) // void collision if shield is active
				{
					vx = newVelX1; // set new velocities
					vy = newVelY1;

					x += vx; // increment positions to avoid players getting
								// stuck
					y += vy;

					// voidAcceleration();

				}
				if (!other.getShield()) // void collision if shield is active
				{
					other.setVX(newVelX2); // same as previous if statement
					other.setVY(newVelY2);

					other.setX((int) (other.getX() + other.getVX()));
					other.setY((int) (other.getY() + other.getVY()));

					// other.voidAcceleration();
				}

			} else if ((dx * dx) + (dy * dy) < radii * radii)
			{
				other.setVY(-other.getVY()); // guarantees that when players are
												// stuck, they will not stay
												// that way (very unlikely to
												// happen)
				other.setVX(-other.getVX());
				vy = -vy;
				vx = -vx;
			}
		}
		return collisionOccured; // return result of collision checks
	}

	/**
	 * Sets desired x position of player
	 * 
	 * @param x
	 *            the x position
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * Sets desired y position of player
	 * 
	 * @param y
	 *            the y position
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * Sets desired velocity in x direction for player
	 * 
	 * @param vx
	 *            the x velocity
	 */
	public void setVX(double vx)
	{
		this.vx = vx;
	}

	/**
	 * Sets desired velocity in y direction for player
	 * 
	 * @param vy
	 *            the y velocity
	 */
	public void setVY(double vy)
	{
		this.vy = vy;
	}

	/**
	 * Sets desired color for player
	 * 
	 * @param c
	 *            the color
	 */
	public void setColor(Color c)
	{
		color = c;
	}

	/**
	 * Returns whether the shield is active
	 * 
	 * @return whether the shield is active
	 */
	public boolean getShield()
	{
		return shield;
	}

	/**
	 * Changes position of player based on acceleration and velocity Updates
	 * state of player including shield
	 */
	public void move()
	{
		if (accUp || accDown)
		{
			vy += accY;
		}
		if (accRight || accLeft)
		{
			vx += accX;
		}

		x += vx;
		y += vy;

		if (shield)
			shieldTicks--;
		if (shieldTicks <= 0)
			shield = false;
	}

	/**
	 * Renders player on Graphics component
	 * 
	 * @param g
	 *            the Graphics object
	 */
	public void draw(Graphics g)
	{
		Color ogColor = g.getColor();
		if (shield)
			g.setColor(ogColor);
		else
			g.setColor(color);
		g.fillOval(x, y, mass, mass);
		g.setColor(ogColor);
		g.fillOval(colX - 5, colY - 5, 10, 10);

	}

	/**
	 * Method to be called if key is pressed to change state of player
	 * 
	 * @param e
	 *            the KeyEvent of a KeyListener
	 */
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == UP)
		{
			accUp = true;
			accDown = false;
			accY = -10 / (double) mass;
		}
		if (e.getKeyCode() == DOWN)
		{
			accDown = true;
			accUp = false;
			accY = 10 / (double) mass;
		}
		if (e.getKeyCode() == LEFT)
		{
			accLeft = true;
			accRight = false;

			accX = -10 / (double) mass;
		}
		if (e.getKeyCode() == RIGHT)
		{
			accRight = true;
			accLeft = false;
			accX = 10 / (double) mass;
		}

		if (shieldTicks >= 0 && e.getKeyCode() == SHIELD)
		{
			shield = true;
		}
	}

	/**
	 * Adds specified amount of mass to player (increases size)
	 * 
	 * @param mass
	 *            the mass
	 */
	public void addMass(int mass)
	{
		this.mass += mass;
	}

	/**
	 * Determines whether player is knocked out of bounds
	 * 
	 * @return whether player is knocked out
	 */
	public boolean isKnockedOut()
	{

		return (x + mass < 0) || (x > GamePanel.WIDTH) || (y + mass < 0) || (y > GamePanel.HEIGHT);
	}

	/**
	 * @return number of ticks the shield has been active
	 */
	public int getShieldTicks()
	{
		return shieldTicks;
	}

	/**
	 * Method to be called if key is released to update state of player
	 * 
	 * @param e
	 *            the KeyEvent from KeyListener
	 */
	public void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();
		if (code == UP)
		{
			accUp = false;
			accY = 0;
		}
		if (code == DOWN)
		{
			accDown = false;
			accY = 0;
		}
		if (code == LEFT)
		{
			accLeft = false;
			accX = 0;
		}
		if (code == RIGHT)
		{
			accRight = false;
			accX = 0;
		}

		if (code == SHIELD)
		{
			shield = false;
		}
	}

	/**
	 * @return current color of player
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Voids acceleration
	 */
	public void voidAcceleration()
	{
		accUp = false;
		accDown = false;
		accLeft = false;
		accRight = false;
	}
}
