package ru.romanov.tests.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.romanov.tests.services.CryptoService;

import java.util.UUID;

@PageTitle("Шифрование файлов")
@Route(value = "/", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    /**
     * Кнопка для шифрования
     */
    private final Button encryptButton = new Button();
    /**
     * Кнопка для дешифрования
     */
    private final Button decryptButton = new Button();
    /**
     * Кнопка для ввода старой парольной фразы
     */
    private final TextField requireEncryptPasswordField = new TextField();
    /**
     * Кнопка для ввода старой парольной фразы
     */
    private final TextField requireDecryptPasswordField = new TextField();
    /**
     * Кнопка для ввода новой парольной фразы
     */
    private final TextField newPasswordField = new TextField();
    /**
     * Кнопка для генерации парольной фразы
     */
    private final Button generateKeyButton = new Button();
    /**
     * Кнопка для очистки временный файлов.
     */
    private final Button deleteTempFile = new Button();
    /**
     * Сервис обработки процесса шифрования
     */
    private final CryptoService cryptoService;

    public MainView(@Autowired CryptoService cryptoService) {
        this.cryptoService = cryptoService;
        add(getDivComponents());
    }

    private VerticalLayout getDivComponents() {
        generateKeyButton.setText("Сгенерировать пароль");
        generateKeyButton.addClickListener(event -> {
            String s = UUID.randomUUID().toString().substring(0, 32);
            newPasswordField.setValue(s);
        });
        generateKeyButton.getStyle().set("margin-right", "130px");


        encryptButton.setText("Шифровать");
        encryptButton.setWidth("300px");
        encryptButton.addClickListener(event -> {
            if (cryptoService.isExpectedPassword(requireEncryptPasswordField.getValue()) && !newPasswordField.getValue().equals("")) {
                cryptoService.encryptFile(newPasswordField.getValue());
                cryptoService.writePasswordToFile(newPasswordField.getValue());
                requireEncryptPasswordField.clear();
                newPasswordField.clear();
                Dialog dialog = new Dialog();
                dialog.add(new Paragraph("Файл успешно зашифрован"));
                dialog.open();
            } else if (newPasswordField.getValue().equals("")) {
                newPasswordField.setInvalid(true);
            } else if (!cryptoService.isExpectedPassword(requireEncryptPasswordField.getValue())) {
                requireEncryptPasswordField.setInvalid(true);
            }
        });
        encryptButton.getStyle().set("margin-right", "15px");


        decryptButton.setText("Дешифровать");
        decryptButton.setWidth("300px");
        decryptButton.addClickListener(event -> {
            if (cryptoService.isExpectedPassword(requireDecryptPasswordField.getValue())) {
                cryptoService.decryptFile(requireDecryptPasswordField.getValue());
                requireDecryptPasswordField.clear();
                Dialog dialog = new Dialog();
                dialog.add(new Paragraph("Файл успешно дешифрован"));
                dialog.open();
            } else {
                requireDecryptPasswordField.setInvalid(true);
            }
        });
        decryptButton.getStyle().set("margin-right", "15px");


        requireEncryptPasswordField.setLabel("Введите действительный ключ для шифрования");
        requireEncryptPasswordField.setErrorMessage("Введён неверный пароль, попробуйте еще раз");
        requireEncryptPasswordField.setPlaceholder("Введите пароль");
        requireEncryptPasswordField.setWidth("320px");
        requireEncryptPasswordField.getStyle().set("margin-right", "15px");


        newPasswordField.setLabel("Введите новый пароль для шифрования");
        newPasswordField.setErrorMessage("Пароль не может быть пустым");
        newPasswordField.setPlaceholder("Введите новый пароль");
        newPasswordField.setWidth("320px");
        newPasswordField.getStyle().set("margin-right", "15px");


        requireDecryptPasswordField.setLabel("Введите ключ для дешифрования");
        requireDecryptPasswordField.setErrorMessage("Введён неверный пароль, попробуйте еще раз");
        requireDecryptPasswordField.setPlaceholder("Введите пароль");
        requireDecryptPasswordField.setWidth("320px");
        requireDecryptPasswordField.getStyle().set("margin-right", "15px");


        deleteTempFile.setText("Удалить временные файлы");
        deleteTempFile.addClickListener(event -> {
            if (cryptoService.clearTempFiles()) {
                Dialog dialog = new Dialog();
                dialog.add(new Paragraph("Временные файлы успешно удалены"));
                dialog.open();
            }
        });


        Div encryptDiv = new Div();
        encryptDiv.setWidth("1400px");
        encryptDiv.add(encryptButton, requireEncryptPasswordField, newPasswordField, generateKeyButton);

        Div decryptDiv = new Div();
        decryptDiv.setWidth("1400px");
        decryptDiv.add(decryptButton, requireDecryptPasswordField);

        Div deleteLayout = new Div();
        deleteLayout.add(deleteTempFile);

        VerticalLayout layout = new VerticalLayout();
        layout.add(encryptDiv, decryptDiv, deleteLayout);
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        return layout;
    }
}
