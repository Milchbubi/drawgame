package com.drawgame;

import java.util.ArrayList;

import com.drawgame.client.drawcomponent.Coordinate;
import com.drawgame.client.drawcomponent.Drawing;
import com.drawgame.client.drawcomponent.Stroke;

public class Game {

	private ArrayList<DrawComponent> registeredDrawComponents = new ArrayList<DrawComponent>(5);
	
	private Drawing drawing;
	
	public Game() {
		drawing = new Drawing();
		
		Stroke stroke1 = new Stroke("#00F", 5.0);
		stroke1.addCoordinate(new Coordinate(125, 75));
		stroke1.addCoordinate(new Coordinate(125, 175));
		stroke1.addCoordinate(new Coordinate(135, 165));
		
		Stroke stroke2 = new Stroke("#00F", 5.0);
		stroke2.addCoordinate(new Coordinate(100, 100));
		stroke2.addCoordinate(new Coordinate(175, 100));
		
		drawing.addStroke(stroke1);
		drawing.addStroke(stroke2);
	}
	
	public synchronized void registerDrawComponents(DrawComponent drawComponent) {
		registeredDrawComponents.add(drawComponent);
	}
	
	public synchronized void deregisterDrawComponents(DrawComponent drawComponent) {
		registeredDrawComponents.remove(drawComponent);
	}
	
	public synchronized void addStroke(Stroke stroke, DrawComponent drawComponent) {
		drawing.addStroke(stroke);
		for (DrawComponent currentComp : registeredDrawComponents) {
			if (!drawComponent.equals(currentComp)) {
				currentComp.addStroke(stroke);
			}
		}
	}
	
	public Drawing getDrawing() {
		return drawing;
	}
	
}
