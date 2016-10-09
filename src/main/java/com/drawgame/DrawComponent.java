package com.drawgame;

import com.drawgame.client.drawcomponent.DrawComponentClientRpc;
import com.drawgame.client.drawcomponent.DrawComponentServerRpc;
import com.drawgame.client.drawcomponent.DrawComponentState;
import com.drawgame.client.drawcomponent.Drawing;
import com.drawgame.client.drawcomponent.Stroke;

@SuppressWarnings("serial")
public class DrawComponent extends com.vaadin.ui.AbstractComponent {
	
	private DrawComponentServerRpc rpc = new DrawComponentServerRpc() {
		
		public void loadDrawing() {
			Drawing drawing = GameStorage.defaultGame.getDrawing();
			getRpcProxy(DrawComponentClientRpc.class).setDrawing(drawing);
		}

		@Override
		public void addStrokeToDrawing(Stroke stroke) {
			GameStorage.defaultGame.addStroke(stroke, DrawComponent.this);
		}

		@Override
		public void rpcPingPong() {
			getRpcProxy(DrawComponentClientRpc.class).rpcPingPong();
		}
		
	};

	public DrawComponent() {
		registerRpc(rpc);
		GameStorage.defaultGame.registerDrawComponents(this);
		rpc.loadDrawing();
	}

	@Override
	public DrawComponentState getState() {
		return (DrawComponentState) super.getState();
	}
	
	public void addStroke(Stroke stroke) {
		getRpcProxy(DrawComponentClientRpc.class).addStroke(stroke);
	}
	
}
