package com.registration.Entities;

import com.sun.istack.NotNull;

import javax.persistence.*;

/**
 * registration table
 */
@Entity
@Table(name="registrations")
public class Registration {

	@NotNull
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="rollNumber")
	private Integer rollNumber;

	@Column(name="Authority")
	private String role;

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	@Column(name="enable")
	private String enable;

    @Column(name="username")
    private String firstname;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(name="lastname")
    private String lastname;

    @Column(name="email",unique = true, nullable = false)
    private String email;

	@NotNull
    @Column(name="mobile",unique = true, nullable = false)
    private String mobile;

    @Column(name="password",nullable = false)
    private String password;

	public Registration(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public Registration() {
		super();
	}

	public Integer getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(Integer rollNumber) {
		this.rollNumber = rollNumber;
	}

	public Registration(String firstname, String lastname, String email, String mobile, String password) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.mobile = mobile;
		this.password = password;
	}

	public Registration(String firstname, String lastname, String email, String mobile, String password, Integer rollNumber) {
        this.rollNumber=rollNumber;
		this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}


	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
