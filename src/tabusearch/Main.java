package tabusearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Main class of our project in which we apply tabu search to the Job Shop
 * Scheduling Problem.
 * 
 * This class includes methods to generate the results as mentioned in the final
 * paper/presentation.
 * 
 * @author Thiebout Dewitte
 * @version 1.0
 */
public class Main {

	/**
	 * Main method
	 */
	public static void main(String args[]) throws FileNotFoundException {
		opendeurdagKulak();
	}

	public static void opendeurdagKulak() {
		Problem p = Parser
				.parseInstance("/Users/thieboutdewitte/Documents/Kulak/Bach2/GegevensstructEnAlg/Project/Implementation/JobShopScheduling/TestInstanceDewitte.txt");
		System.out.println(p);
		Solution s = TabuSearch.getInitialSolution(p);
		System.out.println(s);
		
		Solution optS = TabuSearch.tabuSearch(p);
		System.out.println(optS);
	}

	/**
	 * Tabu search one test instance.
	 */
	public static void tabuSearchOneTestInstance(String path) {
		Problem p = Parser.parseInstance(path);
		Solution s = TabuSearch.tabuSearch(p);
		System.out.println(s.printSolution());
	}

	/**
	 * Generate the content for table 1 in the final report.
	 * 
	 * It takes as input all testinstances in .txt files in a directory. The
	 * output will be written to a file, instead of on the console. This file
	 * can be put into a table environment in LaTeX.
	 */
	public static void makeTable1() throws FileNotFoundException {

		File dir = new File("testinstances");
		File[] directoryListing = dir.listFiles();
		PrintStream out = new PrintStream(new FileOutputStream("table1.txt"));
		System.setOut(out);

		if (directoryListing != null) {
			for (File file : directoryListing) {
				Solution s, s2, s3, s4, s5;
				Problem p = Parser.parseInstance(file.toString());
				Problem p2 = Parser.parseInstance(file.toString());
				Problem p3 = Parser.parseInstance(file.toString());
				Problem p4 = Parser.parseInstance(file.toString());
				Problem p5 = Parser.parseInstance(file.toString());
				try {
					long startTime = System.nanoTime();
					s = TabuSearch.getInitialSolution(p);
					s2 = TabuSearch.getInitialSolution(p2);
					s3 = TabuSearch.getInitialSolution(p3);
					s4 = TabuSearch.getInitialSolution(p4);
					s5 = TabuSearch.getInitialSolution(p5);
					long elapsedTimeNano = (System.nanoTime() - startTime); // in
																			// nanoseconds
					double elapsedTime = ((double) elapsedTimeNano) / 1E9;
					float min = Float.min(s.getCost(), s2.getCost());
					min = Float.min(min, s3.getCost());
					min = Float.min(min, s4.getCost());
					min = Float.min(min, s5.getCost());

					startTime = System.nanoTime();
					s = TabuSearch.getInitialSolutionOnlyLeft(p);
					s2 = TabuSearch.getInitialSolutionOnlyLeft(p2);
					s3 = TabuSearch.getInitialSolutionOnlyLeft(p3);
					s4 = TabuSearch.getInitialSolutionOnlyLeft(p4);
					s5 = TabuSearch.getInitialSolutionOnlyLeft(p5);
					long elapsedTime2Nano = (System.nanoTime() - startTime); // in
																				// nanoseconds
					double elapsedTime2 = ((double) elapsedTime2Nano) / 1E9;
					float min2 = Float.min(s.getCost(), s2.getCost());
					min2 = Float.min(min2, s3.getCost());
					min2 = Float.min(min2, s4.getCost());
					min2 = Float.min(min2, s5.getCost());

					System.out.println(file.toString().replace(".txt", "")
							+ " & " + p.getNumberOfJobs() + " & "
							+ p.getNumberOfMachines() + " & "
							+ Math.round(p.getOptimalCost()) + " & "
							+ Math.round(min) + " & " + elapsedTime + " & "
							+ Math.round(min2) + " & " + elapsedTime2 + "\\\\");
				} catch (Exception e) {
					System.out.println("error");

				}
			}
		}
	}

	/**
	 * Generate the content for table 2 in the final report.
	 * 
	 * It takes as input all testinstances in .txt files in a directory. The
	 * output will be written to a file, instead of on the console. This file
	 * can be put into a table environment in LaTeX.
	 */
	public static void makeTable2() throws FileNotFoundException {
		File dir = new File("testinstances");
		File[] directoryListing = dir.listFiles();

		PrintStream out = new PrintStream(new FileOutputStream("table2.txt"));
		System.setOut(out);

		if (directoryListing != null) {
			String res = "";
			for (File file : directoryListing) {
				try {
					float best = Integer.MAX_VALUE;
					double longestTime = Double.MIN_VALUE;
					float sumCosts = 0;
					double sumTimes = 0;

					Problem p = Parser.parseInstance(file.toString());
					float optimum = p.getOptimalCost();
					String name = file.toString().replace(".txt", "");

					res = name.replace("testinstances/", "") + " & "
							+ p.getNumberOfJobs() + " & "
							+ p.getNumberOfMachines() + " & "
							+ Math.round(p.getOptimalCost());

					for (int i = 0; i < 5; i++) {
						long startTime = System.nanoTime();
						Solution s = TabuSearch.tabuSearch(p);
						long elapsedTimeNano = (System.nanoTime() - startTime);
						double elapsedTime = ((double) elapsedTimeNano) / 1E9;
						float c = s.getCost();

						if (c < best)
							best = c;
						if (elapsedTime > longestTime) {
							longestTime = elapsedTime;
						}
						sumCosts += c;
						sumTimes += elapsedTime;

					}

					res += " & " + Math.round(best);
					if (optimum != -1) {
						float delta = best - optimum;
						res += " & " + delta / optimum * 100; // Delta Z %
					}

					res += " & " + sumCosts / 5; // Z_av
					res += " & " + sumTimes / 5; // T_av
					res += " & " + longestTime; // T_max
					res += " \\\\"; // new line in LaTeX table
				} catch (Exception e) {
				} finally {
					System.out.println(res);
				}
			}
		}
	}

	/**
	 * Tabu search the instance used in the final presentation.
	 */
	public static void finalPresentationInstance() {
		Problem p = Parser
				.parseInstance("/Users/thieboutdewitte/Documents/Kulak/Bach2/GegevensstructEnAlg/Project/Implementation/JobShopScheduling/TestInstanceDewitte.txt");
		System.out.println(p);
		Solution s = TabuSearch.getInitialSolution(p);
		System.out.println(s);
		Solution optS = TabuSearch.tabuSearch(p);
		System.out.println(optS);
	}

}
