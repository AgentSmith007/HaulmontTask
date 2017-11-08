package com.haul.dao;

import com.haul.entity.Doctor;
import com.vaadin.ui.Notification;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;


public class DoctorDao implements DaoInterface<Doctor, Long> {

	private Session currentSession;

    private Transaction currentTransaction;

	public DoctorDao() {
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

		/*
		*
		* Ловим искючение при попытке удалить клиента для которого существует заказ
		*
		*
		* */

		try {
			currentTransaction.commit();
		} catch (ConstraintViolationException ex){
			Notification.show("Нельзя удалить клиента для которого существует заказ!" , Notification.Type.WARNING_MESSAGE);
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

	public void persist(Doctor entity) {
		getCurrentSession().save(entity);
	}

	public void update(Doctor entity) {
		getCurrentSession().update(entity);
	}

	public Doctor findById(Long id) {
		Doctor doctor = (Doctor) getCurrentSession().get(Doctor.class, id);
		return doctor;
	}

	public void delete(Doctor entity) {
		getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public List<Doctor> findAll() {

		List<Doctor> doctors = (List<Doctor>) getCurrentSession().createQuery("from Doctor").list();
		return doctors;
	}

	@SuppressWarnings("unchecked")
	public List<Doctor> findAllRecipes() {
		//List<Doctor> doctors = (List<Doctor>) getCurrentSession().createQuery("SELECT firstname,lastname from Doctor").list();
//		List<Doctor> doctors = (List<Doctor>) getCurrentSession().createQuery("SELECT  firstname," +
//				"patronymic,lastname,speciality, COUNT(DOCTOR_ID) as Рецепты FROM Doctor " +
//				"INNER JOIN ON ID= DOCTOR_ID " +
//				"GROUP BY firstname,lastname,patronymic,speciality").list();
		//("  select all from Doctor  inner join Recipe where   " +"  Doctor.id=Recipe.doctor  ")


		/*Вот это возвращает null */
		String sqlQuery1 = "SELECT DOCTORS.FIRSTNAME,DOCTORS.PATRONYMIC,DOCTORS.LASTNAME,DOCTORS.SPECIALITY, "
		      +" COUNT(RECIPES.DOCTOR_ID) as Рецепты FROM (DOCTORS"+
				" INNER JOIN RECIPES ON DOCTORS.ID = RECIPES.DOCTOR_ID)"+
				" GROUP BY FIRSTNAME,LASTNAME,PATRONYMIC,SPECIALITY";



		Query updateQuery=getCurrentSession().createQuery(" select firstname from Doctor ");
		Query updateQuery2=getCurrentSession().createSQLQuery(sqlQuery1);
		List<Doctor> doctors = (List<Doctor>) updateQuery2.list();

		return doctors;
	}
	public long findOneRecipe(Doctor doctorId) {
		Query countRecipes = getCurrentSession().createQuery("SELECT COUNT(*) FROM Recipe r where r.doctor = ?");
		countRecipes.setParameter(0,doctorId);
		long countRecipe = (Long)countRecipes.iterate().next();
		return countRecipe;
	}
	public void deleteAll() {
		List<Doctor> entityList = findAll();
		for (Doctor entity : entityList) {
			delete(entity);
		}
	}


}