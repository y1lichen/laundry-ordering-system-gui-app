package scenes.MainScene;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import main.UrlList;
import utils.AppData;
import utils.GetRequest;

public class MainTabFactory {

	final static Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
		@Override
		public DateCell call(DatePicker datePicker) {
			return new DateCell() {
				public void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (item.isAfter(LocalDate.now().plusDays(1)) || item.isBefore(LocalDate.now())) {
						setDisable(true);
					}
				};
			};
		}
	};
	
	private static String parsingJsonTimeString(String input) {
		Pattern pattern = Pattern.compile("(\\S+)\\s+(\\S+)");
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			String timeString = matcher.group(2);
			return timeString.substring(0, timeString.length() - 3);
		}
		return null;
	}

	public static Tab create() {
		Tab tab = new Tab();
		tab.setText("Ordering System");
		tab.setClosable(false);
		VBox vBox = new VBox();
		vBox.setSpacing(20);
		Label titleLabel = new Label(String.format("Welcome, %d", AppData.getId()));
		titleLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.EXTRA_BOLD, 30));
		titleLabel.setPadding(new Insets(10, 0, 0, 10));
		vBox.getChildren().add(titleLabel);
		VBox mainVBox = new VBox();
		mainVBox.setSpacing(15);
		mainVBox.setPadding(new Insets(25, 0, 0, 0));
		mainVBox.setAlignment(Pos.CENTER);
		//
		DatePicker datePicker = new DatePicker();
		//
		ObservableList<String> availableTimeList = FXCollections.observableArrayList();
		ListView<String> listView = new ListView<>(availableTimeList);
		datePicker.valueProperty().addListener((observable, oldDate, newDate) -> {
			// do something when value of DatePicker is changed
			String selectedDateString = newDate.toString();
			String urlString = UrlList.GET_AVAILABLE_RESERVATION_TIME_URL.concat("?date=" + selectedDateString);
			GetRequest request = new GetRequest();
			request.sendRequest(urlString);
			int responseCode = request.getResponseCode();
			if (responseCode < 299) {
				JSONObject json = new JSONObject(request.getContent());
				if (json.getInt("replyCode") > 0) {
					JSONArray jsonTimeList = json.getJSONArray("availableTimeList");
					for (int i=0; i<jsonTimeList.length(); i++) {
						String timeString = jsonTimeList.getString(i);
						availableTimeList.add(parsingJsonTimeString(timeString));
					}
				}
			}
			request.closeConnection();
		}
		);
		datePicker.setDayCellFactory(dayCellFactory);
		mainVBox.getChildren().addAll(datePicker, listView);
		vBox.getChildren().add(mainVBox);
		tab.setContent(vBox);
		return tab;
	}
}
