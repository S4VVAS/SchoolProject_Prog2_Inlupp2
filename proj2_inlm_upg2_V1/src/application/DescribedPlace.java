package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DescribedPlace extends Place {

	private String description, toString;

	public DescribedPlace(String category, String name, double x, double y, String description) {
		super(name, category, x, y);
		this.description = description;
		toString = "Described," + category + "," + (int)x + "," + (int)y + "," + name + "," + description;
	}

	@Override
	void showPlaceDescription() {
		Alert alert = new Alert(AlertType.INFORMATION, description);
		alert.setHeaderText(name + " [ x" + pos.getX() + " / y" + pos.getY() + " ]");
		alert.setTitle(category);
		alert.showAndWait();
	}

	@Override
	public String toString() {
		return toString;
	}
}
