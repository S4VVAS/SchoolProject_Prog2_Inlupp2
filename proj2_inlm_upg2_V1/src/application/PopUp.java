package application;

import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;

abstract public class PopUp extends Alert{
	GridPane root = new GridPane();

	public PopUp() {
		super(AlertType.CONFIRMATION);
		
		createWindow();
		
		getDialogPane().setContent(root);
		root.setVgap(5);
	}
	
	abstract void createWindow();

}
