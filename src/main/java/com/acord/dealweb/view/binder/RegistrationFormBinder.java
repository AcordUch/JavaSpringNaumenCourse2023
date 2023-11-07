package com.acord.dealweb.view.binder;

import com.acord.dealweb.domain.WebUser;
import com.acord.dealweb.domain.exception.AlreadyRegisteredException;
import com.acord.dealweb.services.UserService;
import com.acord.dealweb.view.form.RegistrationForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;

public class RegistrationFormBinder {
  private final RegistrationForm registrationForm;
  private final UserService userService;

  public RegistrationFormBinder(RegistrationForm registrationForm, UserService userService) {
    this.registrationForm = registrationForm;
    this.userService = userService;
  }

  public void addBindingAndValidation() {
    BeanValidationBinder<WebUser> binder = new BeanValidationBinder<>(WebUser.class);
    binder.bindInstanceFields(registrationForm);

    binder
        .forField(registrationForm.getUsername())
        .withValidator(
            (username, ctx) -> {
              if (username.isEmpty()) {
                return ValidationResult.error("Username can't be empty!");
              } else {
                return ValidationResult.ok();
              }
            })
        .bind("username");

    binder.setStatusLabel(registrationForm.getErrorMessageField());

    registrationForm
        .getSubmitButton()
        .addClickListener(
            event -> {
              try {
                WebUser webUserBean = new WebUser();
                binder.writeBean(webUserBean);

                userService.addUser(webUserBean);

                showSuccess(webUserBean);
              } catch (ValidationException | AlreadyRegisteredException ex) {
                showError(ex);
              }
            });
  }

  private void showSuccess(WebUser userBean) {
    Notification notification = Notification.show("Success, welcome " + userBean.getUsername());
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    UI.getCurrent().getPage().setLocation("/login");
  }

  private void showError(Exception ex) {
    Notification notification = Notification.show(ex.getMessage());
    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
  }
}
