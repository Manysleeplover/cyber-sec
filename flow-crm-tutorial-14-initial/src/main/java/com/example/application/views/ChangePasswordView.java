package com.example.application.views;

import com.example.application.bean.UserSessionInfo;
import com.example.application.services.ListService;
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

@PageTitle("Смена пароля")
@Route(value = "/change_password")
public class ChangePasswordView extends VerticalLayout {


    private final LoginService loginService;
    private final ListService listService;
    private final TextField username = new TextField();
    private final PasswordField oldPassword = new PasswordField();
    private final PasswordField password = new PasswordField();
    private final PasswordField repeatPassword = new PasswordField();
    private final Button processButton = new Button();
    private final VerticalLayout formLayout = new VerticalLayout();


    public ChangePasswordView(@Autowired LoginService loginService,
                    @Autowired ListService listService){
        this.listService = listService;
        this.loginService = loginService;

        add(configureChangePasswordForm());
    }

    private VerticalLayout configureChangePasswordForm() {
        username.setLabel("Имя пользователя");
        oldPassword.setLabel("Введите старый пароль");
        oldPassword.setRevealButtonVisible(false);
        password.setLabel("Введите новый пароль");
        password.setRevealButtonVisible(false);
        repeatPassword.setLabel("Подтвердите новый пароль");
        repeatPassword.setRevealButtonVisible(false);
        processButton.setText("Изменить");
        processButton.addClickListener(x -> {
            if (listService.isDetected(username.getValue())) {
                if (Objects.equals(password.getValue(), repeatPassword.getValue())) {
                    if (loginService.changeAdminPassword(UserSessionInfo.getInstance().getCurrentUser().getUsername(), oldPassword.getValue(), password.getValue())) {
                        Dialog dialog = new Dialog();
                        dialog.add("Пароль успешно изменён");
                        dialog.open();
                        getUI().get().navigate(LoginPage.class);
                    }
                }
            } else {
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
