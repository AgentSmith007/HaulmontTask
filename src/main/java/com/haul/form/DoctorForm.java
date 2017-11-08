package com.haul.form;

import com.haul.service.DoctorService;
import com.haul.EditWindow;
import com.haul.MyUI;
import com.haul.entity.Doctor;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class DoctorForm extends FormLayout{

    private TextField firstName = new TextField("Имя");
    private TextField lastName = new TextField("Фамилия");
    private TextField patronymic = new TextField("Отчество");
    private TextField speciality = new TextField("Специальность");

    private Button save = new Button("Сохранить", FontAwesome.SAVE);
    private Button close = new Button("Отменить");

    private DoctorService doctorService = new DoctorService();
    private Doctor doctor;
    private MyUI myUI;
    private EditWindow editWindow;
    private boolean rightForm = false;


    public DoctorForm(MyUI myUI) {

        class MySecondValidator implements Validator {
            @Override
            public void validate(Object value)
                    throws InvalidValueException {

                if(value == null){
                    value = " ";
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
        speciality.addValidator(new MySecondValidator());
        speciality.setImmediate(true);

        this.myUI = myUI;
        editWindow = myUI.getEditWindow();

        save.setStyleName(ValoTheme.BUTTON_PRIMARY );
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        save.addClickListener(clickEvent -> save());
        close.addClickListener(e -> editWindow.close());

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponents(save,close);
        buttons.setSpacing(true);
        addComponents(firstName , lastName , patronymic , speciality,buttons);
    }

    public void setDoctor(Doctor doctor){
        if(doctor.getFirstname()==null){
            firstName.setInputPrompt(" ");
        }
        this.doctor = doctor;
        BeanFieldGroup.bindFieldsUnbuffered(doctor , this);
        setVisible(true);
        firstName.selectAll();
    }


    private void save(){
        if(rightForm ) {
            if(doctor.getId() == null){
                doctorService.persist(doctor);
            } else {
                doctorService.update(doctor);
            }
            myUI.UpdateListDoctor();
            setVisible(false);
            editWindow.close();
            Page.getCurrent().reload();
        } else {
            Notification.show("Проверьте корректность введенных данных!");
        }
    }
    //Проверка на валидность данных
    private boolean containNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) return true;
        }
        return false;
    }
//    private void close(){
//        editWindow.close();
//    }
}
