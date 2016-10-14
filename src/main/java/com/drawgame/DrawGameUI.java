package com.drawgame;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@SuppressWarnings("serial")
@Theme("drawgametheme")
public class DrawGameUI extends UI {

	public static final String CLASSNAME = "drawGameUI";
	
	VerticalLayout layout = new VerticalLayout();
	MenuComponent menu = new MenuComponent();
	Panel drawPanel = new Panel();
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        GameComponent gameComponent = new SimpleGameComponent();
        drawPanel.setContent(gameComponent);
        
        setStyleName(CLASSNAME);
        
        layout.addComponents(menu, drawPanel);
        setContent(layout);
        
    }

    @WebServlet(urlPatterns = "/*", name = "DrawGameUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DrawGameUI.class, productionMode = false, widgetset = "com.drawgame.DrawgameWidgetset")
    public static class DrawGameUIServlet extends VaadinServlet {
    }
    
}
