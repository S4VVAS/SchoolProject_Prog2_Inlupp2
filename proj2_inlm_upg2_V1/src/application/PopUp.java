//Savvas Giortsis (sagi2536)
package application;

import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;

abstract public class PopUp extends Alert {
	protected GridPane root = new GridPane();

	public PopUp(double x, double y) {
		super(AlertType.CONFIRMATION);
		createWindow(x, y);
		getDialogPane().setContent(root);
		root.setVgap(5);
	}

	abstract void createWindow(double x, double y);
}
