
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class DecoyPageDefense{

	public void createDecoy(){
		
		final int numSites = 10;
		final int numInstances = 90;
		
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
		String foldout = "batchusenix-pdef/";
		int count = 0;
		
		for(int site=0;site<=numSites;site++)
		{
			System.out.println(site);
			for(int inst=0;inst<=numInstances;inst++)
			{
				count = count+1;
				String file1 = fold + site + "-" + inst;
				BufferedReader reader = null;
				
				ArrayList<String> linesList1 = new ArrayList<String>();
				ArrayList<String> linesList2 = new ArrayList<String>();
				ArrayList<Packet> packetList = new ArrayList<Packet>(); 
				
				try {
					reader = new BufferedReader(new FileReader(file1));
					String line1 = reader.readLine();
					String[] parts1;
					int start1 = 0;
					float startTime1 = 0;
					while(line1 != null){
						if(start1 == 0){
							start1 = 1;
							linesList1.add(line1);
							parts1 = linesList1.get(0).split("\t");
							startTime1 = Float.valueOf(parts1[0]);
							packetList.add(new Packet(Float.valueOf(parts1[0])-startTime1,
									Integer.valueOf(parts1[1])));
							line1 = reader.readLine();
							continue;
						}
						linesList1.add(line1);
						parts1 = line1.split("\t");
						packetList.add(new Packet(Float.valueOf(parts1[0])-startTime1,
								Integer.valueOf(parts1[1])));
						line1 = reader.readLine();
					}
					reader.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String file2 = fold + "" + count;
				try {
					reader = new BufferedReader(new FileReader(file2));
					String line2 = reader.readLine();
					String[] parts2;
					int start2 = 0;
					float startTime2 = 0;
					while(line2 != null){
						if(start2 == 0){
							start2 = 1;
							linesList2.add(line2);
							parts2 = linesList1.get(0).split("\t");
							startTime2 = Float.valueOf(parts2[0]);
							packetList.add(new Packet(Float.valueOf(parts2[0])-startTime2,
									Integer.valueOf(parts2[1])));
							line2 = reader.readLine();
							continue;
						}
						linesList2.add(line2);
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

				Collections.sort(packetList, new PacketComparator());

				try {
					PrintWriter writer = new PrintWriter(folderout+""+site+"-"+inst);
					for(int i=0; i<packetList.size();i++){
						writer.println(packetList.get(i).getTime()+"\t"+packetList.get(i).getDirection());
					}
					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} 
			}
		}
	}
}

