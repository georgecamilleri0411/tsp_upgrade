import java.util.ArrayList;

public class JourneyManager {

	// ArrayList to store the list of cities and their properties
	private static ArrayList<GA_City> destinationCities = new ArrayList<GA_City>();

	/*
	Adds a new City to the destinationCities ArrayList
	 */
	public static void addCity(GA_City city) {
		destinationCities.add(city);
	}

	/*
	Retrieves the city according to the index specified
	 */
	public static GA_City getGA_City(int index){
		return destinationCities.get(index);
	}

	/*
	Retrieves the number of cities in the destinationCities ArrayList
	 */
	public static int numberOfGA_Cities(){
		return destinationCities.size();
	}

}
