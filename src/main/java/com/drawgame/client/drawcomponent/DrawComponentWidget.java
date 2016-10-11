package com.drawgame.client.drawcomponent;

import java.util.ArrayList;

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
	
	public static final int DRAW_STROKE_ANIMATED_MILLIS = 40;
	public static final int DRAW_STROKE_ANIMATED_COUNT = 4;
	public static final int DRAW_STROKE_ANIMATED_REDRAW_COUNT = 2;

	private Drawing drawing = new Drawing();
	private Stroke currentStroke = null;
	
	private UltimateHandler<Stroke> strokeAddedHandler = null;
	
	private ColorPickerWidget colorPickerWidget = new ColorPickerWidget();
	private BrushPickerWidget brushPickerWidget = new BrushPickerWidget();
	private Canvas canvas;
	private Label noteLabel = new Label();
	
	public DrawComponentWidget() {
		setStyleName(CLASSNAME);
		
		canvas = Canvas.createIfSupported();
		if (canvas == null) {
			Window.alert("Error: Canvas is not supported");
		}
		setCanvasSize();
		
		add(colorPickerWidget);
		add(brushPickerWidget);
		add(canvas);
		add(noteLabel);
		
//		canvas.addMouseDownHandler(event -> {
//			beginCurrentStroke(event.getX(),event.getY());
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
				beginCurrentStroke(event.getX(),event.getY());
			}
		});
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				extendCurrentStroke(event.getX(), event.getY());
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
				if (touches.length() == 1) {
					Touch touch = touches.get(0);
					Element drawElement = DrawComponentWidget.this.getElement();
					beginCurrentStroke(touch.getRelativeX(drawElement), touch.getRelativeY(drawElement));
				}
			}
		});
		canvas.addTouchMoveHandler(new TouchMoveHandler() {
			@Override
			public void onTouchMove(TouchMoveEvent event) {
				JsArray<Touch> touches = event.getTouches();
				if (touches.length() == 1) {
					Touch touch = touches.get(0);
					Element drawElement = DrawComponentWidget.this.getElement();
					extendCurrentStroke(touch.getRelativeX(drawElement), touch.getRelativeY(drawElement));
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

	private void setCanvasSize() {
		int width = Window.getClientWidth();
		int height = Window.getClientHeight();
		canvas.getCanvasElement().setWidth(width-50);	// TODO css hide overflow instead
		canvas.getCanvasElement().setHeight(height-50);	// TODO css hide overflow instead
	}
	
	public void setDrawing(Drawing drawing) {
		this.drawing = drawing;
		Context2d cxt = canvas.getContext2d();
		
		cxt.clearRect(0, 0, canvas.getCanvasElement().getWidth(), canvas.getCanvasElement().getHeight());
		
		for (Stroke stroke : drawing.getStrokesAsArrayList()) {
			drawStroke(stroke);
		}
	}
	
	public void setStrokeAddedHandler(UltimateHandler<Stroke> strokeAddedHandler) {
		this.strokeAddedHandler = strokeAddedHandler;
	}
	
	public void addStroke(Stroke stroke) {
		drawing.addStroke(stroke);
		drawStrokeAnimated(stroke);
	}
	
	public void drawStroke(Stroke stroke) {		
		drawStrokeFirstPoint(stroke);
		drawStrokeTail(stroke);
	}
	
	public void drawStrokeAnimated(Stroke stroke) {
		drawStrokeFirstPoint(stroke);
		drawStrokeTailAnimated(stroke, 0);
	}
	
	private void drawStrokeFirstPoint(Stroke stroke) {
		Context2d cxt = canvas.getContext2d();
		Coordinate firstCoord = stroke.getCoordinatesAsArrayList().get(0);
		
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
		for (Coordinate coord : stroke.getCoordinatesAsArrayList()) {
			cxt.lineTo(coord.getXPos(), coord.getYPos());
		}
		
		cxt.stroke();
	}
	
	private void drawStrokeTailAnimated(final Stroke stroke, final int fromIndex) {
		int lastIndex = stroke.getCoordinatesAsArrayList().size()-1;
		int toIndex = fromIndex
				+ DRAW_STROKE_ANIMATED_REDRAW_COUNT
				+ DRAW_STROKE_ANIMATED_COUNT - 1;
		
		drawStrokePart(stroke, fromIndex, toIndex);
		
		if (toIndex < lastIndex) {
			new Timer() {
				@Override
				public void run() {
					drawStrokeTailAnimated(stroke, fromIndex+DRAW_STROKE_ANIMATED_COUNT);
				}
			}.schedule(DRAW_STROKE_ANIMATED_MILLIS);
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
	
	private void beginCurrentStroke(int x, int y) {
		currentStroke = new Stroke(
				colorPickerWidget.getSelectedColor(), 
				brushPickerWidget.getSelectedThickness());
		
		currentStroke.addCoordinate(new Coordinate(x, y));
		
		drawStrokeFirstPoint(currentStroke);
	}
	
	private void extendCurrentStroke(int x, int y) {
		if (currentStroke != null) {
			ArrayList<Coordinate> coords = currentStroke.getCoordinatesAsArrayList();
			noteLabel.setText("size: " + coords.size());
			Coordinate lastCoordinate = coords.get(coords.size()-1);
			
			double xDiff = x - lastCoordinate.getXPos();
			double yDiff = y - lastCoordinate.getYPos();
			if (Math.sqrt(xDiff*xDiff + yDiff*yDiff) < currentStroke.getThickness()/10) {
				return;
			}
			
			currentStroke.addCoordinate(new Coordinate(x, y));
			
			drawStrokePart(currentStroke, coords.size()-3, coords.size()-1);
		}
	}
	
	private void finishCurrentStroke() {
		drawing.addStroke(currentStroke);
		strokeAddedHandler.onEvent(currentStroke);
		currentStroke = null;
	}
	
}