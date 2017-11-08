package com.haul.service;

import com.haul.dao.DoctorDao;
import com.haul.entity.Doctor;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DoctorService {

	private static DoctorDao doctorDao;

	public DoctorService() {
        doctorDao = new DoctorDao();
		doctorDao.openCurrentSession();
		if(doctorDao.findAll().isEmpty()){
			ensureTestData();
		}

	}

	/*
    *
    * Базовые CRUD операции
    *
    * */

	public void persist(Doctor entity) {
		doctorDao.openCurrentSessionwithTransaction();
		doctorDao.persist(entity);
		doctorDao.closeCurrentSessionwithTransaction();
	}

	public void update(Doctor entity) {
		doctorDao.openCurrentSessionwithTransaction();
		doctorDao.update(entity);
		doctorDao.closeCurrentSessionwithTransaction();
	}

	public Doctor findById(Long id) {
		doctorDao.openCurrentSession();
		Doctor doctor = doctorDao.findById(id);
		doctorDao.closeCurrentSession();
		return doctor;
	}


	public void delete(Long id) {
		doctorDao.openCurrentSessionwithTransaction();
		Doctor doctor = doctorDao.findById(id);
		doctorDao.delete(doctor);
		doctorDao.closeCurrentSessionwithTransaction();
	}

	public List<Doctor> findAll() {
		doctorDao.openCurrentSession();
		List<Doctor> doctors = doctorDao.findAll();
		doctorDao.closeCurrentSession();
		Collections.sort(doctors, (o1, o2) -> (int) (o2.getId() - o1.getId()));
		return doctors;
	}
	public List<Doctor> findAllRecipes(){
		doctorDao.openCurrentSession();
		List<Doctor> doctorsCount= doctorDao.findAllRecipes();
		doctorDao.closeCurrentSession();
		//Collections.sort(doctorsCount, (o1, o2) -> (int) (o2.getId() - o1.getId()));
		return doctorsCount;
	}

	public void deleteAll() {
		doctorDao.openCurrentSessionwithTransaction();
		doctorDao.deleteAll();
		doctorDao.closeCurrentSessionwithTransaction();
	}

	public DoctorDao doctorDao() {
		return doctorDao;
	}


	public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] names = new String[] { "Иван Павлов Петрович хирург", "Дмитрий Карякин Павлович терапевт",
					"Али Авиценна Хасанович физиолог","Чарльз Дарвин Роберт биолог 	","Илья Мечников Ильич эмбриолог","Роберт Кох  микробиолог",
			"Дмитрий Менделеев Иванович химик"};

			Random r = new Random(0);
			for (String name : names) {
				String[] split = name.split(" ");
				Doctor c = new Doctor();
				c.setFirstname(split[0]);
				c.setLastname(split[1]);
				c.setPatronymic(split[2]);
				c.setSpeciality(split[3]);

				persist(c);
			}
		}
	}

	public long findOneRecipe(Doctor byId) {
		doctorDao.openCurrentSession();
		long doctorsCount= doctorDao.findOneRecipe(byId);
		doctorDao.closeCurrentSession();
				return doctorsCount;
	}
}