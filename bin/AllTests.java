import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({TestNeuron.class, TestNetwork.class, TestSimulator.class})
public final class AllTests{
	
}
