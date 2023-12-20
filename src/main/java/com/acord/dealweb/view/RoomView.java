package com.acord.dealweb.view;

import com.acord.dealweb.controllers.CardController;
import com.acord.dealweb.controllers.RoomController;
import com.acord.dealweb.domain.Room;
import com.acord.dealweb.domain.card.Card;
import com.acord.dealweb.view.form.CardForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import lombok.val;

@PermitAll
@Route(value = "/room/:uuid", layout = MainLayout.class)
@PageTitle("Room")
public class RoomView extends VerticalLayout implements BeforeEnterObserver {
  private final Grid<Card> grid = new Grid<>(Card.class);
  private final TextField filterText = new TextField();
  private final CardForm cardForm;
  private final RoomController roomController;
  private final CardController cardController;

  private Room currentRoom;

  public RoomView(RoomController roomController, CardController cardController) {
    this.roomController = roomController;
    this.cardController = cardController;
    setSizeFull();
    configureGrid();

    this.cardForm = new CardForm();
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
    grid.setColumns("id", "name", "description", "fromTime", "toTime");
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
    reloadButton.addClickListener(click -> updateGrid());

    return new HorizontalLayout(filterText, addRoomButton, reloadButton);
  }

  private Component makeContent() {
    HorizontalLayout content = new HorizontalLayout(grid, cardForm);
    content.setFlexGrow(2, grid);
    content.setFlexGrow(1, cardForm);
    content.setSizeFull();
    return content;
  }

  private void configureForm() {
    cardForm.setWidth("25em");
    cardForm.addSaveListener(this::saveCard);
    cardForm.addDeleteListener(this::deleteCard);
    cardForm.addCloseListener(e -> closeEditor());
    cardForm.addOpenListener(e -> {}); // TODO
  }

  private void saveCard(CardForm.SaveEvent event) {
    val card = event.getCard();
    cardController.add(card);
    roomController.addExistCardToRoom(currentRoom.getUuid(), card.getId());
    updateGrid();
    closeEditor();
  }

  private void deleteCard(CardForm.DeleteEvent event) {
    roomController.delete(event.getCard().getId());
    updateGrid();
    closeEditor();
  }

  private void editRoom(Card card) {
    if (card == null) {
      closeEditor();
    } else {
      cardForm.setCard(card);
      cardForm.setVisible(true);
    }
  }

  private void closeEditor() {
    cardForm.setCard(null);
    cardForm.setVisible(false);
  }

  private void addRoom() {
    grid.asSingleSelect().clear();
    editRoom(new Card());
  }

  private void updateGrid() {
    grid.setItems(
        roomController.getRoomCards(currentRoom.getUuid(), filterText.getValue()).getBody());
  }
}
