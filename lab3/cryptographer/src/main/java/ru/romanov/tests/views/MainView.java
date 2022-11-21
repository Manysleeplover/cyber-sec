package ru.romanov.tests.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.romanov.tests.services.CryptoService;

@PageTitle("Прохождение тестов")
@Route(value = "/", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    private Button encryptButton = new Button();
    private Button decryptButton = new Button();
    private TextField textField = new TextField();

    private CryptoService cryptoService;

    public MainView(@Autowired CryptoService cryptoService) {
        this.cryptoService = cryptoService;
        add(getDivComponents());
    }

    private VerticalLayout getDivComponents() {
        Div div = new Div();
        div.setWidth("700px");

        encryptButton.setText("Шифровать");
        encryptButton.addClickListener(event -> {
            cryptoService.encryptFile("sfgjs");
            textField.clear();
        });
        encryptButton.getStyle().set("margin-right", "15px");


        decryptButton.setText("Дешифровать");
        decryptButton.addClickListener(event ->{
            textField.clear();
        });
        decryptButton.getStyle().set("margin-right", "15px");

        textField.setLabel("Введите ключ для шифрования/дешифрования");
        textField.setWidth("320px");
        textField.getStyle().set("margin-right", "15px");

        div.add(encryptButton, textField, decryptButton);

        VerticalLayout layout = new VerticalLayout();
        layout.add(div);
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        return layout;
    }
}
