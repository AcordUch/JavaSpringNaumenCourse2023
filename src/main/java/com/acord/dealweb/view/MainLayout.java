package com.acord.dealweb.view;

import com.acord.dealweb.services.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {
  private final SecurityService securityService;

  public MainLayout(SecurityService securityService) {
    this.securityService = securityService;
    createHeader();
    createDrawer();
  }

  private void createHeader() {
    H2 title = new H2("Demo");

    String userName = securityService.getAuthenticatedUser().getUsername();
    Button logout = new Button("Log out " + userName, e -> securityService.logout());

    HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), title, logout);
    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.expand(title);
    header.setWidthFull();

    addToNavbar(header);
  }

  private void createDrawer() {
    addToDrawer(
        new VerticalLayout(
            new RouterLink("Rooms", RoomsView.class),
            new RouterLink("Friends", FriendsView.class)));
  }
}
