package com.drawgame.client.drawcomponent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class ColorPickerWidget extends HorizontalPanel {

	public static final String CLASSNAME = "colorPickerWidget";
	public static final String CLASSNAME_BUTTON = CLASSNAME + "-button";
	public static final String CLASSNAME_WHITE_WITH_SHADOW = "whiteWithShadow";
	
	private ColorButton buttonBlack = new ColorButton("#000");
	private ColorButton buttonRed = new ColorButton("#F00");
	private ColorButton buttonBrown = new ColorButton("#730");
	private ColorButton buttonOrange = new ColorButton("#F70");
	private ColorButton buttonYellow = new ColorButton("#FF0");
	private ColorButton buttonGreen = new ColorButton("#0F0");
	private ColorButton buttonTeal = new ColorButton("#0FF");
	private ColorButton buttonBlue = new ColorButton("#00F");
	private ColorButton buttonGray = new ColorButton("#777");
	private ColorButton buttonWhite = new ColorButton("#FFF");
	
	private ColorButton selectedButton = buttonBlack;
	
	public ColorPickerWidget() {
		setStyleName(CLASSNAME);
		
		buttonBlack.select();
		
		add(buttonBlack);
		add(buttonRed);
		add(buttonBrown);
		add(buttonOrange);
		add(buttonYellow);
		add(buttonGreen);
		add(buttonTeal);
		add(buttonBlue);
		add(buttonGray);
		add(buttonWhite);
	}
	
	public String getSelectedColor() {
		return selectedButton.getColor();
	}
	
	private class ColorButton extends Button {
		
		private String color;
		
		public ColorButton(String color) {
			addStyleName(CLASSNAME_BUTTON);
			addStyleName(CLASSNAME_WHITE_WITH_SHADOW);
			
			this.color = color;
			
			getElement().getStyle().setBackgroundColor(color);
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					select();
				}
			});
		}
		
		public void select() {
			selectedButton.setText("");
			selectedButton = this;
			selectedButton.setText("X");
		}
		
		public String getColor() {
			return color;
		}
		
	}
	
}
