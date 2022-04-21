package scenes.MainScene;

import java.net.http.HttpResponse;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.UrlList;
import scenes.LoginSceneFactory;
import utils.AppData;
import utils.PostRequest;

public class ChangePasswordPopupSceneFactory {

    private static boolean checkMatchOriginalPassword(String originalPassword) {
        if (originalPassword.equals(AppData.getPassword()))
            return true;
        return false;
    }

    public static class ConfirmButtonEventHandler implements EventHandler<ActionEvent> {
        Stage stage;
        Stage modalStage;
        PasswordField origninalPasswordField;
        PasswordField newPasswordField;
        PasswordField checkNewPasswordField;
        Label tinyLabel;

        public ConfirmButtonEventHandler(Stage stage, Stage modalStage, PasswordField originalPasswordField, PasswordField newPasswordField, PasswordField checkNewPasswordField, Label tinyLabel) {
            this.stage = stage;
            this.origninalPasswordField = originalPasswordField;
            this.newPasswordField = newPasswordField;
            this.checkNewPasswordField = checkNewPasswordField;
            this.tinyLabel = tinyLabel;
        }

        @Override
        public void handle(ActionEvent event) {
            String originalPassword = origninalPasswordField.getText();
            String newPassword = newPasswordField.getText();
            // check if original password is correct
            if (!(checkMatchOriginalPassword(originalPassword))) {
                origninalPasswordField.setText("");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Your original password isn't correct.");
                alert.show();
                return;
            } else if (newPassword.isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("New password can't be empty.");
                alert.show();
                return;
            } else if (!(checkNewPasswordField.getText().equals(newPassword))) {
                newPasswordField.setText("");
                checkNewPasswordField.setText("");
                tinyLabel.setText("Not match!");
            	return;
            }
            // change password
            String inputJson = String.format("{\"id\": %d, \"oldPassword\": \"%s\", \"newPassword\": \"%s\"}",
                    AppData.getId(), originalPassword, newPassword);
            HttpResponse<String> response = PostRequest.postAndGetResponse(UrlList.USER_CHANGE_PASSWORD_URL, inputJson);
            if (response.statusCode() == 200) {
                stage.getScene().setRoot((LoginSceneFactory.create(stage)));
                modalStage.close();
            } else {
                // if error occurs ...
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Unable to change your password.");
                alert.show();
            }
        }
    }

    public static Scene create(Stage stage, Stage modalStage) {
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(pane);
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        Label orginalPasswordLabel = new Label("Original password: ");
        PasswordField originalPasswordField = new PasswordField();
        pane.add(orginalPasswordLabel, 0, 1);
        pane.add(originalPasswordField, 1, 1);
        Label newPasswordLabel = new Label("New password: ");
        PasswordField newPasswordField = new PasswordField();
        pane.add(newPasswordLabel, 0, 2);
        pane.add(newPasswordField, 1, 2);
        PasswordField checkNewPasswordField = new PasswordField();
        pane.add(checkNewPasswordField, 1, 3);
        Label tinyLabel = new Label("Type your password again.");
        newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
        	if (newValue.length() != 0) {
        		tinyLabel.setText("Type your password again.");
        	}
        });
        pane.add(tinyLabel, 1, 4);
        Button confirmButton = new Button("Comfirm");
        confirmButton.setOnAction(new ConfirmButtonEventHandler(stage, modalStage, originalPasswordField, newPasswordField, checkNewPasswordField, tinyLabel));
        pane.add(confirmButton, 1, 5);
        return scene;
    }
}
