package application;

import java.io.File;

import javafx.application.Application;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Main extends Application {

	Stage primaryStage;
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

	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		Insets inset = new Insets(10);

		// TOP-----------------------------------------
		create = new Button("New");
		searchBtn = new Button("Search");
		hide = new Button("Hide");
		remove = new Button("Remove");
		coordinates = new Button("Coordinates");

		textSearch = new TextField();
		textSearch.setPromptText("Search");

		namedPlace = new RadioButton("Named");
		describedPlace = new RadioButton("Described");

		ToggleGroup group = new ToggleGroup();
		namedPlace.setToggleGroup(group);
		describedPlace.setToggleGroup(group);

		// Menu bar construction
		loadMap = new MenuItem("Load Map");
		loadMap.setOnAction(new LoadNewMap());

		loadPlaces = new MenuItem("Load Places");
		save = new MenuItem("Save");
		exit = new MenuItem("Exit");

		menuBar = new MenuBar();

		Menu menu = new Menu("File", null, loadMap, loadPlaces, new SeparatorMenuItem(), save, new SeparatorMenuItem(),
				exit);

		menuBar.getMenus().add(menu);

		// Configure top
		HBox topPane = new HBox();
		topPane.setSpacing(10);
		topPane.setPadding(inset);
		topPane.setAlignment(Pos.CENTER);
		VBox CategoryPane = new VBox();
		CategoryPane.setSpacing(5);

		CategoryPane.getChildren().addAll(namedPlace, describedPlace);
		topPane.getChildren().addAll(create, CategoryPane, textSearch, searchBtn, hide, remove, coordinates);

		VBox topLayout = new VBox();
		topLayout.getChildren().addAll(menuBar, topPane);
		root.setTop(topLayout);
		
		// CENTER-------------------------------------
		image = new ImageView();
		image.isPreserveRatio();
		
		mapHolder = new Pane();
		mapHolder.getChildren().add(image);
		
		root.setCenter(new ScrollPane(mapHolder));
		// RIGHT--------------------------------------
		categories = FXCollections.observableArrayList("Bus", "Underground", "Train");
		list = new ListView<String>(categories);
		list.setPrefSize(100, 80);

		VBox rightLayout = new VBox();
		rightLayout.getChildren().addAll(new Label("Categories"), list);
		rightLayout.setAlignment(Pos.CENTER);
		rightLayout.setSpacing(10);
		root.setRight(rightLayout);

		// SETTING STAGE------------------------------
		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
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
				
				
				//________TEST________________________________________
				NamedPlace yo = new NamedPlace("bus", "bus", 20, 20);
				mapHolder.getChildren().add(yo.getMarker());
				//____________________________________________________
				
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
