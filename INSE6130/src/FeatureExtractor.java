import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class FeatureExtractor {
	
	public static int sum(ArrayList<Integer> list) {
	     Integer sum= 0; 
	     for (Integer i:list)
	         sum = sum + i;
	     return sum;
	}
	
	public static void extract(ArrayList<Float> times, ArrayList<Integer> directions, ArrayList<String> features){
		
		//transmission size features 
		features.add(""+times.size()); //how many lines read from the data file
		
		int c = 0;
		for(int i=0; i<directions.size(); i++){
			if (directions.get(i)>0)
				c++;
		}
		
		features.add(""+c);//how many positive sizes
		features.add(""+(times.size() - c));//how many negative sizes
		
		features.add(""+(times.get(-1) - times.get(0)));//last time - first time
		
		//unique packet lengths
		for(int i=-1500; i<1501; i++){
			if (Arrays.asList(directions).contains(i))
				features.add(""+1);
			else
				features.add(""+0);
		}
		
		//Transposition (similar to good distance scheme)
		c = 0;
		for(int i=0; i<directions.size(); i++){
			if(directions.get(i) > 0){
				c++;
				features.add(""+i);//adding first 300 positive sizes
			}
			if(c == 300)
				break;
		}
		for(int i=c; i<300; i++){
			features.add("X");//if positive sizes are less than 300 pad the rest with "X"
		}
		
		c = 0;
		int previousLocation = 0;
		for(int i=0; i<directions.size();i++){
			if(directions.get(i) > 0){
				c++;
				features.add(""+(i-previousLocation));//adding distance between each positive size and the previous positive size
				previousLocation = i;
			}
			if(c == 300)//only consider first 300 positive sizes
				break;
		}
		for(int i=c; i<300; i++){
			features.add("X");//if positive sizes are less than 300 pad the rest with "X"
		}
		
		//Packet distributions (where are the outgoing packets concentrated)
		for(int i=0; i< Math.min(directions.size(), 3000); i++){
			if(i % 30 != 29){
				if(directions.get(i)>0)
					c++;
			}else{
				features.add(""+c);//add number of positive sizes in every 29 lines
				c = 0;
			}
		}
		
		for(int i=directions.size()/30;i<100;i++){//if the groups of 30 are less than 100 (total < 3000) pad the rest with 0
			features.add(""+0);
		}
		
		//Bursts (no two adjacent incoming packets)
		ArrayList<Integer> bursts = new ArrayList<Integer>();
		int currentBurst = 0;
		int stopped = 0;
		for(int i=0; i<directions.size();i++){
			if(directions.get(i) < 0){//outgoing
				stopped = 0;
				currentBurst -= directions.get(i); 
			}else if(directions.get(i) > 0 && stopped == 0){//first incoming
				stopped = 1;
			}else if(directions.get(i) > 0 && stopped == 1){//second incoming
				stopped = 0;
				bursts.add(currentBurst);
			}
		}
		features.add(""+Collections.max(bursts));
		features.add(""+(sum(bursts)/bursts.size()));
		features.add(""+bursts.size());
		
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
			features.add(""+counts[i]);
		
		for(int i=0; i<5; i++){//add first 5 bursts
			//if(bursts[i] != "X")
			features.add(""+bursts.get(i));
		}
		
		for(int i=0; i<20; i++){//add first 20 sizes +1500
			//if(bursts[i] != "X")
			features.add(""+(directions.get(i)+1500));
		}
		
	}//end of extract method

	
	public static void main(String[] args) throws IOException {
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			ArrayList<String> feature = new ArrayList<String>();
			extract(time,size,feature);
			try {
				PrintWriter writer = new PrintWriter(fname + "f");
				for(int i=0; i<feature.size();i++){
					writer.println(feature + " ");
				}
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			}
		}
		
		ArrayList<String> feature = new ArrayList<String>();
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			extract(time1,size1,feature);
			try {
				PrintWriter writer = new PrintWriter(fname + "f");
				for(int i=0; i<feature.size();i++){
					writer.println(feature + " ");
				}
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
