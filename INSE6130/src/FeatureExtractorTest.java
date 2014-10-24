import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

public class FeatureExtractorTest {

	@Test
	public void testSum(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(5);
		list.add(7);
		
		int actualResult = FeatureExtractor.sum(list);
		int expectedResult = 1+5+7;
		
		assertEquals(actualResult , expectedResult);
	}

}
