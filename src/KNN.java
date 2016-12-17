import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class KNN {
	private int k;
	private Data trainData;
	
	public KNN(int k, Data trainData){
		this.k = k;
		this.trainData = trainData;
	}
	
	
	public ArrayList<String[]> testKNN(Data test){
		ArrayList<String[]> results = new ArrayList<String[]>();
		ArrayList<String[]> trainArguments = trainData.getArguments();
		ArrayList<String[]> testArguments = test.getArguments();

		for(String[] testArg : testArguments){
	//		ArrayList<Integer> nearestNeigh = new ArrayList<Integer>();
	//		ArrayList<Double> nearestNeighDist = new ArrayList<Double>();
			TreeMap<Double, Integer> nearestNeigh = new TreeMap<Double, Integer>();
			boolean notSorted = true;
			
			for(int i = 0; i < trainArguments.size(); i++){
				double distance = euclDistance(trainArguments.get(i), testArg);
				nearestNeigh.put(distance, i);
				if(nearestNeigh.size() > k){
					int temp = 1;
					for (Map.Entry<Double, Integer> entry : nearestNeigh.entrySet()) {
						if(temp > k){
							nearestNeigh.remove(entry.getKey());
						}
					    temp++;
					}
				}
			}
		}
		
		return results;
	}
	
	private double euclDistance(String[] trainArg, String[] testArg){
		double distance = 0.0;
		
		for(int i = 0; i < trainArg.length; i++){
			distance += (Double.parseDouble(trainArg[i]) - Double.parseDouble(testArg[i])) * (Double.parseDouble(trainArg[i]) - Double.parseDouble(testArg[i]));
		}
		
		distance = Math.sqrt(distance);
		
		return distance;
	}
}
