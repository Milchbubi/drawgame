package com.drawgame.client.drawcomponent;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ZoomWidget extends VerticalPanel {

	public static final String CLASSNAME = "zoomWidget";
	public static final String CLASSNAME_FREEZE_DRAWING_BUTTON = CLASSNAME + "-freezeDrawingButton";
	public static final String CLASSNAME_FREEZE_TRANSPARENT_PANEL = CLASSNAME + "-freezeTransparentPanel";
	public static final String CLASSNAME_FREEZE_TRANSPARENT_PANEL_LABEL = CLASSNAME_FREEZE_TRANSPARENT_PANEL + "-label";
	public static final String CLASSNAME_CANVAS_SIZE_BUTTON = CLASSNAME + "-canvasSizeButton";
	
	private int canvasWidth;
	private int canvasHeight;
	
	private CanvasSizeChangeHandler onCanvasSizeChangeHandler = null;
	
	private ToggleButton freezeDrawingButton = new ToggleButton("Zoom", "Draw");
	private SimplePanel freezeTransparentPanel = new SimplePanel();
	private Label freezeTransparentPanelLabel = (new Label("Use one finger to move, two to zoom."));
	private Button incCanvasSizeButton = new Button("*1.25");
	private Button decCanvasSizeButton = new Button("*0.8");
	private Button restoreCanvasSizeButton = new Button("1.0");
	
	public ZoomWidget() {
		
		setStyleName(CLASSNAME);
		freezeDrawingButton.setStyleName(CLASSNAME_FREEZE_DRAWING_BUTTON);
		freezeTransparentPanel.setStyleName(CLASSNAME_FREEZE_TRANSPARENT_PANEL);
		freezeTransparentPanelLabel.setStyleName(CLASSNAME_FREEZE_TRANSPARENT_PANEL_LABEL);
		incCanvasSizeButton.addStyleName(CLASSNAME_CANVAS_SIZE_BUTTON);
		decCanvasSizeButton.addStyleName(CLASSNAME_CANVAS_SIZE_BUTTON);
		restoreCanvasSizeButton.addStyleName(CLASSNAME_CANVAS_SIZE_BUTTON);
		
		freezeTransparentPanel.setWidget(freezeTransparentPanelLabel);
		add(freezeDrawingButton);
		add(incCanvasSizeButton);
		add(decCanvasSizeButton);
		add(restoreCanvasSizeButton);
		
		restoreCanvasSize();
		
		freezeDrawingButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (freezeDrawingButton.isDown()) {
					add(freezeTransparentPanel);
				} else {
					remove(freezeTransparentPanel);
				}
			}
		});
		
		incCanvasSizeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				canvasWidth *= 1.25;
				canvasHeight *= 1.25;
				setFreezeTransparentSize();
				if (onCanvasSizeChangeHandler != null) onCanvasSizeChangeHandler.onCanvasSizeChange();
			}
		});
		
		decCanvasSizeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				canvasWidth *= 0.8;
				canvasHeight *= 0.8;
				setFreezeTransparentSize();
				if (onCanvasSizeChangeHandler != null) onCanvasSizeChangeHandler.onCanvasSizeChange();
			}
		});
		
		restoreCanvasSizeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				restoreCanvasSize();
			}
		});
		
	}
	
	private void restoreCanvasSize() {
		canvasWidth = Window.getClientWidth();
		canvasHeight = Window.getClientHeight();
		setFreezeTransparentSize();
		if (onCanvasSizeChangeHandler != null) onCanvasSizeChangeHandler.onCanvasSizeChange();
	}
	
	private void setFreezeTransparentSize() {
		freezeTransparentPanel.setWidth(canvasWidth + "px");
		freezeTransparentPanel.setHeight(canvasHeight + "px");
	}
	
	public int getCanvasWidth() {
		return canvasWidth;
	}
	
	public int getCanvasHeight() {
		return canvasHeight;
	}
	
	public void setCanvasSizeChangeHandler(CanvasSizeChangeHandler onCanvasSizeChangeHandler) {
		this.onCanvasSizeChangeHandler = onCanvasSizeChangeHandler;
	}
	
	public interface CanvasSizeChangeHandler {
		
		public void onCanvasSizeChange();
		
	}
	
}
