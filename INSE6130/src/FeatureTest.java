import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class FeatureTest {

	@Test
	public void testSetAndGet(){
		Feature f = new Feature();
		
		String featureType = "direction";
		String featureValue = ""+ -1;
		
		f.setType(featureType);
		f.setValue(featureValue);
		
		assertEquals(featureType, f.getType());
		assertEquals(featureValue, f.getValue());
	}
	
	@Test
	public void testEquals1(){
		Feature f1 = new Feature("NumPackets", "5");
		Feature f2 = new Feature("NumPackets", "5");
		
		assertEquals(true, f1.equals(f2));
	}
	
	@Test
	public void testEquals2(){
		Feature f1 = new Feature("NumPackets", "7");
		Feature f2 = new Feature("NumPackets", "5");
		
		assertEquals(false, f1.equals(f2));
	}
	
	@Test
	public void testEquals3(){
		Feature f1 = new Feature("NumPackets", "5");
		Feature f2 = new Feature("NumInPackets", "5");
		
		assertEquals(false, f1.equals(f2));
	}
	
	@Test
	public void testEquals4(){
		Feature f1 = new Feature("NumPackets", "5");
		Feature f2 = new Feature("NumInPackets", "6");
		
		assertEquals(false, f1.equals(f2));
	}

}
