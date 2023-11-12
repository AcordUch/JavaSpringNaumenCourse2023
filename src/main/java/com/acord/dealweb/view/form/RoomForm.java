package com.acord.dealweb.view.form;

import com.acord.dealweb.domain.Room;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import lombok.Getter;

public class RoomForm extends FormLayout {
  private final TextField name = new TextField("Name");
  private final PasswordField password = new PasswordField("Password");

  private final Button save = new Button("Save");
  private final Button delete = new Button("Delete");
  private final Button close = new Button("Cancel");

  private final BeanValidationBinder<Room> binder = new BeanValidationBinder<>(Room.class);

  public RoomForm() {
    binder.bindInstanceFields(this);
    add(name, password, createButtonsLayout());
  }

  public void setRoom(Room room) {
    binder.setBean(room);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickShortcut(Key.ENTER);
    delete.addClickShortcut(Key.DELETE);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(event -> validateAndSave());
    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));

    binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));
    return new HorizontalLayout(save, delete, close);
  }

  private void validateAndSave() {
    if (binder.isValid()) {
      fireEvent(new SaveEvent(this, binder.getBean()));
    }
  }

  @Getter
  public abstract static class RoomFormEvent extends ComponentEvent<RoomForm> {
    private Room room;

    protected RoomFormEvent(RoomForm source, Room room) {
      super(source, false);
      this.room = room;
    }
  }

  public static class SaveEvent extends RoomFormEvent {
    public SaveEvent(RoomForm source, Room room) {
      super(source, room);
    }
  }

  public static class DeleteEvent extends RoomFormEvent {
    public DeleteEvent(RoomForm source, Room room) {
      super(source, room);
    }
  }

  public static class CloseEvent extends RoomFormEvent {
    public CloseEvent(RoomForm source) {
      super(source, null);
    }
  }

  public void addSaveListener(ComponentEventListener<SaveEvent> listener) {
    addListener(SaveEvent.class, listener);
  }

  public void addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
    addListener(DeleteEvent.class, listener);
  }

  public void addCloseListener(ComponentEventListener<CloseEvent> listener) {
    addListener(CloseEvent.class, listener);
  }
}
