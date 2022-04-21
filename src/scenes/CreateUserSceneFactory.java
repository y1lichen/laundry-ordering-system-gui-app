package scenes;


import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import main.UrlList;
import utils.PostRequest;

public class CreateUserSceneFactory {

    private static class CreateUserButtonEventHandler implements EventHandler<ActionEvent> {
        private Stage stage;
        private TextField idField;
        private PasswordField passwordField;
        private PasswordField checkPasswordField;

        public CreateUserButtonEventHandler(Stage stage, TextField idField, PasswordField passwordField, PasswordField checkPasswordField) {
            super();
            this.stage = stage;
            this.idField = idField;
            this.passwordField = passwordField;
            this.checkPasswordField = checkPasswordField;
        }

        @Override
        public void handle(ActionEvent event) {
            int id = Integer.parseInt(idField.getText());
            String password = passwordField.getText();
            String inputJson = String.format("{\"id\": %d, \"password\": \"%s\"}", id, password);
            String response = PostRequest.postAndGetJson(UrlList.USER_CREATE_URL, inputJson);
            JSONObject jsonObject = new JSONObject(response);
            int replyCode = jsonObject.getInt("replyCode");
            // successfully create
            if (replyCode == 1) {
                stage.getScene().setRoot((LoginSceneFactory.create(stage)));
                return;
            } else if (replyCode == 0) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("User already exist.");
                alert.show();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Unable to create user, please try again.");
                alert.show();
            }
            idField.setText("");
            passwordField.setText("");
            checkPasswordField.setText("");
        }
    }

    public static Pane create(Stage stage) {
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        Text title = new Text("Create new account");
        title.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.EXTRA_BOLD, 40));
        grid.add(title, 0, 0, 2, 1);
        Label userIdLabel = new Label("Id:");
        userIdLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.NORMAL, 25));
        grid.add(userIdLabel, 0, 1);
        TextField userIdTextField = new TextField();
        VBox userIdFieldVBox = new VBox();
        grid.add(userIdFieldVBox, 1, 1);
        userIdFieldVBox.getChildren().add(userIdTextField);
        Label tinyLabel = new Label("");
        userIdFieldVBox.getChildren().add(tinyLabel);
        tinyLabel.setTextFill(Color.color(1, 0, 0));
        userIdTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            tinyLabel.setText("");
            if (newValue.length() > 9) {
                userIdTextField.setText(oldValue);
                tinyLabel.setText("StudentId contains only 9 numbers.");
            } else if (!(newValue.matches("\\d*"))) {
                userIdTextField.setText(oldValue);
            }
        });
        Label userPasswordLabel = new Label("Password:");
        userPasswordLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.NORMAL, 25));
        grid.add(userPasswordLabel, 0, 2);
        PasswordField userPasswordTextField = new PasswordField();
        grid.add(userPasswordTextField, 1, 2);
        Label checkPsdLabel = new Label("Type your password again.");
        Label checkPsdTinyLabel = new Label("");
        checkPsdTinyLabel.setTextFill(Color.color(1, 0, 0));
        PasswordField checkPsdField = new PasswordField();
        checkPsdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 0 || newValue.equals(userPasswordTextField.getText())) {
                checkPsdTinyLabel.setText("");
            } else if (!(newValue.equals(userPasswordTextField.getText()))) {
                checkPsdTinyLabel.setText("Doesn't match with the password above.");
            }
        });
        VBox checkPsdVBox = new VBox(checkPsdLabel, checkPsdField, checkPsdTinyLabel);
        checkPsdVBox.setSpacing(5);
        grid.add(checkPsdVBox, 1, 3);
        Button createUserBtn = new Button("Create");
        createUserBtn.setOnAction(
                new CreateUserButtonEventHandler(stage, userIdTextField, userPasswordTextField, checkPsdField));
        grid.add(createUserBtn, 1, 4);
        return grid;
    }
}
