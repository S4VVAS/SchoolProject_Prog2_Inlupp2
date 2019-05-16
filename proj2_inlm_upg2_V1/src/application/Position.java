package application;

public class Position {

	private double xCord, yCord;

	public Position(double x, double y) {
		xCord = x;
		yCord = y;
	}

	public double getX() {
		return xCord;
	}

	public double getY() {
		return yCord;
	}
	
	@Override
	public int hashCode() {
		return (int) ((xCord * 1000000) + yCord);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else if (!(obj instanceof Position) || obj == null)
			return false;
		return xCord == ((Position)obj).xCord && yCord == ((Position)obj).yCord ? true : false;
	}
}
