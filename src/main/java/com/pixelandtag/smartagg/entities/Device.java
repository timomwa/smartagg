package com.pixelandtag.smartagg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="device")
public class Device extends BaseEntity<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8145237161096379203L;
	
	@Column(name="registrationID", unique=true, length=300)
	private String registrationID;
	
	@Column(name="registrationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationDate;
	
	@PreUpdate
	@PrePersist
	public void init(){
		
		if(registrationDate==null)
			registrationDate = new Date();
		
	}

	public String getRegistrationID() {
		return registrationID;
	}

	public void setRegistrationID(String registrationID) {
		this.registrationID = registrationID;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	

}
