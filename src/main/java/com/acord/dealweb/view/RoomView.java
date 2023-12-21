package com.acord.dealweb.view;

import com.acord.dealweb.controllers.CardController;
import com.acord.dealweb.controllers.RoomController;
import com.acord.dealweb.controllers.UserController;
import com.acord.dealweb.domain.Room;
import com.acord.dealweb.domain.WebUser;
import com.acord.dealweb.domain.card.Card;
import com.acord.dealweb.view.form.RoomUsersListForm;
import com.acord.dealweb.view.form.creating.CardCreatingForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import lombok.val;

import java.util.List;

@PermitAll
@Route(value = "/room/:uuid", layout = MainLayout.class)
@PageTitle("Room")
public class RoomView extends VerticalLayout implements BeforeEnterObserver {
  private final Span VIEW_NAME = new Span("Room");
  private final Grid<Card> grid = new Grid<>(Card.class);
  private final TextField filterText = new TextField();
  private final CardCreatingForm cardCreatingForm;
  private final RoomUsersListForm roomUsersListForm;

  private final UserController userController;
  private final RoomController roomController;
  private final CardController cardController;

  private Room currentRoom;

  public RoomView(
      RoomController roomController, CardController cardController, UserController userController) {
    this.userController = userController;
    this.roomController = roomController;
    this.cardController = cardController;
    setSizeFull();
    configureGrid();

    this.cardCreatingForm = new CardCreatingForm();
    this.roomUsersListForm = new RoomUsersListForm();
    configureForm();

    add(makeToolbar(), makeContent());
    closeEditor();
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    this.currentRoom =
        event
            .getRouteParameters()
            .get("uuid")
            .flatMap(roomId -> roomController.getOne(roomId).getBody())
            .orElseGet(Room::new);
    updateGrid();
  }

  private void configureGrid() {
    grid.setSizeFull();
    grid.setColumns("id", "name", "description", "goal", "fromTime", "toTime");
    grid.getColumns().forEach(column -> column.setAutoWidth(true));
    grid.asSingleSelect().addValueChangeListener(event -> editRoom(event.getValue()));
  }

  private HorizontalLayout makeToolbar() {
    filterText.setPlaceholder("Filter by name...");
    filterText.setClearButtonVisible(true);
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    filterText.addValueChangeListener(event -> updateGrid());

    Button addRoomButton = new Button("Add card");
    addRoomButton.addClickListener(click -> addRoom());

    Button reloadButton = new Button("Reload");
    reloadButton.addClickListener(
        click -> {
          updateGrid();
          roomUsersListForm.updateGrid(getUsersForList());
        });

    return new HorizontalLayout(VIEW_NAME, filterText, addRoomButton, reloadButton);
  }

  private Component makeContent() {
    HorizontalLayout content = new HorizontalLayout(roomUsersListForm, grid, cardCreatingForm);
    content.setFlexGrow(1, roomUsersListForm);
    content.setFlexGrow(2, grid);
    content.setFlexGrow(1, cardCreatingForm);
    content.setSizeFull();
    return content;
  }

  private void configureForm() {
    cardCreatingForm.setWidth("25em");
    cardCreatingForm.addSaveListener(this::saveCard);
    cardCreatingForm.addDeleteListener(this::deleteCard);
    cardCreatingForm.addCloseListener(e -> closeEditor());
    cardCreatingForm.addOpenListener(e -> openCardView(e.getCard()));

    roomUsersListForm.setWidth("25em");
    roomUsersListForm.addAddListener(this::addUser);
    roomUsersListForm.addRemoveListener(this::removeUser);
  }

  private void saveCard(CardCreatingForm.SaveEvent event) {
    val card = event.getCard();
    cardController.add(card);
    roomController.addExistCardToRoom(currentRoom.getUuid(), card.getId());
    updateGrid();
    closeEditor();
  }

  private void deleteCard(CardCreatingForm.DeleteEvent event) {
    roomController.delete(event.getCard().getId());
    updateGrid();
    closeEditor();
  }

  private void editRoom(Card card) {
    if (card == null) {
      closeEditor();
    } else {
      cardCreatingForm.setCard(card);
      cardCreatingForm.setVisible(true);
    }
  }

  private void closeEditor() {
    cardCreatingForm.setCard(null);
    cardCreatingForm.setVisible(false);
  }

  private void addRoom() {
    grid.asSingleSelect().clear();
    editRoom(new Card());
  }

  private void addUser(RoomUsersListForm.AddEvent event) {
    userController.addRoomToUser(event.getUsername(), currentRoom);
    roomUsersListForm.updateGrid(getUsersForList());
  }

  private void removeUser(RoomUsersListForm.RemoveEvent event) {}

  private void updateGrid() {
    grid.setItems(
        roomController.getRoomCards(currentRoom.getUuid(), filterText.getValue()).getBody());
  }

  private void openCardView(Card card) {
    UI.getCurrent().navigate(String.format("/card/%s", card.getId()));
  }

  private List<WebUser> getUsersForList() {
    return roomController.getUsersInRoom(currentRoom.getUuid()).getBody();
  }
}
