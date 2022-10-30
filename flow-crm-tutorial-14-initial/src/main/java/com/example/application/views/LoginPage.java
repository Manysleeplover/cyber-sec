package com.example.application.views;

import com.example.application.bean.UserSessionInfo;
import com.example.application.entities.User;
import com.example.application.services.ListService;
import com.example.application.services.LoginService;
import com.example.application.views.admin.AdminView;
import com.example.application.views.user.UserView;
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

    private final LoginService loginService;
    private final ListService listService;
    private final TextField username = new TextField();
    private final PasswordField password = new PasswordField();
    private final Button processButton = new Button();
    private final VerticalLayout formLayout = new VerticalLayout();

    public LoginPage(@Autowired LoginService loginService,
                     @Autowired ListService listService) {
        this.listService = listService;
        this.loginService = loginService;
        add(configureLoginForm());
    }

    private VerticalLayout configureLoginForm() {
        username.setLabel("Логин");
        username.setErrorMessage("Такого пользователя не существует");

        password.setLabel("Пароль");
        password.setErrorMessage("Неверный пароль, повторите еще");

        processButton.setText("Войти");
        processButton.addClickListener(x -> {
            if (!listService.isDetected(username.getValue())) {
                username.setInvalid(true);
            } else {
                if (loginService.isDetected(username.getValue(), password.getValue()).equals("admin")) {
                    UserSessionInfo.getInstance().setCurrentUser(loginService.getUser(username.getValue(), password.getValue()));
                    getUI().get().navigate(AdminView.class);
                }
                if (loginService.isDetected(username.getValue(), password.getValue()).equals("user")) {
                    User user = loginService.getUser(username.getValue(), password.getValue());
                    if(user.getIsBlocked()){
                        Dialog dialog = new Dialog();
                        dialog.add("Аккаунт заблокирован");
                        dialog.open();
                    } else{
                        UserSessionInfo.getInstance().setCurrentUser(user);
                        getUI().get().navigate(UserView.class);
                    }
                }
                if (loginService.isDetected(username.getValue(), password.getValue()).equals("none")) {
                    username.clear();
                    password.clear();
                    Dialog dialog = new Dialog();
                    dialog.add("Введён неврный пароль, пожалуйста, повторите попытку");
                    dialog.open();
                }
            }
        });
        processButton.setAutofocus(true);
        formLayout.add(username, password, processButton);

        formLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        return formLayout;
    }
}
