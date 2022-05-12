package utils;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.ApplicationMenuBar;

public class NavigateScene {
	
	private static NavigateScene navigateScene = new NavigateScene();
	
	private static Scene scene = null;
	private static BorderPane borderPane = new BorderPane();
	
	public static void setScene(Scene scene) {
		NavigateScene.scene = scene;
	}
	
	public static void changePane(Pane pane) {
		if (pane.getId() != null && pane.getId().equals("MainScene")) {
			ApplicationMenuBar.enableRefreshMenuItem();
		} else {
			ApplicationMenuBar.disableRefreshMenuItem();
		}
		borderPane.getChildren().clear();
		borderPane.setTop(ApplicationMenuBar.getMenuBar());
		borderPane.setCenter(pane);
		scene.setRoot(borderPane);
	}

	public static NavigateScene getNavigateScene() {
		return navigateScene;
	}

	public static void setNavigateScene(NavigateScene navigateScene) {
		NavigateScene.navigateScene = navigateScene;
	}
}
