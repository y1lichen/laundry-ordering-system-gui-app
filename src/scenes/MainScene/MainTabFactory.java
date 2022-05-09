package scenes.MainScene;


import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.UrlList;
import utils.AppData;
import utils.GetRequest;
import utils.PostRequest;
import utils.alert.PopErrorAlert;

public class MainTabFactory {
	
	private static ObservableList<ReservationData> allReservationData = FXCollections.observableArrayList();

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

	private static void popUpReserveView(ObservableList<String> availableTimeList, DatePicker picker) {
		Stage modalStage = new Stage();
		modalStage.setWidth(500);
		modalStage.setHeight(500);
		modalStage.setResizable(false);
		modalStage.initModality(Modality.WINDOW_MODAL);
		modalStage.setTitle("Reserve");
		modalStage.setScene(ReservePopUpSceneFactory.create(modalStage, availableTimeList, picker));
		modalStage.showAndWait();
	}
	
	private static String getMachineLocationString(int machineNum) {
		int floor = machineNum / 3;
		int num = machineNum % 3;
		return String.format("%df-%d", floor+1, num);
	}
	
	private static String parsingTimeString(String input) {
		String output = input.substring(0,10)+' '+ input.substring(11, input.length() - 3);
		return output;
	}

	public static void fetchAllReservation() {
		String inputJson = String.format("{\"id\": %d, \"password\": \"%s\", \"date\": \"\"}", AppData.getId(), AppData.getPassword());
		Map<String, Object> response = PostRequest.postAndGetJson(UrlList.USER_GET_ALL_RESERVATIONS, inputJson);
		int statusCode = (int) response.get("statusCode");
		allReservationData.clear();
		if (statusCode == 200) {
			String jsonString = (String) response.get("content");
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				int id = jsonObject.getInt("id");
				JSONObject infoObject = jsonObject.getJSONObject("info");
				int machineNum = infoObject.getInt("machine_num");
				String time = infoObject.getString("time");
				ReservationData data = new ReservationData(id, getMachineLocationString(machineNum), parsingTimeString(time));
				allReservationData.add(data);
			}
		}
	}
	
	private static void deleteSelectedItemById(int id) {
		String inputJson = String.format("{\"userId\": %d, \"password\": \"%s\", \"reservationId\": \"%d\"}",
                AppData.getId(), AppData.getPassword(), id);
		HttpResponse<String> response = PostRequest.postAndGetResponse(UrlList.USER_DELETE_RESEVATION_URL, inputJson);
		if (response.statusCode() > 299) {
			PopErrorAlert.show("Unable to remove the reservation.");
		}
	}
	
	public static Tab create() {
		class DeleteMenuItemHandler implements EventHandler<ActionEvent> {
			private TableView<ReservationData> table;
			public DeleteMenuItemHandler(TableView<ReservationData> table) {
				this.table = table;
			}
			@Override
			public void handle(ActionEvent event) {
				ReservationData data = (ReservationData) table.getSelectionModel().getSelectedItem();
				deleteSelectedItemById(data.getId());
				fetchAllReservation();
			}
		}
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
		datePicker.valueProperty().addListener((observable, oldDate, newDate) -> {
			// do something when value of DatePicker is changed
			if (newDate == null) return;
			ObservableList<String> availableTimeList = FXCollections.observableArrayList();
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
			// popup reserve-view
			popUpReserveView(availableTimeList, datePicker);
		}
		);
		datePicker.setDayCellFactory(dayCellFactory);
		//
		TableView<ReservationData> table = new TableView<ReservationData>();
		ContextMenu menu = new ContextMenu();
		MenuItem deleteMenuItem = new MenuItem("delete");
		deleteMenuItem.setOnAction(new DeleteMenuItemHandler(table));
		menu.getItems().add(deleteMenuItem);
		table.setContextMenu(menu);
		TableColumn<ReservationData, Integer> idCol = new TableColumn<>("id");
		TableColumn<ReservationData, String> machineCol = new TableColumn<>("machine"); 
		TableColumn<ReservationData, String> timeCol = new TableColumn<>("time");
		timeCol.setPrefWidth(180);
		//
		idCol.setCellValueFactory(new PropertyValueFactory<ReservationData, Integer>("id"));
		machineCol.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("machine"));
		timeCol.setCellValueFactory(new PropertyValueFactory<ReservationData, String>("time"));
		//
		table.setItems(allReservationData);
		table.setEditable(false);
		table.setMaxWidth(400);
		table.getColumns().add(idCol);
		idCol.setVisible(false);
		table.getColumns().add(machineCol);
		table.getColumns().add(timeCol);
		table.getColumns().forEach(e -> {
			e.setReorderable(false);
			e.setSortable(false);
		});
		mainVBox.getChildren().addAll(datePicker, table);
		vBox.getChildren().add(mainVBox);
		tab.setContent(vBox);
		fetchAllReservation();
		return tab;
	}
}
