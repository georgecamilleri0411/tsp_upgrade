import java.util.ArrayList;

import static java.lang.System.gc;

public class BestFirst_TwoWay {

	private ArrayList<Integer> _solution = new ArrayList<>();
	private ArrayList<City> cityList = new ArrayList();

	// ArrayLists to store the distances between each city. 2nd ArrayList stores a 'hash' to aid performance
	public static ArrayList<Distance> distances = new ArrayList<>();
	public static ArrayList<String> distancesHash = new ArrayList();

	public BestFirst_TwoWay (ArrayList<City> _cityList) {
		this.cityList = _cityList;
		setDistances();
		// Populate the solution ArrayList with the starting and ending points.
		// Middle elements will be populated with 0.
		_solution.add(this.cityList.get(0).getIndex());
		for (int c = 1; c < (this.cityList.size()); c++) {
			_solution.add(0);
		}
		_solution.add(this.cityList.get(0).getIndex());
	}

	public ArrayList<Integer> solveBestFirst_TwoWay() {

		boolean leftSide = true;
		int nextElement = 0;

		while (this._solution.contains(0)) {
			if (leftSide) {
				// Populate the next city on the left-hand side of the ArrayList
				nextElement = this._solution.indexOf(0);
				this._solution.set(nextElement, getBestNeighbour(this._solution.get(nextElement - 1)));
			} else {
				// Populate the next city on the right-hand side of the ArrayList
				nextElement = this._solution.lastIndexOf(0);
				this._solution.set(nextElement, getBestNeighbour(this._solution.get(nextElement + 1)));
			}
			leftSide = !leftSide;
		}
		return this._solution;
	}

	/*
	Finds the best neighbour to the city passed as input
 	*/
	public int getBestNeighbour(int _fromCity) {

		int bn = 0;
		double closestDistance = Double.MAX_VALUE;
		double currentDistance = 0;
		City fromCity = new City();
		City toCity;

		// Find the fromCity
		boolean found = false;
		int i = 0;
		while (!found) {
			if (this.cityList.get(i).getIndex() == _fromCity) {
				found = true;
				fromCity = this.cityList.get(i);
			}
			i++;
		}
		for (int n = 0; n < this.cityList.size(); n++) {
			toCity = this.cityList.get(n);
			// Process only if it has not yet been inserted in the _solution ArrayList
			if (!this._solution.contains(toCity.getIndex())) {
				currentDistance = Utilities.getEuclideanDistance
						(fromCity.getX(), fromCity.getY(), toCity.getX(), toCity.getY());
				if (currentDistance < closestDistance) {
					bn = toCity.getIndex();
					closestDistance = currentDistance;
				}
			}
		}

		gc();
		return bn;
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
			System.out.println("BestFirst_TwoWay.getDistance - An error has occurred - " + e.getMessage());
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
			System.out.println("BestFirst_TwoWay.getJourneyDistance - An error has occurred - " + e.getMessage());
			e.printStackTrace();
			return 0;
		} finally {
			gc();
			return output;
		}
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
			System.out.println("BestFirst_TwoWay.setDistances - An error has occurred - " + e.getMessage());
			e.printStackTrace();
		} finally {
			gc();
		}
	}

}
