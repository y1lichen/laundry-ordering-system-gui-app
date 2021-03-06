package scenes;

import java.net.http.HttpResponse;
import java.util.Map;

import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import main.ApplicationMenuBar;
import main.UrlList;
import scenes.MainScene.MainSceneFactory;
import utils.AppData;
import utils.NavigateScene;
import utils.PostRequest;

public class LoginSceneFactory {
	// handler for create user
	private static class CreateUserButtonEventHandler implements EventHandler<ActionEvent> {
		private Stage stage;

		public CreateUserButtonEventHandler(Stage stage) {
			super();
			this.stage = stage;
		}

		public void handle(ActionEvent event) {
			Scene scene = stage.getScene();
			NavigateScene.changePane(CreateUserSceneFactory.create(stage));
		}
	}

	// handler for login button
	private static class LoginButtonEventHandler implements EventHandler<ActionEvent> {
		private TextField idField;
		private TextField psdField;
		private Label errorLabel;
		private Stage stage;

		public LoginButtonEventHandler(TextField idField, TextField psdField, Label errorLabel, Stage stage) {
			super();
			this.idField = idField;
			this.psdField = psdField;
			this.errorLabel = errorLabel;
			this.stage = stage;
		}

		public void handle(ActionEvent event) {
			int id = Integer.parseInt(idField.getText());
			String password = psdField.getText();
			String inputJson = String.format("{\"id\": %d, \"password\": \"%s\"}", id, password);
			Map<String, Object> response = PostRequest.postAndGetJson(UrlList.USER_LOGIN_URL, inputJson);
			int statusCode = (int) response.get("statusCode");
			if (statusCode == 200) {
				// successfully login
				String jsonString = (String) response.get("content");
				JSONObject json = new JSONObject(jsonString);
				AppData.setId(id);
				AppData.setToken(json.getString("token"));
				naviagateToOderingScene(stage);
			} else {
				this.psdField.setText("");
				this.errorLabel.setText("That doesn't work. Please try again.");
			}
		}
	}

	private static void naviagateToOderingScene(Stage stage) {
		Scene scene = stage.getScene();
		NavigateScene.changePane(MainSceneFactory.create(stage));
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
		final Label tinyLabel = new Label("");
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
		VBox passwordFieldVBox = new VBox();
		passwordFieldVBox.setSpacing(5);
		PasswordField userPasswordTextField = new PasswordField();
		Label errorLabel = new Label("");
		errorLabel.setTextFill(Color.color(1, 0, 0));
		passwordFieldVBox.getChildren().add(userPasswordTextField);
		passwordFieldVBox.getChildren().add(errorLabel);
		grid.add(passwordFieldVBox, 1, 2);
		Button loginButton = new Button("Login");
		loginButton.setOnAction(new LoginButtonEventHandler(userIdTextField, userPasswordTextField, errorLabel, stage));
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
