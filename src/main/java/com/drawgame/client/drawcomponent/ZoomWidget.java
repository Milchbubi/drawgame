package com.drawgame.client.drawcomponent;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
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
	
	private final Canvas referencedCanvas;
	private CanvasSizeChangeHandler onCanvasSizeChangeHandler = null;
	
	private ToggleButton freezeDrawingButton = new ToggleButton("Zoom", "Draw");
	private SimplePanel freezeTransparentPanel = new SimplePanel();
	private Label freezeTransparentPanelLabel = (new Label("Use one finger to move, two to zoom."));
	private Button incCanvasSizeButton = new Button("*1.25");
	private Button decCanvasSizeButton = new Button("*0.8");
	private Button restoreCanvasSizeButton = new Button("1.0");
	
	public ZoomWidget(Canvas referenceCanvas) {
		this.referencedCanvas = referenceCanvas;
		
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
		
		referencedCanvas.addAttachHandler(new Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				restoreCanvasSize();
			}
		});

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
				referencedCanvas.setCoordinateSpaceWidth( (int)(referencedCanvas.getCoordinateSpaceWidth()*1.25) );
				referencedCanvas.setCoordinateSpaceHeight( (int)(referencedCanvas.getCoordinateSpaceHeight()*1.25) );
				
				if (onCanvasSizeChangeHandler != null) onCanvasSizeChangeHandler.onCanvasSizeChange();
			}
		});
		
		decCanvasSizeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				referencedCanvas.setCoordinateSpaceWidth( (int)(referencedCanvas.getCoordinateSpaceWidth()*0.8) );
				referencedCanvas.setCoordinateSpaceHeight( (int)(referencedCanvas.getCoordinateSpaceHeight()*0.8) );
				
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
		referencedCanvas.setCoordinateSpaceWidth( referencedCanvas.getOffsetWidth() );
		referencedCanvas.setCoordinateSpaceHeight( referencedCanvas.getOffsetHeight() );
		
		if (onCanvasSizeChangeHandler != null) onCanvasSizeChangeHandler.onCanvasSizeChange();
	}
	
	public void setCanvasSizeChangeHandler(CanvasSizeChangeHandler onCanvasSizeChangeHandler) {
		this.onCanvasSizeChangeHandler = onCanvasSizeChangeHandler;
	}
	
	public Coordinate canvasPosFromPixelPos(int clientX, int clientY) {
		return new Coordinate(
				(int)(clientX * referencedCanvas.getCoordinateSpaceWidth() / referencedCanvas.getOffsetWidth()), 
				(int)(clientY * referencedCanvas.getCoordinateSpaceHeight() / referencedCanvas.getOffsetHeight())
				);
	}
	
	public interface CanvasSizeChangeHandler {
		
		public void onCanvasSizeChange();
		
	}
	
}
