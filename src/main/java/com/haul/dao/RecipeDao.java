package com.haul.dao;

import com.haul.Prioritet;
import com.haul.entity.Recipe;
import com.vaadin.ui.Notification;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;


public class RecipeDao implements DaoInterface<Recipe, Long> {

	private Session currentSession;

    private Transaction currentTransaction;

	public RecipeDao() {
	}

	public Session openCurrentSession() {
		currentSession = getSessionFactory().openSession();
		return currentSession;
	}

	public Session openCurrentSessionwithTransaction() {
		currentSession = getSessionFactory().openSession();
		currentTransaction = currentSession.beginTransaction();
		return currentSession;
	}
	
	public void closeCurrentSession() {
		currentSession.close();
	}
	
	public void closeCurrentSessionwithTransaction() {

		try {
			currentTransaction.commit();
		} catch (ConstraintViolationException ex){
			Notification.show("Нельзя удалить  доктора или пациента у которого есть рецепт!" , Notification.Type.WARNING_MESSAGE);
			currentTransaction.rollback();

		}

		currentSession.close();
	}
	
	public static SessionFactory getSessionFactory() {
		Configuration configuration = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties());
		SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
		return sessionFactory;
	}

	public Session getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}

	public Transaction getCurrentTransaction() {
		return currentTransaction;
	}

	public void setCurrentTransaction(Transaction currentTransaction) {
		this.currentTransaction = currentTransaction;
	}

	public void persist(Recipe entity) {
		getCurrentSession().save(entity);
	}

	public void update(Recipe entity) {
		getCurrentSession().update(entity);
	}

	public Recipe findById(Long id) {
		Recipe recipe = (Recipe) getCurrentSession().get(Recipe.class, id);
		return recipe;
	}

	public void delete(Recipe entity) {
		getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public List<Recipe> findAll() {
		List<Recipe> recipes = (List<Recipe>) getCurrentSession().createQuery("from Recipe").list();
		return recipes;
	}

	public void deleteAll() {
		List<Recipe> entityList = findAll();
		for (Recipe entity : entityList) {
			delete(entity);
		}
	}

	@SuppressWarnings("unchecked")
    public List<Recipe> findAll(String descriptionFilter, Prioritet chosenPrioritet, Long chosenPacient) {

		Query queryUpdate = getCurrentSession().createQuery("from Recipe" );

		if((descriptionFilter == null || descriptionFilter.isEmpty()) && (chosenPrioritet==null) && chosenPacient == null ){
			queryUpdate = getCurrentSession().createQuery("from Recipe ");
		} else if ((descriptionFilter == null || descriptionFilter.isEmpty()) && (chosenPrioritet==null) && chosenPacient != null ){
			queryUpdate = getCurrentSession().createQuery("from Recipe where  pacient.id="+chosenPacient);
		} else if((descriptionFilter == null || descriptionFilter.isEmpty()) && (chosenPrioritet!=null) && chosenPacient == null){
			queryUpdate = getCurrentSession().createQuery("from Recipe where prioritet =:s  ");
			queryUpdate.setParameter("s",chosenPrioritet);
		} else if((descriptionFilter == null || descriptionFilter.isEmpty()) && (chosenPrioritet!=null) && chosenPacient != null){
			queryUpdate = getCurrentSession().createQuery("from Recipe where prioritet =:s and pacient.id=:c   ");
			queryUpdate.setParameter("s",chosenPrioritet);
			queryUpdate.setParameter("c", chosenPacient );
		}else if((descriptionFilter != null && !descriptionFilter.isEmpty()) && (chosenPrioritet==null) && chosenPacient == null){
			queryUpdate = getCurrentSession().createQuery("from Recipe where description like :d");
			queryUpdate.setParameter("d","%" + descriptionFilter + "%");
		}else if((descriptionFilter != null && !(descriptionFilter.isEmpty())) && (chosenPrioritet==null) && chosenPacient != null){
			queryUpdate = getCurrentSession().createQuery("from Recipe where description like :d and pacient.id=:c");
			queryUpdate.setParameter("d","%" + descriptionFilter + "%");
			queryUpdate.setParameter("c", chosenPacient );
		}else if((descriptionFilter != null && !(descriptionFilter.isEmpty())) && (chosenPrioritet!=null) && chosenPacient == null){
			queryUpdate = getCurrentSession().createQuery("from Recipe where description like :d and prioritet =:s");
			queryUpdate.setParameter("d","%" + descriptionFilter + "%");
			queryUpdate.setParameter("s", chosenPrioritet );
		}else if((descriptionFilter != null && !(descriptionFilter.isEmpty())) && (chosenPrioritet!=null) && chosenPacient != null){
			queryUpdate = getCurrentSession().createQuery("from Recipe where description like :d and prioritet =:s and pacient.id =:c ");
			queryUpdate.setParameter("d","%" + descriptionFilter + "%");
			queryUpdate.setParameter("s", chosenPrioritet );
			queryUpdate.setParameter("c", chosenPacient );
		}

		List<Recipe> recipes = (List<Recipe>) queryUpdate.list();
		//Collections.sort(recipes, (o1, o2) -> (int) (o2.getId() - o1.getId()));
		return recipes;
    }

	public List<Recipe> findAllCount() {
		Query countQuery = getCurrentSession().createQuery(" select  id, description from Recipe ");
		List<Recipe> recipes = (List<Recipe>) countQuery.list();
		return recipes;
	}


}