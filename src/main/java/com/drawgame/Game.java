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
		
		Stroke stroke1 = new Stroke("#00F", 3.0);
		stroke1.addCoordinate(new Coordinate(100, 50));
		stroke1.addCoordinate(new Coordinate(100, 150));
		stroke1.addCoordinate(new Coordinate(110, 140));
		
		Stroke stroke2 = new Stroke("#00F", 3.0);
		stroke2.addCoordinate(new Coordinate(75, 75));
		stroke2.addCoordinate(new Coordinate(150, 75));
		
		drawing.addStroke(stroke1);
		drawing.addStroke(stroke2);
	}
	
	public void registerDrawComponents(DrawComponent drawComponent) {
		registeredDrawComponents.add(drawComponent);
	}
	
	public void deregisterDrawComponents(DrawComponent drawComponent) {
		registeredDrawComponents.remove(drawComponent);
	}
	
	public void addStroke(Stroke stroke, DrawComponent drawComponent) {
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
