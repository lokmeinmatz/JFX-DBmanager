package window;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import db.DBmanager;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import objects.Movie;
import settings.backupmanager;
import settings.exportmanager;
import settings.settingsmanager;
import webCrawler.WebFinder;

public class Main extends Application{
	
	
	private Stage stage;
	private Connection connection;
	private TableView<Movie> table;
	private ObservableList<Movie> allMovies;
	private TextField searchfield;
	private Label movieinfotxt;
	private VBox infobox;
	private String mainpath;
	private NotificationBar notificationbar;
	private AddMovieWindow addmovie;
	private settingsmanager settings;
	private static int WIDTH = 1500, HEIGHT = 750;
	public static void main(String[] args) {
		launch(args);

	}

	
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		
		String MainDir = System.getProperty("user.dir").replace("\\", "/");
		mainpath = MainDir;
		stage.setTitle("Filme-Datenbank");
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				backupmanager.askforBackup();
				exitProgram();
				
			}
			
		});
		//loadingscreen image
		SequentialTransition seq = new SequentialTransition();
		
		Image img = new Image("file:"+MainDir+"/res/Logo.png");
		ImageView imgview = new ImageView(img);
		imgview.setFitHeight(1);
		imgview.setFitWidth(1);
		
		ScaleTransition scaletransition = new ScaleTransition(Duration.millis(1000), imgview);
		
		
		scaletransition.setFromX(0);
		scaletransition.setToX(img.getWidth()/2);
		scaletransition.setFromY(0);
		scaletransition.setToY(img.getHeight()/2);
		
		
		FadeTransition transition = new FadeTransition(Duration.millis(500), imgview);
		transition.setFromValue(1.0);
		transition.setToValue(0);
		seq.getChildren().addAll(scaletransition, new PauseTransition(Duration.seconds(1)), transition);
		
		
		BorderPane loadinglayout = new BorderPane();
		loadinglayout.setCenter(imgview);

		//loading circle
		ProgressIndicator loadingcircle = new ProgressIndicator();
		loadingcircle.setProgress(-1);
		loadingcircle.setScaleX(0.8);
		loadingcircle.setScaleY(0.8);
		loadinglayout.setBottom(loadingcircle);
		
		Scene loadingscene = new Scene(loadinglayout, WIDTH, HEIGHT);
		loadinglayout.setStyle("-fx-background-color: white");
		stage.setScene(loadingscene);
		stage.show();
		seq.play();
		
		//icons
		Image i1 = new Image("file:"+MainDir+"/res/Logo_256.png");
		Image i2 = new Image("file:"+MainDir+"/res/Logo_64.png");
		stage.getIcons().addAll(i1, i2);
		//init
		connection = DBmanager.dbConnector();
		addmovie = new AddMovieWindow();
		table = new TableView<>();
		settings = new settingsmanager();
		settings.loadSettingsData();
		
		reloadallMovies();
		
		{
			TableColumn<Movie, Integer> idcol = new TableColumn<>("ID");
			idcol.setMinWidth(10);
			idcol.setPrefWidth(40);
			idcol.setCellValueFactory(new PropertyValueFactory<>("ID"));
			
			TableColumn<Movie, String> namecol = new TableColumn<>("Titel");
			namecol.setMinWidth(60);
			namecol.setPrefWidth(400);
			namecol.setCellValueFactory(new PropertyValueFactory<>("title"));
			
			TableColumn<Movie, Integer> fskcol = new TableColumn<>("FSK");
			fskcol.setMinWidth(20);
			fskcol.setPrefWidth(40);
			fskcol.setCellValueFactory(new PropertyValueFactory<>("FSK"));
			
			TableColumn<Movie, Integer> lengthcol = new TableColumn<>("Länge");
			lengthcol.setMinWidth(40);
			lengthcol.setPrefWidth(80);
			lengthcol.setCellValueFactory(new PropertyValueFactory<>("length"));
			
			TableColumn<Movie, String> loccol = new TableColumn<>("Specherort");
			loccol.setMinWidth(50);
			loccol.setPrefWidth(120);
			loccol.setCellValueFactory(new PropertyValueFactory<>("location"));
			
			TableColumn<Movie, String> seriescol = new TableColumn<>("Serie");
			seriescol.setMinWidth(50);
			seriescol.setPrefWidth(100);
			seriescol.setCellValueFactory(new PropertyValueFactory<>("series"));
			
			TableColumn<Movie, String> catcol = new TableColumn<>("Kategorie");
			catcol.setMinWidth(50);
			catcol.setPrefWidth(100);
			catcol.setCellValueFactory(new PropertyValueFactory<>("category"));
			
			
			
			infobox = new VBox(10);
			movieinfotxt = new Label("Film-Informationen");
			ImageView previewImage = new ImageView("file:"+MainDir+"/res/ImgUnavailable.png");
			infobox.getChildren().addAll(movieinfotxt, previewImage);
			
			
			
			
			table.setPrefHeight(700);
			table.getColumns().addAll(idcol, namecol, fskcol, lengthcol, loccol, seriescol, catcol);
			table.setEditable(false);
			table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			table.getSelectionModel().selectedItemProperty().addListener((obs, oldselection, newselection) -> {
				if(newselection != null){
					movieinfotxt.setText("Dieser Film besitzt keine Bschreibung.");
					System.out.println(newselection.getTitle());
					if(newselection != oldselection){
						Thread searchthread = new Thread(new Runnable() {
							
							@Override
							public void run() {
								Image preview = WebFinder.findImage(newselection.getTitle(), mainpath);
								if(preview != null){
									previewImage.setImage(preview);
								}
								else{
									previewImage.setImage(new Image("file:"+mainpath+"/res/ImgUnavailable.png"));
								}
								
							}
						});
						searchthread.start();
					}
					if(newselection.getDescription() != null && !newselection.getDescription().isEmpty()){
						movieinfotxt.setText(newselection.getDescription());
					}
					
				}
				else{
					movieinfotxt.setText("Markiere einen Film um die Beschreibung anzeigen zu lassen.");
				}
			});
		}
		
		seq.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("loaded");
				loadmain();
				
			}
		});
		
	}
	
	
	
	private void loadmain(){
		StackPane root = new StackPane();
		BorderPane mainpane = new BorderPane();
		mainpane.setStyle("-fx-background-color: white");
		List<Button> topmenubuttons = new ArrayList<Button>();
		Scene main = new Scene(root, WIDTH, HEIGHT);
		main.getStylesheets().add("window/style.css");
		{
			Button newmoviebutton = new Button();
			newmoviebutton.setText("Neuen Film hinzufügen");
			newmoviebutton.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					if(addmovie.NewAddMovieWindow(connection, settings)){
						reloadallMovies();
						filterList("");
					}
					//reload
					
				}
			});
			topmenubuttons.add(newmoviebutton);
			
			
			Button changemoviebutton = new Button();
			changemoviebutton.setText("Fim ändern");
			changemoviebutton.setDisable(true);
			ChangeMovieWindow c = new ChangeMovieWindow();
			changemoviebutton.setOnAction(e -> {
				
				if(c.NewAddMovieWindow(connection, settings, table.getSelectionModel().getSelectedItem())){
					reloadallMovies();
					filterList("");
				}
			});
			changemoviebutton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
			topmenubuttons.add(changemoviebutton);
			
			
			Button deletemoviebutton = new Button();
			deletemoviebutton.setText("Film löschen");
			//deletemoviebutton.setDisable(true);
			deletemoviebutton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
			deletemoviebutton.setOnAction(e -> deleteSelectedMovie());
			topmenubuttons.add(deletemoviebutton);
			
			
			Button ExportButton = new Button();
			ExportButton.setText("Markierte exportieren");
			ExportButton.setOnAction(e -> {
				exportmanager.exportWindow(table);
			});
			topmenubuttons.add(ExportButton);
			
			for(Button b:topmenubuttons){
				b.setMinSize(100, 20);
				b.setPrefSize(240, 50);
				b.setPadding(new Insets(5));
				b.getStyleClass().add("header-button");
				
				
			}
			
			//searchbox
			searchfield = new TextField();
			searchfield.setPromptText("Suche");
			searchfield.setPrefSize(500, 50);
			searchfield.getStyleClass().add("filterbar");
			
			searchfield.textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					filterList(newValue);
					
				}

				
				
				
			});
			
			
			
			HBox topmenu = new HBox();
			topmenu.getChildren().addAll(topmenubuttons);
			topmenu.getChildren().add(searchfield);
			HBox.setMargin(newmoviebutton, new Insets(5,2, 5, 2));
			HBox.setMargin(changemoviebutton, new Insets(5,2, 5, 2));
			HBox.setMargin(deletemoviebutton, new Insets(5,2, 5, 2));
			HBox.setMargin(ExportButton, new Insets(5,2, 5, 2));
			HBox.setMargin(searchfield, new Insets(5,2, 5, 7));
			mainpane.setTop(topmenu);
		}
		
		
		{
			
			
			
			VBox vbox = new VBox();
			vbox.getChildren().add(table);
			mainpane.setCenter(vbox);
		}
		
		VBox movieinfoframe = new VBox();
		
		movieinfotxt.getStyleClass().add("infotextwindow");
		movieinfoframe.setMargin(infobox, new Insets(5));
		movieinfoframe.setPrefWidth(400);
		movieinfoframe.getChildren().add(infobox);
		mainpane.setRight(movieinfoframe);
		
		notificationbar = new NotificationBar(root);
		mainpane.setBottom(notificationbar);
		root.getChildren().add(mainpane);
		stage.setScene(main);
	}
	
	private void deleteSelectedMovie(){
		System.out.println("Deleting");
		ObservableList<Movie> selected;
		selected = table.getSelectionModel().getSelectedItems();
		for(Movie m:selected){
			DBmanager.deleteMovie(m);
		}
		reloadallMovies();
		
	}
	
	
	private void reloadallMovies(){
		table.setItems(DBmanager.getAllMovies());
		allMovies = DBmanager.getAllMovies();
	}
	private void filterList(String filter){
		ObservableList<Movie> all, filtered;
		all = allMovies;
		if(searchfield.getText().trim().isEmpty()){
			System.out.println("Empty");
		}
		filtered = FXCollections.observableArrayList();
		filter = filter.toLowerCase();
		for(Movie m:all){
			
			toString();
			if(m.getCategory() != null && m.getCategory().toLowerCase().contains(filter) ||
					m.getLocation() != null && m.getLocation().toLowerCase().contains(filter) || m.getSeries() != null && m.getSeries().toLowerCase().contains(filter) || m.getTitle().toLowerCase().contains(filter)
					|| String.valueOf(m.getID()).contains(filter) || String.valueOf(m.getFSK()).contains(filter) || String.valueOf(m.getLength()).contains(filter) ){
				filtered.add(m);
				
			}
				
			
		}
		table.setItems(filtered);
		
		
		
		
	}
	
	public void setDescriptionLabel(Movie m){
		
	}

	
	
	public void exitProgram(){
		stage.close();
	}
}
