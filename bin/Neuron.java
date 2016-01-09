import java.util.Vector;

/**
 * The neuron is the basic unit of the network. It has an output, and then is able 
 * to apply the combination function as well as the transfer function of the incoming values
 * and weights. 
 * @author Alex Rauch
 *
 */
public class Neuron 
{
	
	//The number of neurons that has been created.
	private int numNeurons;
	
	//The other neurons that this neuron is connected to
	private Vector<Neuron> connected;
	
	//Stores the connection weights of the neuron depending on if it is a neuron
	//in the output or hidden layer
	public double[] connWeights;
	
	//Used to specify where in connWeights the weight will be added
	int index = 0;
	
	//The output of the neuron (input layer output is the normalized data value)
	private double output;
	

	/**
	 * The constructor that instantiates all of the variables
	 */
	public Neuron(Runner s)
	{
		this.numNeurons++;
		if(numNeurons > (s.getNumHidden() + s.getNumInputs()))
		{
			connWeights = new double[s.getNumInputs()];
		}
		else
		{
			connWeights = new double[(s.getNumInputs())];
		}
		connected = new Vector<Neuron>();
	}
	
	/**
	 * Getter for the number of neurons created
	 * @return number of neurons
	 */
	public int getNumNeurons()
	{
		return numNeurons;
	}
	
	/**
	 * Adds a connection between two neurons in neighboring layers. The weight is the
	 * corresponding weight associated between the neurons.
	 * @param n neuron to the left in the connection
	 * @param weight of the connection
	 */
	public void addConnection(Neuron n, double weight)
	{
		connected.add(n);
		connWeights[index] = weight;
		index++;
	}	
	
	/**
	 * Combination funtion that is the sum of products of the inputs and the corresponding weights
	 */
	public void combine()
	{
		double sum = 0;
		for(int i=0; i < connected.size(); i++)
		{
			double input = connected.get(i).getOutput();
			double weight = connWeights[i];
			sum = sum + (input*weight);
		}
		output = 1.0/(1.0+(Math.pow(Math.E, -(sum))));
	}
	
	/**
	 * Getter for the list of connected neurons to the current neuron
	 * @return connected
	 */
	public Vector<Neuron> getConnected()
	{
		return connected;
	}
	
	/**
	 * Getter for the connection weights of this neuron
	 * @return connWeights
	 */
	public double[] getConnWeights()
	{
		return connWeights;
	}

	/**
	 * Setter for the output of this neuron
	 * @param out
	 */
	public void setOutput(double out) 
	{
		output = out;
	}
	
	/**
	 * Getter for the output of this neuron
	 * @return output
	 */
	public double getOutput()
	{
		return output;
	}

	public void setConnectionWeight(int index, double newConnectionWeight)
	{
		connWeights[index] = newConnectionWeight;
	}
}
