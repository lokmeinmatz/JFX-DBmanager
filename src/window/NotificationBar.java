package window;



import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import objects.Notification;

public class NotificationBar extends HBox{

	
	private Button infobutton, settings;
	private List<Notification> notifications = new ArrayList<>();
	private Boolean isOpen = false;
	public NotificationBar(StackPane root){
		
		settings = new Button("Einstellungen");
		infobutton = new Button("Infos");
		super.getChildren().addAll(settings, infobutton);
		
		for(Node n:super.getChildren()){
			super.setMargin(n, new Insets(5, 10, 5, 10));
		}
		
		infobutton.setOnAction(e -> {
			if(!isOpen){
				Bounds buttonpos = infobutton.localToScene(infobutton.getBoundsInLocal());
			}
			else{
				
			}
		});
		
	}
	
	
	public void addNotification(Notification n){
		if(!notifications.contains(n)){
			notifications.add(n);
		}
	}
}
