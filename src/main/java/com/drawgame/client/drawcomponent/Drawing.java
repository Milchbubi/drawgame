package com.drawgame.client.drawcomponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Drawing implements Serializable {

	private ArrayList<Stroke> strokes = new ArrayList<Stroke>();
	
	public Drawing() {
	}
	
	public synchronized void addStroke(Stroke stroke) {
		strokes.add(stroke);
	}
	
	/**
	 * @return List because ArrayList seems broken for server-rpc (vaadin 7.7.3)
	 */
	public List<Stroke> getStrokes() {
		return strokes;
	}
	
	public ArrayList<Stroke> getStrokesAsArrayList() {
		return strokes;
	}
	
	public void setStrokes(ArrayList<Stroke> strokes) {
		this.strokes = strokes;
	}
	
}