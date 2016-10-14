package com.drawgame;

import com.drawgame.client.drawcomponent.Drawing;
import com.drawgame.client.drawcomponent.Stroke;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public abstract class GameComponent extends VerticalLayout {

	public static final String CLASSNAME = "gameComponent";
	public static final String CLASSNAME_DESCRIPTION = CLASSNAME + "-description";
	
	private Game game;
	
	private Label description;
	private DrawComponent drawComponent;
	
	public GameComponent(Game game) {
		this.game = game;
		
		description = new Label("Draw what you want, use white brush to erase.");
		drawComponent = new DrawComponent(this);
		addComponents(description, drawComponent);
		
		setStyleName(CLASSNAME);
		description.setStyleName(CLASSNAME_DESCRIPTION);
		
		game.registerComponent(this);
	}
	
	public Drawing getDrawing() {
		return game.getDrawing();
	}
	
	public void addStrokeToServer(Stroke stroke) {
		game.addStroke(stroke, this);
	}
	
	public void addStrokeToClient(Stroke stroke) {
		drawComponent.addStroke(stroke);
	}
	
	public void loadComponent() {
		drawComponent.loadComponent();
	}
	
}
