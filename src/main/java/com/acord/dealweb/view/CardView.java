package com.acord.dealweb.view;

import com.acord.dealweb.controllers.CardController;
import com.acord.dealweb.controllers.RoomController;
import com.acord.dealweb.domain.Room;
import com.acord.dealweb.domain.UserDeposit;
import com.acord.dealweb.domain.WebUser;
import com.acord.dealweb.domain.card.Card;
import com.acord.dealweb.view.form.creating.DepositAddingForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import lombok.val;

import java.util.List;

@PermitAll
@Route(value = "/card/:uuid", layout = MainLayout.class)
@PageTitle("Card")
public class CardView extends VerticalLayout implements BeforeEnterObserver {
  private final Span VIEW_NAME = new Span("Card");

  private final Grid<UserDeposit> usersDepositGrid = new Grid<>(UserDeposit.class);

  private final Span goalTextField = new Span("zero");
  private final Span fromTimeTextField = new Span("zero");
  private final Span toTimeTextField = new Span("zero");
  private final Span cardNameTextField = new Span("nope");
  private final Span descriptionTextField = new Span("nope");

  private final DepositAddingForm depositAddingForm;

  private final CardController cardController;
  private final RoomController roomController;

  private Card currentCard;
  private Room currentRoom;
  private List<WebUser> currentUsers;

  public CardView(RoomController roomController, CardController cardController) {
    this.roomController = roomController;
    this.cardController = cardController;
    setSizeFull();
    configureGrid();

    this.depositAddingForm = new DepositAddingForm();

    configureForm();

    add(makeToolbar(), makeContent());
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    this.currentCard =
        event
            .getRouteParameters()
            .get("uuid")
            .flatMap(roomId -> cardController.get(roomId).getBody())
            .orElseGet(Card::new);
    updateInterface();
  }

  private void configureGrid() {
    usersDepositGrid.setSizeFull();
    usersDepositGrid.setColumns("username", "deposit");
    usersDepositGrid.getColumns().forEach(column -> column.setAutoWidth(true));
  }

  private void configureForm() {
    depositAddingForm.setWidth("25em");
    depositAddingForm.addAddListener(e -> addUserDeposit(e.getUserDeposit()));
  }

  private HorizontalLayout makeToolbar() {
    Button reloadButton = new Button("Reload");
    reloadButton.addClickListener(click -> updateInterface());

    return new HorizontalLayout(VIEW_NAME, reloadButton);
  }

  private Component makeContent() {
    val goalBar = makeGoalBar();
    val infoBar = makeInfoBar();
    val depositBar = makeDepositBar();
    val content = new VerticalLayout(goalBar, depositBar, infoBar);
    content.setFlexGrow(1, goalBar);
    content.setFlexGrow(4, depositBar);
    content.setFlexGrow(2, infoBar);
    content.setSizeFull();
    return content;
  }

  private Component makeGoalBar() {
    return new VerticalLayout(cardNameTextField, goalTextField);
  }

  private Component makeInfoBar() {
    val timeBar = new HorizontalLayout(fromTimeTextField, toTimeTextField);
    return new VerticalLayout(timeBar, descriptionTextField);
  }

  private Component makeDepositBar() {
    val content = new HorizontalLayout(usersDepositGrid, depositAddingForm);
    content.setFlexGrow(2, usersDepositGrid);
    content.setFlexGrow(1, depositAddingForm);
    return content;
  }

  private void updateInterface() {
    cardNameTextField.setText(currentCard.getName());
    goalTextField.setText(String.format("Goal: %s", currentCard.getAccumulate().getGoal()));
    fromTimeTextField.setText(currentCard.getFromTime().toString());
    toTimeTextField.setText(currentCard.getToTime().toString());
    descriptionTextField.setText(currentCard.getDescription());
    updateUserDepositGrid();
  }

  private void updateUserDepositGrid() {
    updateCurrentCard();
    val cardAcc = currentCard.getAccumulate();
    usersDepositGrid.setItems(
        cardAcc.getAccumulateFraction().entrySet().stream()
            .map(e -> new UserDeposit(e.getKey(), e.getValue()))
            .toList());
  }

  private void updateCurrentCard() {
    currentCard = cardController.get(currentCard.getId()).getBody().get();
  }

  private void addUserDeposit(UserDeposit userDeposit) {
    cardController.updateDeposit(currentCard.getId(), userDeposit.getDeposit(), null);
    System.out.println("success add");
    updateInterface();
  }
}
