package scenes;

import java.net.http.HttpResponse;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.UrlList;
import scenes.MainScene.MainSceneFactory;
import utils.AppData;
import utils.PostingRequest;

public class LoginSceneFactory {
    // handler for create user
    private static class CreateUserButtonEventHandler implements EventHandler<ActionEvent> {
        private Stage stage;

        public CreateUserButtonEventHandler(Stage stage) {
            super();
            this.stage = stage;
        }
        @Override
        public void handle(ActionEvent event) {
           stage.getScene().setRoot((CreateUserSceneFactory.create(stage)));
        }
    }
    // handler for login button
    private static class LoginButtonEventHandler implements EventHandler<ActionEvent> {
        private TextField idField;
        private TextField psdField;
        private Stage stage;

        public LoginButtonEventHandler(TextField idField, TextField psdField, Stage stage) {
            super();
            this.idField = idField;
            this.psdField = psdField;
            this.stage = stage;
        }

        @Override
        public void handle(ActionEvent event) {
            int id = Integer.parseInt(idField.getText());
            String password = psdField.getText();
            String inputJson = String.format("{\"id\": %d, \"password\": \"%s\"}", id, password);
            HttpResponse<String> response = PostingRequest.postAndGetResponse(UrlList.USER_LOGIN_URL, inputJson);
            if (response.statusCode() == 200) {
                // successfully login
                AppData.setId(id);
                AppData.setPassword(password);
                naviagateToOderingScene(stage);
            } else {
                this.psdField.setText("");
            }
        }
    }

    private static void naviagateToOderingScene(Stage stage) {
        stage.getScene().setRoot((MainSceneFactory.create(stage)));
    }

    public static Pane create(Stage stage) {
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        Text title = new Text("Login");
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
        Button loginButton = new Button("Login");
        loginButton.setOnAction(new LoginButtonEventHandler(userIdTextField, userPasswordTextField, stage));
        Label orTextLabel = new Label("or");
        Button createUserButton = new Button("Create account");
        createUserButton.setOnAction(new CreateUserButtonEventHandler(stage));
        VBox btnVBox = new VBox(loginButton, orTextLabel, createUserButton);
        btnVBox.setSpacing(10);
        btnVBox.setAlignment(Pos.CENTER);
        grid.add(btnVBox, 1, 4);
        return grid;
    }
}
