package application;

import java.io.File;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	Button create, searchBtn, hide, remove, coordinates, hideCategory;
	RadioButton namedPlace, describedPlace;
	TextField textSearch;
	ImageView image;
	ListView<String> list;
	ObservableList<String> categories;
	MenuBar menuBar;
	MenuItem loadMap, loadPlaces, save, exit;
	Image map;
	Pane mapHolder;

	HashMap<Position, Place> searchPos = new HashMap<Position, Place>();

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

		create = new Button("New");
		searchBtn = new Button("Search");
		hide = new Button("Hide");
		remove = new Button("Remove");
		coordinates = new Button("Coordinates");
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
		topPane.getChildren().addAll(create, CategoryPane, textSearch, searchBtn, hide, remove, coordinates);

		VBox topLayout = new VBox();
		topLayout.getChildren().addAll(menuBar, topPane);
		root.setTop(topLayout);

		// CENTER
		mapHolder.getChildren().add(image);

		root.setCenter(new ScrollPane(mapHolder));
		((ScrollPane) root.getCenter()).setPadding(new Insets(10));

		// RIGHT
		categories = FXCollections.observableArrayList("Bus", "Underground", "Train");

		list.setPrefSize(100, 80);
		list.setItems(categories);

		VBox rightLayout = new VBox();
		rightLayout.getChildren().addAll(new Label("Categories"), list);
		rightLayout.setAlignment(Pos.CENTER);
		rightLayout.setSpacing(10);
		root.setRight(rightLayout);
	}

	private void setupHandlers() {
		loadMap.setOnAction(new LoadNewMap());
		create.setOnAction(new CreateLocation());

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

			}
		}
	}

	class CreateLocation implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			if (namedPlace.isSelected())
				mapHolder.setOnMousePressed(e -> createNamed());
			else if (describedPlace.isSelected())
				mapHolder.setOnMousePressed(e -> createDescribed());
		}

		private void createNamed() {
			System.out.println("named");
			CreateNamed namedPlace = new CreateNamed();
			//storePlace(namedPlace.getPlace(), namedPlace.getPos());
			mapHolder.setOnMousePressed(null);
		}
		
		private void createDescribed() {
			System.out.println("desc");
			CreateDescribed descPlace = new CreateDescribed();
			
			storePlace(descPlace.getPlace(), descPlace.getPos());
			mapHolder.setOnMousePressed(null);
		}
	
		private void storePlace(Place place, Position pos) {
			searchPos.put(pos, place);
			mapHolder.getChildren().add(place.getMarker());
			mapHolder.setOnMousePressed(null);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
