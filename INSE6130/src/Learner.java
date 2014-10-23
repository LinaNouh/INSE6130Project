import java.util.Random;

public class Learner {
	
	//Data Set Attributes
	private int numFeatures = 3736;//how many features
	
	private static final int numSites = 100;//how many sites are monitored
	private static final int numInstances = 60;//how many instances per site are used for distance learning
	private static final int numTestInstances = 30;//how many instances per site are used for kNN training/testing
	
	private int numOpenTest = 0;//how many open instances are used for kNN training/testing
	private int numNeighbors = 1;//how many neighbors are used for kNN
	
	private static final int numRecPoints = 5;//how many neighbors are used for distance learning
	
	//Algorithm Attributes
	private Random randomGenerator = new Random();
	
	private float POWER = (float) 0.1; //not used in this code
	
	private static final int numRecoList = 10;
	private static final int numReco = 1;
	
	public boolean arrayContains(int e, int[] array, int len){
		for(int i = 0; i < len; i++) {
			if (array[i] == e)
				return true;
		}
		return false;
	}
	
	//a method to randomly initialize the weight of each feature 
	public float[] initialize_weights(float[] features, float[] weights){
		for (int i = 0; i < numFeatures; i++) {
			weights[i] = (float)(randomGenerator.nextInt(100)/ 100.0 + 0.5);
		}
		return weights;
		/*float sum = 0;
		for (int j = 0; j < FEAT_NUM; j++) {
			if (abs(weight[j]) > sum) {
			sum += abs(weight[j]);
			}
		}
		for (int j = 0; j < FEAT_NUM; j++) {
			weight[j] = weight[j]/sum * 1000;
		}*/
	}
	
	//a method to compute the distance between two sets of features
	public float compute_distance(float[] firstFeatures, float[] secondFeatures, 
			float[] weight, float power){
		float dist = 0;
		for (int i = 0; i < numFeatures; i++) {
			if (firstFeatures[i] != -1 && secondFeatures[i] != -1) {
				dist += weight[i] * Math.abs(firstFeatures[i] - secondFeatures[i]);
			}
		}
		return dist;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
