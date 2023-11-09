package com.acord.dealweb.view;

import com.acord.dealweb.controllers.RoomController;
import com.acord.dealweb.domain.Room;
import com.acord.dealweb.services.RoomService;
import com.acord.dealweb.view.form.RoomForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Rooms")
public class RoomsView extends VerticalLayout {
  private Grid<Room> grid = new Grid<>(Room.class);
  private TextField filterText = new TextField();
  private RoomForm form;
  private RoomController controller;
  private RoomService service;

  public RoomsView(RoomController controller, RoomService service) {
    this.controller = controller;
    this.service = service;
    setSizeFull();
    configureGrid();
    configureForm();

    add(getToolbar(), getContent());
    updateGrid();
    closeEditor();
  }

  private void configureGrid() {
    grid.setSizeFull();
    grid.setColumns("uuid", "name", "password");
    grid.getColumns().forEach(column -> column.setAutoWidth(true));
    grid.asSingleSelect().addValueChangeListener(event -> editRoom(event.getValue()));
  }

  private HorizontalLayout getToolbar() {
    filterText.setPlaceholder("Filter by name...");
    filterText.setClearButtonVisible(true);
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    filterText.addValueChangeListener(event -> updateGrid());

    Button addRoomButton = new Button("Add room");
    addRoomButton.addClickListener(click -> addRoom());

    return new HorizontalLayout(filterText, addRoomButton);
  }

  private Component getContent() {
    HorizontalLayout content = new HorizontalLayout(grid, form);
    content.setFlexGrow(2, grid);
    content.setFlexGrow(1, form);
    content.setSizeFull();
    return content;
  }

  private void configureForm() {
    form = new RoomForm();
    form.setWidth("25em");
    form.addSaveListener(this::saveRoom);
    form.addDeleteListener(this::deleteRoom);
    form.addCloseListener(e -> closeEditor());
  }

  private void saveRoom(RoomForm.SaveEvent event) {
    controller.add(event.getRoom());
    updateGrid();
    closeEditor();
  }

  private void deleteRoom(RoomForm.DeleteEvent event) {
    controller.delete(event.getRoom().getUuid());
    updateGrid();
    closeEditor();
  }

  private void editRoom(Room room) {
    if (room == null) {
      closeEditor();
    } else {
      form.setRoom(room);
      form.setVisible(true);
    }
  }

  private void closeEditor() {
    form.setRoom(null);
    form.setVisible(false);
  }

  private void addRoom() {
    grid.asSingleSelect().clear();
    editRoom(new Room());
  }

  private void updateGrid() {
    grid.setItems(service.getAll(filterText.getValue()));
  }
}
