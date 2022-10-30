package com.example.application.views.admin;


import com.example.application.bean.UserSessionInfo;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@PageTitle("Страница админа")
@Route(value = "/admin_change_password", layout = AdminLayout.class)
public class AdminChangePassword extends VerticalLayout {

    private LoginService loginService;

    private PasswordField oldPassword = new PasswordField();
    private PasswordField password = new PasswordField();
    private PasswordField repeatPassword = new PasswordField();

    private Button processButton = new Button();
    private VerticalLayout formLayout = new VerticalLayout();

    public AdminChangePassword(@Autowired LoginService loginService, HttpServletResponse resp) throws IOException {
        if(UserSessionInfo.getInstance().getCurrentUser() == null || !UserSessionInfo.getInstance().getCurrentUser().getRole().equals("admin")){
            resp.sendRedirect("/");
        }
        this.loginService = loginService;
        add(configureChangePasswordForm());
    }

    private VerticalLayout configureChangePasswordForm() {
        oldPassword.setLabel("Введите старый пароль");
        password.setLabel("Введите новый пароль");
        repeatPassword.setLabel("Подтвердите новый пароль");
        processButton.setText("Изменить");
        processButton.addClickListener(x -> {
            if (loginService.isDetected(UserSessionInfo.getInstance().getCurrentUser().getUsername(), oldPassword.getValue()).equals("admin")) {
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
