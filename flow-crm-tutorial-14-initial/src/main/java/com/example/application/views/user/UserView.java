package com.example.application.views.user;

import com.example.application.bean.UserSessionInfo;
import com.example.application.services.ListService;
import com.example.application.services.LoginService;
import com.example.application.views.LoginPage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;


@PageTitle("Страница пользователя")
@Route(value = "/user", layout = UserLayout.class)
public class UserView extends VerticalLayout {


    private LoginService loginService;
    private ListService listService;

    private PasswordField oldPassword = new PasswordField();
    private PasswordField password = new PasswordField();
    private PasswordField repeatPassword = new PasswordField();

    private Button processButton = new Button();
    private VerticalLayout formLayout = new VerticalLayout();

    public UserView(@Autowired LoginService loginService,
                    @Autowired ListService listService) {
        this.listService = listService;
        this.loginService = loginService;
        add(configureChangePasswordForm());
    }

    private VerticalLayout configureChangePasswordForm() {

        oldPassword.setLabel("Введите старый пароль");
        password.setLabel("Введите новый пароль");
        repeatPassword.setLabel("Подтвердите новый пароль");
        processButton.setText("Изменить");
        processButton.addClickListener(x -> {
            if (listService.isDetected(UserSessionInfo.getInstance().getCurrentUser().getUsername())) {
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
        formLayout.add(oldPassword, password, repeatPassword, processButton);

        formLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return formLayout;
    }

}
