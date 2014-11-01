import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

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
	
	@Test
	public void testExtract(){
		ArrayList<Float> times = new ArrayList<Float>(); 
		ArrayList<Integer> directions = new ArrayList<Integer>();
		ArrayList<String> features = new ArrayList<String>(); 
		
		FeatureExtractor.extract(times, directions, features);
		ArrayList<String> expectedResult = new ArrayList<String>(); 
		
		assertEquals(features , expectedResult);
	}

}
