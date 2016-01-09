import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This class tests the functionality of the network class.
 * Tests that I can populate the network with the correct number of neurons in each layer, 
 * propagate the inputs through the network, and update the weights of the connections.
 * @author Alex Rauch
 *
 */
public class TestNetwork
{

	/**
	 * First test the creation of the network, and that the simulator is 
	 * giving the correct values to the network.
	 */
	@Test
	public void testInstantiateNetwork() 
	{
		Runner s = new Runner();
		Network n = new Network(s);
		assertEquals(-0.5, n.getVariables().getMinWeight(), 0.001);
		assertEquals(0.5, n.getVariables().getMaxWeight(), 0.001);
		assertEquals(120, n.getVariables().getNumInputs());
		assertEquals(30, n.getVariables().getNumOutputs());
		assertEquals(50, n.getVariables().getNumHidden());
	}

	/**
	 * Tests that the network can hold values in each of the layers.
	 */
	@Test
	public void testPopulateWithNeurons()
	{
		Runner s = new Runner();
		Network n = new Network(s);
		assertEquals(120, n.inputNeurons.size());
		assertEquals(50, n.hiddenNeurons.size());
		assertEquals(30, n.outputNeurons.size());
	}

	/**
	 * Tests that the weights among the connections are random
	 */
	@Test
	public void testIntializeRandomWeights()
	{
		Runner s = new Runner();
		Network n = new Network(s);
		assertEquals(120, n.inputNeurons.size());
		assertEquals(50, n.hiddenNeurons.size());
		assertEquals(30, n.outputNeurons.size());
		assertEquals(6000, n.hiConnectionWeights.length);
		assertEquals(1500, n.ohConnectionWeights.length);
		for(int i=0; i<n.hiConnectionWeights.length; i++)
		{
			assertTrue(n.hiConnectionWeights[i] <= 0.5 && n.hiConnectionWeights[i] >= -0.5);
		}
		for(int j=0; j<n.ohConnectionWeights.length; j++)
		{
			assertTrue(n.ohConnectionWeights[j] <= 0.5 && n.ohConnectionWeights[j] >= -0.5);
		}
	}

	/**
	 * Tests that connections between layers are established correctly and randomly and different between
	 * each neuron
	 */
	@Test
	public void testConnectLayers()
	{
		Runner s = new Runner();
		Network n = new Network(s);
		for(int i=0; i<s.getNumHidden(); i++)
		{
			//Used so to show that the connections among the hidden and input layer neurons
			//are randomized
			assertEquals(120, n.hiddenNeurons.get(i).getConnected().size());
		}
		for(int j=0; j<n.outputNeurons.size(); j++)
		{
			//Used so to show that the connections among the hidden and input layer neurons
			//are randomized
			assertEquals(50, n.outputNeurons.get(j).getConnected().size());
		}
	}

	/**
	 * Tests that the set inputs method works properly
	 */
	@Test
	public void testSetInputsAndComboFunction()
	{
		SingletonRandom rand = SingletonRandom.getInstance();
		Runner s = new Runner();
		Network n = new Network(s);
		double[] in = new double[s.getNumInputs()];
		for(int i=0; i<in.length; i++)
		{
			in[i] = rand.nextDouble(1, -1);
			assertTrue((in[i]<=1) && (in[i]>=-1));
		}
		n.setInputs(in);
		for(int i=0; i<n.inputNeurons.size(); i++)
		{
			assertTrue(in[i] == n.inputNeurons.get(i).getOutput());
		}
		
		//Used the system.out.printlns to verify that feed forward algorithm was working properly.
		for(int i=0; i< n.inputNeurons.size(); i++)
		{
//			System.out.println(n.inputNeurons.get(i).getOutput());
		}
		n.feedForward();
		for(int i=0; i<n.hiddenNeurons.size(); i++)
		{
//			System.out.println(n.hiddenNeurons.get(i).getOutput());
		}
		for(int j=0; j<n.outputNeurons.size(); j++)
		{
//			System.out.println(n.outputNeurons.get(j).getOutput());
		}
	}
	
	@Test
	public void testCalcOutputError()
	{
		SingletonRandom rand = SingletonRandom.getInstance();
		Runner s = new Runner();
		Network n = new Network(s);
		double[] in = new double[s.getNumInputs()];
		for(int i=0; i<in.length; i++)
		{
			in[i] = rand.nextDouble(1, -1);
			assertTrue((in[i]<=1) && (in[i]>=-1));
		}
		n.setInputs(in);
		for(int i=0; i<s.getNumInputs(); i++)
		{
			assertTrue(in[i] == n.inputNeurons.get(i).getOutput());
		}
		n.feedForward();
		double[] actuals = new double[s.getNumOutputs()];
		for(int i=0; i<s.getNumOutputs(); i++)
		{
			actuals[i] = rand.nextDouble(1, -1);
		}
		n.setActuals(actuals);
		n.calculateError();
//		System.out.println(n.outputNeurons.get(0).getOutput());
//		System.out.println(actuals[0]);
		assertEquals(-0.1653070829625309, n.outputErrors[0], 0.000001);
		
//		System.out.println(n.outputNeurons.get(1).getOutput());
//		System.out.println(actuals[1]);
		assertEquals(-.2798379904412391, n.outputErrors[1], 0.0001);
//		for(int i=0; i<n.outputErrors.length; i++)
//		{
//			System.out.println(n.outputErrors[i]);
//		}
//		System.out.println(n.outputNeurons.get(0).getOutput());
	}
	
	@Test
	public void testCalcHiddenError()
	{
		SingletonRandom rand = SingletonRandom.getInstance();
		Runner s = new Runner();
		Network n = new Network(s);
		double[] in = new double[s.getNumInputs()];
		for(int i=0; i<in.length; i++)
		{
			in[i] = rand.nextDouble(1, -1);
			assertTrue((in[i]<=1) && (in[i]>=-1));
		}
		n.setInputs(in);
		for(int i=0; i<s.getNumInputs(); i++)
		{
			assertTrue(in[i] == n.inputNeurons.get(i).getOutput());
		}
		n.feedForward();
		double[] actuals = new double[s.getNumOutputs()];
		for(int i=0; i<s.getNumOutputs(); i++)
		{
			actuals[i] = rand.nextDouble(1, -1);
		}
		n.setActuals(actuals);
		n.calculateError();
		double sum=0;
		for(int i=0; i<s.getNumOutputs(); i++)
		{
			double tempError = n.outputErrors[i];
			double connWeights = n.outputNeurons.get(i).connWeights[0];
			sum = sum + tempError*connWeights;
		}
		assertEquals(-.0026546857434935614, n.hiddenErrors[0], 0.001);
	}
	
	@Test
	public void testUpdateWeights()
	{
		SingletonRandom rand = SingletonRandom.getInstance();
		Runner s = new Runner();
		Network n = new Network(s);
		double[] in = new double[s.getNumInputs()];
		for(int i=0; i<in.length; i++)
		{
			in[i] = rand.nextDouble(1, 1);
			assertTrue((in[i]<=1) && (in[i]>=1));
		}
		n.setInputs(in);
		for(int i=0; i<s.getNumInputs(); i++)
		{
			assertTrue(in[i] == n.inputNeurons.get(i).getOutput());
		}
		n.feedForward();
		double[] actuals = new double[s.getNumOutputs()];
		for(int i=0; i<s.getNumOutputs(); i++)
		{
			actuals[i] = rand.nextDouble(1, 1);
		}
		n.setActuals(actuals);
		n.calculateError();
		
		@SuppressWarnings("unused")
		double sum=0;
		for(int i=0; i<s.getNumOutputs(); i++)
		{
			double tempError = n.outputErrors[i];
			double connWeights = n.outputNeurons.get(i).connWeights[0];
			sum += tempError*connWeights;
		}
		n.updateWeights();	
//		System.out.println(n.hiddenNeurons.get(0).connWeights[0]);
//		System.out.println(n.hiddenNeurons.get(0).connWeights[1]);
//		System.out.println(n.hiddenNeurons.get(0).connWeights[2]);
//		System.out.println(n.hiddenNeurons.get(0).connWeights[3]);
//		System.out.println(n.hiddenNeurons.get(0).connWeights[4]);
//		System.out.println(n.hiddenNeurons.get(0).connWeights[5]);
//		System.out.println(n.hiddenNeurons.get(0).connWeights[6]);
		
	}
	
	@Test
	public void testNormalize()
	{
		Runner s = new Runner();
		Network n = new Network(s);
		double[] unNormValues = new double[3];
		unNormValues[0] = 5.22;
		unNormValues[1] = 3.43;
		unNormValues[2] = 7.81;
		double[] normValues = n.normalizeValues(unNormValues, unNormValues[2], unNormValues[1]);
		assertEquals(1.0, normValues[2], 0.01);
		assertEquals(0.0, normValues[1], 0.01);
		assertEquals(0.4086757991, normValues[0], 0.01);
	}
	
	@Test
	public void testDenormalize()
	{
		Runner s = new Runner();
		Network n = new Network(s);
		double[] unNormValues = new double[3];
		unNormValues[0] = 5.22;
		unNormValues[1] = 3.43;
		unNormValues[2] = 7.81;
		double[] normValues = n.normalizeValues(unNormValues, unNormValues[2], unNormValues[1]);
		assertEquals(1.0, normValues[2], 0.01);
		assertEquals(0.0, normValues[1], 0.01);
		assertEquals(0.4086757991, normValues[0], 0.01);
		double deNormValues[] = n.denormalizeValues(normValues, unNormValues[2], unNormValues[1]);
		assertEquals(5.22, deNormValues[0], 0.001);
		assertEquals(3.43, deNormValues[1], 0.001);
		assertEquals(7.81, deNormValues[2], 0.001);
		
	}
}

