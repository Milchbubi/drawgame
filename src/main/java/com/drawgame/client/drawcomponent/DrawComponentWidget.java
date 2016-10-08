package com.drawgame.client.drawcomponent;

import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class DrawComponentWidget extends SimplePanel {

	public static final String CLASSNAME = "drawcomponent";

	private Drawing drawing;
	
	private Canvas canvas;
	
	private Stroke currentStroke = null;
	
	public DrawComponentWidget() {
		this(new Drawing());
	}
	
	public DrawComponentWidget(Drawing drawing) {
		setStyleName(CLASSNAME);
		
		canvas = Canvas.createIfSupported();
		if (canvas == null) {
			Window.alert("Error: Canvas is not supported");
		}
		setCanvasSize();
		setDrawing(drawing);
		setWidget(canvas);
		
//		drawing.addMouseDownHandler(event -> {
//			currentStroke = true;
//		});
//		drawing.addMouseMoveHandler(event -> {
//			if (true == currentStroke) {
//				drawPoint(event.getX(), event.getY(), 10);
//			}
//		});
//		drawing.addMouseUpHandler(event -> {
//			currentStroke = false;
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
	}

	private void setCanvasSize() {
		int width = Window.getClientWidth();
		int height = Window.getClientHeight();
		canvas.getCanvasElement().setWidth(width);
		canvas.getCanvasElement().setHeight(height);
	}
	
	public void setDrawing(Drawing drawing) {
		this.drawing = drawing;
		Context2d cxt = canvas.getContext2d();
		
		cxt.clearRect(0, 0, canvas.getCanvasElement().getWidth(), canvas.getCanvasElement().getHeight());
		
		for (Stroke stroke : drawing.getStrokes()) {
			drawStroke(stroke);
		}
	}
	
	public void addStroke(Stroke stroke) {
		drawing.addStroke(stroke);
		drawStroke(stroke, 10);
	}
	
	public void drawStroke(Stroke stroke) {
		Context2d cxt = canvas.getContext2d();
		
		cxt.setStrokeStyle(stroke.getColor());
		cxt.setLineWidth(stroke.getThickness());
		cxt.beginPath();
		
		Coordinate firstCoord = stroke.getCoordinates().get(0);
		cxt.moveTo(firstCoord.getXPos(), firstCoord.getYPos());
		for (Coordinate coord : stroke.getCoordinates()) {
			cxt.lineTo(coord.getXPos(), coord.getYPos());
		}
		
		cxt.stroke();
	}
	
	public void drawStroke(Stroke stroke, int millis) {
		Context2d cxt = canvas.getContext2d();
		ArrayList<Coordinate> coords = stroke.getCoordinates();
		
		for (int i = 1; i < coords.size(); i++) {
			Coordinate lastCoord = coords.get(i-1);
			Coordinate currentCoord = coords.get(i);
			
			cxt.beginPath();
			cxt.moveTo(lastCoord.getXPos(), lastCoord.getYPos());
			cxt.lineTo(currentCoord.getXPos(), currentCoord.getYPos());
			cxt.setStrokeStyle(stroke.getColor());
			cxt.setLineWidth(stroke.getThickness());
			cxt.stroke();
			
//			try {	// TODO gwt-issue?
//				Thread.sleep(millis);
//			} catch (InterruptedException e) {
//				Thread.currentThread().interrupt();
//				return;
//			}
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
			Coordinate lastCoordinate = currentStroke.getCoordinates().get(currentStroke.getCoordinates().size()-1);
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
		currentStroke = null;
	}
	
	private void drawTest() {
		Context2d cxt = canvas.getContext2d();
		
		cxt.setFillStyle("#F00");
		cxt.fillRect(200, 100, 100, 50);
	}
	
	private void drawPoint(int x, int y, int radius) {
		Context2d cxt = canvas.getContext2d();
		
		cxt.setFillStyle("#00F");
		cxt.fillRect(x-radius, y-radius, radius*2, radius*2);
	}
	
}