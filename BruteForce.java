import java.math.BigInteger;
import java.util.ArrayList;

import static java.lang.System.gc;

public class BruteForce {

	private ArrayList<City> cityList = new ArrayList();
	private ArrayList<Integer> _cityList = new ArrayList();
	public static ArrayList<Distance> distances = new ArrayList<>();
	public static ArrayList<String> distancesHash = new ArrayList();


	public BruteForce() {}

	public BruteForce (ArrayList<City> _cityList) {
		this.cityList = _cityList;
		for (int c = 0; c < this.cityList.size(); c++) {
			this._cityList.add(this.cityList.get(c).getIndex());
		}
		setDistances();
	}

	/*
	Sets the distance between all the cities
 	*/
	public void setDistances() {
		try {
			distances.clear();
			distances.trimToSize();
			distancesHash.clear();
			distancesHash.trimToSize();
			for (City c1 : cityList) {
				for (City c2 : cityList) {
					if (c1 != c2) {
						distances.add(new Distance(c1.getIndex(), c2.getIndex(),
								Utilities.getEuclideanDistance(c1.getX(), c1.getY(), c2.getX(), c2.getY())));
						distancesHash.add (c1.getIndex() + "|" + c2.getIndex());
					}
				}
			}
		} catch (Exception e) {
			System.out.println("BruteForce.setDistances - An error has occurred - " + e.getMessage());
			e.printStackTrace();
		} finally {
			gc();
		}
	}

	/*
	Retrieves the distance between two cities from the distances ArrayList
	*/
	public double getDistance(int from, int to) {
		try {
			/* This finds the element in distances using distancesHash.indexOf  */
			int result = distancesHash.indexOf(String.valueOf(from).trim() + "|" + String.valueOf(to).trim());
			if (result != -1) {
				return distances.get(result).getDistance();
			}
			return 0;

		} catch (Exception e) {
			System.out.println("BruteForce.getDistance - An error has occurred - " + e.getMessage());
			e.printStackTrace();
			return 0;
		} finally {
			gc();
		}
	}

	/*
	Calculates the distance of a whole 'journey'
	 */
	public double getJourneyDistance(ArrayList<Integer> journey) {
		double output = 0;
		try {
			for (int v = 1; v < journey.size(); v++) {
				output += getDistance(journey.get(v - 1), journey.get(v));
			}
		} catch (Exception e) {
			System.out.println("BruteForce.getJourneyDistance - An error has occurred - " + e.getMessage());
			e.printStackTrace();
			return 0;
		} finally {
			gc();
			return output;
		}
	}

	/*
	Swaps two elements of an Integer Array with each other
	 */
	private void swap(int[] input, int e1, int e2) {
		int tmp = input[e1];
		input[e1] = input[e2];
		input[e2] = tmp;
	}

	/*
	Solve the TSP using Brute Force. ArrayList _cityList must contain the list of
	cities read from the training/test file in order.
	 */
	public ArrayList<Integer> solveBruteForce () {
		ArrayList<Integer> solution = new ArrayList<>();
		ArrayList<Integer> nextJourney = new ArrayList<>();

		// Set up the initial solution (all cities in order, including the start and end cities)
		for (int s = 0; s < this.cityList.size(); s++) {
			solution.add(this.cityList.get(s).getIndex());
		}
		solution.add(this.cityList.get(0).getIndex());
		double shortestDistance = getJourneyDistance(solution);

		// Set up an Integer array containing the cities excluding the stand and end cities
		int[] input = new int[solution.size() - 2];
		for (int i = 0; i < (solution.size() - 2); i++) {
			input[i] = solution.get(i + 1).intValue();
		}

		// Initialise an integer array for the (number of cities - 1), since city 1 will always start
		int[] sequence = new int[this._cityList.size() - 1];
		int i;
		BigInteger p = BigInteger.ONE;
		BigInteger maxPerms = factorial(this.cityList.size() - 1);

		// Loop through all permutations to generate all possible routes
		// Use iterations to swap array elements
		i = 1; // The start and end points will be skipped

		while (i < sequence.length) {
			if (sequence[i] < i) {
				swap (input, i % 2 == 0 ? 0 : sequence[i], i);
				if (p.compareTo(factorial(this.cityList.size() - 1)) <= 0) {
					if (p.mod(BigInteger.valueOf(1000)) == BigInteger.valueOf(0)) {System.out.println ("Journey " + p.toString() + " of " + factorial(this.cityList.size() - 1));}
					nextJourney.clear();
					nextJourney.trimToSize();
					// Populate nextJourney with the newly-generated route
					nextJourney.add(this.cityList.get(0).getIndex());	// Start location
					for (int j = 0; j < (this._cityList.size() - 1); j++) {
						nextJourney.add(input[j]);
					}
					nextJourney.add(this.cityList.get(0).getIndex());	// End location
					// Check if the  new journey is a shorter route that the solution route
					if (getJourneyDistance(nextJourney) < shortestDistance) {
						solution.clear();
						solution.trimToSize();
						for (int s = 0; s < nextJourney.size(); s++) {
							solution.add(nextJourney.get(s).intValue());
						}
						shortestDistance = getJourneyDistance(solution);
					}
				}
				p = p.add(BigInteger.ONE);
				sequence[i]++;
				i = 0;
			} else {
				sequence[i] = 0;
				i++;
			}
		}
		return solution;
	}

	/*
	Calculate factorial
	 */
	private BigInteger factorial (int num) {
		BigInteger factorial = BigInteger.ONE;

		for (int f = 1; f <= num; f++) {
			factorial = factorial.multiply(BigInteger.valueOf(f));
		}
		return factorial;
	}

}
