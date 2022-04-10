package main;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scenes.LoginSceneFactory;

public class MainApplication extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Laundry Ordering System");
		Scene scene = new Scene(new Parent() {
		});
		scene.setRoot(LoginSceneFactory.create(primaryStage));
		primaryStage.setScene(scene);
		primaryStage.setWidth(600);
		primaryStage.setHeight(800);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}