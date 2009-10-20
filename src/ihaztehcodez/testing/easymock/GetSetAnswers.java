package ihaztehcodez.testing.easymock;

import org.easymock.IAnswer;
import static org.easymock.EasyMock.getCurrentArguments;;

/** This provides a quick and easy method to stub getters/setters in easymock.
 * This class represents a field hidden behind a getter/setter combo. 
 * 
 * @author <a href="http://tinyurl.com/getsetanswers">Michael</a>
 *
 * @param <T>
 */
public class GetSetAnswers<T> {
	/** The current value for this field. */
	private T value;
	
	/** This should be applied to the set method. */
	
    public IAnswer<Object> set() {
    	
    	return new IAnswer<Object>() {
            @Override
            @SuppressWarnings("unchecked")
            public Object answer() throws Throwable {
                Object[] arguments = getCurrentArguments();
                
                value = (T) arguments[0];
                return null;
            }
        };
    }
   
    /** This should be applied to the get method. */
    public IAnswer<T> get() {
        return new IAnswer<T>() {
            @Override
            public T answer() throws Throwable {
                return value;
            }
        };
    }
}
