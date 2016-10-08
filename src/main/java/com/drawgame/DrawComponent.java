package com.drawgame;

import com.drawgame.client.drawcomponent.DrawComponentClientRpc;
import com.drawgame.client.drawcomponent.DrawComponentServerRpc;
import com.vaadin.shared.MouseEventDetails;
import com.drawgame.client.drawcomponent.DrawComponentState;

public class DrawComponent extends com.vaadin.ui.AbstractComponent {

	private DrawComponentServerRpc rpc = new DrawComponentServerRpc() {
		private int clickCount = 0;

		public void clicked(MouseEventDetails mouseDetails) {
			// nag every 5:th click using RPC
			if (++clickCount % 5 == 0) {
				getRpcProxy(DrawComponentClientRpc.class).alert(
						"Ok, that's enough!");
			}
			// update shared state
			getState().text = "You have clicked " + clickCount + " times";
		}
	};  

	public DrawComponent() {
		registerRpc(rpc);
	}

	@Override
	public DrawComponentState getState() {
		return (DrawComponentState) super.getState();
	}
}
