package application;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SearchCoordsWindow extends PopUp {
	private TextField xCord, yCord;

	public SearchCoordsWindow() {
		super(0, 0);
	}

	@Override
	void createWindow(double x, double y) {
		setTitle("Input Coordinates:");
		setHeaderText(null);
		xCord = new TextField();
		xCord.setPromptText("Type X coordinate");

		yCord = new TextField();
		yCord.setPromptText("Type Y coordinate");

		root.add(new Label("X: "), 0, 0);
		root.add(xCord, 1, 0);
		root.add(new Label("Y: "), 0, 1);
		root.add(yCord, 1, 1);
	}
	
	public String getXCord() {
		return xCord.getText() + ".0";
	}

	public String getYCord() {
		return yCord.getText() + ".0";
	}

}
