package window;



import java.sql.Connection;

import db.DBmanager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objects.Movie;
import settings.settingsmanager;

public class ChangeMovieWindow {

	
	ChoiceBox<String> location;
	ChoiceBox<String> Series;
	ChoiceBox<String> Category;
	settingsmanager settings;
	public boolean NewAddMovieWindow(Connection c, settingsmanager settings, Movie m){
		this.settings = settings;
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Film ändern");
		window.setWidth(800);
		window.setHeight(400);
		window.setResizable(false);
		int nextid = m.getID();
		
		VBox mainframe = new VBox();
		mainframe.setStyle("-fx-background-color: white");
		
		
			HBox titlebox = new HBox();
		
				TextField titleinput = new TextField();
				//titleinput.set
				titleinput.setPromptText("Titel");
				titleinput.setPrefSize(600, 50);
				titleinput.setText(m.getTitle());
				Label nextidlbl = new Label("ID des Films: "+String.valueOf(nextid));
				nextidlbl.setPrefSize(150, 50);
				nextidlbl.setPadding(new Insets(10));
				titlebox.getChildren().addAll(titleinput, nextidlbl);
				
				//nextid
				
			
			mainframe.getChildren().add(titlebox);
			
			HBox fsklengthbox = new HBox();
			
				ChoiceBox<Integer> fsk = new ChoiceBox<>();
				ObservableList<Integer> fsklist = fsk.getItems();
				fsklist.addAll(0, 6, 12, 16, 18);
				fsk.setValue(m.getFSK());
				fsklengthbox.getChildren().add(fsk);
				
				TextField lengthinput = new TextField();
				lengthinput.setPromptText("Länge (in Minuten)");
				lengthinput.setText(String.valueOf(m.getLength()));
				lengthinput.textProperty().addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						if(newValue.trim().isEmpty()) System.out.println("Empty");
						else if(!isInt(newValue)){
							System.out.println(newValue+" -> "+oldValue);
							lengthinput.setText(oldValue);}
						
					}
				});
				fsklengthbox.getChildren().add(lengthinput);
				
				Label loclbl = new Label("Speicherort");
				location = new ChoiceBox<>();
				location.setPrefSize(200, 30);
				
				reloadSaveLocs();
				location.setValue(m.getLocation());
				
			
				
				//add saveloc buttton
				Button addloc = new Button();
				addloc.setText("+");
				addloc.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						String newloc = AddValueWindow.NewValueWindow("Neuer Speicherort", "Füge einen Neuen Speciherort hinzu");
						if(!location.getItems().contains(newloc) && newloc != null && !newloc.trim().isEmpty()){
							settings.addSavelocation(newloc);
							reloadSaveLocs();
						}
					}
				});
				
				fsklengthbox.getChildren().addAll(loclbl, location, addloc);
				
			HBox seriescatbox = new HBox();
				
			
				Label srslbl = new Label("Serie");
				Series = new ChoiceBox<>();
				Series.setPrefSize(200, 30);
				reloadSeries();
				Series.setValue(m.getSeries());
				
				
				Button addseries = new Button("+");
				addseries.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						String newseries = AddValueWindow.NewValueWindow("Neue Serie", "Füge eine neue Serie hinzu");
						if(!location.getItems().contains(newseries) && newseries != null && !newseries.trim().isEmpty()){
							settings.addSerie(newseries);
							reloadSeries();
						}
						
					}
				});
				
				
				Label catlbl = new Label("Kategorie");
				Category = new ChoiceBox<>();
				Category.setPrefSize(200, 30);
				reloadCategory();
				Category.setValue(m.getCategory());
				
				
				Button addcats = new Button("+");
				addcats.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						String newcat = AddValueWindow.NewValueWindow("Neue Kategorie", "Füge eine neue kategorie hinzu");
						if(!Category.getItems().contains(newcat) && newcat != null && !newcat.trim().isEmpty()){
							settings.addCategory(newcat);
							reloadCategory();
						}
						
					}
				});
							
				seriescatbox.getChildren().addAll(srslbl, Series, addseries, catlbl, Category, addcats);
				
			HBox descriptionframe = new HBox();
				TextArea description = new TextArea();
				description.setPromptText("Beschreibung");
				description.setPrefSize(780, 130);
				description.setText(m.getDescription());
				descriptionframe.getChildren().add(description);
				
			HBox confirmcancelframe = new HBox();
				Button confirm = new Button("Film hinzufügen");
			
				
				confirm.setPrefSize(200, 50);
				confirm.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						// addmovie to database
						int id = m.getID();
						String title = titleinput.getText();
						int ifsk = fsk.getValue();
						int length = 0;
						try{
							length = Integer.parseInt(lengthinput.getText());
						}
						catch (Exception e) {
							// TODO: handle exception
						}
						
						String loc = location.getValue();
						String series = Series.getValue();
						String category = Category.getValue();
						String dsc = description.getText();
						System.out.println(dsc);
						
						//title verification
						if(title != null && !title.trim().isEmpty() && length < 5000){
							
							//loc veification
							if(loc == null || loc.trim().isEmpty() || loc.equals("-")){
								loc = null;
							}
							if(series == null || series.trim().isEmpty() || series.equals("-")){
								series = null;
							}
							if(category == null || category.trim().isEmpty() || category.equals("-")){
								category = null;
							}
							if(dsc == null || dsc.trim().isEmpty() || dsc.equals("-")){
								dsc = null;
							}
							Movie nm = new Movie(id, title, ifsk, length, loc, series, category, dsc);
							DBmanager.deleteMovie(m);
							DBmanager.addMovie(nm);
							
							window.close();
							//new popup("Fehler", "Fehler bei Titel oder Länge", "überprüfe ob du einen namen eingegeben hast,\n er noch nicht existiert und die Länge < 5000 ist");
						}
						else{
							new popup("Fehler", "Fehler bei Titel oder Länge", "überprüfe ob du einen Namen eingegeben hast,\n und die Länge < 5000 ist");
						}
							
					}
				});
			
				Button cancel = new Button("Abbrechen");
				
				
				cancel.setPrefSize(200, 50);
				cancel.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						window.close();
						
					}
				});
				
				
				confirmcancelframe.getChildren().addAll(confirm, cancel);
				
			mainframe.getChildren().addAll(fsklengthbox, seriescatbox, descriptionframe, confirmcancelframe);
			
				
			
		HBox.setMargin(fsk, new Insets(10));
		HBox.setMargin(lengthinput, new Insets(10));
		VBox.setMargin(titlebox, new Insets(10));

		HBox.setMargin(location, new Insets(10));
		HBox.setMargin(loclbl, new Insets(15, 0, 10, 20));
		HBox.setMargin(srslbl, new Insets(15, 0, 10, 10));
		HBox.setMargin(catlbl, new Insets(15, 0, 10, 20));
		HBox.setMargin(description, new Insets(10));
		HBox.setMargin(addloc, new Insets(10));
		HBox.setMargin(Series, new Insets(10));
		HBox.setMargin(addseries, new Insets(10));
		
		HBox.setMargin(Category, new Insets(10));
		HBox.setMargin(addcats, new Insets(10));
		
		Scene scene = new Scene(mainframe, 800, 400);
		window.setScene(scene);
		window.showAndWait();
		return true;
	}
				
	private void reloadCategory() {
		ObservableList<String> cats = FXCollections.observableArrayList();
		for(String s:settings.getCategorys()){
			
			if(s != null && s != "null" && !s.trim().isEmpty()){
				cats.add(s);
				System.out.println(s);
			}
		}
		cats.add("-");
		Category.setItems(cats);
	}

	private void reloadSaveLocs() {
		ObservableList<String> locs = FXCollections.observableArrayList();
		for(String s:settings.getSaveLocations()){
			
			if(s != null && s != "null" && !s.trim().isEmpty()){
				locs.add(s);
			}
		}
		locs.add("-");
		location.setItems(locs);
	}
	
	private void reloadSeries(){
		ObservableList<String> series = FXCollections.observableArrayList();
		for(String s:settings.getSeries()){
			if(s != null && s != "null" && !s.trim().isEmpty()){
				series.add(s);
			}
		}
		series.add("-");
		Series.setItems(series);
	}

	private boolean isInt(String s){
		try{
			int i = Integer.parseInt(s);
			System.out.println("is int");
			return true;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
