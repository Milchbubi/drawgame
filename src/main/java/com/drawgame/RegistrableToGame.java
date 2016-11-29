package com.drawgame;

import com.drawgame.client.drawcomponent.Stroke;

public interface RegistrableToGame {

	public void addStrokeToClient(Stroke stroke);
	
	public void loadComponent();
}
