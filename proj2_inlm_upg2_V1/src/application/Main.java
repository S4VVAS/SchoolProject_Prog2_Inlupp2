package application;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Main extends Application {

	Stage primaryStage;
	BorderPane root;
	Button createBtn, searchBtn, hideBtn, removeBtn, coordinatesBtn, hideCategoryBtn;
	RadioButton namedPlace, describedPlace;
	TextField textSearch;
	ImageView image;
	ListView<String> list;
	ObservableList<String> categories;
	MenuBar menuBar;
	MenuItem loadMap, loadPlaces, save, exit;
	Image map;
	Pane mapHolder;

	HashMap<String, Position> searchPos = new HashMap<>();
	TreeMap<String, HashSet<Place>> searchName = new TreeMap<>();
	HashSet<Place> searchMarked = new HashSet<>();
	TreeMap<String, HashSet<Place>> searchCategory = new TreeMap<>();

	@Override
	public void start(Stage primaryStage) {
		createScene();
		setupScene();
		setupHandlers();

		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void createScene() {
		// TOP
		root = new BorderPane();

		createBtn = new Button("New");
		searchBtn = new Button("Search");
		hideBtn = new Button("Hide");
		removeBtn = new Button("Remove");
		coordinatesBtn = new Button("Coordinates");
		textSearch = new TextField();

		namedPlace = new RadioButton("Named");
		describedPlace = new RadioButton("Described");

		loadMap = new MenuItem("Load Map");
		loadPlaces = new MenuItem("Load Places");
		save = new MenuItem("Save");
		exit = new MenuItem("Exit");
		menuBar = new MenuBar();

		// CENTER
		image = new ImageView();
		mapHolder = new Pane();

		// RIGHT
		list = new ListView<String>();
		hideCategoryBtn = new Button("Hide Category");
	}

	private void setupScene() {

		// TOP
		textSearch.setPromptText("Search");

		ToggleGroup group = new ToggleGroup();
		namedPlace.setToggleGroup(group);
		describedPlace.setToggleGroup(group);

		Menu menu = new Menu("File", null, loadMap, loadPlaces, new SeparatorMenuItem(), save, new SeparatorMenuItem(),
				exit);
		menuBar.getMenus().add(menu);

		HBox topPane = new HBox();
		topPane.setSpacing(10);
		topPane.setPadding(new Insets(10));
		topPane.setAlignment(Pos.CENTER);
		VBox CategoryPane = new VBox();
		CategoryPane.setSpacing(5);

		CategoryPane.getChildren().addAll(namedPlace, describedPlace);
		topPane.getChildren().addAll(createBtn, CategoryPane, textSearch, searchBtn, hideBtn, removeBtn,
				coordinatesBtn);

		VBox topLayout = new VBox();
		topLayout.getChildren().addAll(menuBar, topPane);
		root.setTop(topLayout);

		// CENTER
		mapHolder.getChildren().add(image);

		root.setCenter(new ScrollPane(mapHolder));
		((ScrollPane) root.getCenter()).setPadding(new Insets(10));

		// RIGHT
		categories = FXCollections.observableArrayList("Bus", "Underground", "Train");

		list.setPrefSize(130, 80);
		list.setItems(categories);

		VBox rightLayout = new VBox(new Label("Categories"), list, hideCategoryBtn);
		rightLayout.setAlignment(Pos.CENTER);
		rightLayout.setSpacing(10);
		root.setRight(rightLayout);
	}

	private void setupHandlers() {
		loadMap.setOnAction(new LoadNewMap());

		createBtn.setOnAction(new CreateLocation());

		searchBtn.setOnAction(new SearchPlace());

		removeBtn.setOnAction(new RemovePlace());

	}

	private void refreshMap() {
		mapHolder.getChildren().clear();
		mapHolder.getChildren().add(image);
	}

	private String getSelectedCategory() {
		if (list.getSelectionModel().getSelectedItem() == null)
			return "";
		return list.getSelectionModel().getSelectedItem();
	}

	private void storePlace(Place newPlace) {
		if (!searchName.containsKey(newPlace.getName()))
			searchName.put(newPlace.getName(), new HashSet<Place>());
		searchName.get(newPlace.getName()).add(newPlace);

		if (!searchCategory.containsKey(newPlace.getCategory()))
			searchCategory.put(newPlace.getCategory(), new HashSet<Place>());
		searchCategory.get(newPlace.getCategory()).add(newPlace);

		searchMarked.add(newPlace);

		searchPos.put(newPlace.getPos().getKey(), newPlace.getPos());

		mapHolder.getChildren().add(newPlace);
	}

	private void unmarkAll() {
		for(Place p: searchMarked)
			p.setMarkedProperty(false);
	}
	
	class LoadNewMap implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose map");
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
			File choosenFile = fileChooser.showOpenDialog(primaryStage);
			if (choosenFile != null) {
				map = new Image("file:" + choosenFile.getAbsolutePath());
				image.setImage(map);
				refreshMap();
			}
		}
	}

	class SearchPlace implements EventHandler<ActionEvent> {
		HashSet<Place> results;

		@Override
		public void handle(ActionEvent event) {
			unmarkAll();
			if ((results = searchName.get(textSearch.getText())) != null)
				showResults();
		}

		private void showResults() {
			for (Place p : results)
				if(p.getName().equals(textSearch.getText())) 
					p.setMarkedProperty(true);
		}
	}

	class RemovePlace implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Iterator<Place> iterator = searchMarked.iterator();

			while (iterator.hasNext()) {
				Place p = iterator.next();
				if (p.isMarked()) {
					iterator.remove();
					deleteFromAll(p);
				}
			}
		}

		private void deleteFromAll(Place p) {
			if (searchName.get(p.getName()).size() > 1)
				searchName.get(p.getName()).remove(p);
			else
				searchName.remove(p.getName());
			
			if (searchCategory.get(p.getCategory()).size() > 1)
				searchCategory.get(p.getCategory()).remove(p);
			else
				searchCategory.remove(p.getCategory());
			
			searchPos.remove(p.getPos().getKey());
			searchMarked.remove(p);

			mapHolder.getChildren().remove(p);
		}
	}

	class CreateLocation implements EventHandler<ActionEvent> {
		Place newPlace;
		String category, name, description;

		@Override
		public void handle(ActionEvent event) {
			mapHolder.setCursor(Cursor.CROSSHAIR);

			if (namedPlace.isSelected()) {
				mapHolder.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						createNamed(event.getX(), event.getY());
					}
				});
			}

			else if (describedPlace.isSelected()) {
				mapHolder.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						createDescribed(event.getX(), event.getY());
					}
				});
			}
		}

		private void createNamed(double x, double y) {
			if (!searchPos.containsKey(Double.toString(x) + Double.toString(y))) {
				CreateNamedPlace named = new CreateNamedPlace(x, y);
				Optional<ButtonType> anwser = named.showAndWait();
				if (anwser.isPresent() && anwser.get() == ButtonType.OK) {
					newPlace = new NamedPlace(getSelectedCategory(), named.getName(), x, y);
					storePlace(newPlace);
				}
			} else
				error("Could not create place here!");
			restoreMouse();
		}

		private void createDescribed(double x, double y) {
			if (!searchPos.containsKey(Double.toString(x) + Double.toString(y))) {
				CreateDescribedPlace described = new CreateDescribedPlace(x, y);
				Optional<ButtonType> anwser = described.showAndWait();
				if (anwser.isPresent() && anwser.get() == ButtonType.OK) {
					newPlace = new DescribedPlace(getSelectedCategory(), described.getName(), x, y,
							described.getDescription());
					storePlace(newPlace);
				}
			} else
				error("Could not create place here!");
			restoreMouse();
		}

		private void restoreMouse() {
			mapHolder.setOnMouseClicked(null);
			mapHolder.setCursor(Cursor.DEFAULT);
		}

		private void error(String text) {
			new Alert(AlertType.ERROR, text).showAndWait();
			mapHolder.setOnMouseClicked(null);
			mapHolder.setCursor(Cursor.DEFAULT);
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
