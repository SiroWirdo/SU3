import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {

	public static void main(String[] args){

		int folds = 10;
		int[] crossval = {3, 5, 10};
		String name = "wheather";
		int[] k = {3,5,7,9};
		int[] vote = {0,1,2};
		int[] measure = {1,2};
		String sourcePath = "c:/Studia/Systemy ucz鉍e sie v2/" + name + "/" + name + ".data";
		String sourcePath2 = "c:/Studia/Systemy ucz鉍e sie v2/" + name + "/" + name + ".names";

		DataSource dataSource = new DataSource(sourcePath, sourcePath2);
		Data data = dataSource.getData();
		//data.printData();
		DataSet dataset = new DataSet(data, folds);
		//	data.printUniqArguments();
		double[] finResults = {0.0, 0.0, 0.0, 0.0};

		PrintWriter writer;

		try {
			String path = "c:/Studia/Systemy ucz鉍e sie v2/zad5/";

			writer = new PrintWriter(path + name + ".csv", "UTF-8");
			writer.println("Accuracy;Precision;Recall;FScore;k");
			for(int k1 : k){

				for(int i = 0; i < folds; i++){
					System.out.println("-------" + i + "-------");
					Data train = dataset.getTrainingData();
					//	train.printUniqArguments();
					Data test = dataset.getTestData();

					/* parametr 3: 1 - gloowanie z waga, 2 - losowe g這sowanie
					 * parametr 4: 1 - miara euclidesowa, inna wartosc miara manhattan
					 */
					KNN knn = new KNN(k1, train, 0, 1);

					System.out.println("-------TEST-------");
					ArrayList<String[]> results = knn.testKNN(test);

					Results result = new Results();

					double[] fin = result.countTF(test.getUniqClasses(), results);

					for(int j = 0; j < finResults.length; j++){
						finResults[j] += fin[j];
					}

					dataset.nextFold();
				}

				for(int j = 0; j < finResults.length; j++){
					finResults[j] /= folds;
				}
				
				
				printResults(writer, finResults, k1);

				System.out.println("==========FINAL AVERAGE===========");
				System.out.println("Accuracy = " + finResults[0] + " Precision = " + finResults[1] + " Recall = " + finResults[2] + " FScore " + finResults[3]);
				finResults[0] = 0.0;
				finResults[1] = 0.0;
				finResults[2] = 0.0;
				finResults[3] = 0.0;
			}
			writer.println();
			/**** DRUGI PARAMETR *****/
			
			writer.println("Accuracy;Precision;Recall;FScore;vote");
			for(int vot : vote){

				for(int i = 0; i < folds; i++){
					System.out.println("-------" + i + "-------");
					Data train = dataset.getTrainingData();
					//	train.printUniqArguments();
					Data test = dataset.getTestData();

					/* parametr 3: 1 - gloowanie z waga, 2 - losowe g這sowanie
					 * parametr 4: 1 - miara euclidesowa, inna wartosc miara manhattan
					 */
					KNN knn = new KNN(5, train, vot, 1);

					System.out.println("-------TEST-------");
					ArrayList<String[]> results = knn.testKNN(test);

					Results result = new Results();

					double[] fin = result.countTF(test.getUniqClasses(), results);

					for(int j = 0; j < finResults.length; j++){
						finResults[j] += fin[j];
					}

					dataset.nextFold();
				}

				for(int j = 0; j < finResults.length; j++){
					finResults[j] /= folds;
				}
				
				
				printResults(writer, finResults, vot);

				System.out.println("==========FINAL AVERAGE===========");
				System.out.println("Accuracy = " + finResults[0] + " Precision = " + finResults[1] + " Recall = " + finResults[2] + " FScore " + finResults[3]);
				finResults[0] = 0.0;
				finResults[1] = 0.0;
				finResults[2] = 0.0;
				finResults[3] = 0.0;
			}
			
			writer.println();
			/* TRZECI PARAMETR */
			
			writer.println("Accuracy;Precision;Recall;FScore;measure");
			for(int mes : measure){

				for(int i = 0; i < folds; i++){
					System.out.println("-------" + i + "-------");
					Data train = dataset.getTrainingData();
					//	train.printUniqArguments();
					Data test = dataset.getTestData();

					/* parametr 3: 1 - gloowanie z waga, 2 - losowe g這sowanie
					 * parametr 4: 1 - miara euclidesowa, inna wartosc miara manhattan
					 */
					KNN knn = new KNN(5, train, 0, mes);

					System.out.println("-------TEST-------");
					ArrayList<String[]> results = knn.testKNN(test);

					Results result = new Results();

					double[] fin = result.countTF(test.getUniqClasses(), results);

					for(int j = 0; j < finResults.length; j++){
						finResults[j] += fin[j];
					}

					dataset.nextFold();
				}

				for(int j = 0; j < finResults.length; j++){
					finResults[j] /= folds;
				}
				
				
				printResults(writer, finResults, mes);

				System.out.println("==========FINAL AVERAGE===========");
				System.out.println("Accuracy = " + finResults[0] + " Precision = " + finResults[1] + " Recall = " + finResults[2] + " FScore " + finResults[3]);
				finResults[0] = 0.0;
				finResults[1] = 0.0;
				finResults[2] = 0.0;
				finResults[3] = 0.0;
			}
			
			writer.println();
			/* KROSWALIDACJA PARAMETR */
			
			writer.println("Accuracy;Precision;Recall;FScore;crossval");
			for(int cross : crossval){
				 dataSource = new DataSource(sourcePath, sourcePath2);
				 data = dataSource.getData();
				folds = cross;
				dataset = new DataSet(data, folds);
				for(int i = 0; i < folds; i++){
					System.out.println("-------" + i + "-------");
					Data train = dataset.getTrainingData();
					//	train.printUniqArguments();
					Data test = dataset.getTestData();

					/* parametr 3: 1 - gloowanie z waga, 2 - losowe g這sowanie
					 * parametr 4: 1 - miara euclidesowa, inna wartosc miara manhattan
					 */
					KNN knn = new KNN(5, train, 0, 1);

					System.out.println("-------TEST-------");
					ArrayList<String[]> results = knn.testKNN(test);

					Results result = new Results();

					double[] fin = result.countTF(test.getUniqClasses(), results);

					for(int j = 0; j < finResults.length; j++){
						finResults[j] += fin[j];
					}

					dataset.nextFold();
				}

				for(int j = 0; j < finResults.length; j++){
					finResults[j] /= folds;
				}
				
				
				printResults(writer, finResults, cross);

				System.out.println("==========FINAL AVERAGE===========");
				System.out.println("Accuracy = " + finResults[0] + " Precision = " + finResults[1] + " Recall = " + finResults[2] + " FScore " + finResults[3]);
				finResults[0] = 0.0;
				finResults[1] = 0.0;
				finResults[2] = 0.0;
				finResults[3] = 0.0;
			}
		
			writer.close();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*PrintWriter writer;
		try {
			Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
			String path = "c:/Studia/Systemy ucz鉍e sie v2/zad2/";
			String nameRes = name + "_" +sdf.format(cal.getTime()) + "_" + folds + "_" + width + "_" + sectSize + ".csv";

			writer = new PrintWriter(path + nameRes, "UTF-8");
			writer.println("Accuracy;Precision;Recall;FScore");

			String acc = Double.toString(finResults[0]).replaceFirst("\\.", ",");
			String prec = Double.toString(finResults[1]).replaceFirst("\\.", ",");
			String recall = Double.toString(finResults[2]).replaceFirst("\\.", ",");
			String fscr = Double.toString(finResults[3]).replaceFirst("\\.", ",");

			writer.println(acc + ";" + prec + ";" + recall + ";" + fscr);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/


	}

	private static void printResults(PrintWriter writer, double[] results, int parVal){
		String acc = Double.toString(results[0]).replaceFirst("\\.", ",");
		String prec = Double.toString(results[1]).replaceFirst("\\.", ",");
		String recall = Double.toString(results[2]).replaceFirst("\\.", ",");
		String fscr = Double.toString(results[3]).replaceFirst("\\.", ",");

		writer.println(acc + ";" + prec + ";" + recall + ";" + fscr + ";" + parVal);
	}
}
