/**
 * Vikram Siddam
 * Period 3
 * 5/25/27
 * FoodManager.java
 * 
 * Object to manipulate multiple food objects at once
 */
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class FoodManager
{
	// class variables consisting of food list, a spawn frequency (rate) and
	// various constants
	private List<Food> foods;
	private int rate;
	public static final double FOOD_SIZE = 5;
	public static final double MAX_VEL = 10;

	private int spawnCount;

	/**
	 * Creates a new FoodManager with certain food spawn-rate
	 * 
	 * @param speed
	 *            the rate - none with 0, faster for more positive
	 */
	public FoodManager(int rate)
	{
		foods = new ArrayList<Food>();
		if (rate < 0)
			rate = 0;
		else
			this.rate = rate;

		spawnCount = 0;
	}

	/**
	 * Draws all managed foods on Graphics component
	 * 
	 * @param g
	 *            the Graphic object which it will be painted on
	 */
	public void draw(Graphics g)
	{
		spawnFoods();
		for (int i = 0; i < foods.size(); i++)
		{
			foods.get(i).draw(g);
		}
	}

	/**
	 * Moves all managed foods
	 */
	public void move()
	{
		for (int i = 0; i < foods.size(); i++)
		{
			foods.get(i).move();
		}
	}

	/**
	 * Checks whether the managed foods have collided with a player and, if so,
	 * discards them If it has collided with wall, the food object is recycled
	 * 
	 * @param other
	 *            the player
	 * @return the mass of food consumed
	 */
	public int collided(Player other)
	{
		int index = 0;
		int totalSize = 0; // keep track of mass of food eaten
		while (index < foods.size())
		{
			// checks if object collided with player, adds size to totalSize
			if (foods.get(index).collided(other))
			{
				totalSize += foods.remove(index).getSize();
			}

			// check if object collided with wall, and if so, destroys it but
			// spawns a new one
			else if (foods.get(index).getX() < 0 || foods.get(index).getX() > GamePanel.WIDTH
					|| foods.get(index).getY() < 0 || foods.get(index).getY() > GamePanel.HEIGHT)
			{
				foods.remove(index).getSize();
				spawnFood();
			}
			index++;
		}

		return (int) (Math.sqrt(totalSize));
	}

	/**
	 * Spawns food based on number of total foods spawned
	 */
	public void spawnFoods()
	{
		if ((Math.random() * spawnCount++) <= rate)
			spawnFood();

	}

	/**
	 * Private method that spawns a single food object with random attributes
	 */
	private void spawnFood()
	{
		int randX = (int) (Math.random() * GamePanel.WIDTH);
		int randY = (int) (Math.random() * GamePanel.HEIGHT);
		int randSize = (int) (Math.random() * FOOD_SIZE) + 1;
		int randVX = (int) (Math.random() * MAX_VEL) - (int) MAX_VEL / 2;
		int randVY = (int) (Math.random() * MAX_VEL) - (int) MAX_VEL / 2;

		// makes sure no food object is created with a zero velocity
		if (randVX == 0)
			randVX = (int) MAX_VEL;
		if (randVY == 0)
			randVY = (int) MAX_VEL;

		// Color with random rgb and a
		Color randColor = new Color((int) (Math.random() * 256), (int) (Math.random() * 256),
				(int) (Math.random() * 256), (int) (Math.random() * 256));

		// create food
		foods.add(new Food(randX, randY, randSize, randColor, randVX, randVY));
	}
}
