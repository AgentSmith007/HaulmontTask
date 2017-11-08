package com.haul;


import com.haul.entity.Doctor;
import com.haul.entity.Pacient;
import com.haul.entity.Recipe;
import com.haul.form.DoctorForm;
import com.haul.form.PacientForm;
import com.haul.form.RecipeForm;
import com.haul.service.DoctorService;
import com.haul.service.PacientService;
import com.haul.service.RecipeService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.servlet.annotation.WebServlet;
import java.util.List;


@Theme(ValoTheme.THEME_NAME)
public class MyUI extends UI {
    private DoctorService doctorService = new DoctorService();
    private RecipeService recipeService = new RecipeService();
    private PacientService pacientService = new PacientService();
    private String[] option = {"Доктора","Пациенты","Рецепты"};
    private NativeSelect chooseList = new NativeSelect("Выбор списка");
    private NativeSelect choosePrioritet = new NativeSelect("Выбор приоритета");
    private NativeSelect choosePacient = new NativeSelect("Выбор пациента");
    private Grid gridDoctors = new Grid();
    private Grid gridRecipes = new Grid();
    private Grid gridPacients = new Grid();
    private TextField filterDescription = new TextField();


    private Button buttonDoctorRecipesFilter = new Button("Показать статистику");
    private Button buttonApplyFilter = new Button("Фильтрация");
    private Button buttonAddDoctor = new Button("Добавить доктора");
    private Button buttonAddPacient = new Button("Добавить пациента");
    private Button buttonAddRecipe = new Button("Добавить рецепт");
    private Button buttonEditPacient = new Button("Изменить пациента");
    private Button buttonEditDoctor = new Button("Изменить доктора");
    private Button buttonEditRecipe = new Button("Изменить рецепт");
    private Button buttonDeleteDoctor = new Button("Удалить доктора");
    private Button buttonDeletePacient = new Button("Удалить пациента");
    private Button buttonDeleteRecipe = new Button("Удалить рецепт");

    private EditWindow editWindow = new EditWindow();
    private RecipeForm recipeForm = new RecipeForm(this);
    private DoctorForm doctorForm = new DoctorForm(this);
    private PacientForm pacientForm = new PacientForm(this);


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle("Hospital");
        //Главный layout
        VerticalLayout layout = new VerticalLayout();
        //Выбор нужного для работы списка
        chooseList.addItems((Object[]) option);

        filterDescription.setWidth(300, Unit.PIXELS);
        filterDescription.setInputPrompt("Фильтр по описанию рецепта..");
        choosePrioritet.addItems((Object[]) Prioritet.values());
        choosePacient.addItems(pacientService.findAll());

        buttonApplyFilter.addClickListener(e->{
            gridRecipes.setContainerDataSource(new BeanItemContainer<>(Recipe.class,
                    recipeService.findAll(
                    filterDescription.getValue().toString(),
                    choosePrioritet.getValue()==null ? null: (Prioritet) choosePrioritet.getValue(),
                    choosePacient.getValue()==null ? null: (((Pacient)(choosePacient.getValue())).getId()))
            ));
        });

        HorizontalLayout choosing = new HorizontalLayout();
        choosing.setSpacing(true);
        choosing.addComponents(choosePrioritet, choosePacient);

        HorizontalLayout filterAndApply = new HorizontalLayout();
        filterAndApply.addComponents(buttonApplyFilter,filterDescription);
        filterAndApply.setSpacing(true);
        VerticalLayout filter = new VerticalLayout();
        filter.addComponents(choosing,filterAndApply);
        filter.setSpacing(true);

        /*
        *
        * кнопки добавления , удаления и редактирования
        *
        *
        * */
        //************************ Добавление**************************
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);



        buttons.addComponents(buttonAddDoctor);
        buttonAddDoctor.addClickListener(e -> {
            gridDoctors.select(null);
            doctorForm.setDoctor(new Doctor());
            doctorForm.setMargin(true);
            editWindow.setCaption("Добавление нового доктора");
            editWindow.setContent(doctorForm);
            addWindow(editWindow);

        });
        buttons.addComponents(buttonAddRecipe);
        buttonAddRecipe.addClickListener(e -> {
            gridRecipes.select(null);
            recipeForm.setRecipe(new Recipe());
            recipeForm.setMargin(true);
            editWindow.setCaption("Добавление нового рецепта");
            editWindow.setContent(recipeForm);
            addWindow(editWindow);

        });
        buttons.addComponents(buttonAddPacient);
        buttonAddPacient.addClickListener(e -> {
            gridPacients.select(null);
            pacientForm.setPacient(new Pacient());
            pacientForm.setMargin(true);
            editWindow.setCaption("Добавление нового пациента");
            editWindow.setContent(pacientForm);
            addWindow(editWindow);

        });

        //************************ Редактирование************************

        buttons.addComponents(buttonEditDoctor);
        buttonEditDoctor.addClickListener(e -> {
            if(gridDoctors.getSelectedRow() == null){
                doctorForm.setVisible(false);
                Notification.show("Выберите доктора для редактирования!");
            } else {
                Doctor doctor = (Doctor) gridDoctors.getSelectedRow();
                doctorForm.setDoctor(doctor);
                doctorForm.setMargin(true);
                editWindow.setCaption("Изменение доктора");
                editWindow.setContent(doctorForm);
                addWindow(editWindow);
            }
        });

        buttons.addComponents(buttonEditRecipe);
        buttonEditRecipe.addClickListener(e -> {
            if(gridRecipes.getSelectedRow() == null){
                recipeForm.setVisible(false);
                Notification.show("Выберите рецепт для редактирования!");
            } else {
                Recipe recipe = (Recipe) gridRecipes.getSelectedRow();
                recipeForm.setRecipe(recipe);
                recipeForm.setMargin(true);
                editWindow.setCaption("Изменение рецепта");
                editWindow.setContent(recipeForm);
                addWindow(editWindow);
            }
        });

        buttons.addComponents(buttonEditPacient);
        buttonEditPacient.addClickListener(e -> {
            if(gridPacients.getSelectedRow() == null){
                pacientForm.setVisible(false);
                Notification.show("Выберите пацана для редактирования!");
            } else {
                Pacient pacient = (Pacient) gridPacients.getSelectedRow();
                pacientForm.setPacient(pacient);
                pacientForm.setMargin(true);
                editWindow.setCaption("Изменение пациента");
                editWindow.setContent(pacientForm);
                addWindow(editWindow);
            }
        });

        //************************Удаление**********************

        buttons.addComponents(buttonDeleteDoctor);
        buttonDeleteDoctor.addClickListener(e -> {
            if(gridDoctors.getSelectedRow() == null){
                Notification.show("Выберите доктора для удаления!");
            } else {
                Doctor doctor = (Doctor) gridDoctors.getSelectedRow();
                doctorService.delete(doctor.getId());

                UpdateListDoctor();
                Page.getCurrent().reload();
            }
        });

        buttons.addComponents(buttonDeleteRecipe);
        buttonDeleteRecipe.addClickListener(e -> {
            if(gridRecipes.getSelectedRow() == null){
                recipeForm.setVisible(false);
                Notification.show("Выберите рецепт для удаления!");
            } else {
                Recipe recipe = (Recipe) gridRecipes.getSelectedRow();
                recipeService.delete(recipe.getId());
                UpdateListRecipe();
            }
        });

        buttons.addComponents(buttonDeletePacient);
        buttonDeletePacient.addClickListener(e -> {
            if(gridPacients.getSelectedRow() == null){
                pacientForm.setVisible(false);
                Notification.show("Выберите пациента для удаления!");
            } else {
                Pacient pacient = (Pacient) gridPacients.getSelectedRow();
                pacientService.delete(pacient.getId());
                UpdateListPacient();
            }
        });

        /************************Статистика докторов**********************/

        buttons.addComponents(buttonDoctorRecipesFilter);
        buttonDoctorRecipesFilter.addClickListener(e->{

            if(gridDoctors.getSelectedRow() == null){
                Notification.show("Выберите доктора для вывода статистики рецептов!");
            } else {
                Doctor doctor = (Doctor) gridDoctors.getSelectedRow();
                doctorService.findById(doctor.getId());
            Notification.show("Количесвто рецептов: "+ doctorService.findOneRecipe(doctorService.findById(doctor.getId())));}

        });





        /******************************Списки********************************/
        gridDoctors.setColumns("id", "firstname", "lastname" , "patronymic" , "speciality");
        gridDoctors.getColumn("firstname").setHeaderCaption("Имя");
        gridDoctors.getColumn("lastname").setHeaderCaption("Фамилия");
        gridDoctors.getColumn("patronymic").setHeaderCaption("Отчество");
        gridDoctors.getColumn("speciality").setHeaderCaption("Специальность");
        gridDoctors.setSizeFull();

        gridRecipes.setColumns("id" , "description" , "doctor" , "pacient" , "creationDate" ,"durationDate" ,"prioritet");
        gridRecipes.getColumn("description").setHeaderCaption("Описание");
        gridRecipes.getColumn("doctor").setHeaderCaption("Доктор");
        gridRecipes.getColumn("pacient").setHeaderCaption("Пациент");
        gridRecipes.getColumn("creationDate").setHeaderCaption("Дата создания");
        gridRecipes.getColumn("durationDate").setHeaderCaption("Срок действия");
        gridRecipes.getColumn("prioritet").setHeaderCaption("Приоритет");
        gridRecipes.setSizeFull();

        gridPacients.setColumns("id","firstName","lastName","patronymic","phoneNumber");
        gridPacients.getColumn("firstName").setHeaderCaption("Имя");
        gridPacients.getColumn("lastName").setHeaderCaption("Фамилия");
        gridPacients.getColumn("patronymic").setHeaderCaption("Отчество");
        gridPacients.getColumn("phoneNumber").setHeaderCaption("Номер телефона");
        gridPacients.setSizeFull();

        VerticalLayout menu = new VerticalLayout();
        menu.setSpacing(true);
        menu.addComponents(chooseList , buttons);
        HorizontalLayout menuAndFilter = new HorizontalLayout();
        menuAndFilter.addComponents(menu , filter);
        menuAndFilter.setSpacing(true);
        layout.addComponents(menuAndFilter, gridDoctors, gridRecipes,gridPacients);

        chooseList.addValueChangeListener(e -> {

            if(chooseList.getValue() == (option[0]) ){
                showDoctorView();

            }
            else if(chooseList.getValue() == (option[1])){
                showPacientView();

            } else if(chooseList.getValue() == (option[2])){
                showRecipeView();

            } else{
                showDoctorView();
            }

        });

        showDoctorView();
        UpdateListDoctor();
        UpdateListRecipe();
        UpdateListPacient();


        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
    }

    public EditWindow getEditWindow() {
        return editWindow;
    }

    private void showDoctorView() {
        filterDescription.setVisible(false);
        buttonAddRecipe.setVisible(false);
        buttonEditRecipe.setVisible(false);
        buttonDeleteRecipe.setVisible(false);
        gridRecipes.setVisible(false);
        choosePacient.setVisible(false);
        choosePrioritet.setVisible(false);
        buttonApplyFilter.setVisible(false);
        buttonEditPacient.setVisible(false);
        buttonDeletePacient.setVisible(false);
        buttonAddPacient.setVisible(false);
        gridPacients.setVisible(false);


        buttonDoctorRecipesFilter.setVisible(true);
        buttonAddDoctor.setVisible(true);
        buttonEditDoctor.setVisible(true);
        buttonDeleteDoctor.setVisible(true);
        gridDoctors.setVisible(true);
    }

    private void showRecipeView() {
        filterDescription.setVisible(true);
        buttonAddRecipe.setVisible(true);
        buttonEditRecipe.setVisible(true);
        buttonDeleteRecipe.setVisible(true);
        gridRecipes.setVisible(true);
        choosePacient.setVisible(true);
        choosePrioritet.setVisible(true);
        buttonApplyFilter.setVisible(true);

        buttonDoctorRecipesFilter.setVisible(false);
        buttonEditPacient.setVisible(false);
        buttonDeletePacient.setVisible(false);
        buttonAddPacient.setVisible(false);
        buttonAddDoctor.setVisible(false);
        buttonEditDoctor.setVisible(false);
        buttonDeleteDoctor.setVisible(false);
        gridDoctors.setVisible(false);
        gridPacients.setVisible(false);
    }

    private void showPacientView() {
        filterDescription.setVisible(false);
        buttonAddRecipe.setVisible(false);
        buttonEditRecipe.setVisible(false);
        buttonDeleteRecipe.setVisible(false);
        gridRecipes.setVisible(false);
        choosePacient.setVisible(false);
        choosePrioritet.setVisible(false);
        buttonApplyFilter.setVisible(false);
        buttonAddDoctor.setVisible(false);
        buttonEditDoctor.setVisible(false);
        buttonDeleteDoctor.setVisible(false);
        gridDoctors.setVisible(false);
        buttonDoctorRecipesFilter.setVisible(false);

        buttonEditPacient.setVisible(true);
        buttonDeletePacient.setVisible(true);
        buttonAddPacient.setVisible(true);
        gridPacients.setVisible(true);
    }

    public void UpdateListDoctor() {
        List<Doctor> doctors = doctorService.findAll();
        gridDoctors.setContainerDataSource(new BeanItemContainer<>(Doctor.class , doctors));

    }
    public void UpdateListRecipe() {
        List<Recipe> recipes = recipeService.findAll();
        gridRecipes.setContainerDataSource(new BeanItemContainer<>(Recipe.class , recipes));
    }

    public void UpdateListPacient() {
        List<Pacient> pacients = pacientService.findAll();
        gridPacients.setContainerDataSource(new BeanItemContainer<>(Pacient.class , pacients));
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    }
}






