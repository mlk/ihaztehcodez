package ihaztehcodez.testing.easymock.getsetanswers;

import junit.framework.Assert;
import ihaztehcodez.testing.easymock.GetSetAnswers;
import static org.easymock.EasyMock.*;
import org.junit.Test;

public class TestGetSetAnswers {
    @Test
    public void testSetGet() {
        GetSetAnswers<Integer> value = new GetSetAnswers<Integer>();
       
        GetSetId id = createMock(GetSetId.class);
        
        id.setID(anyInt());
        expectLastCall().andAnswer(value.set());
        
        expect(id.getID()).andAnswer(value.get());

        replay(id);
        
        int generatedValue = (int) (Math.random() * 100.0);
        
        id.setID(generatedValue);
        Assert.assertEquals(generatedValue, id.getID());
        
        verify(id);
    }
}
