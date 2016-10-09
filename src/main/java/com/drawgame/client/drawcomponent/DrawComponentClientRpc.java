package com.drawgame.client.drawcomponent;

import com.vaadin.shared.communication.ClientRpc;

public interface DrawComponentClientRpc extends ClientRpc {
	
	public void setDrawing(Drawing drawing);

	public void addStroke(Stroke stroke);
	
	public void rpcPingPong();
	
}