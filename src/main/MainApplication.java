package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scenes.LoginSceneFactory;
import utils.AppData;
import utils.PostRequest;

public class MainApplication extends Application {

	private static Scene scene;
	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Laundry Ordering System");
		scene = new Scene(new Parent() {
		});
		scene.setRoot(LoginSceneFactory.create(primaryStage));
		ApplicationMenuBar.addMenuToScene(scene);
		primaryStage.setScene(scene);
		primaryStage.setWidth(600);
		primaryStage.setHeight(800);
		primaryStage.setOnCloseRequest(e -> Platform.exit());
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		if (AppData.getPassword() != null) {
			int id = AppData.getId();
			String password = AppData.getPassword();
			String inputJson = String.format("{\"id\": %d, \"password\": \"%s\"}", id, password);
			PostRequest.postAndGetResponse(UrlList.USER_LOGOUT_URL, inputJson);
		}
		super.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}


}