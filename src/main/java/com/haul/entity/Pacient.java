package com.haul.entity;

import javax.persistence.*;


@Entity
@Table(name = "pacients	")
public class Pacient {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO )
	@Column(name = "id")
	private Long id;
	
	@Column(name = "firstname")
	private String firstName="";
	
	@Column(name= "lastname")
	private String lastName="";

	@Column(name= "patronymic")
	private String patronymic="";

	@Column(name= "phonenumber")
	private String phoneNumber;




	public Pacient() {
	}

	public Pacient(Long id, String firstName, String lastName, String patronymic, String phoneNumber) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.patronymic = patronymic;
		this.phoneNumber = phoneNumber;
	}

	public Pacient(String firstName, String lastName, String patronymic, String phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.patronymic = patronymic;
		this.phoneNumber = phoneNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return this.firstName + " " + this.lastName;
	}
	
}