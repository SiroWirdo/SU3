import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class KNN {
	private int k;
	private Data trainData;
	private int voting;
	private int distMeasure;

	public KNN(int k, Data trainData, int voting, int distMeasure){
		this.k = k;
		this.trainData = trainData;
		this.voting = voting;
		this.distMeasure = distMeasure;
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

			for(int i = 0; i < trainArguments.size(); i++){
				double distance;
				if(distMeasure == 1){
			//		System.out.println("Eucl");
					distance = euclDistance(trainArguments.get(i), testArg);
				}else{
		//			System.out.println("Manhatta");
					distance = manhattanDistance(trainArguments.get(i), testArg);
				}
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

			if(voting != 2){
				HashMap<String, Double> bestClasses = new HashMap<String, Double>();
				for(Map.Entry<Double, Integer> entry : nearestNeigh.entrySet()){
					String classVal = trainData.getClasses().get(entry.getValue());
					double addVal = 1;

					if(voting == 1){
						System.out.println(entry.getKey());
						addVal = 1.0/entry.getKey();
					}

					if(bestClasses.containsKey(classVal)){
						bestClasses.put(classVal, bestClasses.get(classVal) + addVal);
					}else{
						bestClasses.put(classVal, addVal);
					}
				}

				String bestClass = "";
				double bestVal = 0;
				for(Map.Entry<String, Double> entry : bestClasses.entrySet()){
					if(entry.getValue() > bestVal){
						bestClass = entry.getKey();
						bestVal = entry.getValue();
					}
				}
				String[] res = {testClasses.get(j),bestClass};
				results.add(res);
				
			}else{
			//	System.out.println("Random");
				ArrayList<String> bestClasses = new ArrayList<String>();
				for(Map.Entry<Double, Integer> entry : nearestNeigh.entrySet()){
					String classVal = trainData.getClasses().get(entry.getValue());
					bestClasses.add(classVal);
				}
				
				Random rand = new Random();
				int index = rand.nextInt(bestClasses.size());
				
				String[] res = {testClasses.get(j),bestClasses.get(index)};
				results.add(res);
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

	private double manhattanDistance(String[] trainArg, String[] testArg){
		double distance = 0.0;

		for(int i = 0; i < trainArg.length; i++){
			distance += Math.abs(Double.parseDouble(trainArg[i]) - Double.parseDouble(testArg[i]));
		}

		return distance;
	}
}
