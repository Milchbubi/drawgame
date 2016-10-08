package com.drawgame.client.drawcomponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

import com.drawgame.DrawComponent;
import com.drawgame.client.drawcomponent.DrawComponentWidget;
import com.drawgame.client.drawcomponent.DrawComponentServerRpc;
import com.vaadin.client.communication.RpcProxy;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.drawgame.client.drawcomponent.DrawComponentClientRpc;
import com.drawgame.client.drawcomponent.DrawComponentState;
import com.vaadin.client.communication.StateChangeEvent;

@Connect(DrawComponent.class)
public class DrawComponentConnector extends AbstractComponentConnector {

	DrawComponentServerRpc rpc = RpcProxy
			.create(DrawComponentServerRpc.class, this);
	
	public DrawComponentConnector() {
		registerRpc(DrawComponentClientRpc.class, new DrawComponentClientRpc() {
			public void alert(String message) {
				// TODO Do something useful
				Window.alert(message);
			}
		});

		// TODO ServerRpc usage example, do something useful instead
//		getWidget().addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				final MouseEventDetails mouseDetails = MouseEventDetailsBuilder
//					.buildMouseEventDetails(event.getNativeEvent(),
//								getWidget().getElement());
//				rpc.clicked(mouseDetails);
//			}
//		});

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

