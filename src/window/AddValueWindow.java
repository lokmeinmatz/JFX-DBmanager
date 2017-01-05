package window;

import java.sql.Connection;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddValueWindow {
	static String answer;
	
	public static String NewValueWindow(String title, String infotext){
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setWidth(250);
		window.setHeight(150);
		VBox mainframe = new VBox();
		Label l = new Label(infotext);
		TextField text = new TextField();
		text.setPromptText(title);
			HBox buttonframe = new HBox();
			Button okbutton = new Button("OK");
			Button Abbrechenbutton = new Button("Abbrechen");
			okbutton.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					if(text.getText() != null || !text.getText().trim().isEmpty()){
						answer = text.getText();
						window.close();
					}
					
				}
			});
			
			Abbrechenbutton.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					answer = null;
					window.close();
				}
			});
			
			buttonframe.getChildren().addAll(okbutton, Abbrechenbutton);
		mainframe.getChildren().addAll(l, text, buttonframe);
		Scene s = new Scene(mainframe);
		window.setScene(s);
		window.showAndWait();
		return answer;
	}

}
