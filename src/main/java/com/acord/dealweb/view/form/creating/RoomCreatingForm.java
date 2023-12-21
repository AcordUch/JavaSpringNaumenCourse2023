package com.acord.dealweb.view.form.creating;

import com.acord.dealweb.domain.Room;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import lombok.Getter;

public class RoomCreatingForm extends FormLayout {
  private final TextField name = new TextField("Name");
  private final PasswordField password = new PasswordField("Password");

  private final Button save = new Button("Save");
  private final Button delete = new Button("Delete");
  private final Button close = new Button("Cancel");
  private final Button open = new Button("Open");

  private final BeanValidationBinder<Room> binder = new BeanValidationBinder<>(Room.class);

  public RoomCreatingForm() {
    binder.bindInstanceFields(this);
    add(name, password, createButtonsLayout());
  }

  public void setRoom(Room room) {
    binder.setBean(room);
  }

  private VerticalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    open.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    save.addClickShortcut(Key.ENTER);
    delete.addClickShortcut(Key.DELETE);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(event -> validateAndSave());
    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));
    open.addClickListener(event -> fireEvent(new OpenEvent(this, binder.getBean())));

    binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));
    return new VerticalLayout(new HorizontalLayout(save, delete, close), open);
  }

  private void validateAndSave() {
    if (binder.isValid()) {
      fireEvent(new SaveEvent(this, binder.getBean()));
    }
  }

  @Getter
  public abstract static class RoomFormEvent extends ComponentEvent<RoomCreatingForm> {
    private Room room;

    protected RoomFormEvent(RoomCreatingForm source, Room room) {
      super(source, false);
      this.room = room;
    }
  }

  public static class SaveEvent extends RoomFormEvent {
    public SaveEvent(RoomCreatingForm source, Room room) {
      super(source, room);
    }
  }

  public static class DeleteEvent extends RoomFormEvent {
    public DeleteEvent(RoomCreatingForm source, Room room) {
      super(source, room);
    }
  }

  public static class CloseEvent extends RoomFormEvent {
    public CloseEvent(RoomCreatingForm source) {
      super(source, null);
    }
  }

  public static class OpenEvent extends RoomFormEvent {
    public OpenEvent(RoomCreatingForm source, Room room) {
      super(source, room);
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

  public void addOpenListener(ComponentEventListener<OpenEvent> listener) {
    addListener(OpenEvent.class, listener);
  }
}
