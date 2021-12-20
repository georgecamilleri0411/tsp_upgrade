import java.lang.System;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class tsp_upgrade {

	public static void main(String[] args) {

		displayMenu(true);

	}

	private static void displayMenu(boolean displayChoices) {

		long start = 0;

		if (displayChoices) {
			System.out.println ("\n            TSP - Console Menu\n-------------------------------------------\n");
			System.out.println ("A: Load file");
			System.out.println ("B: Generate all permutations");
			System.out.println ("C: Solve TSP using Brute Force (exhaustive)");
			System.out.println ("D: Solve TSP using Best First Search (BeFS)");
			System.out.println ("E: Solve TSP using 2-Way Best First Search (BeFS)");
			System.out.println ("G: Solve TSP using Genetic Algorithm (GA)");
			System.out.println ("\n-------------------------------------------\n");
			System.out.println ("X: Exit");
		}
		System.out.println ("\nPlease make your choice: ");

		Scanner myScanner = new Scanner (System.in);
		String userInput = myScanner.nextLine();

		switch (userInput.toUpperCase()) {
			case "A":	// Load file
				// Clear the distances and distancesHash ArrayLists
				Utilities.distances.clear();
				Utilities.distancesHash.clear();

				System.out.println ("Please enter the filename you wish to load: ");
				String fileName = myScanner.nextLine();
				start = System.nanoTime();
				try {
					FileReader.readFile("files/" + fileName);
					if (Utilities.cities.size() > 0) {
						System.out.println ("File " + fileName + " has been loaded. "
								+ Utilities.cities.size() + " localities loaded.");
					}
				} catch (Exception e) {
					System.out.println ("tsp.displayMenu - an error has occurred." + e.getMessage());
					displayMenu(false);
				}
				long readFileNanoTime = (System.nanoTime() - start);
				// Display the time taken
				displayNanoTime(readFileNanoTime);
				// Bring up the menu
				displayMenu(false);
				break;

			case "B":	// Generate permutations
				// Display a warning to the user if the number of cities exceeds 12
				if (Utilities.cities.size() > 12) {
					System.out.println ("There are too many cities to be able to generate and store all permutations!");
				} else {
					// Create an integer array with the city indexes (numbers)
					int[] sequence = new int[Utilities.cities.size() - 1];

					// Populate the Integer array with the city indexes, skipping the first city
					int n = 0;
					for (int c = 0; c < Utilities.cities.size(); c++) {
						if (Utilities.cities.get(c).getIndex() != 1) {
							sequence[n] = Utilities.cities.get(c).getIndex();
							n++;
						}
					}

					start = System.nanoTime();
					Utilities.generatePermutations(sequence, false);
					long generatePermutationsNanoTime = (System.nanoTime() - start);
					System.out.println (Utilities.permutations.length + " permutations generated.");

					// Display the time taken
					displayNanoTime(generatePermutationsNanoTime);

				}
				// Bring up the menu
				displayMenu(false);
				break;

			case "C":	// Solve the TSP using Brute Force
				BruteForce bf = new BruteForce(Utilities.cities);
				start = System.nanoTime();
				ArrayList<Integer> bfSolution = bf.solveBruteForce();
				long bfTime = (System.nanoTime() - start);

				System.out.println ("Solution found!");
				for (int s = 0; s < bfSolution.size(); s++) {
					System.out.print (bfSolution.get(s).toString());
					if (s < (bfSolution.size() -1)) {
						System.out.print (" > ");
					} else {
						System.out.println (" :: Total distance: " + bf.getJourneyDistance((bfSolution)));
					}
				}

				// Display the time taken
				displayNanoTime(bfTime);

				// Bring up the menu
				displayMenu(false);
				break;

			case "D":	// Solve TSP using Greedy Best First Search (BeFS)
				ArrayList<Integer> input = new ArrayList();
				for (int l = 0; l < Utilities.cities.size(); l++) {
					input.add(Utilities.cities.get(l).getIndex());
				}
				start = System.nanoTime();
				ArrayList<Integer> output = Utilities.solveTSP_GreedyBeFS(input);
				long befsNanoTime = (System.nanoTime() - start);

				// Display the answer
				for (int o = 0; o < output.size(); o++) {
					System.out.print (output.get(o));
					if (o < (output.size() - 1)) {
						System.out.print (" >> ");
					} else {
						System.out.println (". Path found using Best First Search (Greedy Best Neighbour). ");
						System.out.println ("Total distance: " + Utilities.getVoyageDistance(output));
					}
				}
				// Display the time taken
				displayNanoTime(befsNanoTime);
				// Bring up the menu
				displayMenu(false);

				break;

			case "E":	// Solve TSP using a 2-way Greedy Best First search algorithm
				BestFirst_TwoWay bf2W = new BestFirst_TwoWay(Utilities.cities);
				start = System.nanoTime();
				ArrayList<Integer> bf2WSolution = bf2W.solveBestFirst_TwoWay();
				long bf2WTime = (System.nanoTime() - start);

				// Display the answer
				for (int o = 0; o < bf2WSolution.size(); o++) {
					System.out.print (bf2WSolution.get(o));
					if (o < (bf2WSolution.size() - 1)) {
						System.out.print (" >> ");
					} else {
						System.out.println (". Path found using 2-Way Best First Search (Greedy Best Neighbour). ");
						System.out.println ("Total distance: " + Utilities.getVoyageDistance(bf2WSolution));
					}
				}
				// Display the time taken
				displayNanoTime(bf2WTime);
				// Display the menu
				displayMenu(false);
				break;

			case "G":	// Solve TSP using a Genetic Algorithm

				int popSize = 50;
				int genSize = 15;

				System.out.println ("Please enter the number of generations you with to evolve the journeys: ");
				String gen = myScanner.nextLine();

				while (!Utilities.isNumeric(gen)) {
					System.out.println ("Please enter the number of generations you with to evolve the journeys: ");
					gen = myScanner.nextLine();
				}
				genSize = Integer.parseInt(gen);

				start = System.nanoTime();
				// Add the cities to the Journey Master
				for (int c = 0; c < Utilities.cities.size(); c++) {
					JourneyManager.addCity
							(new GA_City(Utilities.cities.get(c).getX(), Utilities.cities.get(c).getY(), Utilities.cities.get(c).getIndex()));
				}

				// Initialise the population
				Population myPop = new Population(popSize, true);
				System.out.println ("First population created. Best distance: " + myPop.getFittestJourney().getJourneyDistance());

				// Evolve the population for the number of generations required.
				myPop = GA.evolvePopulation(myPop);
				for (int e = 0; e < genSize; e++) {
					myPop = GA.evolvePopulation(myPop);
				}
				long gaNanoTime = (System.nanoTime() - start);

				System.out.println ("Evolution of " + genSize + " generations ready.");
				System.out.println ("Best distance: " + myPop.getFittestJourney().getJourneyDistance());
				System.out.print ("Best journey: ");
				for (int j = 0; j < myPop.getFittestJourney().journeySize(); j++) {
					System.out.print (myPop.getFittestJourney().getCity(j).getIdx() + " >> ");
				}
				System.out.println (myPop.getFittestJourney().getCity(0).getIdx());

				// Display the time taken
				displayNanoTime(gaNanoTime);
				// Bring up the menu
				displayMenu(false);

				break;

			case "X":	// Exit
				System.exit(0);
				break;

			default:
				System.out.println ("Invalid input. Please try again: ");
				displayMenu(false);
				break;
		}
	}

	private static void displayNanoTime (long timeSpan) {
		DecimalFormat formatNS = new DecimalFormat("###.######");
		formatNS.setRoundingMode(RoundingMode.CEILING);
		System.out.println ("\nTime taken: " + formatNS.format(timeSpan * 0.000000001)
				+ " seconds (" + timeSpan + " nano seconds).");
	}

}
