package com.haul.service;

import com.haul.Prioritet;
import com.haul.dao.RecipeDao;
import com.haul.entity.Recipe;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class RecipeService {

	private static RecipeDao recipeDao;
	DoctorService doctorService = new DoctorService();
	PacientService pacientService =new PacientService();

	public RecipeService() {
        recipeDao = new RecipeDao();
		recipeDao.openCurrentSession();
		if(recipeDao.findAll().isEmpty()){
			ensureTestData();
		}

	}

	/*
    *
    * Базовые CRUD операции
    *
    * */

	public void persist(Recipe entity) {
		recipeDao.openCurrentSessionwithTransaction();
		recipeDao.persist(entity);
		recipeDao.closeCurrentSessionwithTransaction();
	}

	public void update(Recipe entity) {
		recipeDao.openCurrentSessionwithTransaction();
		recipeDao.update(entity);
		recipeDao.closeCurrentSessionwithTransaction();
	}

	public Recipe findById(Long id) {
		recipeDao.openCurrentSession();
		Recipe recipe = recipeDao.findById(id);
		recipeDao.closeCurrentSession();
		return recipe;
	}

	public void delete(Long id) {
		recipeDao.openCurrentSessionwithTransaction();
		Recipe recipe = recipeDao.findById(id);
		recipeDao.delete(recipe);
		recipeDao.closeCurrentSessionwithTransaction();
	}

	public List<Recipe> findAll() {
		recipeDao.openCurrentSession();
		List<Recipe> recipes = recipeDao.findAll();
		recipeDao.closeCurrentSession();
		Collections.sort(recipes, (o1, o2) -> (int) (o2.getId() - o1.getId()));
		return recipes;
	}

	public List<Recipe> findAll(String descriptionFilter, Prioritet chosenPrioritet, Long chosenPacient) {
		recipeDao.openCurrentSession();
		List<Recipe> recipes = recipeDao.findAll(descriptionFilter,chosenPrioritet,chosenPacient);
		recipeDao.closeCurrentSession();
		//Collections.sort(recipes, (o1, o2) -> (int) (o2.getId() - o1.getId()));
		return recipes;
	}

	public void deleteAll() {
		recipeDao.openCurrentSessionwithTransaction();
		recipeDao.deleteAll();
		recipeDao.closeCurrentSessionwithTransaction();
	}
	public List<Recipe> findAllCount() {
		recipeDao.openCurrentSessionwithTransaction();
		List<Recipe> recipes = recipeDao.findAllCount();
		recipeDao.closeCurrentSessionwithTransaction();
		//Collections.sort(recipes, (o1, o2) -> (int) (o2.getId() - o1.getId()));
		return recipes;
	}

	public RecipeDao getRecipeDao() {
		return recipeDao;
	}


	public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] names = new String[] { "от головы", "от живота","от всего" };

			Random r = new Random(0);
			for (String name : names) {
				//String[] split = name.split(" ");
				Recipe recipe = new Recipe();

				recipe.setDescription(name);
				recipe.setPacient(pacientService.findAll().get(r.nextInt(pacientService.findAll().size())));
				recipe.setDoctor(doctorService.findAll().get(r.nextInt(doctorService.findAll().size())));
				recipe.setPrioritet(Prioritet.values()[r.nextInt(3)]);
                Calendar calendar = Calendar.getInstance();

                int daysOld = 0 - r.nextInt(365 * 15 );
				calendar.add(Calendar.DAY_OF_MONTH, daysOld);
				recipe.setCreationDate(calendar.getTime());

				calendar = Calendar.getInstance();
				daysOld = 0 - r.nextInt(365 * 15 );
				calendar.add(Calendar.DAY_OF_MONTH, daysOld);
				recipe.setDurationDate(calendar.getTime());

				persist(recipe);

			}
		}
	}


}