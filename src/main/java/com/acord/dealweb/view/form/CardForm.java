package com.acord.dealweb.view.form;

import com.acord.dealweb.domain.card.Card;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import lombok.Getter;

public class CardForm extends FormLayout {
  private final TextField name = new TextField("Name");
  private final TextField description = new TextField("Description");

  private final Button save = new Button("Save");
  private final Button delete = new Button("Delete");
  private final Button close = new Button("Cancel");
  private final Button open = new Button("Open");

  private final BeanValidationBinder<Card> binder = new BeanValidationBinder<>(Card.class);

  public CardForm() {
    binder.bindInstanceFields(this);
    add(name, description, createButtonsLayout());
  }

  public void setCard(Card card) {
    binder.setBean(card);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    open.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

    save.addClickShortcut(Key.ENTER);
    delete.addClickShortcut(Key.DELETE);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(event -> validateAndSave());
    delete.addClickListener(event -> fireEvent(new CardForm.DeleteEvent(this, binder.getBean())));
    close.addClickListener(event -> fireEvent(new CardForm.CloseEvent(this)));
    open.addClickListener(event -> fireEvent(new CardForm.OpenEvent(this, binder.getBean())));

    binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));
    return new HorizontalLayout(save, delete, close);
  }

  private void validateAndSave() {
    if (binder.isValid()) {
      fireEvent(new CardForm.SaveEvent(this, binder.getBean()));
    }
  }

  @Getter
  public abstract static class CardFormEvent extends ComponentEvent<CardForm> {
    private Card card;

    protected CardFormEvent(CardForm source, Card card) {
      super(source, false);
      this.card = card;
    }
  }

  public static class SaveEvent extends CardForm.CardFormEvent {
    public SaveEvent(CardForm source, Card Card) {
      super(source, Card);
    }
  }

  public static class DeleteEvent extends CardForm.CardFormEvent {
    public DeleteEvent(CardForm source, Card card) {
      super(source, card);
    }
  }

  public static class CloseEvent extends CardForm.CardFormEvent {
    public CloseEvent(CardForm source) {
      super(source, null);
    }
  }

  public static class OpenEvent extends CardForm.CardFormEvent {
    public OpenEvent(CardForm source, Card card) {
      super(source, card);
    }
  }

  public void addSaveListener(ComponentEventListener<CardForm.SaveEvent> listener) {
    addListener(CardForm.SaveEvent.class, listener);
  }

  public void addDeleteListener(ComponentEventListener<CardForm.DeleteEvent> listener) {
    addListener(CardForm.DeleteEvent.class, listener);
  }

  public void addCloseListener(ComponentEventListener<CardForm.CloseEvent> listener) {
    addListener(CardForm.CloseEvent.class, listener);
  }

  public void addOpenListener(ComponentEventListener<CardForm.OpenEvent> listener) {
    addListener(CardForm.OpenEvent.class, listener);
  }
}
