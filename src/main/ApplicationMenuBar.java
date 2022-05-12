package main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Mac")) {
			this.setUseSystemMenuBar(true);
		}
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
	
	public static void disableRefreshMenuItem() {
		refreshItem.setDisable(true);
	}

	public static ApplicationMenuBar getMenuBar() {
		return menuBar;
	}

	public static void setMenuBar(ApplicationMenuBar menuBar) {
		ApplicationMenuBar.menuBar = menuBar;
	}
}
