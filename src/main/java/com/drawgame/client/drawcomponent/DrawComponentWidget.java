package com.drawgame.client.drawcomponent;

import java.util.ArrayList;

import com.drawgame.client.drawcomponent.ZoomWidget.CanvasSizeChangeHandler;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DrawComponentWidget extends VerticalPanel {

	public static final String CLASSNAME = "drawcomponent";
	public static final String CLASSNAME_CANVAS = CLASSNAME + "-canvas";
	
	public static final int DRAW_STROKE_ANIMATED_COUNT = 4;
	public static final int DRAW_STROKE_ANIMATED_REDRAW_COUNT = 2;

	private Drawing drawing = new Drawing();
	private Stroke currentStroke = null;
	private long currentStrokeStartedMillis;
	
	private UltimateHandler<Stroke> strokeAddedHandler = null;
	
	private ColorPickerWidget colorPickerWidget = new ColorPickerWidget();
	private BrushPickerWidget brushPickerWidget = new BrushPickerWidget();
	private ZoomWidget zoomWidget;
	private Canvas canvas;
	private Label debugLabel = new Label("debugLabel");
	
	public DrawComponentWidget() {
		
		canvas = Canvas.createIfSupported();
		if (canvas == null) {
			Window.alert("Error: Canvas is not supported");
		}
		
		zoomWidget = new ZoomWidget(canvas);
		
		setStyleName(CLASSNAME);
		canvas.setStyleName(CLASSNAME_CANVAS);
		canvas.setPixelSize(Window.getClientWidth(), Window.getClientHeight());
		
		add(colorPickerWidget);
		add(brushPickerWidget);
		add(zoomWidget);
		add(canvas);
		add(debugLabel);
		
		zoomWidget.setCanvasSizeChangeHandler(new CanvasSizeChangeHandler() {
			@Override
			public void onCanvasSizeChange() {
				drawAll();
			}
		});
		
//		canvas.addMouseDownHandler(event -> {
//			beginCurrentStroke(zoomWidget.canvasPosFromPixelPos(event.getX(),event.getY()));
//		});
//		canvas.addMouseMoveHandler(event -> {
//			extendCurrentStroke(event.getX(), event.getY());
//		});
//		canvas.addMouseUpHandler(event -> {
//			finishCurrentStroke();
//		});
		
		canvas.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				beginCurrentStroke(zoomWidget.canvasPosFromPixelPos(event.getX(),event.getY()));
			}
		});
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				extendCurrentStroke(zoomWidget.canvasPosFromPixelPos(event.getX(),event.getY()));
			}
		});
		
		canvas.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				finishCurrentStroke();
			}
		});
		
		canvas.addTouchStartHandler(new TouchStartHandler() {
			@Override
			public void onTouchStart(TouchStartEvent event) {
				JsArray<Touch> touches = event.getTouches();
				debugLabel.setText("touches" + touches.length());
				if (touches.length() == 1) {
					Touch touch = touches.get(0);
					Element drawElement = DrawComponentWidget.this.getElement();
					beginCurrentStroke(zoomWidget.canvasPosFromPixelPos(
							touch.getRelativeX(drawElement), 
							touch.getRelativeY(drawElement)));
				}
			}
		});
		canvas.addTouchMoveHandler(new TouchMoveHandler() {
			@Override
			public void onTouchMove(TouchMoveEvent event) {
				JsArray<Touch> touches = event.getTouches();
				debugLabel.setText("touches" + touches.length());
				if (touches.length() == 1) {
					event.preventDefault(); // prevent scrolling
					
					Touch touch = touches.get(0);
					Element drawElement = DrawComponentWidget.this.getElement();
					extendCurrentStroke(zoomWidget.canvasPosFromPixelPos(
							touch.getRelativeX(drawElement), 
							touch.getRelativeY(drawElement)));
				}
			}
		});
		canvas.addTouchEndHandler(new TouchEndHandler() {
			@Override
			public void onTouchEnd(TouchEndEvent event) {
				finishCurrentStroke();
			}
		});
	}
	
	private void drawAll() {
		if (null == drawing) return;
		
		Context2d cxt = canvas.getContext2d();
		
		cxt.clearRect(0, 0, canvas.getCanvasElement().getWidth(), canvas.getCanvasElement().getHeight());
		
		for (Stroke stroke : drawing.getStrokesAsArrayList()) {
			drawStroke(stroke);
		}
	}
	
	public void setDrawing(Drawing drawing) {
		this.drawing = drawing;
		
		drawAll();
	}
	
	public void setStrokeAddedHandler(UltimateHandler<Stroke> strokeAddedHandler) {
		this.strokeAddedHandler = strokeAddedHandler;
	}
	
	public void addStroke(Stroke stroke) {
		drawing.addStroke(stroke);
		drawStrokeAnimated(stroke);
	}
	
	public void drawStroke(Stroke stroke) {
		debugLabel.setText("drawing stroke first point");
		drawStrokeFirstPoint(stroke);
		debugLabel.setText("drawing stroke tail");
		drawStrokeTail(stroke);
	}
	
	public void drawStrokeAnimated(Stroke stroke) {
		double millisPerCoordinate = ((double)stroke.getDurationMillis()) / stroke.getCoordinatesAsArrayList().size();
		
		drawStrokeFirstPoint(stroke);
		drawStrokeTailAnimated(stroke, millisPerCoordinate, 0);
	}
	
	private void drawStrokeFirstPoint(Stroke stroke) {
		Context2d cxt = canvas.getContext2d();
		Coordinate firstCoord = stroke.getCoordinatesAsArrayList().get(0);
		if (!debugCoordinateIsOk(firstCoord)) return;
		
		cxt.setFillStyle(stroke.getColor());
		cxt.setFillStyle(stroke.getColor());
		cxt.beginPath();
		cxt.arc(firstCoord.getXPos(), firstCoord.getYPos(), stroke.getThickness()/2, 0, 2*Math.PI);
		cxt.fill();
	}
	
	private void drawStrokeTail(Stroke stroke) {
		Context2d cxt = canvas.getContext2d();
		
		cxt.setStrokeStyle(stroke.getColor());
		cxt.setLineWidth(stroke.getThickness());
		cxt.beginPath();
		ArrayList<Coordinate> coords = stroke.getCoordinatesAsArrayList();	// FIXME debug, sometimes gets broken for some reason
		int i = 0;
		for (Coordinate coord : stroke.getCoordinatesAsArrayList()) {
			debugLabel.setText("drawing coordinate at index " + (i++) + " out of " + (coords.size()-1));
			if (!debugCoordinateIsOk(coord)) return;
			String lastCoord = "";
			if (i > 0) {
				lastCoord = "(" + coords.get(i-1).getXPos() + "," + coords.get(i-1).getXPos() + ")";
			}
			debugLabel.setText("lastCoord:" + lastCoord + ", coord:(" + coord.getXPos() + "," + coord.getYPos() + ")");
			cxt.lineTo(coord.getXPos(), coord.getYPos());
		}
		
		cxt.stroke();
	}
	
	private boolean debugCoordinateIsOk(Coordinate coord) {
		// FIXME delete this method, should always return true but doesn't?
		if (null == coord) {
			Window.alert("coord is null");
			return false;
		}
		return true;
	}
	
	private void drawStrokeTailAnimated(final Stroke stroke, final double millisPerCoordinate, final int fromIndex) {
		int lastIndex = stroke.getCoordinatesAsArrayList().size()-1;
		int toIndex = fromIndex
				+ DRAW_STROKE_ANIMATED_REDRAW_COUNT
				+ DRAW_STROKE_ANIMATED_COUNT - 1;
		
		drawStrokePart(stroke, fromIndex, toIndex);
		
		if (toIndex < lastIndex) {
			new Timer() {
				@Override
				public void run() {
					drawStrokeTailAnimated(stroke, millisPerCoordinate, fromIndex+DRAW_STROKE_ANIMATED_COUNT);
				}
			}.schedule((int)(millisPerCoordinate * DRAW_STROKE_ANIMATED_COUNT));
		}
		
	}
	
	private void drawStrokePart(Stroke stroke, int fromIndex, int toIndex) {
		Context2d cxt = canvas.getContext2d();
		ArrayList<Coordinate> coords = stroke.getCoordinatesAsArrayList();
		
		if (fromIndex < 0) fromIndex = 0;
		if (toIndex > coords.size()-1) toIndex = coords.size()-1;
		
		cxt.setStrokeStyle(stroke.getColor());
		cxt.setLineWidth(stroke.getThickness());
		cxt.beginPath();
		for (int i = fromIndex; i <= toIndex; i++) {
			Coordinate coord = coords.get(i);
			cxt.lineTo(coord.getXPos(), coord.getYPos());
		}
		
		cxt.stroke();
	}
	
	private void beginCurrentStroke(Coordinate coord) {
		currentStroke = new Stroke(
				colorPickerWidget.getSelectedColor(), 
				brushPickerWidget.getSelectedThickness());
		currentStrokeStartedMillis = System.currentTimeMillis();
		
		currentStroke.addCoordinate(coord);
		
		drawStrokeFirstPoint(currentStroke);
	}
	
	private void extendCurrentStroke(Coordinate coord) {
		if (currentStroke != null) {
			ArrayList<Coordinate> coords = currentStroke.getCoordinatesAsArrayList();
			Coordinate lastCoordinate = coords.get(coords.size()-1);
			
			double xDiff = coord.getXPos() - lastCoordinate.getXPos();
			double yDiff = coord.getYPos() - lastCoordinate.getYPos();
			if (Math.sqrt(xDiff*xDiff + yDiff*yDiff) < currentStroke.getThickness()/10) {
				return;
			}
			
			currentStroke.addCoordinate(coord);
			
			drawStrokePart(currentStroke, coords.size()-3, coords.size()-1);
		}
	}
	
	private void finishCurrentStroke() {
		currentStroke.setDurationMillis(System.currentTimeMillis() - currentStrokeStartedMillis);
		
		drawing.addStroke(currentStroke);
		strokeAddedHandler.onEvent(currentStroke);
		
		currentStroke = null;
	}
	
}