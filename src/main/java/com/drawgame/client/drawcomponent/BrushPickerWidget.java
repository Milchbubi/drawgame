package com.drawgame.client.drawcomponent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

public class BrushPickerWidget extends FlowPanel {

	public static final String CLASSNAME = "brushPickerWidget";
	public static final String CLASSNAME_BUTTON = CLASSNAME + "-button";
	public static final String CLASSNAME_WHITE_WITH_SHADOW = "whiteWithShadow";
	
	private BrushButton button1px = new BrushButton(1.0);
	private BrushButton button3px = new BrushButton(3.0);
	private BrushButton button5px = new BrushButton(5.0);
	private BrushButton button9px = new BrushButton(9.0);
	private BrushButton button17px = new BrushButton(17.0);
	private BrushButton button33px = new BrushButton(33.0);
	private BrushButton button65px = new BrushButton(65.0);
	
	private BrushButton selectedButton = button9px;
	
	public BrushPickerWidget() {
		setStyleName(CLASSNAME);
		
		button9px.select();
		
		add(button1px);
		add(button3px);
		add(button5px);
		add(button9px);
		add(button17px);
		add(button33px);
		add(button65px);
	}
	
	public double getSelectedThickness() {
		return selectedButton.getThickness();
	}
	
	private class BrushButton extends Button {
		
		private double thickness;
		
		public BrushButton(double thickness) {
			addStyleName(CLASSNAME_BUTTON);
			addStyleName(CLASSNAME_WHITE_WITH_SHADOW);
			
			this.thickness = thickness;
			
			setButtonContent("");
			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					select();
				}
			});
		}
		
		private void setButtonContent(String text) {
			String style = "\""
					+ "display: table-cell;"
					+ "vertical-align: middle;"
					+ "margin: auto;"
					+ "background: #000;"
					+ "min-width: " + thickness + "px;"
					+ "height: " + thickness + "px;"
					+ "border-radius: " + thickness/2 + "px"
					+ "\"";
			String html = "<div style=" + style + ">"
					+ text
					+ "</div>";
			
			setHTML(html);
		}
		
		public void select() {
			selectedButton.setButtonContent("");
			selectedButton = this;
			selectedButton.setButtonContent("X");
		}
		
		public double getThickness() {
			return thickness;
		}
		
	}
}
