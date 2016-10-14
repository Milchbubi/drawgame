package com.drawgame;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.MenuBar.MenuItem;

@SuppressWarnings("serial")
public class MenuComponent extends Panel {

	public static final String CLASSNAME = "menu";
	
	private MenuBar menuBar = new MenuBar();
	
	private MenuBar.Command defaultGameCommand = new MenuBar.Command() {
		@Override
		public void menuSelected(MenuItem selectedItem) {
			// TODO
		}
	};
	
	public MenuComponent() {
		MenuItem menu = menuBar.addItem("Menu", null);
		menu.addItem("Default Game", defaultGameCommand);
		
		setStyleName(CLASSNAME);
		setContent(menuBar);
	}
}
