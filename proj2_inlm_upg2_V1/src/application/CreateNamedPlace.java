package application;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateNamedPlace extends PopUp {

	TextField name;

	public CreateNamedPlace(double x, double y) {
		super(x, y);
	}

	@Override
	void createWindow(double x, double y) {
		setTitle("Create Named: X " + Double.toString(x) + " Y " + Double.toString(y));
		setHeaderText(null);
		name = new TextField();
		name.setPromptText("name the place");

		root.add(new Label("Name: "), 0, 0);
		root.add(name, 0, 0);
	}

	public String getName() {
		return name.getText();
	}
}
