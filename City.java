import java.util.ArrayList;

public class City {

	private boolean isStart = false;
	private int index;
	private int x;
	private int y;

	// Default constructor
	public City () {}

	public City (int index, int x, int y) {
		this.index = index;
		this.x = x;
		this.y = y;
	}

	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getX() {
		return this.x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return this.y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean start) {
		isStart = start;
	}

	// Returns the Euclidean distance from this city ot the one passed as an argument
	public double distanceToCity (City toCity) {
		try {
			return Utilities.getEuclideanDistance(getX(), getY(), toCity.getX(), toCity.getY());
		} catch (Exception e) {
			System.out.println("An error has occurred - " + e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}
}
