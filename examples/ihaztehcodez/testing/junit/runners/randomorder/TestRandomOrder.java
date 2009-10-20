package ihaztehcodez.testing.junit.runners.randomorder;

import ihaztehcodez.testing.junit.runners.RandomBlockJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

// This is the important line. Running with this will mean than the tests will not
// execute in the same order as they are defined.
@RunWith(RandomBlockJUnit4ClassRunner.class)
public class TestRandomOrder {

	@Test
	public void one() {}
	@Test
	public void two() {}
	@Test
	public void three() {}
	@Test
	public void four() {}
}
