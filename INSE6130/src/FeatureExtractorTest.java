import static org.junit.Assert.*;
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
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testExtract(){
		ArrayList<Float> times = new ArrayList<Float>(); 
		ArrayList<Integer> directions = new ArrayList<Integer>();
		ArrayList<Feature> features = new ArrayList<Feature>(); 
		
		times.add((float) 0.0);
		times.add((float) 0.116133928299);
		times.add((float) 0.499715805054);
		times.add((float) 0.782404899597);
		times.add((float) 0.969846963882);
		times.add((float) 1.0993039608);
		
		directions.add(1);
		directions.add(1);
		directions.add(1);
		directions.add(-1);
		directions.add(-1);
		directions.add(1);
		
		ArrayList<Feature> expectedResult = new ArrayList<Feature>();
		
		expectedResult.add(new Feature("NumPackets",""+directions.size()));
		expectedResult.add(new Feature("NumInPackets",""+4));
		expectedResult.add(new Feature("NumOutPackets",""+2));
		expectedResult.add(new Feature("TotalTime",""+((float) 1.0993039608 - (float) 0.0)));
		expectedResult.add(new Feature("containLength"+(-2),""+0));
		expectedResult.add(new Feature("containLength"+(-1),""+1));
		expectedResult.add(new Feature("containLength"+(0),""+0));
		expectedResult.add(new Feature("containLength"+(1),""+1));
		expectedResult.add(new Feature("containLength"+(2),""+0));
		expectedResult.add(new Feature("InPacket",""+0));
		expectedResult.add(new Feature("InPacket",""+1));
		expectedResult.add(new Feature("InPacket",""+2));
		expectedResult.add(new Feature("InPacket",""+5));
		for(int i=4; i<10;i++){
			expectedResult.add(new Feature("InPacket","X"));
		}
		
		expectedResult.add(new Feature("InPacketDist",""+0));
		expectedResult.add(new Feature("InPacketDist",""+1));
		expectedResult.add(new Feature("InPacketDist",""+1));
		expectedResult.add(new Feature("InPacketDist",""+3));
		for(int i=4; i<10;i++){
			expectedResult.add(new Feature("InPacketDist","X"));
		}
		
		expectedResult.add(new Feature("NumInPacketsInB",""+4));
		for(int i=1; i<100;i++){
			expectedResult.add(new Feature("NoBlock",""+0));
		}
		expectedResult.add(new Feature("MaxBurst",""+2));
		expectedResult.add(new Feature("AverageBurst",""+2));
		expectedResult.add(new Feature("NumBursts",""+1));
		expectedResult.add(new Feature("Bursts0",""+0));
		expectedResult.add(new Feature("Bursts1",""+0));
		expectedResult.add(new Feature("Bursts2",""+0));
		expectedResult.add(new Feature("Burst",""+2));
		expectedResult.add(new Feature("Size",""+1501));
		expectedResult.add(new Feature("Size",""+1501));
		expectedResult.add(new Feature("Size",""+1501));
		expectedResult.add(new Feature("Size",""+1499));
		expectedResult.add(new Feature("Size",""+1499));
		expectedResult.add(new Feature("Size",""+1501));
		for(int i=6; i<20;i++){
			expectedResult.add(new Feature("Size",""+0));
		}
		
		features = FeatureExtractor.extract(times, directions); 
	
		assertEquals(expectedResult.size(), features.size());
		for(int i=0; i<expectedResult.size(); i++){
			assertTrue(expectedResult.get(i).equals(features.get(i)));
		}
	}
}
