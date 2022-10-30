package com.example.application.views;

import com.example.application.services.LoginService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import org.springframework.beans.factory.annotation.Autowired;


@Theme(themeFolder = "flowcrmtutorial")
@PageTitle("Авторизация")
@Route(value = "")
public class LoginPage extends VerticalLayout {

    private LoginService loginService;
    private TextField username = new TextField();
    private PasswordField password = new PasswordField();

    private Button processButton = new Button();
    private VerticalLayout formLayout = new VerticalLayout();


    public LoginPage(@Autowired LoginService loginService) {
        this.loginService = loginService;
        add(configureLoginForm());
    }

    private VerticalLayout configureLoginForm() {
        username.setLabel("Логин");

        password.setLabel("Пароль");

        processButton.setText("Войти");
        processButton.addClickListener(x -> {
            if (loginService.isDetected(username.getValue(), password.getValue()).equals("admin")) {
                getUI().get().navigate(AdminView.class);
            }
            if (loginService.isDetected(username.getValue(), password.getValue()).equals("user")) {
                getUI().get().navigate(UserView.class);
            }
            if (loginService.isDetected(username.getValue(), password.getValue()).equals("none")) {
                username.clear();
                password.clear();
                Dialog dialog = new Dialog();
                dialog.add("Введён неврный пароль, пожалуйста, повторите попытку");
                dialog.open();
            }


        });
        formLayout.add(username, password, processButton);

        formLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        return formLayout;
    }


}
