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
import com.google.gwt.user.client.ui.SimplePanel;

public class DrawComponentWidget extends SimplePanel {

	public static final String CLASSNAME = "drawcomponent";

	private Drawing drawing = new Drawing();
	
	private UltimateHandler<Stroke> strokeAddedHandler = null;
	
	private Canvas canvas;
	
	private Stroke currentStroke = null;
	
	public DrawComponentWidget() {
		setStyleName(CLASSNAME);
		
		canvas = Canvas.createIfSupported();
		if (canvas == null) {
			Window.alert("Error: Canvas is not supported");
		}
		setCanvasSize();
		setWidget(canvas);
		
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
		
		for (Stroke stroke : drawing.getStrokes()) {
			drawStroke(stroke);
		}
	}
	
	public void setStrokeAddedHandler(UltimateHandler<Stroke> strokeAddedHandler) {
		this.strokeAddedHandler = strokeAddedHandler;
	}
	
	public void addStroke(Stroke stroke) {
		drawing.addStroke(stroke);
		drawStroke(stroke, 10);
	}
	
	public void drawStroke(Stroke stroke) {		
		drawStrokeFirstPoint(stroke);
		drawStrokeTail(stroke);
	}
	
	public void drawStroke(Stroke stroke, int millis) {
		drawStrokeFirstPoint(stroke);
		drawStrokeTail(stroke, millis, 1);
	}
	
	private void drawStrokeFirstPoint(Stroke stroke) {
		Context2d cxt = canvas.getContext2d();
		Coordinate firstCoord = stroke.getCoordinatesAsArrayList().get(0);
		
		cxt.setFillStyle(stroke.getColor());
		double thickness = stroke.getThickness();
		cxt.fillRect(firstCoord.getXPos() - thickness/2, firstCoord.getYPos() - thickness/2, thickness, thickness);
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
	
	private void drawStrokeTail(final Stroke stroke, final int millis, final int startIndex) {
		Context2d cxt = canvas.getContext2d();
		ArrayList<Coordinate> coords = stroke.getCoordinatesAsArrayList();
		Coordinate lastCoord = coords.get(startIndex-1);
		Coordinate currentCoord = coords.get(startIndex);
		
		cxt.beginPath();
		cxt.setStrokeStyle(stroke.getColor());
		cxt.setLineWidth(stroke.getThickness());
		cxt.moveTo(lastCoord.getXPos(), lastCoord.getYPos());
		cxt.lineTo(currentCoord.getXPos(), currentCoord.getYPos());
		cxt.stroke();
		
		if (startIndex+1 < coords.size()) {
			new Timer() {
				@Override
				public void run() {
					drawStrokeTail(stroke, millis, startIndex+1);
				}
			}.schedule(millis);
		}
		
	}
	
	private void beginCurrentStroke(int x, int y) {
		currentStroke = new Stroke();
		Context2d cxt = canvas.getContext2d();
		
		cxt.setFillStyle(currentStroke.getColor());
		double thickness = currentStroke.getThickness();
		cxt.fillRect(x - thickness/2, y - thickness/2, thickness, thickness);
		
		currentStroke.addCoordinate(new Coordinate(x, y));
	}
	
	private void extendCurrentStroke(int x, int y) {
		if (currentStroke != null) {
			ArrayList<Coordinate> coords = currentStroke.getCoordinatesAsArrayList();
			Coordinate lastCoordinate = coords.get(coords.size()-1);
			Context2d cxt = canvas.getContext2d();
			
			cxt.setStrokeStyle(currentStroke.getColor());
			cxt.setLineWidth(currentStroke.getThickness());
			cxt.beginPath();
			cxt.moveTo(lastCoordinate.getXPos(), lastCoordinate.getYPos());
			cxt.lineTo(x, y);
			cxt.stroke();
			
			currentStroke.addCoordinate(new Coordinate(x, y));
		}
	}
	
	private void finishCurrentStroke() {
		drawing.addStroke(currentStroke);
		strokeAddedHandler.onEvent(currentStroke);
		currentStroke = null;
	}
	
}