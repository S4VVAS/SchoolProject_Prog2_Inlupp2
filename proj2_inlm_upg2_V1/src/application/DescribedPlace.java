package application;

public class DescribedPlace extends Place {
	
	String description;

	public DescribedPlace(String name,double x, double y, String description) {
		super(name, x, y);
		this.description = description;
	}
	
	public DescribedPlace(String category, String name, double x, double y, String description) {
		super(name, category, x, y);
		this.description = description;
	}
}
