package com.acord.dealweb.view.form;

import com.acord.dealweb.domain.WebUser;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.List;

public class RoomUsersListForm extends FormLayout {
  private final Grid<WebUser> usersGrid = new Grid<>(WebUser.class);

  private final TextField username = new TextField("Username");

  private final Button addUserButton = new Button("Add User");
  private final Button removeUserButton = new Button("Remove User");

  @Getter @Setter private WebUser webUser;

  public RoomUsersListForm() {
    add(createAddUserLayout(), usersGrid, createButtonsLayout());
    setupButtons();
    configureGrid();
  }

  private HorizontalLayout createAddUserLayout() {
    return new HorizontalLayout(username, addUserButton);
  }

  private VerticalLayout createButtonsLayout() {
    return new VerticalLayout(removeUserButton);
  }

  private void setupButtons() {
    addUserButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
    removeUserButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

    addUserButton.addClickListener(event -> fireEvent(new AddEvent(this, username.getValue())));
    removeUserButton.addClickListener(event -> fireEvent(new RemoveEvent(this, webUser)));

    removeUserButton.setVisible(false);
  }

  private void configureGrid() {
    usersGrid.setSizeFull();
    usersGrid.setColumns("username");
    usersGrid.getColumns().forEach(column -> column.setAutoWidth(true));
    usersGrid.addSelectionListener(
        event -> {
          val maybeUser = event.getFirstSelectedItem();
          webUser = maybeUser.orElse(null);
          removeUserButton.setVisible(maybeUser.isPresent());
        });
  }

  public void updateGrid(List<WebUser> webUsers) {
    usersGrid.setItems(webUsers);
  }

  public void addAddListener(ComponentEventListener<AddEvent> listener) {
    addListener(AddEvent.class, listener);
  }

  public void addRemoveListener(ComponentEventListener<RemoveEvent> listener) {
    addListener(RemoveEvent.class, listener);
  }

  public abstract static class CardFormEvent extends ComponentEvent<RoomUsersListForm> {
    protected CardFormEvent(RoomUsersListForm source) {
      super(source, false);
    }
  }

  public static class AddEvent extends RoomUsersListForm.CardFormEvent {
    @Getter private final String username;

    public AddEvent(RoomUsersListForm source, String username) {
      super(source);
      this.username = username;
    }
  }

  public static class RemoveEvent extends RoomUsersListForm.CardFormEvent {
    @Getter private final WebUser webUser;

    public RemoveEvent(RoomUsersListForm source, WebUser webUser) {
      super(source);
      this.webUser = webUser;
    }
  }
}
