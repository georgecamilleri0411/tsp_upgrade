public class GA_City {

	int x;
	int y;
	int idx;

	public GA_City() {
		this.x = (int) (Math.random() * 100);
		this.y = (int) (Math.random() * 100);
		this.idx = 0;
	}

	public GA_City (int _x, int _y, int _idx) {
		this.x = _x;
		this.y = _y;
		this.idx = _idx;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getIdx() {
		return this.idx;
	}

	public double distanceTo (GA_City city) {
		return Utilities.getEuclideanDistance(this.x, this.y, city.getX(), city.getY());
	}

	@Override
	public String toString() {
		return String.valueOf(this.idx);
	}

}
