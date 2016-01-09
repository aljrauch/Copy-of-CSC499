import static org.junit.Assert.*;

import org.junit.Test;


public class TestSimulator 
{

	/**
	 * Just tests that the variables are correct and storing the correct values
	 */
	@Test
	public void testInitialization()
	{
		Runner s = new Runner();
		assertEquals(-0.5, s.getMinWeight(), 0.001);
		assertEquals(0.5, s.getMaxWeight(), 0.001);
		assertEquals(120, s.getNumInputs());
		assertEquals(30, s.getNumOutputs());
		assertEquals(50, s.getNumHidden());
		assertEquals(0.5, s.getLearningRate(), 0.001);


	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testCalcPercError()
	{
		Runner s = new Runner();
		assertEquals(-0.5, s.getMinWeight(), 0.001);
		assertEquals(0.5, s.getMaxWeight(), 0.001);
		assertEquals(120, s.getNumInputs());
		assertEquals(30, s.getNumOutputs());
		assertEquals(50, s.getNumHidden());
		assertEquals(0.5, s.getLearningRate(), 0.001);
		
		assertEquals(0,  s.calculatePercError(.5, .5), 0.0001);
		
	}
}
