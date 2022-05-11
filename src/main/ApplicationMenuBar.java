package main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import scenes.MainScene.MainTabFactory;


public final class ApplicationMenuBar extends MenuBar {
	
	private static ApplicationMenuBar menuBar = new ApplicationMenuBar();
	private static Menu menu;
	private static MenuItem refreshItem;
	private static MenuItem quitItem;
	// constructor
	public ApplicationMenuBar() {
		super();
		this.useSystemMenuBarProperty().set(true);
		this.setUseSystemMenuBar(true);
		menu = new Menu("File");
		refreshItem = new MenuItem("Refresh");
		refreshItem.setDisable(true);
		refreshItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainTabFactory.fetchAllReservation();
			}
		});
		menu.getItems().add(refreshItem);
		addQuitMenuItem();
		this.getMenus().add(menu);
	}
	
	private void addQuitMenuItem() {
		String os = System.getProperty("os.name");
		if (os != null && !(os.startsWith("Mac"))) {
			quitItem = new MenuItem("Quit");
			quitItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Platform.exit();
				}
			});
			menu.getItems().add(quitItem);
		}
	}
	
	public static void enableRefreshMenuItem() {
		refreshItem.setDisable(false);
	}
	
	public static void addMenuToScene(Scene scene) {
		Parent rootNode = scene.getRoot();
		if (rootNode instanceof Pane) {
			Pane rootPane = (Pane) rootNode;
			String id = rootPane.getId();
			if (id != null && id.equals("MainScene")) {
				refreshItem.setDisable(false);
			} else {
				refreshItem.setDisable(true);
			}
			rootPane.getChildren().add(menuBar);
		}
	}

	public static ApplicationMenuBar getMenuBar() {
		return menuBar;
	}

	public static void setMenuBar(ApplicationMenuBar menuBar) {
		ApplicationMenuBar.menuBar = menuBar;
	}
}
