package com.haul.service;

import com.haul.dao.PacientDao;
import com.haul.entity.Pacient;

import java.util.Collections;
import java.util.List;
import java.util.Random;


public class PacientService {

	private static PacientDao pacientDao;

	public PacientService() {
        pacientDao = new PacientDao();
		pacientDao.openCurrentSession();
		if(pacientDao.findAll().isEmpty()){
			ensureTestData();
		}

	}

	/*
    *
    * Базовые CRUD операции
    *
    * */

	public void persist(Pacient entity) {
		pacientDao.openCurrentSessionwithTransaction();
		pacientDao.persist(entity);
		pacientDao.closeCurrentSessionwithTransaction();
	}

	public void update(Pacient entity) {
		pacientDao.openCurrentSessionwithTransaction();
		pacientDao.update(entity);
		pacientDao.closeCurrentSessionwithTransaction();
	}

	public Pacient findById(Long id) {
		pacientDao.openCurrentSession();
		Pacient pacient = pacientDao.findById(id);
		pacientDao.closeCurrentSession();
		return pacient;
	}

	public void delete(Long id) {
		pacientDao.openCurrentSessionwithTransaction();
		Pacient pacient = pacientDao.findById(id);
		pacientDao.delete(pacient);
		pacientDao.closeCurrentSessionwithTransaction();
	}

	public List<Pacient> findAll() {
		pacientDao.openCurrentSession();
		List<Pacient> pacients = pacientDao.findAll();
		pacientDao.closeCurrentSession();
		Collections.sort(pacients, (o1, o2) -> (int) (o2.getId() - o1.getId()));
		return pacients;
	}

	public void deleteAll() {
		pacientDao.openCurrentSessionwithTransaction();
		pacientDao.deleteAll();
		pacientDao.closeCurrentSessionwithTransaction();
	}

	public PacientDao pacientDao() {
		return pacientDao;
	}


	public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] names = new String[] { "Анастасия Перминова Сергеевна  ", "Антон Гордеев Петрович " ,"Никола Тесла  ",
					"Альберт Эйнштейн ","Томас Эдисон"};

			System.out.println("*** Persist - start ***");

			Random r = new Random(0);
			for (String name : names) {
				String[] split = name.split(" ");
				Pacient c = new Pacient();
				c.setFirstName(split[0]);
				c.setLastName(split[1]);
				c.setPatronymic(split[2]);
				int a = 100 + r.nextInt(899);
				int b = 1000000 + r.nextInt(8999999);
				c.setPhoneNumber("+7(" + a +")" + b);

				persist(c);
			}


		}
	}
}