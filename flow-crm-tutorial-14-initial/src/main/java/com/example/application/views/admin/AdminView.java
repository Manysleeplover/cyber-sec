package com.example.application.views.admin;

import com.example.application.bean.UserSessionInfo;
import com.example.application.entities.User;
import com.example.application.services.ListService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@PageTitle("Страница админа")
@Route(value = "/admin", layout = AdminLayout.class)
public class AdminView extends VerticalLayout {


    private Grid<User> userGrid = new Grid<>(User.class, false);

    private VerticalLayout gridLayout = new VerticalLayout();
    private ListService listService;

    private Button addNewUserButton;

    public AdminView(@Autowired ListService listService,
                     HttpServletResponse resp) throws IOException {
        if(UserSessionInfo.getInstance().getCurrentUser() == null || !UserSessionInfo.getInstance().getCurrentUser().getRole().equals("admin")){
            resp.sendRedirect("/");
        }
        this.listService = listService;
        configureGrid();
        configureAddingUsersButton();

        add(userGrid, addNewUserButton);
        setDefaultHorizontalComponentAlignment(Alignment.START);
    }

    private void configureAddingUsersButton(){
        addNewUserButton = new Button();
        addNewUserButton.setText("Добавить нового пльзователя");
        addNewUserButton.addClickListener(x ->{
            Dialog dialog = new Dialog();
            Paragraph p1 = new Paragraph();
            Text text = new Text("Дабвелние нового пользователя");
            p1.add(text);
            TextField usernameField = new TextField();
            usernameField.setLabel("Имя пользователя");
            Paragraph p2 = new Paragraph();
            p2.add(usernameField);
            Button submitButton = new Button();
            submitButton.setText("Отправить");
            Paragraph p3 = new Paragraph();
            p3.add(submitButton);
            submitButton.addClickListener(event ->{
                if(listService.isDetected(usernameField.getValue())){
                    usernameField.setErrorMessage("Пользователь с таким именем уже существует");
                    usernameField.setInvalid(true);
                } else if (!usernameField.getValue().isEmpty() || !usernameField.getValue().equals("")){
                    listService.addUser(usernameField.getValue());
                    dialog.removeAll();
                    dialog.add(new H3("Пользователь успешно добавлен"));
                    userGrid.setItems(listService.getListWithUsers());
                }
            });
            dialog.add(p1, p2, p3);
            dialog.open();
            userGrid.setItems(listService.getListWithUsers());

        });
    }

    private void configureGrid(){
        userGrid.addColumn(User::getUsername).setHeader("Имя пользователя");
        userGrid.addColumn(User::getRole).setHeader("Роль");
        userGrid.addColumn(User::getIsBlocked).setHeader("Заблокирован?");
        userGrid.addColumn(User::getPasswordRestriction).setHeader("Есть ограничения на пароль?");
        userGrid.addColumn(User::getPassword).setVisible(false);

        userGrid.addItemClickListener(x -> {
            Dialog dialog = new Dialog();
            Select<String> selectBlock = new Select<>();
            selectBlock.setItems("true", "false");
            selectBlock.setLabel("Выключить аккаунт?");
            selectBlock.setValue("false");

            Select<String> selectRestrict = new Select<>();
            selectRestrict.setItems("true", "false");
            selectRestrict.setLabel("Включить ограничение на пароль?");
            selectRestrict.setValue("false");



            Button button = new Button();
            button.setText("Принять");
            button.addClickListener(y -> {
                listService.changeParams(x.getItem(), selectBlock.getValue(), selectRestrict.getValue());
                userGrid.setItems(listService.getListWithUsers());
                dialog.close();
            });

            selectBlock.getStyle().set("margin-left", "10px");
            selectRestrict.getStyle().set("margin-left", "10px");
            button.getStyle().set("margin-left", "10px");
            Paragraph p = new Paragraph();
            p.getStyle().set("margin-left", "38%");
            p.add(button);

            dialog.add(selectBlock, selectRestrict, p);
            dialog.open();
        });

        userGrid.setItems(listService.getListWithUsers());
    }

}
