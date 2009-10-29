package ihaztehcodez.testing.io;

import static ihaztehcodez.testing.io.SimpleTestFileUtils.*;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.*;

/** Tests the SimpleTestFileUtils class. Badly.
 * 
 * @author <a href="http://tinyurl.com/junit47temp">Michael</a>
 *
 */
public class TestSimpleTestFileUtils {
	
	/** A happy path for the getFolder. */
	@Test
	public final void testHappyPath() {
		final File actual = getTestFolder();
		assertTrue("The file should exist.", actual.exists());
		final File expected = new File(System.getProperty("java.io.tmpdir"), 
				"junit/ihaztehcodez.testing.io.TestSimpleTestFileUtils/testHappyPath");
		assertEquals("The created file was not what exected", expected, actual);
	}
	
	/** Checks that if called from a none-test method the test method is returned. */
	@Test
	public final void testInMethod() {
		final File actual = otherMethod(1);
		final File expected = new File(System.getProperty("java.io.tmpdir"), 
			"junit/ihaztehcodez.testing.io.TestSimpleTestFileUtils/testInMethod");
		assertEquals("The created file was not what exected", expected, actual);
	}
	
	/** Checks that if called from different class, 
	 * the correct test method is returned. 
	 */
	@Test
	public final void testInClass() {
		final OtherClass oc = new OtherClass();
		File actual = oc.otherMethod();
		File expected = new File(System.getProperty("java.io.tmpdir"), 
			"junit/ihaztehcodez.testing.io.TestSimpleTestFileUtils/testInClass");
		assertEquals("The created file was not what exected", expected, actual);
	}
	
	/** Method used to check that the correct testInMethod 
	 * folder is returned when using getFolder().
	 * @param ignoredValue ignored
	 * @return SimpleTestFileUtils.getFolder()
	 */
	private File otherMethod(final int ignoredValue) {
		
		return getTestFolder();
	}
	
	/** used to check that the correct testInClass
	 * folder is returned when using getFolder().
	 */
	private static class OtherClass {
		
		/** used to check that the correct testInClass
		 * folder is returned when using getFolder().
		 * @return SimpleTestFileUtils.getFolder()
		 */
		File otherMethod() {
			return getTestFolder();
		}
	}
}
