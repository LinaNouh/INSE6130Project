package tests;

import packets.Packet;
import packets.PacketComparator;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
import static org.junit.Assert.*;

public class PacketComparatorTest {

	@Test
	public void testCompare1(){
		Packet p1 = new Packet((float)0.5467, 1);
		Packet p2 = new Packet((float)0.4321, -1);
		
		PacketComparator pc = new PacketComparator();
		
		int actualResult = pc.compare(p1,p2);
		
		assertEquals(1, actualResult);
	}
	
	@Test
	public void testCompare2(){
		Packet p1 = new Packet((float)0.2134, -1);
		Packet p2 = new Packet((float)0.4321, -1);
		
		PacketComparator pc = new PacketComparator();
		
		int actualResult = pc.compare(p1,p2);
		
		assertEquals(-1, actualResult);
	}
	
	@Test
	public void testCompare3(){
		Packet p1 = new Packet((float)0.4321, 1);
		Packet p2 = new Packet((float)0.4321, 1);
		
		PacketComparator pc = new PacketComparator();
		
		int actualResult = pc.compare(p1,p2);
		
		assertEquals(0, actualResult);
	}
	
	@Test
	public void testSort(){
		Packet p1 = new Packet((float)0.4321, 1);
		Packet p2 = new Packet((float)0.5321, 1);
		Packet p3 = new Packet((float)0.6321, 1);
		Packet p4 = new Packet((float)0.7321, 1);
		
		ArrayList<Packet> pList1 = new ArrayList<Packet>();
		ArrayList<Packet> pList2 = new ArrayList<Packet>();
		
		pList1.add(p3);
		pList1.add(p1);
		pList1.add(p4);
		pList1.add(p2);
		
		pList2.add(p1);
		pList2.add(p2);
		pList2.add(p3);
		pList2.add(p4);
		
		assertFalse(pList1.equals(pList2));
		
		Collections.sort(pList1, new PacketComparator());
		
		assertEquals(pList2, pList1);
	}

}
