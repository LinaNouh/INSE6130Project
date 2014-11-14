import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;


public class FeatureExtractor {
	
	public static int sum(ArrayList<Integer> list) {
	     Integer sum= 0; 
	     for (Integer i:list)
	         sum = sum + i;
	     return sum;
	}
	
	/*
	 * This function returns an array list of features in the following order:
	 * {NumPackets, NumInPackets, NumOutPackets, TotalTime, containLengthXXX(multiple), InPacket(multiple), InPacketDist(multiple),
	 * NumInPacketsInB(multiple), NoBlock*, MaxBurst, AverageBurst, NumBursts, Bursts(i)(multiple), Burst(multiple), Size(multiple)}       
	 */
	public static ArrayList<Feature> extract(ArrayList<Float> times, ArrayList<Integer> directions){
		
		int numBirstsToInc = 5;
		int numSizesToInc = 20;
		int minLength = -2;//-1500;
		int maxLength = 3;//1501;
		int maxInPackets = 10;//300;
		int packetBlockSize = 30;
		int numPacketsForDist = 3000;
		
		ArrayList<Feature> features = new ArrayList<Feature>();
		
		//transmission size features 
		features.add(new Feature("NumPackets", ""+times.size())); //how many lines read from the data file
		
		int c = 0;
		for(int i=0; i<directions.size(); i++){
			if (directions.get(i)>0)
				c++;
		}
		
		features.add(new Feature("NumInPackets", ""+c));//how many positive direction
		features.add(new Feature("NumOutPackets", ""+(times.size() - c)));//how many negative direction
		features.add(new Feature("TotalTime",
				""+(times.get(times.size()-1) - times.get(0))));//last time - first time
		
		
		//unique packet lengths between -1500 and 1501 
		for(int i=minLength; i<maxLength; i++){
			if (directions.contains(i))
				features.add(new Feature("containLength"+i,""+1));
			else
				features.add(new Feature("containLength"+i,""+0));
		}
		
		
		//Transposition (similar to good distance scheme)
		c = 0;
		for(int i=0; i<directions.size(); i++){
			if(directions.get(i) > 0){
				c++;
				features.add(new Feature("InPacket",""+i));//adding first 300 positive direction
			}
			if(c == maxInPackets)
				break;
		}
		for(int i=c; i<maxInPackets; i++){
			features.add(new Feature("InPacket","X"));//if positive direction are less than 300 pad the rest with "X"
		}
		
		c = 0;
		int previousLocation = 0;
		for(int i=0; i<directions.size();i++){
			if(directions.get(i) > 0){
				c++;
				features.add(new Feature("InPacketDist",""+(i-previousLocation)));//adding distance between each positive direction and the previous positive
				previousLocation = i;
			}
			if(c == maxInPackets)//only consider first 300 positive direction
				break;
		}
		for(int i=c; i<maxInPackets; i++){
			features.add(new Feature("InPacketDist","X"));//if positive directions are less than 300 pad the rest with "X"
		}
		
		c = 0;
		//Packet distributions (where are the outgoing packets concentrated)
		for(int i=0; i< Math.min(directions.size(), numPacketsForDist); i++){
			if(i % packetBlockSize != packetBlockSize-1){
				if(directions.get(i)>0){
					c++;
				}
			}else{
				features.add(new Feature("NumInPacketsInB",""+c));//add number of positive direction in every 30 lines
				c = 0;
			}
		}
		
		if(directions.size() < packetBlockSize-1){
			features.add(new Feature("NumInPacketsInB",""+c));
		}
		if(directions.size()<30){
			for(int i=1;i<100;i++){
				features.add(new Feature("NoBlock",""+0));
			}
		}else
			for(int i=directions.size()/30;i<100;i++){//if the groups of 30 are less than 100 (total < 3000) pad the rest with 0
				features.add(new Feature("NoBlock",""+0));
			}
		
		//Bursts (no two adjacent incoming packets)
		ArrayList<Integer> bursts = new ArrayList<Integer>();
		int currentBurst = 0;
		int stopped = 0;
		boolean addedBurst = false;
		for(int i=0; i<directions.size();i++){
			if(directions.get(i) < 0){//outgoing
				stopped = 0;
				currentBurst -= directions.get(i); 
				addedBurst = false;
			}else if(directions.get(i) > 0 && stopped == 0){//first incoming
				addedBurst = false;
				stopped = 1;
			}else if(directions.get(i) > 0 && stopped == 1 && currentBurst != 0){//second incoming
				stopped = 0;
				bursts.add(currentBurst);
				addedBurst = true;
			}
		}
		if(addedBurst == false)
			bursts.add(currentBurst);
		
		features.add(new Feature("MaxBurst",""+Collections.max(bursts)));
		features.add(new Feature("AverageBurst",""+(sum(bursts)/bursts.size())));
		features.add(new Feature("NumBursts",""+bursts.size()));
		
		int[] counts = new int[3];
		counts[0] = 0;
		counts[1] = 0;
		counts[2] = 0;
		for(int i=0; i<bursts.size();i++){
			if(bursts.get(i)>5)
				counts[0]++;
			if(bursts.get(i)>10)
				counts[1]++;
			if(bursts.get(i)>15)
				counts[2]++;
		}
		
		for(int i=0; i<counts.length;i++)
			features.add(new Feature("Bursts"+i,""+counts[i]));
		
		for(int i=0; i<numBirstsToInc; i++){//add first numBirstsToInc bursts
			if(i < bursts.size())
				features.add(new Feature("Burst",""+bursts.get(i)));
		}
		
		for(int i=0; i<numSizesToInc; i++){//add first numSizesToInc sizes +1500
			if(i < directions.size())
				features.add(new Feature("Size",""+(directions.get(i)+1500)));
			else
				features.add(new Feature("Size",""+0));
		}
		
		return features;
	}//end of extract method

	
	public static void main(String[] args) throws IOException {
		ArrayList<Feature> feature = new ArrayList<Feature>();
		for(int site=0; site<=100; site++)
		{
		for (int inst=0; inst<=90; inst++)
		{
			String fname = "batch/" + site + "-" + inst;
			BufferedReader reader = null;
			ArrayList<String> lineList = new ArrayList<String>();
			ArrayList<Float> time = new ArrayList<Float>();
			ArrayList<Integer> size = new ArrayList<Integer>();
			try {
				 reader = new BufferedReader(new FileReader(fname));
				 String line1 = reader.readLine();
				 String[] parts1;
				 while(line1 != null){
					 lineList.add(line1);
					 parts1 = line1.split("\t");
					 time.add(Float.valueOf(parts1[0]));
					 size.add(Integer.valueOf(parts1[1]));
					 line1 = reader.readLine();
				 }
				 reader.close();
			}
				 catch (FileNotFoundException e) {
						e.printStackTrace();
					}
			
			feature = extract(time,size);
			try {
				PrintWriter writer = new PrintWriter(fname + "f");
				for(int i=0; i<feature.size();i++){
					writer.println(feature + " ");
				}
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			}
		}
		
		for(int site1=0; site1<=9000; site1++)
		{
			String fname = "batch/" + site1;
			BufferedReader reader = null;
			ArrayList<String> lineList = new ArrayList<String>();
			ArrayList<Float> time1 = new ArrayList<Float>();
			ArrayList<Integer> size1 = new ArrayList<Integer>();
			try {
				 reader = new BufferedReader(new FileReader(fname));
				 String line1 = reader.readLine();
				 String[] parts1;
				 while(line1 != null){
					 lineList.add(line1);
					 parts1 = line1.split("\t");
					 time1.add(Float.valueOf(parts1[0]));
					 size1.add(Integer.valueOf(parts1[1]));
					 line1 = reader.readLine();
				 }
				 reader.close();
			}
				 catch (FileNotFoundException e) {
						e.printStackTrace();
					}
			
			feature = extract(time1,size1);
			try {
				PrintWriter writer = new PrintWriter(fname + "f");
				for(int i=0; i<feature.size();i++){
					writer.println(feature + " ");
				}
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
		}
		
		try {
			PrintWriter writer1 = new PrintWriter("fdetails");
			int S = feature.size();
			writer1.println(S);
			System.out.println(feature.size());
			writer1.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
}
