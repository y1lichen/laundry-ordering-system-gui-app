package utils;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.ApplicationMenuBar;

public class NavigateScene {
	
	private static NavigateScene navigateScene = new NavigateScene();
	
	private static Scene scene = null;
	private static VBox vBox = new VBox();
	
	public static void setScene(Scene scene) {
		NavigateScene.scene = scene;
	}
	
	public static void changePane(Pane pane) {
		if (pane.getId() != null && pane.getId().equals("MainScene")) {
			ApplicationMenuBar.enableRefreshMenuItem();
		} else {
			ApplicationMenuBar.disableRefreshMenuItem();
		}
		vBox.getChildren().clear();
		vBox.getChildren().add(ApplicationMenuBar.getMenuBar());
		vBox.getChildren().add(pane);
		scene.setRoot(vBox);
	}

	public static NavigateScene getNavigateScene() {
		return navigateScene;
	}

	public static void setNavigateScene(NavigateScene navigateScene) {
		NavigateScene.navigateScene = navigateScene;
	}
}
