package com.haul.form;

import com.haul.EditWindow;
import com.haul.MyUI;
import com.haul.service.PacientService;
import com.haul.entity.Pacient;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class PacientForm extends FormLayout{

    private TextField firstName = new TextField("Имя");
    private TextField lastName = new TextField("Фамилия");
    private TextField patronymic = new TextField("Отчество");
    private TextField phoneNumber = new TextField("Телефон");

    private Button save = new Button("Сохранить", FontAwesome.SAVE);
    private Button close = new Button("Отменить");

    private PacientService pacientService = new PacientService();
    private Pacient pacient;
    private MyUI myUI;
    private EditWindow editWindow;
    private boolean rightForm = false;
    private boolean rightFormNumber = false;

    public PacientForm(MyUI myUI) {

        //валидатор для проверки на валидность поля номера телефона
        class MyValidator implements Validator {
            @Override
            public void validate(Object value)
                    throws InvalidValueException {
                //Регулярное выражение для номера телефона
                if (!( value.toString().matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")) ||
                        (((String)value).length() < 11) ||
                        (((String)value).length() > 14)){
                    Notification.show("Введите корректный номер телефона!");
                    rightFormNumber = false;
                } else
                    rightFormNumber = true;
            }
        }

        /*
        *
        *  Валидатор для поля номера телефона
        *
        * */

        phoneNumber.addValidator(new MyValidator());
        phoneNumber.setImmediate(true);

        // Валидатор для проверки на корректность строковых полей формы

        class MySecondValidator implements Validator {
            @Override
            public void validate(Object value)
                    throws InvalidValueException {

                if(value == null){
                    value = "";
                    Notification.show("Поле не должно быть пустым !");
                    rightForm = false;
                }
                else if(value.toString().isEmpty()){
                    Notification.show("Поле не должно быть пустым !");
                    rightForm = false;
                }else if ( containNumber(value.toString()) ){
                    Notification.show("Поле не должно содержать цифр !");
                    rightForm = false;
                } else {
                    rightForm = true;
                }

            }
        }
        firstName.addValidator(new MySecondValidator());
        lastName.addValidator(new MySecondValidator());
        patronymic.addValidator(new MySecondValidator());

        this.myUI = myUI;
        editWindow = myUI.getEditWindow();

        save.setStyleName(ValoTheme.BUTTON_PRIMARY );
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        save.addClickListener(e -> save());
        close.addClickListener(e -> close());

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponents(save,close);
        buttons.setSpacing(true);
        addComponents(firstName , lastName , patronymic , phoneNumber ,buttons);
    }

    public void setPacient(Pacient pacient){
        if(pacient.getPhoneNumber() == null) {
            pacient.setPhoneNumber("+7");
            rightFormNumber = false;
        }
        this.pacient = pacient;
        BeanFieldGroup.bindFieldsUnbuffered(pacient , this);
        setVisible(true);
        firstName.selectAll();
    }


    private void save(){
        if(rightForm && rightFormNumber) {
            if(pacient.getId() == null){
                pacientService.persist(pacient);
            } else {
                pacientService.update(pacient);
            }
            myUI.UpdateListPacient();
            setVisible(false);
            editWindow.close();
            rightForm=false;
        } else {
            Notification.show("Проверьте корректность введенных данных!");
        }
    }
    //Проверка на наличие цифр в строке данных
    private boolean containNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
           try{ if (Character.isDigit(str.charAt(i))) return true;}
           catch (Exception e){return  true;}
        }
        return false;
    }
    private void close(){
        editWindow.close();
    }
}
