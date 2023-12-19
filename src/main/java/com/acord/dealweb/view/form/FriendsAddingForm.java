package com.acord.dealweb.view.form;

import com.acord.dealweb.domain.UserFriend;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import lombok.Getter;
import lombok.val;

public class FriendsAddingForm extends FormLayout {
  private final TextField username = new TextField("username");
  private final Button addFriendButton = new Button("Add friend");
  private final Button removeFriendButton = new Button("Remove friend");
  private final BeanValidationBinder<UserFriend> binder =
      new BeanValidationBinder<>(UserFriend.class);

  public FriendsAddingForm() {
    binder.bindInstanceFields(this);
    add(username, createButtonsLayout());
  }

  private HorizontalLayout createButtonsLayout() {
    addFriendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    removeFriendButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

    addFriendButton.addClickShortcut(Key.ENTER);

    binder
        .forField(username)
        .withValidator(
            (username, ctx) -> {
              if (username.isEmpty()) {
                return ValidationResult.error("Username can't be empty!");
              } else {
                return ValidationResult.ok();
              }
            })
        .bind("username");

    addFriendButton.addClickListener(event -> validateAndSave());
    removeFriendButton.addClickListener(
        event -> fireEvent(new DeleteFriendEvent(this, binder.getBean())));

    return new HorizontalLayout(addFriendButton, removeFriendButton);
  }

  public void setFriend(UserFriend userFriend) {
    binder.setBean(userFriend);
  }

  private void validateAndSave() {
    try {
      val userFriend = new UserFriend();
      binder.writeBean(userFriend);
      fireEvent(new FriendsAddingForm.AddFriendEvent(this, userFriend));
      //      showSuccess(userFriend); TODO: не показывать когда не найдено
    } catch (ValidationException ex) {
      showError(ex);
    }
  }

  private void showSuccess(UserFriend friendBean) {
    Notification notification =
        Notification.show(String.format("Success adding %s", friendBean.getUsername()));
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
  }

  private void showError(Exception ex) {
    Notification notification = Notification.show(ex.getMessage());
    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
  }

  public void addSaveListener(ComponentEventListener<AddFriendEvent> listener) {
    addListener(AddFriendEvent.class, listener);
  }

  public void addDeleteListener(ComponentEventListener<DeleteFriendEvent> listener) {
    addListener(DeleteFriendEvent.class, listener);
  }

  @Getter
  public abstract static class FriendsAddingFormEvent extends ComponentEvent<FriendsAddingForm> {
    private UserFriend userFriend;

    protected FriendsAddingFormEvent(FriendsAddingForm source, UserFriend userFriend) {
      super(source, false);
      this.userFriend = userFriend;
    }
  }

  public static class AddFriendEvent extends FriendsAddingFormEvent {
    public AddFriendEvent(FriendsAddingForm source, UserFriend userFriend) {
      super(source, userFriend);
    }
  }

  public static class DeleteFriendEvent extends FriendsAddingFormEvent {
    public DeleteFriendEvent(FriendsAddingForm source, UserFriend userFriend) {
      super(source, userFriend);
    }
  }
}
