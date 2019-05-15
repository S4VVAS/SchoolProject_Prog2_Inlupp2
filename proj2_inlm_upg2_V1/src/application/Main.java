package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

	private Stage primaryStage;
	private BorderPane root;
	private Button createBtn, searchBtn, hideBtn, removeBtn, coordinatesBtn, hideCategoryBtn, unmarkAllBtn;
	private RadioButton namedPlace, describedPlace;
	private TextField textSearch;
	private ImageView image;
	private ListView<String> list;
	private ObservableList<String> categories;
	private MenuBar menuBar;
	private MenuItem loadMap, loadPlaces, save, exit;
	private Image map;
	private Pane mapHolder;
	private SimpleBooleanProperty hasChanged = new SimpleBooleanProperty(false);

	private HashMap<String, Position> searchPos = new HashMap<>();
	private TreeMap<String, HashSet<Place>> searchName = new TreeMap<>();
	private TreeMap<String, HashSet<Place>> searchCategory = new TreeMap<>();
	private HashSet<Place> allMarked = new HashSet<>();

	@Override
	public void start(Stage primaryStage) {
		createScene();
		setupScene();
		setupHandlers(primaryStage);
		setupListeners();
		
		this.primaryStage = primaryStage;
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
		unmarkAllBtn = new Button("Unmark All");
	}

	private void setupScene() {

		// TOP
		textSearch.setPromptText("Search");

		ToggleGroup group = new ToggleGroup();
		namedPlace.setToggleGroup(group);
		describedPlace.setToggleGroup(group);

		namedPlace.setSelected(true);

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

		VBox rightLayout = new VBox(new Label("Categories"), list, hideCategoryBtn, unmarkAllBtn);
		rightLayout.setAlignment(Pos.CENTER);
		rightLayout.setSpacing(10);
		root.setRight(rightLayout);
	}

	private void setupHandlers(Stage primaryStage) {
		loadMap.setOnAction(new LoadNewMap());
		loadPlaces.setOnAction(new LoadPlaces());
		save.setOnAction(new SavePlaces());
		exit.setOnAction(
				action -> primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST)));

		createBtn.setOnAction(new CreateLocation());

		searchBtn.setOnAction(new SearchPlace());

		hideBtn.setOnAction(new HidePlace());

		removeBtn.setOnAction(new RemovePlace());

		coordinatesBtn.setOnAction(new SearchCoordinates());

		list.setOnMouseClicked(new ShowCategory());
		hideCategoryBtn.setOnAction(new HideCategory());
		unmarkAllBtn.setOnAction(e -> unmarkAll());

		primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new ExitApplication());
	}

	private void setupListeners() {
		save.setDisable(true);
		hasChanged.addListener((obs, old, nevv) -> {
			if (!hasChanged.get())
				save.setDisable(true);
			else
				save.setDisable(false);
		});
	}

	private void refreshMap() {
		mapHolder.getChildren().clear();
		mapHolder.getChildren().add(image);

		for (Place p : allMarked) {
			mapHolder.getChildren().add(p);
		}
	}

	private String getSelectedCategory() {
		if (list.getSelectionModel().getSelectedItem() == null)
			return "None";
		return list.getSelectionModel().getSelectedItem();
	}

	private void storePlace(Place newPlace) {
		allMarked.add(newPlace);
		newPlace.getBool().addListener((obs, old, nevv) -> {
			if (nevv == true) {
				allMarked.add(newPlace);
			} else if (nevv == false) {
				allMarked.remove(newPlace);
			}
		});

		searchName.putIfAbsent(newPlace.getName(), new HashSet<Place>());
		searchName.get(newPlace.getName()).add(newPlace);

		searchCategory.putIfAbsent(newPlace.getCategory(), new HashSet<Place>());
		searchCategory.get(newPlace.getCategory()).add(newPlace);

		searchPos.put(newPlace.getPos().getKey(), newPlace.getPos());

		mapHolder.getChildren().add(newPlace);
	}

	private void unmarkAll() {
		Iterator<Place> iterator = allMarked.iterator();
		while (iterator.hasNext()) {
			Place p = iterator.next();
			if (p.isMarked()) {
				iterator.remove();
				p.setMarkedProperty(false);
			}
		}
	}

	private void removeAll() {
		searchName.clear();
		searchCategory.clear();
		allMarked.clear();
		searchPos.clear();
	}

	private boolean unsaved() {
		Optional<ButtonType> anwser = null;
		if (hasChanged.get())
			anwser = new Alert(AlertType.CONFIRMATION,
					"Your changes have not been saved!\nThis action will erase your places!").showAndWait();
		if (!hasChanged.get() || anwser.isPresent() && anwser.get() == ButtonType.OK)
			return true;
		return false;
	}

	class LoadNewMap implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			if (unsaved()) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose map");
				fileChooser.getExtensionFilters()
						.addAll(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
				File choosenFile = fileChooser.showOpenDialog(primaryStage);
				if (choosenFile != null) {
					map = new Image("file:" + choosenFile.getAbsolutePath());
					image.setImage(map);
					removeAll();
					refreshMap();
					hasChanged.set(false);
				}
			}
		}
	}

	class LoadPlaces implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			if (unsaved()) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Choose places file");
				fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
				File choosenFile = fileChooser.showOpenDialog(primaryStage);

				if (choosenFile != null) {
					removeAll();
					refreshMap();
					hasChanged.set(false);

					try {
						FileReader file = new FileReader(choosenFile);
						BufferedReader bufferedFile = new BufferedReader(file);

						String line;
						try {
							while ((line = bufferedFile.readLine()) != null)
								createPlace(line.split(","));
						} catch (Exception e) {
							new Alert(AlertType.ERROR, "File format not as expected").showAndWait();
						}
						new Alert(AlertType.INFORMATION, "Places loaded successfully!").showAndWait();

						file.close();
						bufferedFile.close();
					} catch (IOException e) {
						new Alert(AlertType.ERROR, "Could not open Place file").showAndWait();
					}
				}
			}

		}

		private void createPlace(String[] arg) {
			if (arg[0].equals("Named"))
				storePlace(new NamedPlace(arg[1], arg[4], Double.parseDouble(arg[2]), Double.parseDouble(arg[3])));
			else if (arg[0].equals("Described"))
				storePlace(new DescribedPlace(arg[1], arg[4], Double.parseDouble(arg[2]), Double.parseDouble(arg[3]),
						arg[5]));
		}

	}

	class SavePlaces implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose places file");
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
			File choosenFile = fileChooser.showSaveDialog(primaryStage);

			if (choosenFile != null) {
				hasChanged.set(false);
				try {
					FileWriter file = new FileWriter(choosenFile);
					PrintWriter outBound = new PrintWriter(file);
					for (Entry<String, Position> p : searchPos.entrySet()) {
						outBound.println(p.getValue().getPlace().toString());
					}
					file.close();
					outBound.close();
					new Alert(AlertType.INFORMATION, "Places saved successfully!").showAndWait();
				} catch (IOException e) {
					new Alert(AlertType.ERROR, "Could not save Place file").showAndWait();
				}
			}
		}
	}

	class ExitApplication implements EventHandler<WindowEvent> {

		@Override
		public void handle(WindowEvent event) {
			if (hasChanged.get()) {
				Optional<ButtonType> anwser = new Alert(AlertType.CONFIRMATION,
						"You have made changes that have not yet been saved!\nDo you wish to quit anyway?")
								.showAndWait();
				if(anwser.isPresent() && anwser.get() == ButtonType.CANCEL || anwser.get() == ButtonType.CLOSE)
					event.consume();
				
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
				if (p.getName().equals(textSearch.getText())) {
					p.setMarkedProperty(true);
					p.setVisible(true);
				}
		}
	}

	class HidePlace implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			hideMarked();
		}

		private void hideMarked() {
			Iterator<Place> iterator = allMarked.iterator();

			while (iterator.hasNext()) {
				Place p = iterator.next();
				if (p.isMarked()) {
					p.setVisible(false);
					iterator.remove();
					p.setMarkedProperty(false);
				}
			}
		}
	}

	class RemovePlace implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Iterator<Place> iterator = allMarked.iterator();

			while (iterator.hasNext()) {
				Place p = iterator.next();
				iterator.remove();
				deleteFromAll(p);
			}
			hasChanged.set(true);
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
			allMarked.remove(p);

			mapHolder.getChildren().remove(p);
		}
	}

	class SearchCoordinates implements EventHandler<ActionEvent> {

		public void handle(ActionEvent event) {
			SearchCoordsWindow search = new SearchCoordsWindow();
			Optional<ButtonType> anwser = search.showAndWait();
			if (anwser.isPresent() && anwser.get() == ButtonType.OK) {
				try {
					if (Double.parseDouble(search.getXCord()) >= 0 && Double.parseDouble(search.getYCord()) >= 0) {
						if (searchPos.get(search.getXCord() + search.getYCord()) != null) {
							unmarkAll();
							Place p = searchPos.get(search.getXCord() + search.getYCord()).getPlace();
							p.setMarkedProperty(true);
							p.setVisible(true);
						} else
							noPlaceError();
					} else
						negativeNumError();
				} catch (NumberFormatException e) {
					formatError();
				}
			}
		}

		private void noPlaceError() {
			new Alert(AlertType.ERROR, "Place not found!").showAndWait();
		}

		private void formatError() {
			new Alert(AlertType.ERROR, "Wrong input! Numbers only").showAndWait();
		}

		private void negativeNumError() {
			new Alert(AlertType.ERROR, "Wrong input! Positive coordinates only").showAndWait();
		}
	}

// can be made to lambda
	class HideCategory implements EventHandler<ActionEvent> {
		public void handle(ActionEvent event) {
			for (Place p : searchCategory.get(getSelectedCategory())) {
				p.setVisible(false);
				p.setMarkedProperty(false);
			}
		}
	}

	class ShowCategory implements EventHandler<MouseEvent> {
		public void handle(MouseEvent event) {
			unmarkAll();
			try {
				for (Place p : searchCategory.get(getSelectedCategory())) {
					p.setVisible(true);
					p.setMarkedProperty(true);
				}
			} catch (NullPointerException e) {
			}
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
					hasChanged.set(true);
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
					hasChanged.set(true);
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
