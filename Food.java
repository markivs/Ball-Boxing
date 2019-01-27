/*
 * Vikram Siddam
 * Period 3
 * 5/25/17
 * Food.java
 * 
 * A class that will represent food particles on screen.
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Food
{
	// class variables for position, size , velocity, color
	private int x, y;
	private int size;
	private int vx, vy;
	private Color color;

	/**
	 * Constructs a new food with specified attributes
	 * 
	 * @param startX
	 *            the initial x position
	 * @param startY
	 *            the initial y position
	 * @param size
	 *            the size of the food particle
	 * @param color
	 *            the color of the food particle
	 * @param vx
	 *            the velocity in the x direction
	 * @param vy
	 *            the velocity in the y direction
	 */
	public Food(int startX, int startY, int size, Color color, int vx, int vy)
	{
		x = startX;
		y = startY;

		this.size = size;
		this.vx = vx;
		this.vy = vy;

		this.color = color;
	}

	/**
	 * Increments the food particle's position based on velocity
	 */
	public void move()
	{
		x += vx;
		y += vy;
	}

	/**
	 * @return the x position of the food particle
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @return the y position of the food particle
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * @return the size of the food particle
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * Draws the food particle
	 * 
	 * @param g
	 *            the Graphics object which it will be depicted on
	 */
	public void draw(Graphics g)
	{
		Color ogColor = g.getColor();
		g.setColor(color);
		g.drawOval(x, y, size, size);
		g.setColor(ogColor);
	}

	/**
	 * Determines whether the particle has collided with a player
	 * 
	 * @param other
	 *            the player
	 * @return whether the particle has collided with the player
	 */
	public boolean collided(Player other)
	{
		// inaccurate collision detection to improve speed. Food is circular but
		// use rectangle collisions
		Rectangle food = new Rectangle(x, y, size, size);
		Rectangle player = new Rectangle(other.getX(), other.getY(), other.getMass(), other.getMass());

		return food.intersects(player);
	}
}

