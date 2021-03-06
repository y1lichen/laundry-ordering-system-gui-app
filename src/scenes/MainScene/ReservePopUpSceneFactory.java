package scenes.MainScene;

import java.time.LocalDate;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.UrlList;
import utils.AppData;
import utils.PostRequest;
import utils.alert.PopErrorAlert;

public class ReservePopUpSceneFactory {
	private static class ReserveButtonHandler implements EventHandler<ActionEvent> {

		private Stage modalStage;
		private LocalDate date;
		private ListView<String> listView;

		public ReserveButtonHandler(Stage modalStage, ListView<String> listView, LocalDate date) {
			this.modalStage = modalStage;
			this.date = date;
			this.listView = listView;
		}

		@Override
		public void handle(ActionEvent event) {
			String selectedTime = listView.getSelectionModel().getSelectedItem();
			if (date == null || selectedTime == null)
				return;
			String time = date.toString().concat(String.format("T%s:00", selectedTime));
			String inputJson = String.format("{\"id\": %d, \"token\": \"%s\", \"time\": \"%s\"}", AppData.getId(),
					AppData.getToken(), time);
			Map<String, Object> response = PostRequest.postAndGetJson(UrlList.USER_RESERVE_URL, inputJson);
			int statusCode = (Integer) response.get("statusCode");
			if (statusCode == 417) {
				PopErrorAlert.show("You can only have one reservation per day.");
			}
			MainTabFactory.fetchAllReservation();
			modalStage.close();
		}
	}

	public static Scene create(Stage modalStage, ObservableList<String> availableTimeList, LocalDate date) {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(10);
		Scene scene = new Scene(grid);
		ListView<String> listView = new ListView<>(availableTimeList);
		grid.add(listView, 1, 1);
		Button confirmButton = new Button("Confirm");
		confirmButton.setOnAction(new ReserveButtonHandler(modalStage, listView, date));
		grid.add(confirmButton, 1, 2);
		return scene;
	}
}
