package com.haul.form;

import com.haul.*;
import com.haul.entity.Recipe;
import com.haul.service.DoctorService;
import com.haul.service.PacientService;
import com.haul.service.RecipeService;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class RecipeForm extends FormLayout{

    private TextField description = new TextField("Описание");
    private NativeSelect doctor = new NativeSelect("Доктор");
    private NativeSelect pacient = new NativeSelect("Пациент");
    private PopupDateField creationDate = new PopupDateField("Дата создания");
    private PopupDateField durationDate = new PopupDateField("Срок действия");

    private NativeSelect prioritet = new NativeSelect("Приоритет");

    private Button save = new Button("Сохранить", FontAwesome.SAVE);
    private Button close = new Button("Отменить");

    private RecipeService recipeService = new RecipeService();
    private PacientService pacientService = new PacientService();
    private DoctorService doctorService = new DoctorService();
    private Recipe recipe;
    private MyUI myUI;
    private EditWindow editWindow;
    private boolean rightForm = false;

    public RecipeForm(MyUI myUI) {


        class MySecondValidator implements Validator {
            @Override
            public void validate(Object value)
                    throws InvalidValueException {

                if(value == null){
                   // value = "";
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
        description.addValidator(new MySecondValidator());

        this.myUI = myUI;
        editWindow = myUI.getEditWindow();
        doctor.addItems(doctorService.findAll());
        pacient.addItems(pacientService.findAll());
        prioritet.addItems((Object[]) Prioritet.values());

        save.setStyleName(ValoTheme.BUTTON_PRIMARY );
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        save.addClickListener(e -> save());
        close.addClickListener(e -> close());

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addComponents(save,close);
        buttons.setSpacing(true);
        addComponents(description, doctor, creationDate, durationDate,pacient, prioritet,buttons);
    }

    public void setRecipe(Recipe recipe){
        this.recipe = recipe;
//
        BeanFieldGroup.bindFieldsUnbuffered(recipe , this);
        setVisible(true);
        description.selectAll();
        creationDate.isTextFieldEnabled();


    }

    private void save(){
        if(rightForm){
            if(recipe.getId() == null){
                recipeService.persist(recipe);
            }
            recipeService.update(recipe);
            myUI.UpdateListRecipe();
            setVisible(false);
            editWindow.close();
            rightForm=false;
        }else{
            Notification.show("Проверьте корректность введенных данных!");
        }
    }

    private boolean containNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            try{if (Character.isDigit(str.charAt(i)))
                return true;}
                catch (Exception ex){ return  true;}
        }
        return false;
    }

    private void close(){
        editWindow.close();
    }
}
