package com.drawgame.client.drawcomponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

import com.drawgame.DrawComponent;
import com.drawgame.client.drawcomponent.DrawComponentWidget;
import com.drawgame.client.drawcomponent.DrawComponentServerRpc;
import com.vaadin.client.communication.RpcProxy;
import com.drawgame.client.drawcomponent.DrawComponentClientRpc;
import com.drawgame.client.drawcomponent.DrawComponentState;
import com.vaadin.client.communication.StateChangeEvent;

@SuppressWarnings("serial")
@Connect(DrawComponent.class)
public class DrawComponentConnector extends AbstractComponentConnector {

	DrawComponentServerRpc rpc = RpcProxy
			.create(DrawComponentServerRpc.class, this);
	
	public DrawComponentConnector() {
		registerRpc(DrawComponentClientRpc.class, new DrawComponentClientRpc() {

			@Override
			public void setDrawing(Drawing drawing) {
				getWidget().setDrawing(drawing);
			}

			@Override
			public void addStroke(Stroke stroke) {
				getWidget().addStroke(stroke);
			}

			@Override
			public void rpcPingPong() {
				new Timer() {
					@Override
					public void run() {
						rpc.rpcPingPong();
					}
				}.schedule(100);
			}
			
		});

		getWidget().setStrokeAddedHandler(new UltimateHandler<Stroke>() {
			@Override
			public void onEvent(Stroke event) {
				rpc.addStrokeToDrawing(event);
			}
		});
		
		// server pushes are not received until client makes a request
		new Timer() {
			@Override
			public void run() {
				rpc.rpcPingPong();
			}
		}.schedule(100);
		
	}

	@Override
	protected Widget createWidget() {
		return GWT.create(DrawComponentWidget.class);
	}

	@Override
	public DrawComponentWidget getWidget() {
		return (DrawComponentWidget) super.getWidget();
	}

	@Override
	public DrawComponentState getState() {
		return (DrawComponentState) super.getState();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
	}

}

