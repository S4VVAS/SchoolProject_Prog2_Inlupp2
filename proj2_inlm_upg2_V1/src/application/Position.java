package application;

public class Position {

	private double xCord, yCord;
	private Place place;
	
	public Position(double x, double y, Place place) {
		xCord = x;
		yCord = y;
		this.place = place;
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
	
}
