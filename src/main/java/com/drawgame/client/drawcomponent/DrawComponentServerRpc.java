package com.drawgame.client.drawcomponent;

import com.vaadin.shared.communication.ServerRpc;

public interface DrawComponentServerRpc extends ServerRpc {
	
	public void loadDrawing();

	public void addStrokeToDrawing(Stroke stroke);
	
	/**
	 * frequently polled by client
	 * server pushes are not received until client makes a request
	 */
	public void rpcPingPong();
	
}
