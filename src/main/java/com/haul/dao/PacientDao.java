package com.haul.dao;

import com.haul.entity.Pacient;
import com.vaadin.ui.Notification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;


public class PacientDao implements DaoInterface<Pacient, Long> {

	private Session currentSession;

    private Transaction currentTransaction;

	public PacientDao() {
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

	public void persist(Pacient entity) {
		getCurrentSession().save(entity);
	}

	public void update(Pacient entity) {
		getCurrentSession().update(entity);
	}

	public Pacient findById(Long id) {
		Pacient pacient = (Pacient) getCurrentSession().get(Pacient.class, id);
		return pacient;
	}

	public void delete(Pacient entity) {
		getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public List<Pacient> findAll() {
		List<Pacient> pacients = (List<Pacient>) getCurrentSession().createQuery("from Pacient").list();
		return pacients;
	}

	public void deleteAll() {
		List<Pacient> entityList = findAll();
		for (Pacient entity : entityList) {
			delete(entity);
		}
	}
}