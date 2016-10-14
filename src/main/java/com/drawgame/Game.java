package com.drawgame;

import java.util.ArrayList;

import com.drawgame.client.drawcomponent.Coordinate;
import com.drawgame.client.drawcomponent.Drawing;
import com.drawgame.client.drawcomponent.Stroke;

public class Game {

	private ArrayList<GameComponent> registeredComponents = new ArrayList<GameComponent>(5);
	
	private Drawing drawing;
	
	public Game() {
		drawing = new Drawing();
		
		Stroke stroke1 = new Stroke("#00F", 5.0);
		stroke1.addCoordinate(new Coordinate(150, 75));
		stroke1.addCoordinate(new Coordinate(150, 175));
		stroke1.addCoordinate(new Coordinate(160, 165));
		
		Stroke stroke2 = new Stroke("#00F", 5.0);
		stroke2.addCoordinate(new Coordinate(125, 100));
		stroke2.addCoordinate(new Coordinate(200, 100));
		
		drawing.addStroke(stroke1);
		drawing.addStroke(stroke2);
	}
	
	public synchronized void registerComponent(GameComponent component) {
		registeredComponents.add(component);
	}
	
	public synchronized void deregisterComponent(GameComponent component) {
		registeredComponents.remove(component);
	}
	
	public synchronized void addStroke(Stroke stroke, GameComponent author) {
		drawing.addStroke(stroke);

		for (GameComponent currentComp : registeredComponents) {
			if (!author.equals(currentComp)) {
				currentComp.addStrokeToClient(stroke);
			}
		}
	}
	
	public Drawing getDrawing() {
		return drawing;
	}
	
}
