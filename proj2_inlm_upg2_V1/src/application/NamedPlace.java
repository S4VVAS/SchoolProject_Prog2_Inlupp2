package application;

public class NamedPlace extends Place{
	
	public NamedPlace(String name, double x, double y) {
		super(name, x, y);
	}
	
	public NamedPlace(String category, String name, double x, double y) {
		super(name, category, x, y);
	}
}
