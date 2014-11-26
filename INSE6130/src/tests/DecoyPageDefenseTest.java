package tests;

import packets.Packet;
import packets.PacketComparator;
import defenses.DecoyPageDefense;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.Test;

public class DecoyPageDefenseTest {

	@Test
	public void testExtractPacketsInfo(){
		String file = "batch/1-1";
		ArrayList<Packet> actual = DecoyPageDefense.extractPacketsInfo(file);

		ArrayList<Packet> expected = new ArrayList<Packet>();
		expected.add(new Packet((float)0.0,1));
		expected.add(new Packet((float)0.116133928299,1));
		expected.add(new Packet((float)0.499715805054,-1));
		expected.add(new Packet((float)0.499715805054,1));
		expected.add(new Packet((float)0.91280579567,1));

		for(int i=0; i<expected.size(); i++){
			assertTrue(expected.get(i).equals(actual.get(i)));
		}
	}

	@Test
	public void testWritePacketsInfo(){
		String outFile = "outTest"+"1-1";
		ArrayList<Packet> packetList = new ArrayList<Packet>();
		packetList.add(new Packet((float)0.0,1));
		packetList.add(new Packet((float)0.116133928299,1));
		packetList.add(new Packet((float)0.499715805054,-1));
		packetList.add(new Packet((float)0.499715805054,1));
		packetList.add(new Packet((float)0.91280579567,1));

		DecoyPageDefense.writePacketsInfo(outFile, packetList);
	}

	@Test
	public void testDecoyPage(){
		File folderout = new File("outTest");
		if (!folderout.exists())
		{
			folderout.mkdir();
		}

		File folder = new File("batch");
		if (!folder.exists())
		{
			System.out.println("Batch folder needs to exist");
			System.exit(0);
		}

		String fold = "batch/";

		String file1 = fold + "1";
		ArrayList<Packet> packetList = DecoyPageDefense.extractPacketsInfo(file1);

		String file2 = fold + "2";
		packetList.addAll(DecoyPageDefense.extractPacketsInfo(file2));

		Collections.sort(packetList, new PacketComparator());

		String outFile = folderout+"1";
		DecoyPageDefense.writePacketsInfo(outFile, packetList);
	}

}
