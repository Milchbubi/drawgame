package com.drawgame.client.drawcomponent;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Stroke implements Serializable {
	
	private String color;
	
	private double thickness;
	
	private ArrayList<Coordinate> coordinates;
	
	public Stroke() {
		this("#000000", 3.0);
	}
	
	public Stroke(String color, double thickness) {
		this(color, thickness, new ArrayList<Coordinate>());
	}
	
	public Stroke(String color, double thickness, ArrayList<Coordinate> coordinates) {
		this.color = color;
		this.thickness = thickness;
		this.coordinates = coordinates;
	}
	
	public String getColor() {
		return color;
	}
	
	public double getThickness() {
		return thickness;
	}
	
	public void addCoordinate(Coordinate coordinate) {
		coordinates.add(coordinate);
	}
	
	public ArrayList<Coordinate> getCoordinates() {
		return coordinates;
	}
	
}