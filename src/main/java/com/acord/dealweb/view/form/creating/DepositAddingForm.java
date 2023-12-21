package com.acord.dealweb.view.form.creating;

import com.acord.dealweb.domain.UserDeposit;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import lombok.Getter;
import lombok.val;

public class DepositAddingForm extends FormLayout {
  private final IntegerField deposit = new IntegerField("Deposit");

  private final Button add = new Button("Add");

  private final BeanValidationBinder<UserDeposit> binder =
      new BeanValidationBinder<>(UserDeposit.class);

  public DepositAddingForm() {
    binder.bindInstanceFields(this);
    binder.forField(deposit).bind("deposit");
    deposit.setReadOnly(false);
    add(deposit, createButtonsLayout());
  }

  private VerticalLayout createButtonsLayout() {
    add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    add.addClickShortcut(Key.ENTER);
    add.addClickListener(
        event -> {
          try {
            val bean = new UserDeposit();
            binder.writeBean(bean);
            fireEvent(new AddEvent(this, bean));
          } catch (ValidationException ex) {
            System.err.println(ex.getMessage());
          }
        });

    return new VerticalLayout(add);
  }

  public void addAddListener(ComponentEventListener<AddEvent> listener) {
    addListener(AddEvent.class, listener);
  }

  public abstract static class DepositAddingFormEvent extends ComponentEvent<DepositAddingForm> {
    protected DepositAddingFormEvent(DepositAddingForm source) {
      super(source, false);
    }
  }

  public static class AddEvent extends DepositAddingForm.DepositAddingFormEvent {
    @Getter private final UserDeposit userDeposit;

    public AddEvent(DepositAddingForm source, UserDeposit userDeposit) {
      super(source);
      this.userDeposit = userDeposit;
    }
  }
}
