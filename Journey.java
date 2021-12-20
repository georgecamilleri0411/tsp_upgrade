import java.util.ArrayList;
import java.util.Collections;

public class Journey {

	// Holds our tour of cities
	private ArrayList<GA_City> journey = new ArrayList();

	private double fitness = 0;
	private double distance = 0;

	/*
	Creates an instance of Journey with all cities in the journey ArrayList set to null
	 */
	public Journey() {
		for (int i = 0; i < JourneyManager.numberOfGA_Cities(); i++) {
			journey.add(null);
		}
	}

	public Journey (ArrayList journey) {
		this.journey = journey;
	}

	/*
	Generates a journey and if required, randomises it.
	 */
	public void generateJourney (boolean randomiseOrder) {
		// Loop through all cities and add them to the journey
		for (int c = 0; c < JourneyManager.numberOfGA_Cities(); c++) {
			setCity(c, JourneyManager.getGA_City(c));
		}
		// Randomise the order of this journey?
		if (randomiseOrder) {
			Collections.shuffle(journey);
		}
	}

	/*
	Retrieves the city at the specified index in the journey ArrayList
	 */
	public GA_City getCity (int journeyPosition) {
		return (GA_City) journey.get(journeyPosition);
	}

	/*
	Saves an instance of a city at the specified index in the journey ArrayList
	 */
	public void setCity (int journeyPosition, GA_City city) {
		journey.set (journeyPosition, city);
		// Reset the fitness and distance properties
		fitness = 0;
		distance = 0;
	}

	/*
	Calculates the fitness level of this journey, according to the total distance.
	In order to enforce a logical value (i.e. a shorter distance produces a higher
	fitness value), the result is inverted (1/distance).
	 */
	public double getJourneyFitness () {
		if (fitness == 0) {
			fitness = (1 / getJourneyDistance());
		}
		return fitness;
	}

	/*
	Calculates and returns the journey distance
	 */
	public double getJourneyDistance () {
		if (distance == 0) {
			double journeyDistance = 0;
			// Loop through the cities in this journey
			for (int c = 0; c < journeySize(); c++) {
				GA_City fromCity = getCity(c);
				GA_City destinationCity;
				// In the final iteration, the final city will be set to the starting/ending city
				if (c + 1 < journeySize()){
					destinationCity = getCity(c + 1);
				} else {
					destinationCity = getCity(0);
				}
				// Get the distance between the two cities
				journeyDistance += fromCity.distanceTo(destinationCity);
			}
			distance = journeyDistance;
		}
		return distance;
	}

	/*
	Retrieves the number of cities in this journey
	 */
	public int journeySize () {
		return journey.size();
	}

	/*
	Returns True is the city specified already exists in this journey. Otherwise, returns False.
	 */
	public boolean cityExists (GA_City city) {
		return journey.contains(city);
	}

	@Override
	public String toString () {
		String output = "";
		for (int s = 0; s < journeySize(); s++) {
			output += getCity(s);
			if (s < (journeySize() - 1)) {
				output += " >> ";
			}
		}
		output += "Total distance: " + distance;
		return output;
	}

}
