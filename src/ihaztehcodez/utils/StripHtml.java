package ihaztehcodez.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/** Strips HTML from text. 
 * 
 * @author mlk
 *
 */
public final class StripHtml {
	
	/** Utility class. */
	private StripHtml() { }
	
	/** Strips HTML from the content given.
	 * 
	 * @param value The string to strip.
	 * @return A textual representation.
	 */
	public static String stripped(final String value) {
		try {
			return stripped(new StringReader(value));
		} catch(final IOException ioe) {
			// This should never happen as the StringReader can not throw an IOE.
			throw new RuntimeException(ioe);
		}
			
	}
	
	/** Strips HTML from the content given.
	 * 
	 * @param value The content to strip.
	 * @return A textual representation.
	 */
	public static String stripped(final Reader value) throws IOException {
		ParserDelegator delegator = new ParserDelegator();
		StripOMatic matic = new StripOMatic();
		delegator.parse(value, matic, true);
		
		return matic.toString();
	}
	
	/** Read in the text file and adds a new line when a breaking tag is encountered.
	 * 
	 * @author Michael Lee
	 *
	 */
	private static class StripOMatic extends HTMLEditorKit.ParserCallback {
		/** The string value. */
		private final StringBuilder value = new StringBuilder();
		/** Some tags should be ignored. */
		private static Collection<HTML.Tag> ignore = Arrays.asList(HTML.Tag.HTML, 
				HTML.Tag.BODY, HTML.Tag.HEAD);
		
		/** If the tag breaks the flow then add a new line, otherwise ignore.
		 * {@inheritDoc} 
		 */
		public void handleStartTag(final HTML.Tag t, final MutableAttributeSet a, final int pos) {
			if (ignore.contains(t)) {
				return;
			}
			if (t.breaksFlow()) {
				value.append("\n");
			}
		}

		/** Delegates to handleStartTag.
		 * {@inheritDoc} 
		 */
		public void handleSimpleTag(final HTML.Tag t, final MutableAttributeSet a, final int pos) {
			handleStartTag(t, a, pos);
		}
		
		/** Adds the following chunk of text.
		 * 
		 * {@inheritDoc}
		 */
		public void handleText(final char[] text, final int pos) {
			value.append(text);
		}
		
		/** @return The text version of the data parsed so far. */
		public String toString() {
			return value.toString();
		}
	}
}
