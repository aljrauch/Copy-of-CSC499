import java.util.Random;

/**
 * Singleton Pattern that is used to test the functionality of random numbers and also apply random
 * weights of the connections between neurons.
 * @author Alex Rauch
 *
 */
public class SingletonRandom 
{
	//Plants the seed so that the random values are consistent and the same every time
	//Allows for easier and more accurate testing.
	private static SingletonRandom instance = new SingletonRandom(123456789);
	
	private Random generator;
	
	/**
	 * Private constructor of this class
	 */
	private SingletonRandom(long seed)
	{
		generator = new Random(seed);
	}
	
	/**
	 * Establishes the max and generates a random number from the return statement
	 * @param max
	 * @return generator.nextInt(max)
	 */
	public double nextDouble(double max, double min)
	{
		double randomValue = (min) + (max - min) *generator.nextDouble();
		return randomValue;
	}

	/**
	 * Main point of retrieval of the random instance that has been created.
	 * @return instance
	 */
	public static SingletonRandom getInstance()
	{
		return instance;
	}
	
}
