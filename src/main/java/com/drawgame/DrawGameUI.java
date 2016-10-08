package com.drawgame;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
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
@Theme("drawgametheme")
public class DrawGameUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        DrawComponent drawComponent = new DrawComponent();
        
        setContent(drawComponent);
    }

    @WebServlet(urlPatterns = "/*", name = "DrawGameUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = DrawGameUI.class, productionMode = false, widgetset = "com.drawgame.DrawgameWidgetset")
    public static class DrawGameUIServlet extends VaadinServlet {
    }
}
