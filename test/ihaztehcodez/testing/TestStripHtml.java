package ihaztehcodez.testing;

import ihaztehcodez.utils.StripHtml;

import java.util.Arrays;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Validates that HTML is striped from text files.
 * 
 * @author Michael Lee
 * 
 */
public class TestStripHtml {

	/**
	 * Plain text is passed through unchanged.
	 */
	@Test
	public void plainTextUnchanged() {
		String expected = "one two three";

		String actual = StripHtml.stripped(expected);

		assertEquals(expected, actual);
	}

	/**
	 * BRs should be converted into new lines.
	 */
	@Test
	public void brConvertedIntoNewlines() {
		String expected = "one\ntwo\nthree";

		String actual = StripHtml.stripped("one<br>two<br>three");

		assertEquals(expected, actual);
	}

	/**
	 * Ps are converted into new lines.
	 */
	@Test
	public void paraConvertedIntoNewlines() {
		String expected = "\none\ntwo\nthree";

		String actual = StripHtml.stripped("<p>one</p><p>two</p><p>three</p>");

		assertEquals(expected, actual);
	}

	/**
	 * Escape code are handled correctly.
	 */
	@Test
	public void escapeCodeHandled() {
		String expected = "&";

		String actual = StripHtml.stripped("&amp;");

		assertEquals(expected, actual);
	}

	/**
	 * Bold tags are stripped.
	 * 
	 * @throws Exception Environment
	 *             
	 */
	@Test
	public void bold() throws Exception {
		String expected = "hello";

		String actual = StripHtml.stripped("<b>hello</b>");

		assertEquals(expected, actual);
	}

	/**
	 * Links are stripped.
	 * 
	 * @throws Exception Environment
	 *             
	 */
	@Test
	public void links() throws Exception {
		String expected = "hello";

		String actual = StripHtml.stripped("<a href='Hi'>hello</b>");

		assertEquals(expected, actual);
	}
}
