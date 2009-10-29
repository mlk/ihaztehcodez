package ihaztehcodez.testing.io;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/** A selection of utility methods for creating files.
 * 
 * @author <a href="http://tinyurl.com/junit47temp">Michael</a>
 *
 */
public final class SimpleTestFileUtils {
	/** Our logger. */
	private static final Logger LOG = Logger.getLogger(
			SimpleTestFileUtils.class.getName());
	
	/** The root folder for all test directories.*/
	private static final File ROOT;
	static {
		if (System.getProperty("unit.root") == null) {
			ROOT = new File(new File(System.getProperty("java.io.tmpdir")), "junit");
		} else {
			ROOT = new File(System.getProperty("unit.root"));
		}
		try {
			FileUtils.deleteDirectory(ROOT);
		} catch (final IOException ioe) {
			LOG.log(Level.INFO, "Failed to remove ROOT.", ioe);
		}
		if (!ROOT.mkdirs()) {
			LOG.log(Level.INFO, "Failed to create ROOT.");
		}
	}
	
	/** Util class. */
	private SimpleTestFileUtils() { }

	/** The implementation of the get*TestFolder calls.
	 * 
	 * @param strategy The strategy to use to decide if a method is a test method or not.
	 * @return A folder Test can use.
	 */
	private static File getTestFolder(final GetTestFolderStrategy strategy) {
		final Throwable stackTraceProvider = new Throwable();
		stackTraceProvider.fillInStackTrace();
		final StackTraceElement[] stack = 
			stackTraceProvider.getStackTrace();
		for (StackTraceElement element : stack) {
			try {
				final Class< ? > clazz = Class.forName(element.getClassName());
				final Method method = clazz.getDeclaredMethod(element.getMethodName());
				if (strategy.isTestMethod(method)) {
					final File clazzFile = new File(ROOT, element.getClassName());
					final File methodFile = new File(clazzFile, element.getMethodName());
					if (!methodFile.mkdirs() && !methodFile.exists()) {
						LOG.info("Failed to create folder: " + methodFile);
					}
					return methodFile;
				}
			} catch (final NoSuchMethodException nsme) {
				LOG.log(Level.FINEST, "No method found.", nsme);
			} catch (final ClassNotFoundException cnfe) {
				LOG.log(Level.FINEST, "No class found.", cnfe);
			}
		}
		final File unknown = new File(ROOT, "unknown");
		if (!unknown.mkdirs()) {
			LOG.info("Failed to create folder: " + unknown);
		}
		return unknown;
	}
	
	/** Creates a folder based on the current test. Emptied if required. 
	 * 
	 * @return The file to create.
	 */
	public static File getJUnit3TestFolder() {		
		return getTestFolder(new GetTestFolderStrategy() {

			@Override
			public boolean isTestMethod(final Method method) {
				return method.getName().startsWith("test");
			}
			
		});
	}
	
	/** Creates a folder based on the current test. Emptied if required. 
	 * 
	 * @return The file to create.
	 */
	public static File getTestFolder() {		
		return getTestFolder(new GetTestFolderStrategy() {

			@Override
			public boolean isTestMethod(final Method method) {
				return method.getAnnotation(Test.class) != null;
			}
			
		});
	}
	
	/** Turns a URL into a file in the resource/test sub folder.
	 * 
	 * Supports Crap4js unhappiness with spaces.  
	 * 
	 * @param file The file to get.
	 * @return the file for the above URL.
	 */
	public static File file(final String file) {
		try {
			URL url = SimpleTestFileUtils.class.getResource("/test/" + file);
			url = new URL(url.toString().replaceAll(" ", "%20"));
			return new File(url.toURI());
		} catch (final URISyntaxException urie) {
			throw new IllegalStateException(urie);
		} catch (final MalformedURLException mfue) {
			throw new IllegalStateException(mfue);
		}
	}
	
	/** Works out if a method is test method or not. 
	 */
	static interface GetTestFolderStrategy {
		
		/** Returns true if the method is a test method.
		 * 
		 * @param method The method to check.
		 * @return True if it is a test method.
		 */
		boolean isTestMethod(Method method);
	}
}
