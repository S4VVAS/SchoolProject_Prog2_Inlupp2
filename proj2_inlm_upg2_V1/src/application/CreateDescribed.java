package application;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class CreateDescribed {
	private DescribedPlace newPlace;
	private String name, description, category;

	public CreateDescribed() {
		new Create();
	}

	public Place getPlace() {
		return newPlace;

	}

	public Position getPos() {
		return newPlace.getPos();
	}

	
	
	class Create implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			newPlace = new DescribedPlace("bus", "sfd", 20.0, 20.0, "sdf");

		}
	}
	
}
