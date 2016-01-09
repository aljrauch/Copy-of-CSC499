import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The specifications of this particular neural network
 * @author Alex Rauch
 *
 */
public class Runner
{
	//Min and max weight are used to randomize initial weights of the neurons
	private static double minWeight = -0.5;
	private static double maxWeight = 0.5;

	//4 inputs represents the first 4 trading days of week
	private static int numInputs = 40;
	//1 output represents predicted 5th day closing price for that stock
	private static int numOutputs = 10;
	//Starts at 50 hidden layer neurons but can be optimized later
	private static int numHidden = 17;
	//The learning rate that is used in the formula that updates the weights 1.65
	private static double learningRate = .7;

	private static double maxNorm = 2.0;
	private static double minNorm = 1.0;

	private static double dataMax = 348.48;
	private static double dataMin = 8.92;
	


	/**
	 * Main method that puts together the neural network with the given parameters above.
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String args[]) throws FileNotFoundException
	{
		Runner r = new Runner();
		Network n = new Network(r);

		Scanner scan = new Scanner(new File("//Users/ARauch/Documents/CSC499/TechnologyIndustry.csv"));
		scan.useDelimiter(",");

		Scanner endLine = new Scanner(new File("//Users/ARauch/Documents/CSC499/TechnologyIndustry.csv"));
		endLine.useDelimiter(",");

		Scanner endFile = new Scanner(new File("//Users/ARauch/Documents/CSC499/TechnologyIndustry.csv"));

		//Input positions
		int counter = 0;
		//Actuals positions
		int position = 0;
		//Keeps track of the iterations for inputs vs. the actuals in the file
		int index = 0;

		//Inputs that are passed through the network
		double[] in = null;
		//Actuals that are used to determine the error
		double[] actuals = null;

		while(endFile.hasNextLine())
		{
			in = new double[r.getNumInputs()];
			while((index%4 != 0 || index==0) && endLine.hasNextLine())
			{
				while(endLine.hasNextDouble())
				{
					in[counter] = scan.nextDouble();
					counter++;
					endLine.nextDouble();
				}

				endLine.nextLine();
				scan.nextLine();
				index++;
			}
			double[] temp = n.normalizeValues(in, dataMax, dataMin);
			n.setInputs(temp);

			n.feedForward();
			counter = 0;

			actuals = new double[r.getNumOutputs()];
			while((index%4 == 0 && index!=0) && endLine.hasNextLine())
			{
				while(endLine.hasNextDouble())
				{
					actuals[position] = scan.nextDouble();
					position++;
					endLine.nextDouble();
				}
				endLine.nextLine();
				scan.nextLine();
				index++;
				int x = 5;
				while(x>0)
				{
					endFile.nextLine();
					x--;
				}
				double[] tempActuals = n.normalizeValues(actuals, dataMax, dataMin);
				n.setActuals(tempActuals);


				n.calculateError();
				n.updateWeights();
			}

			position = 0;
			index = 0;

		}
		scan.close();
		endLine.close();
		endFile.close();

		double[] tempPredictedOuts = new double[r.getNumOutputs()];
		for(int i=0; i<tempPredictedOuts.length; i++)
		{
			tempPredictedOuts[i] = n.outputNeurons.get(i).getOutput();
		}
		double[] predictedOuts  = n.denormalizeValues(tempPredictedOuts, dataMax, dataMin);
		double[] reals = {70.07,	21.21,	39.29,	21.08,	28.3,	348.48,	33.79,	26.57,	48.75,	35.46};
		System.out.println("DJIA Training Results:");
		for(int i=0; i<predictedOuts.length; i++)
		{
//			System.out.println(predictedOuts[i]);
			System.out.println(calculatePercError(reals[i], predictedOuts[i]));
		}
	}

	public static double calculatePercError(double actual, double predicted)
	{
		double temp = Math.abs(predicted - actual);
		temp = temp/Math.abs(actual);
		return temp *100;
	}

	/**
	 * Getter for the minimum randomized weight of input
	 * @return minWeight
	 */
	public double getMinWeight()
	{
		return minWeight;
	}

	/**
	 * Getter for the maximum randomized weight of input
	 * @return maxWeight
	 */
	public double getMaxWeight()
	{
		return maxWeight;
	}

	/**
	 * Gets the number of inputs of the network
	 * @return numInputs
	 */
	public int getNumInputs()
	{
		return numInputs;
	}

	/**
	 * Gets the number of outputs the neural network produces.
	 * @return numOutputs
	 */
	public int getNumOutputs()
	{
		return numOutputs;
	}

	/**
	 * Gets the number of neurons in hidden layer
	 * @return numHidden
	 */
	public int getNumHidden()
	{
		return numHidden;
	}

	/**
	 * Getter for the learning rate
	 * @return learning rate
	 */
	public double getLearningRate()
	{
		return learningRate;
	}

	/**
	 * Getter for the maximum normalized value
	 * @return maxNorm
	 */
	public double getMaxNorm()
	{
		return maxNorm;
	}

	/**
	 * Getter for the minimum normalized value
	 * @return minNorm
	 */
	public double getMinNorm()
	{
		return minNorm;
	}
}