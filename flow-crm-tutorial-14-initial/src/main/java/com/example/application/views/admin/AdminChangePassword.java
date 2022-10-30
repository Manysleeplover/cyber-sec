package com.example.application.views.admin;


import com.example.application.services.LoginService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@PageTitle("Страница админа")
@Route(value = "/admin_change_password", layout = AdminLayout.class)
public class AdminChangePassword extends VerticalLayout {

    private LoginService loginService;

    private TextField username = new TextField();
    private PasswordField oldPassword = new PasswordField();
    private PasswordField password = new PasswordField();
    private PasswordField repeatPassword = new PasswordField();

    private Button processButton = new Button();
    private VerticalLayout formLayout = new VerticalLayout();

    public AdminChangePassword(@Autowired LoginService loginService) {
        this.loginService = loginService;
        add(configureChangePasswordForm());
    }

    private VerticalLayout configureChangePasswordForm() {
        username.setLabel("Подтвердите имя пользователя");
        oldPassword.setLabel("Введите старый пароль");
        password.setLabel("Введите новый пароль");
        repeatPassword.setLabel("Подтвердите новый пароль");
        processButton.setText("Войти");
        processButton.addClickListener(x -> {
            if (loginService.isDetected(username.getValue(), oldPassword.getValue()).equals("admin")) {
                if (Objects.equals(password.getValue(), repeatPassword.getValue())) {
                    if (loginService.changeAdminPassword(username.getValue(), oldPassword.getValue(), password.getValue())) {
                        Dialog dialog = new Dialog();
                        dialog.add("Пароль успешно изменён");
                        dialog.open();
                        getUI().get().navigate(AdminView.class);
                    }
                }
            } else {
                username.clear();
                oldPassword.clear();
                password.clear();
                repeatPassword.clear();
                Dialog dialog = new Dialog();
                dialog.add("Что-то пошло не так, попробуйте еще раз");
                dialog.open();
            }
        });
        formLayout.add(username, oldPassword, password, repeatPassword, processButton);

        formLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return formLayout;
    }
}
