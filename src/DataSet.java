import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class DataSet {
	private Data data;
	private int crossvalCounter;
	private int folds;
	private ArrayList<Data> crossValData;
	private ArrayList<Integer> contIndex;
	private ArrayList<Integer> normalIndex;
	private ArrayList<ArrayList<String>> uniqArguments;

	public DataSet(Data data, int folds, boolean width, int sect){
		this.data = data;
		this.crossvalCounter = 0;
		this.folds = folds;
		this.crossValData = new ArrayList<Data>();
		this.contIndex = new ArrayList<Integer>();
		this.normalIndex = new ArrayList<Integer>();
		this.uniqArguments = new ArrayList<ArrayList<String>>();

		for(int i = 0; i < data.getArguments().get(0).length; i++){
			uniqArguments.add(new ArrayList<String>());
		}

		fillIndexes();
		fillUniqArguments();

	/*	if(width){
			equalWidth(sect);
		}else{
			equalFrequency(sect);
		}
		changeArgForSect();*/
		crossvalidation();

		this.data.addUniqArguments(uniqArguments);
		for(Data crossData : crossValData){
			crossData.addUniqArguments(uniqArguments);
		}
	}
	
	private void normalization(){
		ArrayList<String[]> arguments = (ArrayList<String[]>)data.getArguments().clone();
		ArrayList<ArrayList<String>> argumentList = new ArrayList<ArrayList<String>>();
		
		for(int i = 0; i < arguments.get(0).length; i++){
			argumentList.add(new ArrayList<String>());
		}
		
		for(String[] arg : arguments){
			
			for(int i = 0; i < arg.length; i++){
				argumentList.get(i).add(arg[i]);
			}
		}
		
		for(ArrayList<String> argument : argumentList){
			Collections.sort(argument);
		}
	}

	private void crossvalidation(){
		int dataSize = data.getDataSize();
		int counter = 0;

		ArrayList<String[]> arguments = (ArrayList<String[]>)data.getArguments().clone();
		ArrayList<String> classes = (ArrayList<String>)data.getClasses().clone();

		for(int i = 0; i < folds; i++){
			crossValData.add(new Data());
		}

		for(int i = 0; i < dataSize; i++){
			Random rand = new Random();
			int argument = rand.nextInt(arguments.size());

			crossValData.get(counter).addArgument(arguments.get(argument));
			crossValData.get(counter).addClass(classes.get(argument));

			arguments.remove(argument);
			classes.remove(argument);

			if(counter == crossValData.size() - 1){
				counter = 0;
			}else{
				counter++;
			}
		}
	}

	public Data getTrainingData(){
		ArrayList<String[]> trainingArg = new ArrayList<String[]>();
		ArrayList<String> trainingClass = new ArrayList<String>();

		for(int i = 0; i < folds; i++){//zmieniæ?
			if(i != crossvalCounter){
				trainingArg.addAll(crossValData.get(i).getArguments());
				trainingClass.addAll(crossValData.get(i).getClasses());

			}
		}
		//System.out.println("UNIQ "+trainingClass.size()+", "+data.getNames().size());
		//for (String s: trainingClass) System.out.println("name "+s);
		Data newTrain = new Data(trainingArg, trainingClass, data.getUniqClasses(), data.getNames(), data.getTypes());
		newTrain.addUniqArguments(uniqArguments);

		return newTrain;
	}

	public Data getTestData(){
		ArrayList<String[]> trainingArg = new ArrayList<String[]>();
		ArrayList<String> trainingClass = new ArrayList<String>();

		trainingArg = (ArrayList<String[]>)crossValData.get(crossvalCounter).getArguments().clone();
		trainingClass = (ArrayList<String>)crossValData.get(crossvalCounter).getClasses().clone();

		Data testData = new Data(trainingArg, trainingClass, data.getUniqClasses(), data.getNames(), data.getTypes());
		testData.addUniqArguments(uniqArguments);
		return testData;
	}

	private void fillIndexes(){
		ArrayList<String> types = data.getTypes();

		for(int i = 0; i < types.size(); i++){
			if(types.get(i).equals("continuous")){
				contIndex.add(i);
			}else{
				if(!types.get(i).equals("class")){
					normalIndex.add(i);
				}
			}
		}
	}

	private void fillUniqArguments(){
		ArrayList<String[]> arguments = data.getArguments();

		for(String[] argument : arguments){
			for(Integer i : normalIndex){
				if(!uniqArguments.get(i).contains(argument[i])){
					uniqArguments.get(i).add(argument[i]);
				}
			}
		}
	}

	private ArrayList<String> getSections(double sect_size, double[] minMax, int numSect){
		ArrayList<String> newSections = new ArrayList<String>();

		newSections.add("-" + minMax[0]);

		double low = 0;
		double high = 0;
		for(int i = 1; i <= numSect; i++){
			low = minMax[0] + (i - 1) * sect_size;
			high = minMax[0] + i * sect_size;

			newSections.add(low + "_" + high);
		}

		newSections.add(high + "+");

		return newSections;
	}


	public void nextFold(){
		if(crossvalCounter == folds - 1){
			crossvalCounter = 0;
		}else{
			crossvalCounter++;
		}
	}
	
	public void changeArgForSect(){
		ArrayList<String[]> arguments = data.getArguments();
		ArrayList<String> classes = data.getClasses();
		for(int i = 0; i < arguments.size(); i++){
			String[] argument = arguments.get(i);
			String className = classes.get(i);
		//	for(String arg : argument){
		//	System.out.print(arg + " ");
		//	}
		//	System.out.println(className);
			for(int index : contIndex){
				ArrayList<String> uniqArg = uniqArguments.get(index);
				int newIndex = getIndexOfSection(uniqArg, argument[index]);
				argument[index] = uniqArg.get(newIndex);
			} 
		//	for(String arg : argument){
		//		System.out.print(arg + " ");
		//		}
		//	System.out.println("*****");
		}
	}
	
	private int getIndexOfSection(ArrayList<String> sections, String argument){
		double value = Double.parseDouble(argument);

		if(value <= Double.parseDouble(sections.get(0).replace("-", ""))){
			return 0;
		}

		if(value > Double.parseDouble(sections.get(sections.size() - 1).replace("+", ""))){
			return sections.size() - 1;
		}

		for(int i = 1; i < sections.size() - 1; i++){
			String[] section = sections.get(i).split("_");

			if(value > Double.parseDouble(section[0]) && value <= Double.parseDouble(section[1])){
				return i;
			}
		}

		return -1;
	}
}
