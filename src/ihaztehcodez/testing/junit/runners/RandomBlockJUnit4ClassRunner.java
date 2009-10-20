package ihaztehcodez.testing.junit.runners;

import java.util.Collections;
import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/** Runs JUnit tests in a random order.
*
* @author <a href="http://tinyurl.com/randomjunit">mlk</a>
*
*/
public class RandomBlockJUnit4ClassRunner extends BlockJUnit4ClassRunner {

	public RandomBlockJUnit4ClassRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	/** This randomises the order of the tests and then returns them.
	 * {@inheritDoc} */
	@SuppressWarnings("unchecked")
	protected List computeTestMethods() {
		List methods = super.computeTestMethods();
		Collections.shuffle(methods);
		return methods;
	}
}
