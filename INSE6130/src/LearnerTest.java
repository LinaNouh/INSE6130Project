import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class LearnerTest {

	@Test
	public void testArrayContains1(){
		int[] array = new int[3];
		
		array[0] = 1;
		array[1] = 2;
		array[2] = 3;
		
		boolean actual = Learner.arrayContains(2, array);
		boolean expected = true; 
		
		assertEquals(expected , actual);
	}
	
	@Test
	public void testArrayContains2(){
		int[] array = new int[3];
		
		array[0] = 1;
		array[1] = 2;
		array[2] = 3;
		
		boolean actual = Learner.arrayContains(5, array);
		boolean expected = false; 
		
		assertEquals(expected , actual);
	}
	
	@Test
	public void testArrayContains3(){
		int[] array = null;
		
		boolean actual = Learner.arrayContains(5, array);
		boolean expected = false; 
		
		assertEquals(expected , actual);
	}
	
	@Test
	public void testArrayMax(){
		float[] array = new float[3];
		
		array[0] = (float) 0.5;
		array[1] = (float) 0.6;
		array[2] = (float) 0.8;
		
		String actual = ""+Learner.arrayMax(array);
		String expected = ""+(float) 0.8; 
		
		assertEquals(expected , actual);
	}
	
	@Test
	public void testArrayMinIndex1(){
		float[] array = new float[3];
		
		array[0] = (float) 0.4;
		array[1] = (float) 0.6;
		array[2] = (float) 0.8;
		
		int actual = Learner.arrayMinIndex(array,0,array.length);
		int expected = 0; 
		
		assertEquals(expected , actual);
	}
	
	@Test
	public void testArrayMinIndex2(){
		float[] array = new float[4];
		
		array[0] = (float) 0.4;
		array[1] = (float) 0.2;
		array[2] = (float) 0.8;
		array[3] = (float) 0.1;
		
		int actual = Learner.arrayMinIndex(array,0,2);
		int expected = 1; 
		
		assertEquals(expected , actual);
	}
	
	@Test
	public void testArrayMinIndex3(){
		float[] array = null;
		
		int actual = Learner.arrayMinIndex(array,0,1);
		int expected = 0; 
		
		assertEquals(expected , actual);
	}
	 
	@Test
	public void testComputeDistance(){
		float[] features1 = new float[3];
		float[] features2 = new float[3];
		
		features1[0] = (float) 0.5;
		features1[1] = (float) 0.9;
		features1[2] = (float) 0.4;
		
		features2[0] = (float) 0.8;
		features2[1] = (float) 0.6;
		features2[2] = (float) 0.7;
		
		float[] weights = new float[3];
		weights = Learner.initializeWeights(3);
		
		String actual = ""+(double)Learner.computeDistance(features1, features2, weights);
		String expected = ""+(double)((0.8-0.5)*weights[0]+(0.9-0.6)*weights[1]+(0.7-0.4)*weights[2]); 
		
		assertEquals(expected , actual);
	}
	
}
