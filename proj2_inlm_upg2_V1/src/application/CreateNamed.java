package application;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class CreateNamed implements EventHandler<MouseEvent> {
	Place newPlace;
	String name, category;

	public void handle(MouseEvent event) {
		
	}
	
	public Place getPlace() {
		return newPlace;
		
	}
	
	public Position getPos() {
		return newPlace.getPos();
		
	}

}
