import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the functionality of the neuron.
 * Checks for things such as the number of neurons created, the combination function
 * and transfer function used.
 */
public class TestNeuron 
{

	/**
	 * Tests that a neuron can be created.
	 */
	@Test
	public void testInitialization() 
	{
		Runner s = new Runner();
		Neuron n = new Neuron(s);
		assertEquals(1, n.getNumNeurons());
	}
	
	/**
	 * Tests the connection between two neurons and the initialization of the random weights
	 * associated with the connection
	 */
	@Test
	public void testAddConnections()
	{
		Runner s = new Runner();
		Neuron n1 = new Neuron(s);
		Neuron n2 = new Neuron(s);
		double weight1 = 0.75;
		n2.addConnection(n1, weight1);
		assertEquals(1, n2.getConnected().size());
		Neuron n3 = new Neuron(s);
		double weight2 = 0.99;
		n2.addConnection(n3,weight2);
		assertEquals(2, n2.getConnected().size());
		assertEquals(0.75, n2.connWeights[0], 0.001);
		assertEquals(0.99, n2.connWeights[1], 0.001);
	}
	
	/**
	 * Tests the combine fucntion. Sets the output of the various input neurons
	 * and then sums up the products of the inputs and the weights for each incoming input and
	 * connection
	 */
	@Test
	public void testComboFunction()
	{
		Runner s = new Runner();
		Neuron n1 = new Neuron(s);
		Neuron n2 = new Neuron(s);
		Neuron n3 = new Neuron(s);
		n1.setOutput(0.45);
		n2.setOutput(0.55);
		n3.setOutput(0.65);
		double weight1 = 0.75;
		double weight2 = 0.25;
		n3.addConnection(n1, weight1);
		n3.addConnection(n2, weight2);
		assertEquals(2, n3.getConnected().size());
		n3.combine();
		assertEquals(0.45, n1.getOutput(),0.001);
		assertEquals(0.55, n2.getOutput(),0.001);
		assertEquals(0.616566504213193, n3.getOutput(),0.001);
		assertEquals(0.75, n3.connWeights[0], 0.001);
		assertEquals(0.25, n3.connWeights[1], 0.001);
	}
	
	/**
	 * Applies the transfer function to a smaller set of neurons to verify its accuracy.
	 * The transfer function occurs after the combine function has been applied.
	 */
	@Test
	public void testTransferFunction()
	{
		Runner s = new Runner();
		Neuron n1 = new Neuron(s);
		Neuron n2 = new Neuron(s);
		Neuron n3 = new Neuron(s);
		Neuron n4 = new Neuron(s);
		n1.setOutput(0.45);
		n2.setOutput(0.55);
		n3.setOutput(0.65);
		n4.setOutput(0.75);
		double weight1 = 0.75;
		double weight2 = 0.25;
		double weight3 = 0.55;
		n3.addConnection(n1, weight1);
		n3.addConnection(n2, weight2);
		n3.addConnection(n4, weight3);
		assertEquals(3, n3.getConnected().size());
		n3.combine();
		assertEquals(0.45, n1.getOutput(),0.001);
		assertEquals(0.55, n2.getOutput(),0.001);
		assertEquals(0.708373991032327, n3.getOutput(),0.001);
		assertEquals(0.75, n4.getOutput(), 0.001);
		assertEquals(0.75, n3.connWeights[0], 0.001);
		assertEquals(0.25, n3.connWeights[1], 0.001);
		assertEquals(0.55, n3.connWeights[2], 0.001);
	}
}
