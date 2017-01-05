package window;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class popup {
	
	public popup(String windowtitle, String header, String text){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(windowtitle);
		alert.setHeaderText(header);
		alert.setContentText(text);
		alert.showAndWait();
	}
}
