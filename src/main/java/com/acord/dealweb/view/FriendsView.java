package com.acord.dealweb.view;

import com.acord.dealweb.controllers.UserController;
import com.acord.dealweb.domain.UserFriend;
import com.acord.dealweb.view.form.FriendsAddingForm;
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
import lombok.val;

@PermitAll
@Route(value = "/friends", layout = MainLayout.class)
@PageTitle("Friends")
public class FriendsView extends VerticalLayout {
  private final Grid<UserFriend> grid = new Grid<>(UserFriend.class);
  private final TextField filterText = new TextField();

  private final FriendsAddingForm friendsAddingForm;
  private final UserController userController;

  public FriendsView(UserController userController) {
    this.userController = userController;
    setSizeFull();
    configureGrid();

    this.friendsAddingForm = new FriendsAddingForm();
    configureForm();

    add(makeToolbar(), makeContent());
    updateGrid();
    closeEditor();
  }

  private void configureGrid() {
    grid.setSizeFull();
    grid.setColumns("username");
    grid.getColumns().forEach(column -> column.setAutoWidth(true));
    grid.asSingleSelect().addValueChangeListener(event -> editFriend(event.getValue()));
  }

  private HorizontalLayout makeToolbar() {
    filterText.setPlaceholder("Filter by username...");
    filterText.setClearButtonVisible(true);
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    filterText.addValueChangeListener(event -> updateGrid());

    Button addRoomButton = new Button("Add friend");
    addRoomButton.addClickListener(click -> addFriend());

    Button reloadButton = new Button("Reload");
    reloadButton.addClickListener(click -> updateGrid());

    return new HorizontalLayout(filterText, addRoomButton, reloadButton);
  }

  private Component makeContent() {
    HorizontalLayout content = new HorizontalLayout(grid, friendsAddingForm);
    content.setFlexGrow(2, grid);
    content.setFlexGrow(1, friendsAddingForm);
    content.setSizeFull();
    return content;
  }

  private void configureForm() {
    friendsAddingForm.setWidth("25em");
    friendsAddingForm.addSaveListener(this::saveFriend);
    friendsAddingForm.addDeleteListener(this::deleteFriend);
  }

  private void saveFriend(FriendsAddingForm.AddFriendEvent event) {
    val friend = event.getUserFriend();
    userController.addFriendToUser(friend.getUsername(), null);
    updateGrid();
  }

  private void deleteFriend(FriendsAddingForm.DeleteFriendEvent event) {
    userController.removeFriendsFromUser(event.getUserFriend().getUsername(), null);
    updateGrid();
    closeEditor();
  }

  private void addFriend() {
    grid.asSingleSelect().clear();
    editFriend(new UserFriend());
  }

  private void editFriend(UserFriend friend) {
    if (friend == null) {
      closeEditor();
    } else {
      friendsAddingForm.setFriend(friend);
      friendsAddingForm.setVisible(true);
    }
    friendsAddingForm.setVisible(true);
  }

  private void closeEditor() {
    friendsAddingForm.setFriend(null);
    friendsAddingForm.setVisible(false);
  }

  private void updateGrid() {
    grid.setItems(
        userController.getUserFriends(filterText.getValue(), null).getBody().stream()
            .map(webUser -> new UserFriend(webUser.getUsername()))
            .toList());
  }
}
