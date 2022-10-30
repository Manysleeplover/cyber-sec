package com.example.application.views;

import com.example.application.entities.User;
import com.example.application.services.ListService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


@PageTitle("Страница админа")
@Route(value = "/admin")
public class AdminView extends VerticalLayout {

    private Button changePasswordbutton = new Button();
    private Button viewUsersButton = new Button();

    private Grid<User> userGrid = new Grid<>(User.class, false);

    private VerticalLayout gridLayout = new VerticalLayout();

    private ListService listService;

    public AdminView(@Autowired ListService listService) {
        this.listService = listService;
        configureGrid();
        changePasswordbutton.setText("Сменить пароль");
        changePasswordbutton.addClickListener(x -> {
            getUI().get().navigate(AdminChangePassword.class);
        });
        viewUsersButton.setText("Показать список пользователей");
        viewUsersButton.addClickListener(x -> {
            gridLayout.removeAll();
            userGrid.setItems(listService.getListWithUsers());
            gridLayout.add(userGrid);
            add(gridLayout);
        });


        add(changePasswordbutton,viewUsersButton);
        setDefaultHorizontalComponentAlignment(Alignment.START);
    }

    private void configureGrid(){
        userGrid.addColumn(User::getUsername).setHeader("Имя пользователя");
        userGrid.addColumn(User::getEmail).setHeader("Почта");
        userGrid.addColumn(User::getBirthDate).setHeader("Дата рождения");
        userGrid.addColumn(User::getRole).setHeader("Роль");
        userGrid.addColumn(User::getIsBlocked).setHeader("Заблокирован?");
        userGrid.addColumn(User::getPasswordRestriction).setHeader("Есть ограничения на пароль?");
        userGrid.addColumn(User::getPassword).setVisible(false);

        userGrid.addItemClickListener(x -> {
            Dialog dialog = new Dialog();
            Select<String> selectBlock = new Select<>();
            selectBlock.setItems("true", "false");
            selectBlock.setLabel("Выключить аккаунт?");

            Select<String> selectRestrict = new Select<>();
            selectRestrict.setItems("true", "false");
            selectRestrict.setLabel("Включить ограничение на пароль?");

            Button button = new Button();
            button.setText("Принять");
            button.addClickListener(y -> {
                listService.changeParams(x.getItem(), selectBlock.getValue(), selectRestrict.getValue());
                userGrid.setItems(listService.getListWithUsers());
                dialog.close();
            });

            dialog.add(selectBlock, selectRestrict, button);
            dialog.open();
        });
    }

}
