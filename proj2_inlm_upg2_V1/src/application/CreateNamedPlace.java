package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateNamedPlace extends PopUp{

	RadioButton bus, train, underground;
	TextField name;
	double x, y;

	public CreateNamedPlace(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	void createWindow() {
		setTitle("Create a described place at: x" + Double.toString(x) + " y" + Double.toString(y));
		setHeaderText(null);
		name = new TextField();
		name.setPromptText("name the place");

		ToggleGroup group = new ToggleGroup();
		bus = new RadioButton();
		train = new RadioButton();
		underground = new RadioButton();

		bus.setToggleGroup(group);
		train.setToggleGroup(group);
		underground.setToggleGroup(group);

		root.add(new Label("Name: "), 0, 0);
		root.add(name, 0, 0);

		HBox categoryBox = new HBox(new VBox(bus, new Label("Bus")), new VBox(train, new Label("Train")),
				new VBox(underground, new Label("Underground")));
		categoryBox.setSpacing(20);

		root.add(categoryBox, 0, 1);

	}

	public String getName() {
		return name.getText();
	}

	public String getCategory() {
		if (bus.isSelected())
			return "bus";
		else if (train.isSelected())
			return "train";
		else if (underground.isSelected())
			return "underground";
		return "";
	}
	
}
