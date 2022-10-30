package com.example.application.views.admin;


import com.example.application.bean.UserSessionInfo;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;

@Theme(themeFolder = "flowcrmtutorial")
public class AdminLayout extends AppLayout {

    public AdminLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        Button logout = new Button("Выйти", e -> {
            try {
                (((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()).logout();
                UserSessionInfo.getInstance().cleanCurrentUser();
            } catch (ServletException ex) {
                throw new RuntimeException(ex);
            }
        });
        logout.getStyle().set("margin-right", "15px").set("margin-left", "15px");
        logout.setWidth("150px");

        addToNavbar(header, logout);

    }

    private void createDrawer() {
        RouterLink adminLink = new RouterLink("Страница Админа", AdminView.class);
        RouterLink changePasswordLink = new RouterLink("Смена пароля", AdminChangePassword.class);
        adminLink.setHighlightCondition(HighlightConditions.sameLocation());
        changePasswordLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                adminLink, changePasswordLink
        ));
    }
}