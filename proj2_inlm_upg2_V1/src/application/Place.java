package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public abstract class Place {

	private double xCord, yCord;
	private String name, category;
	private Polygon marker;

	public Place(String name, String category, double x, double y) {
		this.name = name;
		this.category = category;
		xCord = x;
		yCord = y;

		switch (category.toUpperCase()) {
		case "BUS":
			setupMarker(Color.RED);
			break;
		case "TRAIN":
			setupMarker(Color.GREEN);
			break;
		case "UNDERGROUND":
			setupMarker(Color.BLUE);
			break;
		default:
			setupMarker(Color.LIGHTGRAY);
		}
	}

	public Place(String name, double x, double y) {
		this.name = name;
		xCord = x;
		yCord = y;

		setupMarker(Color.BLACK);
	}

	private void setupMarker(Color color) {
		marker = new Polygon();
		marker.getPoints().addAll(new Double[] { 0.0, 0.0, -10.0, -20.0, 10.0, -20.0 });
		marker.setFill(color);
		marker.relocate(xCord-10, yCord-20);
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}
	
	public Polygon getMarker() {
		return marker;
	}
	
	public double getX() {
		return xCord;
	}
	
	public double getY() {
		return yCord;
	}

}
