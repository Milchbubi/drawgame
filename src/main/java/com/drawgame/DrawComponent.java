package com.drawgame;

import com.drawgame.client.drawcomponent.DrawComponentClientRpc;
import com.drawgame.client.drawcomponent.DrawComponentServerRpc;
import com.drawgame.client.drawcomponent.DrawComponentState;
import com.drawgame.client.drawcomponent.Stroke;

@SuppressWarnings("serial")
public class DrawComponent extends com.vaadin.ui.AbstractComponent {
	
	private GameComponent gameComponent;
	
	private DrawComponentServerRpc rpc = new DrawComponentServerRpc() {
		
		public void loadDrawing() {
			getRpcProxy(DrawComponentClientRpc.class).setDrawing(gameComponent.getDrawing());
		}

		@Override
		public void addStrokeToDrawing(Stroke stroke) {
			gameComponent.addStrokeToServer(stroke);
		}

		@Override
		public void rpcPingPong() {
			getRpcProxy(DrawComponentClientRpc.class).rpcPingPong();
		}
		
	};

	public DrawComponent(GameComponent gameComponent) {
		this.gameComponent = gameComponent;
		
		registerRpc(rpc);
		
		loadComponent();
	}

	@Override
	public DrawComponentState getState() {
		return (DrawComponentState) super.getState();
	}
	
	public void addStroke(Stroke stroke) {
		getRpcProxy(DrawComponentClientRpc.class).addStroke(stroke);
	}
	
	public void loadComponent() {
		rpc.loadDrawing();
	}
	
}
