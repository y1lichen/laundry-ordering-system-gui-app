package scenes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.UrlList;
import utils.NavigateScene;
import utils.PostRequest;
import utils.alert.PopErrorAlert;

public class CreateUserSceneFactory {

	private static class CreateUserButtonEventHandler implements EventHandler<ActionEvent> {
		private Stage stage;
		private TextField idField;
		private PasswordField passwordField;
		private PasswordField checkPasswordField;

		public CreateUserButtonEventHandler(Stage stage, TextField idField, PasswordField passwordField,
				PasswordField checkPasswordField) {
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
			int statusCode = PostRequest.postAndGetStatusCode(UrlList.USER_CREATE_URL, inputJson);
			if (statusCode == 409) {
				PopErrorAlert.show("User already exist.");
			} else if (statusCode == 200) {
				goLoginScene(stage);
				return;
			} else {
				PopErrorAlert.show("Unable to create user, please try again.");
			}
			idField.setText("");
			passwordField.setText("");
			checkPasswordField.setText("");
		}
	}
	
	private static void goLoginScene(Stage stage) {
		NavigateScene.changePane(LoginSceneFactory.create(stage));
	}

	public static Pane create(Stage stage) {
		BorderPane pane = new BorderPane();
		GridPane grid = new GridPane();
		grid.setVgap(20);
		grid.setHgap(10);
		grid.setAlignment(Pos.CENTER);
		Text title = new Text("Create new account");
		title.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.EXTRA_BOLD, 40));
		grid.add(title, 0, 0, 2, 1);
		Button backButton = new Button();
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				goLoginScene(stage);
			}
		});
		Image image;
		try {
			image = new Image(new FileInputStream("assets/go-back.png"), 16, 16, true, true);
			backButton.setGraphic(new ImageView(image));
		} catch (FileNotFoundException e) {
			PopErrorAlert.show(e.getMessage());
			backButton.setText("Go back");
		}
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
		pane.setTop(backButton);
		pane.setCenter(grid);
		return pane;
	}
}
