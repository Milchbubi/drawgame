package com.drawgame.client.drawcomponent;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Drawing implements Serializable {

	private ArrayList<Stroke> strokes = new ArrayList<Stroke>();
	
	public void addStroke(Stroke stroke) {
		strokes.add(stroke);
	}
	
	public ArrayList<Stroke> getStrokes() {
		return strokes;
	}
	
	public void setStrokes(ArrayList<Stroke> strokes) {
		this.strokes = strokes;
	}
	
}