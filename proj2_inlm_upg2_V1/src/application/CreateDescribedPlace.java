//Savvas Giortsis (sagi2536)
package application;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateDescribedPlace extends PopUp {

	private TextField name, description;

	public CreateDescribedPlace(double x, double y) {
		super(x, y);
	}

	@Override
	void createWindow(double x, double y) {
		setTitle("Create Described: X " + Double.toString(x) + " Y " + Double.toString(y));
		setHeaderText(null);
		name = new TextField();
		name.setPromptText("name the place");

		description = new TextField();
		description.setPromptText("describe the place");

		root.add(new Label("Name: "), 0, 0);
		root.add(name, 1, 0);
		root.add(new Label("Description: "), 0, 1);
		root.add(description, 1, 1);
	}

	public String getName() {
		return name.getText();
	}

	public String getDescription() {
		return description.getText();
	}
}
