package settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objects.Datum;

public class backupmanager {

	public static void askforBackup(){
		File maindb = new File("Movies.sqlite");
		List<File> backupfiles = new ArrayList<>();
		try{
			for(File f:new File("backup/").listFiles()){
				System.out.println(f.getName());
				backupfiles.add(f);
			}
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		
		Datum aktuell = new Datum();
		int day = LocalDate.now().getDayOfMonth();
		int month = LocalDate.now().getMonthValue();
		int year = LocalDate.now().getYear();
		aktuell.setDay(day);
		aktuell.setMonth(month);
		aktuell.setYear(year);
		boolean alreadybackuped = false;
		Datum lastbackup = new Datum(1, 1, 1);
		for(File f: backupfiles){
			String[] bdsarray = f.getName().replace(".sqlite", "").split("_");
			Datum backupdatum = new Datum(Integer.parseInt(bdsarray[0]), Integer.parseInt(bdsarray[1]), Integer.parseInt(bdsarray[2]));
			if(backupdatum.getYear() > lastbackup.getYear() && backupdatum.getMonth() > lastbackup.getMonth() && backupdatum.getDay() > lastbackup.getDay()){
				lastbackup = backupdatum;
			}
		}
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Backup-Manager");
		window.setWidth(350);
		window.setHeight(150);
		window.setResizable(false);
		
		VBox mainframe = new VBox();
		Label labl = new Label("Möchten sie ein Backup der Datenbank anlegen?\n(Das letzte Backup war am "+String.valueOf(lastbackup.getDay())+"."
				+String.valueOf(lastbackup.getMonth())+"."+String.valueOf(lastbackup.getYear())+")");
		
		HBox buttonbox = new HBox();
		Button okbutton = new Button("Ja");
		okbutton.setOnAction(e -> {
			backupdata(aktuell);
			window.close();
		});
		Button cancelbutton = new Button("nein");
		cancelbutton.setOnAction(e -> {
			window.close();
		});
		buttonbox.setMargin(okbutton, new Insets(10));
		buttonbox.setMargin(cancelbutton, new Insets(10));
		buttonbox.getChildren().addAll(okbutton, cancelbutton);
		
		mainframe.getChildren().addAll(labl, buttonbox);
		
		Scene s = new Scene(mainframe);
		window.setScene(s);
		
		window.show();
		
		
	}
	
	private static void backupdata(Datum aktuell){
		String newfilename = aktuell.getDay()+"_"+aktuell.getMonth()+"_"+aktuell.getYear()+".sqlite";
		File newfile = new File("backup/"+newfilename);
		File fromfile = new File("Movies.sqlite");
		try {
			Files.copy(fromfile.toPath() , newfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
