package scenes.MainScene;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import scenes.MainScene.MainTab.MainTabFactory;

public class MainSceneFactory {
	public static TabPane create(Stage stage) {
		TabPane pane = new TabPane();
		Tab mainTab = MainTabFactory.create();
		Tab settingsTab = SettingTabFactory.create(stage);
		pane.getTabs().addAll(mainTab, settingsTab);
		return pane;
	}
}
