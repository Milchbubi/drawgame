package com.drawgame;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.MenuBar.MenuItem;

@SuppressWarnings("serial")
public class MenuComponent extends Panel {

	public static final String CLASSNAME = "menu";
	
	private MenuBar menuBar = new MenuBar();
	
//	private MenuBar.Command defaultGameCommand = new MenuBar.Command() {
//		@Override
//		public void menuSelected(MenuItem selectedItem) {
//			// TODO
//		}
//	};
	
	private MenuBar.Command clearDefaultGameCommand = new MenuBar.Command() {
		@Override
		public void menuSelected(MenuItem selectedItem) {
			GameStorage.defaultGame.clearDrawing();
		}
	};
	
	public MenuComponent() {
		MenuItem menu = menuBar.addItem("Menu", null);
//		menu.addItem("Default Game", defaultGameCommand);
		menu.addItem("clear Default Game", clearDefaultGameCommand);
		
		setStyleName(CLASSNAME);
		setContent(menuBar);
	}
}
