package com.drawgame.client.drawcomponent;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Coordinate implements Serializable {

	private int xPos;
	
	private int yPos;
	
	public Coordinate() {
	}
	
	public Coordinate(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
}