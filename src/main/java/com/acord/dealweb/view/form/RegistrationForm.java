package com.acord.dealweb.view.form;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;

public class RegistrationForm extends FormLayout {
  private final H3 title;
  @Getter private final TextField username;
  private final TextField password;
  @Getter private final Span errorMessageField;
  @Getter private final Button submitButton;

  public RegistrationForm() {
    title = new H3("Signup form");
    username = new TextField("Username");
    password = new TextField("Password");
    errorMessageField = new Span();
    submitButton = new Button("SignUp!");
    submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    add(title, username, password, errorMessageField, submitButton);

    // Max width of the Form
    setMaxWidth("500px");

    // Allow the form layout to be responsive.
    // On device widths 0-490px we have one column.
    // Otherwise, we have two columns.
    setResponsiveSteps(
        new ResponsiveStep("0", 1, ResponsiveStep.LabelsPosition.TOP),
        new ResponsiveStep("490px", 2, ResponsiveStep.LabelsPosition.TOP));

    // These components always take full width
    setColspan(title, 2);
    setColspan(errorMessageField, 2);
    setColspan(submitButton, 2);
  }
}
