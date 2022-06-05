package scenes.MainScene;

import java.net.http.HttpResponse;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.ApplicationMenuBar;
import main.UrlList;
import scenes.LoginSceneFactory;
import utils.AppData;
import utils.NavigateScene;
import utils.PostRequest;
import utils.alert.PopErrorAlert;

public class SettingTabFactory {
   private static class ChangePasswordButtonEventHandler implements EventHandler<ActionEvent> {

      private Stage stage;
      
      public ChangePasswordButtonEventHandler(Stage stage) {
         this.stage = stage;
      }
      
      @Override
      public void handle(ActionEvent event) {
         Stage popupWindow = new Stage();
         popupWindow.setWidth(500);
         popupWindow.setHeight(280);
         popupWindow.setResizable(false);
         popupWindow.initModality(Modality.APPLICATION_MODAL);
         popupWindow.setTitle("Change password");
         popupWindow.setScene(ChangePasswordPopupSceneFactory.create(stage, popupWindow));
         popupWindow.showAndWait();
      }
   }

   private static class LogoutButtonEventHandler implements EventHandler<ActionEvent> {

      private Stage stage;

      public LogoutButtonEventHandler(Stage stage) {
         this.stage = stage;
      }

      @Override
      public void handle(ActionEvent event) {
         int id = AppData.getId();
         String token = AppData.getToken();
         String inputJson = String.format("{\"id\": %d, \"token\": \"%s\"}", id, token);
         HttpResponse<String> response = PostRequest.postAndGetResponse(UrlList.USER_LOGOUT_URL, inputJson);
         if (response.statusCode() == 200) {
            // successfully logout
            NavigateScene.changePane(LoginSceneFactory.create(stage));
         } else {
            // error
        	 PopErrorAlert.show("Unable to logout, please try again.");
         }
      }
   }

   public static Tab create(Stage stage) {
      Tab tab = new Tab();
      tab.setText("Settings");
      tab.setClosable(false);
      VBox mainVbox = new VBox();
      mainVbox.setSpacing(30);
      Label titleLabel = new Label("Settings");
      titleLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.EXTRA_BOLD, 30));
      titleLabel.setPadding(new Insets(10, 0, 0, 10));
      mainVbox.getChildren().add(titleLabel);
      VBox buttonVBox = new VBox();
      buttonVBox.setSpacing(20);
      buttonVBox.setAlignment(Pos.CENTER);
      Button changePasswordButton = new Button("Change Password");
      changePasswordButton.setOnAction(new ChangePasswordButtonEventHandler(stage));
      Button logoutButton = new Button("Logout");
      logoutButton.setOnAction(new LogoutButtonEventHandler(stage));
      buttonVBox.getChildren().add(changePasswordButton);
      buttonVBox.getChildren().add(logoutButton);
      mainVbox.getChildren().add(buttonVBox);
      tab.setContent(mainVbox);
      return tab;
   }
}
