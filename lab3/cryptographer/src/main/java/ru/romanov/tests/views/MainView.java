package ru.romanov.tests.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.romanov.tests.services.CryptoService;

import java.util.UUID;

@PageTitle("Прохождение тестов")
@Route(value = "/", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    /**
     * Кнопка для шифрования
     */
    private Button encryptButton = new Button();
    /**
     * Кнопка для дешифрования
     */
    private Button decryptButton = new Button();
    /**
     * Кнопка для ввода парольной фразы
     */
    private TextField textField = new TextField();
    /**
     * Кнопка для генерации парольной фразы
     */
    private Button generateKeyButton = new Button();

    private CryptoService cryptoService;

    public MainView(@Autowired CryptoService cryptoService) {
        this.cryptoService = cryptoService;
        add(getDivComponents());
    }

    private VerticalLayout getDivComponents() {
        Div div = new Div();
        div.setWidth("700px");

        generateKeyButton.setText("Сгенерировать пароль");
        generateKeyButton.addClickListener(event -> {
            String s = UUID.randomUUID().toString().substring(0, 32);
            textField.setValue(s);
        });
        generateKeyButton.getStyle().set("margin-right", "130px");

        encryptButton.setText("Шифровать");
        encryptButton.addClickListener(event -> {
            String value = textField.getValue();
            cryptoService.encryptFile(value);
            textField.clear();
        });
        encryptButton.getStyle().set("margin-right", "15px");


        decryptButton.setText("Дешифровать");
        decryptButton.addClickListener(event -> {
            cryptoService.decryptFile(textField.getValue());
            textField.clear();
        });
        decryptButton.getStyle().set("margin-right", "15px");

        textField.setLabel("Введите ключ для шифрования/дешифрования");
        textField.setPlaceholder("Введите пароль");
        textField.setWidth("320px");
        textField.getStyle().set("margin-right", "15px");

        div.add(encryptButton, textField, decryptButton);
        Div div1 = new Div();
        div1.add(generateKeyButton);

        VerticalLayout layout = new VerticalLayout();
        layout.add(div, div1);
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        return layout;
    }
}
