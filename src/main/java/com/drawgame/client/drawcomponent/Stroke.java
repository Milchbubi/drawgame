package com.drawgame.client.drawcomponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Stroke implements Serializable {
	
	private String color;
	
	private double thickness;
	
	private ArrayList<Coordinate> coordinates;
	
	private long durationMillis = 0;
	
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
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public double getThickness() {
		return thickness;
	}
	
	public void setThickness(double thickness) {
		this.thickness = thickness;
	}
	
	public void addCoordinate(Coordinate coordinate) {
		coordinates.add(coordinate);
	}
	
	/**
	 * @return List because ArrayList seems broken for server-rpc (vaadin 7.7.3)
	 */
	public List<Coordinate> getCoordinates() {
		return coordinates;
	}
	
	public ArrayList<Coordinate> getCoordinatesAsArrayList() {
		return coordinates;
	}
	
	public void setCoordinates(ArrayList<Coordinate> coordinates) {
		this.coordinates = coordinates;
	}
	
	public long getDurationMillis() {
		return durationMillis;
	}
	
	public void setDurationMillis(long durationMillis) {
		this.durationMillis = durationMillis;
	}
	
}