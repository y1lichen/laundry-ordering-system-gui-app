package scenes.MainScene;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
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
		DatePicker datePicker = new DatePicker();
		datePicker.valueProperty().addListener((observable, oldDate, newDate) -> {
			// do something when value of DatePicker is changed
			String selectedDateString = newDate.toString();
			String urlString = UrlList.GET_AVALIABLE_RESERVATION_TIME_URL.concat("?time=" + selectedDateString);
			String resultString = GetRequest.getJsonString(urlString);
			System.out.println(resultString);
		});
		datePicker.setDayCellFactory(dayCellFactory);
		mainVBox.getChildren().add(datePicker);
		vBox.getChildren().add(mainVBox);
		tab.setContent(vBox);
		return tab;
	}
}
