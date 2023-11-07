package com.acord.dealweb.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/hi") // map view to the root
class MainView extends VerticalLayout {
    MainView() {
        add(new H1("Hello, world"));
    }
}
