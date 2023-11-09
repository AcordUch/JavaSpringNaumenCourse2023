package com.acord.dealweb.view;

import com.acord.dealweb.services.UserService;
import com.acord.dealweb.view.binder.RegistrationFormBinder;
import com.acord.dealweb.view.form.RegistrationForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/registration")
public class RegistrationView extends VerticalLayout {
  public RegistrationView(@Autowired UserService userService) {
    RegistrationForm registrationForm = new RegistrationForm();
    setHorizontalComponentAlignment(Alignment.CENTER, registrationForm);

    add(registrationForm);

    RegistrationFormBinder registrationFormBinder =
        new RegistrationFormBinder(registrationForm, userService);
    registrationFormBinder.addBindingAndValidation();
  }
}
