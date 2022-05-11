package scenes.MainScene;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainSceneFactory {
	
	public static StackPane create(Stage stage) {
		StackPane pane = new StackPane();
		pane.setId("MainScene");
		TabPane tabPane = new TabPane();
		Tab mainTab = MainTabFactory.create();
		Tab settingsTab = SettingTabFactory.create(stage);
		tabPane.getTabs().addAll(mainTab, settingsTab);
		pane.getChildren().add(tabPane);
		return pane;
	}
}
