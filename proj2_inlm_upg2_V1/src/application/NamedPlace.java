//Savvas Giortsis (sagi2536)
package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NamedPlace extends Place {

	private String toString;
	
	public NamedPlace(String category, String name, double x, double y) {
		super(name, category, x, y);
		toString = "Named," + category + "," + (int)x + "," + (int)y +"," + super.name;
	}

	@Override
	void showPlaceDescription() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(name + " [ x" + pos.getX() + " / y" + pos.getY() + " ]");
		alert.setTitle(category);
		alert.showAndWait();
	}

	@Override
	public String toString() {
		return toString;
	}
}
