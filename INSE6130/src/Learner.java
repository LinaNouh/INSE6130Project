import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Learner {

	//Data Set Attributes
	private static int numFeatures = 3736;//how many features

	private static final int numSites = 100;//how many sites are monitored
	private static final int numInstances = 60;//how many instances per site are used for distance learning
	private static final int numTestInstances = 30;//how many instances per site are used for kNN training/testing

	private static int numOpenTest = 0;//how many open instances are used for kNN training/testing
	private static int numNeighbors = 1;//how many neighbors are used for kNN

	private static final int numRecPoints = 5;//how many neighbors are used for distance learning

	//Algorithm Attributes
	private static Random randomGenerator = new Random();

	//private static float POWER = (float) 0.1; //not used in this code

	//private static final int numRecoList = 10;
	//private static final int numReco = 1;

	public static boolean arrayContains(int e, int[] array){
		if(array == null || array.length == 0)
			return false;
		for(int i = 0; i < array.length; i++) {
			if (array[i] == e)
				return true;
		}
		return false;
	}

	public static float arrayMax(float[] array){
		if(array == null || array.length == 0)
			return 0;
		float max = 0;
		for(int i=0; i<array.length;i++){
			if(array[i] > max){
				max = array[i];
			}
		}
		return max;
	}

	public static int arrayMinIndex(float[] array, int startInd, int endInd){
		if(array == null || array.length == 0)
			return 0;
		float min = array[startInd];
		int minInd = startInd;
		for(int i=startInd; i<endInd;i++){
			if(array[i] < min){
				min = array[i];
				minInd = i;
			}
		}
		return minInd;
	}

	//A method to randomly initialize the weight of each feature 
	public static float[] initializeWeights(int numWeights){
		float[] weights = new float[numWeights];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = (float)(randomGenerator.nextInt(100)/ 100.0 + 0.5);
		}

		return weights;
	}

	//a method to compute the distance between two sets of features
	public static float computeDistance(float[] firstFeatures, float[] secondFeatures, 
			float[] weight){
		float dist = 0;
		for (int i = 0; i < Math.min(firstFeatures.length, secondFeatures.length); i++) {
			if (firstFeatures[i] != -1 && secondFeatures[i] != -1) {
				dist += weight[i] * Math.abs(firstFeatures[i] - secondFeatures[i]);
			}
		}
		return dist;
	}

	public static void recommend(int beginning, int ending, float[][] feat, float[] weights){

		//initializing arrays
		float[] distanceList = new float[numSites * numInstances];
		int[] goodListRec = new int[numRecPoints];
		int[] badListRec = new int[numRecPoints];

		//loop through the features starting from the beginning index until the ending index
		for(int i=beginning; i<ending; i++){
			System.out.println("Learning distance..."+i+" "+(beginning - ending));
			int currentSite = i/numInstances;
			//int currentInstance = i % numInstances;

			float badness = 0;
			float goodDistMax = 0;

			//compute the distance between this set of features i and all other sets of features and save in distanceList
			for(int j=0;j<numSites * numInstances; j++){
				distanceList[j] = computeDistance(feat[i], feat[j], weights);
			}

			//find the max distance
			float max = arrayMax(distanceList);
			distanceList[i] = max;

			//setting the maximum good distance 
			for(int j = 0; j < numRecPoints; j++) {
				//finding the minimum distance within the range
				int minInd = arrayMinIndex(distanceList,currentSite*numInstances,(currentSite+1)*numInstances);
				if (distanceList[minInd] > goodDistMax) 
					goodDistMax = distanceList[minInd];
				distanceList[minInd] = max;
				goodListRec[j] = minInd;
			}

			for (int j = 0; j < numInstances; j++) {
				distanceList[currentSite*numInstances+j] = max;
			}

			for (int j = 0; j < numRecPoints; j++) {
				int minInd = arrayMinIndex(distanceList,0,numInstances*numSites);//find index of min element within range
				if (distanceList[minInd] <= goodDistMax) 
					badness += 1;
				distanceList[minInd] = max;
				badListRec[j] = minInd;
			}

			badness /= (float)numRecPoints;
			badness += 0.2;

			float[] featuresDist = new float[numFeatures];
			//initializing distances to 0
			for (int f = 0; f < featuresDist.length; f++) {
				featuresDist[f] = 0;
			}

			int[] badList = new int[numFeatures];
			int badListMin = 0;
			int badListCount = 0;

			for (int f = 0; f < numFeatures; f++) {
				if (weights[f] == 0){ 
					badList[f] = 0;
				}else{
					float goodMax = 0;
					int badCount = 0;
					for (int k = 0; k < numRecPoints; k++) {
						float n = Math.abs(feat[i][f] - feat[goodListRec[k]][f]);
						if (feat[i][f] == -1 || feat[badListRec[k]][f] == -1) 
							n = 0;
						if (n >= goodMax) 
							goodMax = n;
					}

					for (int k = 0; k < numRecPoints; k++) {
						float n = Math.abs(feat[i][f] - feat[badListRec[k]][f]);
						if (feat[i][f] == -1 || feat[badListRec[k]][f] == -1) 
							n = 0;
						featuresDist[f] += n;
						if (n <= goodMax) badCount += 1;
					}

					badList[f] = badCount;
					if (badCount < badListMin) 
						badListMin = badCount;
				}
			}

			for (int f = 0; f < numFeatures; f++) {
				if (badList[f] != badListMin) 
					badListCount += 1;
			}

			int[] w0id = new int[badListCount];
			float[] change = new float[badListCount];
			int temp = 0;
			float C1 = 0;
			//float C2 = 0;

			for (int f = 0; f < numFeatures; f++) {
				if (badList[f] != badListMin) {
					w0id[temp] = f;
					change[temp] = (float) (weights[f] * 0.01 * badList[f]/(float)numRecPoints * badness);
					if (change[temp] < 1.0/1000) 
						change[temp] = weights[f];
					C1 += change[temp] * featuresDist[f];
					//C2 += change[temp];
					weights[f] -= change[temp];
					temp += 1;
				}
			}

			float totalfd = 0;
			for (int f = 0; f < numFeatures; f++) {
				if (badList[f] == badListMin && weights[f] > 0) {
					totalfd += featuresDist[f];
				}
			}

			for (int f = 0; f < numFeatures; f++) {
				if (badList[f] == badListMin && weights[f] > 0) {
					weights[f] += C1/(totalfd);
				}
			}
		}

		Random randomGenerator = new Random();
		for (int j = 0; j < numFeatures; j++) {
			if (weights[j] > 0)
				weights[j] *= (0.9 + randomGenerator.nextInt(100) / 500.0);
		}
	}

	static float accuracy(float[][] closedfeat, float[] weight, float[][] openfeat) {

		float tp = 0;
		float tn = 0;

		float[][] feat = new float[numSites*numTestInstances + numOpenTest][];

		for (int i = 0; i < numSites*numTestInstances; i++) {
			feat[i] = closedfeat[i];
		}
		for (int i = 0; i < numOpenTest; i++) {
			feat[i + numSites * numTestInstances] = openfeat[i];
		}

		float[] distlist = new float[numSites * numTestInstances + numOpenTest];
		int[] classlist = new int[numSites + 1];

		for (int is = 0; is < numSites*numTestInstances + numOpenTest; is++) {
			System.out.printf("\rComputing accuracy... %d (%d-%d)", is,
					numSites*numTestInstances + numOpenTest);


			for (int i = 0; i < numSites+1; i++) {
				classlist[i] = 0;
			}
			int maxclass = 0;
			for (int at = 0; at < numSites * numTestInstances + numOpenTest; at++) {
				distlist[at] = computeDistance(feat[is], feat[at], weight);
			}
			float max =  arrayMax(distlist);
			distlist[is] = max;
			for (int i = 0; i < numNeighbors; i++) {
				int ind = arrayMinIndex(distlist, 0, numSites*numTestInstances+numOpenTest-1);
				int classind = 0;
				if (ind < numSites * numTestInstances) {
					classind = ind/numTestInstances;
				}
				else {
					classind = numSites;
				}
				classlist[classind] += 1;
				if (classlist[classind] > maxclass) {
					maxclass = classlist[classind];
				}
				distlist[ind] = max;
			}

			int trueclass = is/numTestInstances;
			if (trueclass > numSites) trueclass = numSites;

			int countclass = 0;
			int hascorrect = 0;

			int hasconsensus = 0;
			for (int i = 0; i < numSites+1; i++) {
				if (classlist[i] == numNeighbors) {
					hasconsensus = 1;
				}
			}
			if (hasconsensus == 0) {
				for (int i = 0; i < numSites; i++) {
					classlist[i] = 0;
				}
				classlist[numSites] = 1;
				maxclass = 1;
			}

			for (int i = 0; i < numSites+1; i++) {
				if (classlist[i] == maxclass) {
					countclass += 1;
					if (i == trueclass) {
						hascorrect = 1;
					}
				}
			}

			float thisacc = 0;
			if (hascorrect == 1) {
				thisacc = (float) (1.0/countclass);
			}
			
			if (trueclass == numSites) {
				tn += thisacc;
			}
			else {
				tp += thisacc;
			}

		}

		System.out.println("\n");


		tp /= numSites*numTestInstances;
		if (numOpenTest > 0)                
			tn /= numOpenTest;
		else 
			tn = 1;
		
		return tp;
	}

	public static void main(String[] args) {
		
		System.out.println("Hello,");

		//initializing number of open tests 
		System.out.println("Choosing from: {0, 10, 50, 100, 200, 300, 500, 1000, 1500, 2000, 3000, 5000}");
		System.out.println("Please enter the number of open tests:");
		numOpenTest = Integer.parseInt(System.console().readLine());

		//initializing number of neighbors
		System.out.println("Choosing from: {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 15}");
		System.out.println("Please enter the number of neighbors:");
		numNeighbors = Integer.parseInt(System.console().readLine());

		//Random randomGenerator = new Random(System.currentTimeMillis());

		float[][] feat = new float[numSites * numInstances][];
		float[][] testfeat = new float[numSites * numTestInstances][];
		float[][] opentestfeat = new float[numOpenTest][];

		//creating the feature array for each instance of each site
		for (int i = 0; i < numSites * numInstances; i++)
		{
			feat[i] = new float[numFeatures];
		}

		//creating the feature array for each test instance of each site
		for (int i = 0; i < numSites * numTestInstances; i++)
		{
			testfeat[i] = new float[numFeatures];
		}

		//creating the feature array for each open test instance
		for (int i = 0; i < numOpenTest; i++)
		{
			opentestfeat[i] = new float[numFeatures];
		}

		//loop for each instance of each site
		for (int cur_site = 0; cur_site < numSites; cur_site++)
		{
			int real_inst = 0;
			for (int cur_inst = 0; cur_inst < numInstances; cur_inst++)
			{
				boolean fileExists = false;
				BufferedReader reader = null;

				while (!fileExists)
				{
					String file_name = "batch/"+cur_site+"-"+real_inst;
					try {
						reader = new BufferedReader(new FileReader(file_name));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

					//check if file exists
					if ((file_name.length()) > 0 && (new File(file_name).exists()))
					{
						fileExists = true;
					}
					real_inst++;
				}
				//read first line from data file
				String str;
				try {
					str = reader.readLine();
					reader.close();
					
					String tempstr = "";
					int feat_count = 0;
					//populating the features array for the current instance
					for (int i = 0; i < str.length(); i++)
					{
						if (str.charAt(i) == ' ')
						{
							if (tempstr.charAt(1) == 'X')
							{
								feat[cur_site * numInstances + cur_inst][feat_count] = -1F;
							}
							else
							{
								feat[cur_site * numInstances + cur_inst][feat_count] = Float.parseFloat(tempstr);
							}
							feat_count += 1;
							tempstr = "";
						}
						else
						{
							tempstr += str.charAt(i);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			for (int cur_inst = 0; cur_inst < numTestInstances; cur_inst++)
			{
				boolean fileExists = false;
				BufferedReader reader = null;

				while (!fileExists)
				{
					String file_name = "batch/"+cur_site+"-"+real_inst;
					try {
						reader = new BufferedReader(new FileReader(file_name));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					//check if file exists
					if ((file_name.length()) > 0 && (new File(file_name).exists()))
					{
						fileExists = true;
					}
					real_inst++;
				}
				String str;
				try {
					str = reader.readLine();
					reader.close();
					
					String tempstr = "";
					int feat_count = 0;
					for (int i = 0; i < str.length(); i++)
					{
						if (str.charAt(i) == ' ')
						{
							if (tempstr.charAt(1) == 'X')
							{
								testfeat[cur_site * numTestInstances + cur_inst][feat_count] = -1F;
							}
							else
							{
								testfeat[cur_site * numTestInstances + cur_inst][feat_count] = Float.parseFloat(tempstr);
							}
							feat_count += 1;
							tempstr = "";
						}
						else
						{
							tempstr += str.charAt(i);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//float[] weight = new float[numFeatures];

		int TRIAL_NUM = 1;
		int SUBROUND_NUM = 5;
		float maxacc = 0F;
		
		float[] weights = initializeWeights(numFeatures);

		float[] prevweight = new float[numFeatures];
		for (int i = 0; i < numFeatures; i++)
		{
			prevweight[i] = weights[i];
		}

		for (int trial = 0; trial < TRIAL_NUM; trial++){
			for (int subround = 0; subround < SUBROUND_NUM; subround++)
			{
				int start = (numSites * numInstances) / SUBROUND_NUM * subround;
				int end = (numSites * numInstances) / SUBROUND_NUM * (subround + 1);
				recommend(start, end, feat, weights);
				float tp = accuracy(testfeat, weights, opentestfeat);

				if (tp > maxacc)
				{
					maxacc = tp;
				}
				//System.out.printf("Round %d-%d, accuracy: %f %f, best accuracy: %f\n", trial,subround,tp, tn, maxacc);
			}
		}

		PrintWriter writer;
		try {
			writer = new PrintWriter("weights");
			
			for (int i = 0; i < numFeatures; i++)
			{
				writer.println(weights[i] * 1000);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
}


