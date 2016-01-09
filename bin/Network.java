import java.util.Vector;

/**
 * The network class is the structure of the model. It allows each layer to hold neurons, 
 * and pass values through them, calculate the error, and update weights for training.
 * Once training has occurred, the network will then be able to predict prices of the stock market.
 * @author Alex Rauch
 *
 */
public class Network
{
	//The current network's variables and specs (number of neurons in each layer, weights. etc.)
	private Runner variables;

	//The nerons contained in each of the layers in the network
	Vector<Neuron> inputNeurons;
	Vector<Neuron> hiddenNeurons;
	Vector<Neuron> outputNeurons;

	//hi and oh connection weights hold the randomized weights of the connections between the 
	//three layers
	public double[] hiConnectionWeights;
	public double[] ohConnectionWeights;

	//The random number generator used for the weights. 
	private SingletonRandom random = SingletonRandom.getInstance();

	//Actuals hold the expected output which is used to compute the error
	public double[] actuals;

	//The following two arrays hold the errors of the output and hidden layer neurons
	public double[] outputErrors;
	public double[] hiddenErrors;

	/**
	 * Creates a neural network with the given specs and variables
	 * @param r the specifications of the network
	 */
	public Network(Runner r)
	{
		variables = r;
		inputNeurons = new Vector<Neuron>();
		hiddenNeurons = new Vector<Neuron>();
		outputNeurons = new Vector<Neuron>();
		hiConnectionWeights = new double[(variables.getNumHidden())*(variables.getNumInputs())];
		ohConnectionWeights = new double[(variables.getNumOutputs())*(variables.getNumHidden())];
		actuals = new double[variables.getNumOutputs()];
		outputErrors = new double[variables.getNumOutputs()];
		hiddenErrors = new double[variables.getNumHidden()];
		populateNetwork();
		randomizeWeights();
		connectLayers();
	}

	/**
	 * Adds neurons and connections to each of the layers of the network
	 */
	private void populateNetwork() 
	{
		//Input Layer neurons
		for(int i=0; i<variables.getNumInputs(); i++)
		{
			Neuron n = new Neuron(variables);
			inputNeurons.add(n);
		}
		//Hidden Layer neurons
		for(int j=0; j<variables.getNumHidden(); j++)
		{
			Neuron n = new Neuron(variables);
			hiddenNeurons.add(n);
		}
		//Output Layer neurons
		for(int k=0; k<variables.getNumOutputs(); k++)
		{
			Neuron n = new Neuron(variables);
			outputNeurons.add(n);
		}
	}

	/**
	 * Randomizes the weights of the connections between the layers by using the 
	 * singleton random class generated
	 */
	public void randomizeWeights()
	{
		for(int i=0; i<hiConnectionWeights.length; i++)
		{
			hiConnectionWeights[i] = random.nextDouble(variables.getMaxWeight(), variables.getMinWeight());
			//			hiConnectionWeights[i] = 0.01;
		}
		for(int j=0; j<ohConnectionWeights.length; j++)
		{
			ohConnectionWeights[j] = random.nextDouble(variables.getMaxWeight(), variables.getMinWeight());
			//			ohConnectionWeights[j] = 0.01;
		}
	}

	/**
	 * This takes the output and hidden layers and adds connections between the layer of neurons to the
	 * right so that the connection weights are randomized and the inputs have a path to the output
	 */
	public void connectLayers()
	{
		int hiconnectCounter = 0;
		for(int i=0; i<variables.getNumHidden(); i++)
		{
			for(int j=0; j<variables.getNumInputs(); j++)
			{
				hiddenNeurons.get(i).addConnection(inputNeurons.get(j), hiConnectionWeights[hiconnectCounter]);
				hiconnectCounter++;
			}
		}
		int ohconnectCounter=0;
		for(int i=0; i<variables.getNumOutputs(); i++)
		{
			for(int j=0; j<variables.getNumHidden(); j++)
			{
				outputNeurons.get(i).addConnection(hiddenNeurons.get(j), ohConnectionWeights[ohconnectCounter]);
				ohconnectCounter++;
			}
		}
	}

	/**
	 * Sets the inputs of the normalized values of the training data
	 * @param in
	 */
	public void setInputs(double[] in)
	{
		for(int i=0; i<variables.getNumInputs(); i++)
		{
			inputNeurons.get(i).setOutput(in[i]);
		}
	}

	/**
	 * Passes the inputs through the network, and thus producing an output for each output neuron
	 */
	public void feedForward()
	{		
		//Pass the hidden neurons through the network
		for(int i=0; i<variables.getNumHidden(); i++)
		{
			hiddenNeurons.get(i).combine();
		}
		//Pass the output neurons through the network
		for(int j=0; j<variables.getNumOutputs(); j++)
		{
			outputNeurons.get(j).combine();
		}
	}

	/**
	 * First calculates the error among the output neurons, then calculates the
	 * error among the hidden neurons
	 */
	public void calculateError()
	{
		//Calculate output errors
		for(int i=0; i<variables.getNumOutputs(); i++)
		{
			double tempOut = outputNeurons.get(i).getOutput();
			outputErrors[i] = (tempOut)*(1-tempOut)*(actuals[i]-tempOut);
		}

		//Calculate hidden neuron errors
		for(int i=0; i < variables.getNumHidden(); i++)
		{
			double hiddenOut = hiddenNeurons.get(i).getOutput();
			double sum = 0;
			for(int j=0; j < variables.getNumOutputs(); j++)
			{
				double outputError = outputErrors[j];
				double connWeight = outputNeurons.get(j).getConnected().get(i).connWeights[j];
				sum = sum + (outputError*connWeight);
			}
			hiddenErrors[i] = hiddenOut*(1-hiddenOut)*sum;
		}
	}

	/**
	 * First the delta weight of each connection will be calculated using the error of the 
	 * hidden and output neurons. Then once that is complete, the weights of the connections 
	 * are updated by adding delta weight to the connection.
	 */
	public void updateWeights()
	{	
		double learningRate = variables.getLearningRate();
		//Updates the weights between output and hidden layers
		for(int i=0; i<variables.getNumOutputs(); i++)
		{
			double outError = outputErrors[i];
			for(int j=0; j < outputNeurons.get(i).getConnected().size(); j++)
			{
				double hidOut = outputNeurons.get(i).getConnected().get(j).getOutput();
				double connWeight = outputNeurons.get(i).connWeights[j];
				double newWeight = connWeight + (learningRate * (outError) * hidOut);
				outputNeurons.get(i).setConnectionWeight(j, newWeight);
			}
		}

		//Updates the weights between input and hidden layers
		for(int i=0; i< variables.getNumHidden(); i++)
		{
			double hidError = hiddenErrors[i];
			for(int j=0; j < hiddenNeurons.get(i).getConnected().size(); j++)
			{
				double inOut = hiddenNeurons.get(i).getConnected().get(j).getOutput();
				double connWeight = hiddenNeurons.get(i).connWeights[j];
				double newWeight = connWeight + (learningRate * (hidError) * inOut);
				hiddenNeurons.get(i).setConnectionWeight(j, newWeight);
			}
		}
	}

	/**
	 * Sets the actual values for computing the error among the output layer neurons
	 * @param x
	 */
	public void setActuals(double[] x)
	{
		actuals = x;
	}

	/**
	 * Getter for the variables of the network
	 * @return variables
	 */
	public Runner getVariables()
	{
		return variables;
	}

	/**
	 * Normalizes the inputs of the network by using the max and min of the data.
	 * the values will then range from 0-1
	 * @param in
	 * @param max
	 * @param min
	 * @return normValues
	 */
	public double[] normalizeValues(double[] in, double max, double min)
	{
		double[] normValues = new double[in.length];
		for(int i=0; i<in.length; i++)
		{
			double temp = in[i];
			normValues[i] = (temp-(min))/((max) - (min));
		}
		return normValues;
	}

	/**
	 * Once the network is trained, we can denormalize the outputs for each output
	 * so we can interpret the predictions.
	 * @param ins
	 * @return deNormValues
	 */
	public double[] denormalizeValues(double[] outs, double max, double min)
	{
		double[] deNormValues = new double[outs.length];
		for(int i=0; i<outs.length; i++)
		{
			double temp = outs[i];
			deNormValues[i] = temp*(max-min)+min;
		}
		return deNormValues;
	}

}
