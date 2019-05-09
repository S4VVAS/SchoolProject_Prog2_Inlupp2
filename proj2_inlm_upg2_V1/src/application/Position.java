package application;

public class Position {

	private double xCord, yCord;
	private Place place;
	private String key;

	public Position(double x, double y, Place place) {
		xCord = x;
		yCord = y;
		this.place = place;
		key = Double.toString(x) + Double.toString(y);
	}

	public double getX() {
		return xCord;
	}

	public double getY() {
		return yCord;
	}

	public Place getPlace() {
		return place;
	}
	
	public String getKey() {
		return key;
	}

}
