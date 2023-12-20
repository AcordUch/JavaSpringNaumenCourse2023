package com.acord.dealweb.view;

import com.acord.dealweb.controllers.RoomController;
import com.acord.dealweb.controllers.UserController;
import com.acord.dealweb.domain.Room;
import com.acord.dealweb.view.form.RoomForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import lombok.val;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Rooms")
public class RoomsView extends VerticalLayout {
  private final Grid<Room> grid = new Grid<>(Room.class);
  private final TextField filterText = new TextField();
  private final RoomForm form;
  private final RoomController roomController;
  private final UserController userController;

  public RoomsView(RoomController roomController, UserController userController) {
    this.roomController = roomController;
    this.userController = userController;
    setSizeFull();
    configureGrid();

    this.form = new RoomForm();
    configureForm();

    add(makeToolbar(), makeContent());
    updateGrid();
    closeEditor();
  }

  private void configureGrid() {
    grid.setSizeFull();
    grid.setColumns("uuid", "name", "password");
    grid.getColumns().forEach(column -> column.setAutoWidth(true));
    grid.asSingleSelect().addValueChangeListener(event -> editRoom(event.getValue()));
  }

  private HorizontalLayout makeToolbar() {
    filterText.setPlaceholder("Filter by name...");
    filterText.setClearButtonVisible(true);
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    filterText.addValueChangeListener(event -> updateGrid());

    Button addRoomButton = new Button("Add room");
    addRoomButton.addClickListener(click -> addRoom());

    Button reloadButton = new Button("Reload");
    reloadButton.addClickListener(click -> updateGrid());

    return new HorizontalLayout(filterText, addRoomButton, reloadButton);
  }

  private Component makeContent() {
    HorizontalLayout content = new HorizontalLayout(grid, form);
    content.setFlexGrow(2, grid);
    content.setFlexGrow(1, form);
    content.setSizeFull();
    return content;
  }

  private void configureForm() {
    form.setWidth("25em");
    form.addSaveListener(this::saveRoom);
    form.addDeleteListener(this::deleteRoom);
    form.addCloseListener(e -> closeEditor());
    form.addOpenListener(e -> openRoomView(e.getRoom()));
  }

  private void saveRoom(RoomForm.SaveEvent event) {
    val room = event.getRoom();
    roomController.add(room);
    userController.addExistRoomToUser(null, room.getUuid());
    updateGrid();
    closeEditor();
  }

  private void deleteRoom(RoomForm.DeleteEvent event) {
    roomController.delete(event.getRoom().getUuid());
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
    grid.setItems(userController.getUserRooms(null, filterText.getValue()).getBody());
  }

  private void openRoomView(Room room) {
    UI.getCurrent().navigate(String.format("/room/%s", room.getUuid()));
  }
}
