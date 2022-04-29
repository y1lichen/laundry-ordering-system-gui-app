package utils.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PopErrorAlert {
	public static void show(String errorMsg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(errorMsg);
        alert.show();
	}
}
