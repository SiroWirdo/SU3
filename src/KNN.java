import java.util.ArrayList;
import java.util.HashMap;
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
		ArrayList<String> testClasses = test.getClasses();

		for(int j = 0; j < testArguments.size(); j++){
			String[] testArg = testArguments.get(j);
	//		ArrayList<Integer> nearestNeigh = new ArrayList<Integer>();
	//		ArrayList<Double> nearestNeighDist = new ArrayList<Double>();
			TreeMap<Double, Integer> nearestNeigh = new TreeMap<Double, Integer>();
			boolean notSorted = true;
			
			for(int i = 0; i < trainArguments.size(); i++){
				double distance = euclDistance(trainArguments.get(i), testArg);
				nearestNeigh.put(distance, i);
				if(nearestNeigh.size() > k){
					//int temp = 1;
					//for(Map.Entry<Double, Integer> entry : nearestNeigh.entrySet()){
					//	if(temp > k){
					nearestNeigh.remove(nearestNeigh.lastKey());
				//		}
					//    temp++;
				//	}
				}
			}
			
			HashMap<String, Integer> bestClasses = new HashMap<String, Integer>();
			for(Map.Entry<Double, Integer> entry : nearestNeigh.entrySet()){
				String classVal = trainData.getClasses().get(entry.getValue());
				if(bestClasses.containsKey(classVal)){
					bestClasses.put(classVal, bestClasses.get(classVal) + 1);
				}else{
					bestClasses.put(classVal, 1);
				}
			}
			
			String bestClass = "";
			int bestVal = 0;
			for(Map.Entry<String, Integer> entry : bestClasses.entrySet()){
				if(entry.getValue() > bestVal){
					bestClass = entry.getKey();
					bestVal = entry.getValue();
				}
			}
			String[] res = {testClasses.get(j),bestClass};
			results.add(res);
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
