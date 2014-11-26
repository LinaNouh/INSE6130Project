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

}
