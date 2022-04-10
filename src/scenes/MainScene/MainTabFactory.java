package scenes.MainScene;

import javafx.scene.control.Tab;

public class MainTabFactory {
   public static Tab create() {
      Tab tab = new Tab();
      tab.setText("Ordering System");
      tab.setClosable(false);
      return tab;
   }
}
