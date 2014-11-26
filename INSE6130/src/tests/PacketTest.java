package tests;

import packets.Packet;
import org.junit.Test;
import static org.junit.Assert.*;

public class PacketTest {

	@Test
	public void testSetAndGet(){
		Packet p = new Packet();
		
		float time = (float) 0.9987;
		int dir = -1;
		
		p.setTime(time);
		p.setDirection(dir);
		
		assertEquals(String.valueOf(time) , String.valueOf(p.getTime()));
		assertEquals(dir , p.getDirection());
	}
	
	@Test
	public void testCompareTo1(){
		Packet p1 = new Packet((float)0.5467, 1);
		Packet p2 = new Packet((float)0.4321, -1);
		
		int actualResult = p1.compareTo(p2);
		
		assertEquals(1, actualResult);
	}
	
	@Test
	public void testCompareTo2(){
		Packet p1 = new Packet((float)0.2134, 1);
		Packet p2 = new Packet((float)0.4321, -1);
		
		int actualResult = p1.compareTo(p2);
		
		assertEquals(-1, actualResult);
	}
	
	@Test
	public void testCompareTo3(){
		Packet p1 = new Packet((float)0.4321, 1);
		Packet p2 = new Packet((float)0.4321, -1);
		
		int actualResult = p1.compareTo(p2);
		
		assertEquals(0, actualResult);
	}

}
