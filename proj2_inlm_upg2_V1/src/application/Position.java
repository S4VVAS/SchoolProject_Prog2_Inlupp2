package application;

public class Position {

	private double xCord, yCord;
	private int hashMultiplier;

	public Position(double x, double y) {
		xCord = x;
		yCord = y;
		hashMultiplier = getHashMult();
	}
	
	private int getHashMult() {
		int lengthX = String.valueOf(xCord).length() - 1;
		int lengthY = String.valueOf(yCord).length() - 1;
		int multiplier = 1;
		
		if(lengthX > lengthY)
			for(int i = 1; i < lengthX; i++) 
				multiplier = multiplier * 10;
		else
			for(int i = 1; i < lengthY; i++) 
				multiplier = multiplier * 10;
		return multiplier;
	}

	public double getX() {
		return xCord;
	}

	public double getY() {
		return yCord;
	}
	
	@Override
	public int hashCode() {
		return (int) ((xCord * hashMultiplier) + yCord);
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
