package com.acord.dealweb.view.form.creating;

import com.acord.dealweb.domain.card.Card;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import lombok.Getter;

public class CardCreatingForm extends FormLayout {
  private final TextField name = new TextField("Name");
  private final TextField description = new TextField("Description");
  private final IntegerField goal = new IntegerField("Goal");
  private final DatePicker fromDate = new DatePicker("From Date");
  private final DatePicker toDate = new DatePicker("From Date");

  private final Button save = new Button("Save");
  private final Button delete = new Button("Delete");
  private final Button close = new Button("Cancel");
  private final Button open = new Button("Open");

  private final BeanValidationBinder<Card> binder = new BeanValidationBinder<>(Card.class);

  public CardCreatingForm() {
    binder.bindInstanceFields(this);
    //    binder.forField(goal)
    //            .withConverter(new StringToIntegerConverter("Not a number"))
    //                    .bind(Card::getGoal, Card::setGoal);

    goal.setReadOnly(false);
    add(name, description, goal, createButtonsLayout());
  }

  public void setCard(Card card) {
    binder.setBean(card);
  }

  private VerticalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    open.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

    save.addClickShortcut(Key.ENTER);
    delete.addClickShortcut(Key.DELETE);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(event -> validateAndSave());
    delete.addClickListener(
        event -> fireEvent(new CardCreatingForm.DeleteEvent(this, binder.getBean())));
    close.addClickListener(event -> fireEvent(new CardCreatingForm.CloseEvent(this)));
    open.addClickListener(
        event -> fireEvent(new CardCreatingForm.OpenEvent(this, binder.getBean())));

    binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));
    return new VerticalLayout(new HorizontalLayout(save, delete, close), open);
  }

  private void validateAndSave() {
    if (binder.isValid()) {
      fireEvent(new CardCreatingForm.SaveEvent(this, binder.getBean()));
    }
  }

  @Getter
  public abstract static class CardFormEvent extends ComponentEvent<CardCreatingForm> {
    private final Card card;

    protected CardFormEvent(CardCreatingForm source, Card card) {
      super(source, false);
      this.card = card;
    }
  }

  public static class SaveEvent extends CardCreatingForm.CardFormEvent {
    public SaveEvent(CardCreatingForm source, Card Card) {
      super(source, Card);
    }
  }

  public static class DeleteEvent extends CardCreatingForm.CardFormEvent {
    public DeleteEvent(CardCreatingForm source, Card card) {
      super(source, card);
    }
  }

  public static class CloseEvent extends CardCreatingForm.CardFormEvent {
    public CloseEvent(CardCreatingForm source) {
      super(source, null);
    }
  }

  public static class OpenEvent extends CardCreatingForm.CardFormEvent {
    public OpenEvent(CardCreatingForm source, Card card) {
      super(source, card);
    }
  }

  public void addSaveListener(ComponentEventListener<CardCreatingForm.SaveEvent> listener) {
    addListener(CardCreatingForm.SaveEvent.class, listener);
  }

  public void addDeleteListener(ComponentEventListener<CardCreatingForm.DeleteEvent> listener) {
    addListener(CardCreatingForm.DeleteEvent.class, listener);
  }

  public void addCloseListener(ComponentEventListener<CardCreatingForm.CloseEvent> listener) {
    addListener(CardCreatingForm.CloseEvent.class, listener);
  }

  public void addOpenListener(ComponentEventListener<CardCreatingForm.OpenEvent> listener) {
    addListener(CardCreatingForm.OpenEvent.class, listener);
  }
}
