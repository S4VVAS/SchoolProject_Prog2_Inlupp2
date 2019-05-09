package application;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public abstract class Place extends Polygon {

	protected final Position pos;
	protected final String name;
	protected final String category;
	private boolean isMarked = true;
	private final Color color, markedColor;

	public Place(String name, String category, double x, double y) {
		this.name = name;
		this.category = category;
		pos = new Position(x, y, this);
		this.setOnMouseClicked(new MarkerEvent());

		switch (category.toUpperCase()) {
		case "BUS":
			color = Color.DARKRED;
			markedColor = Color.RED;
			setupMarker();
			break;
		case "TRAIN":
			color = Color.GREEN;
			markedColor = Color.LIMEGREEN;
			setupMarker();
			break;
		case "UNDERGROUND":
			color = Color.DEEPSKYBLUE;
			markedColor = Color.SKYBLUE;
			setupMarker();
			break;
		default:
			color = Color.BLACK;
			markedColor = Color.DIMGRAY;
			setupMarker();
		}
	}

	private void setupMarker() {
		getPoints().addAll(new Double[] { 0.0, 0.0, -10.0, -20.0, 10.0, -20.0 });
		setFill(color);
		relocate(getX() - 10, getY() - 20);
		setMarkedProperty();
	}

	private void setMarkedProperty() {
		if (isMarked) {
			relocate(getX() - 10, getY() - 23);
			setStroke(Color.BLACK);
			setFill(markedColor);
			setStrokeWidth(3);
		} else {
			setStroke(null);
			setFill(color);
			relocate(getX() - 10, getY() - 20);
		}
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public double getX() {
		return pos.getX();
	}

	public double getY() {
		return pos.getY();
	}

	public Position getPos() {
		return pos;
	}

	public boolean isMarked() {
		return isMarked;
	}
	
	public void setMarkedProperty(boolean bool) {
		isMarked = bool;
		setMarkedProperty();
	}

	class MarkerEvent implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if (event.getButton() == MouseButton.PRIMARY) {
				isMarked = !isMarked;
				setMarkedProperty();
			} else if (event.getButton() == MouseButton.SECONDARY) {
				showPlaceDescription();
			}

		}

	}

	abstract void showPlaceDescription();

}
