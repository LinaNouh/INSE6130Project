
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class DecoyPageDefense{
	final static int NUM_SITES = 10;
	final static int NUM_INSTANCES = 90;

	public static ArrayList<Packet> extractPacketsInfo(String file){
		BufferedReader reader = null;
		ArrayList<String> linesList = new ArrayList<String>();
		ArrayList<Packet> packetList = new ArrayList<Packet>();
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String line2 = reader.readLine();
			String[] parts2;
			int start2 = 0;
			float startTime2 = 0;
			while(line2 != null){
				if(start2 == 0){
					start2 = 1;
					linesList.add(line2);
					parts2 = linesList.get(0).split("\t");
					startTime2 = Float.valueOf(parts2[0]);
					packetList.add(new Packet(Float.valueOf(parts2[0])-startTime2,
							Integer.valueOf(parts2[1])));
					line2 = reader.readLine();
					continue;
				}
				linesList.add(line2);
				parts2 = line2.split("\t");
				packetList.add(new Packet(Float.valueOf(parts2[0])-startTime2,
						Integer.valueOf(parts2[1])));
				line2 = reader.readLine();
			} 
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return packetList;
	}
	
	public static void writePacketsInfo(String file, ArrayList<Packet> packetList){
		try {
			PrintWriter writer = new PrintWriter(file);
			for(int i=0; i<packetList.size();i++){
				writer.println(packetList.get(i).getTime()+"\t"+packetList.get(i).getDirection());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// if the directory does not exist, create it
		File folderout = new File("batchusenix-pdef");
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
		int count = 0;

		for(int site=0; site <= NUM_SITES; site++)
		{
			System.out.println(site);
			for(int inst=0; inst <= NUM_INSTANCES; inst++)
			{
				count = count+1;
				
				String file1 = fold + site + "-" + inst;
				ArrayList<Packet> packetList = extractPacketsInfo(file1);
				
				String file2 = fold + "" + count;
				packetList.addAll(extractPacketsInfo(file2));
				
				Collections.sort(packetList, new PacketComparator());
				
				String outFile = folderout+""+site+"-"+inst;
				writePacketsInfo(outFile, packetList);
			}
		}
	}
}

