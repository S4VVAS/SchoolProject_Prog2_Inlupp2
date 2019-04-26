package application;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class CreateDescribed implements EventHandler<MouseEvent>{
	private DescribedPlace newPlace;
	private String name, description, category;

	@Override
	public void handle(MouseEvent event) {
		newPlace = new DescribedPlace("bus", "sfd", event.getX(), event.getY(), "sdf");		
	}
	
	public Place getPlace() {
		return newPlace;

	}
}
