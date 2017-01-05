package settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import objects.Movie;

public class exportmanager {
	
	
	public static void exportWindow(TableView<Movie> table){
		Stage s = new Stage();
		File f = null;
		
		FileChooser filechooser = new FileChooser();
		filechooser.setTitle("Export als txt");
		filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Textdatei", ".txt"));
		filechooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Textdatei", ".txt"));
		f = filechooser.showSaveDialog(s);
		if(f != null){
			try {
				PrintWriter out = new PrintWriter(f);
				for(Movie m:table.getSelectionModel().getSelectedItems()){
					out.print(m.getID()+" - "+m.getTitle());
					out.print(" | FSK: "+m.getFSK());
					if(m.getLength() > 0){
						out.print(" | Länge: "+m.getLength());
					}
					if(m.getLength() > 0){
						out.print(" | Länge: "+m.getLength());
					}
					if(m.getCategory() != null){
						out.print(" | Kategorie: "+m.getCategory());
					}
					if(m.getSeries() != null){
						out.print(" | Serie: "+m.getSeries());
					}
					if(m.getLocation() != null){
						out.print(" | Speicherort: "+m.getLocation());
					}
					if(m.getDescription() != null){
						out.print(" | Beschreibung: "+m.getDescription());
					}
					out.println();
					
				}
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
}
