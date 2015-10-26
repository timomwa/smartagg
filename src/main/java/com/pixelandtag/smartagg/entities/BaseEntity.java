package com.pixelandtag.smartagg.entities;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.ws.rs.FormParam;

@MappedSuperclass
public class BaseEntity<ID> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7752770423298582242L;
	
	@FormParam("id")
	@Id@GeneratedValue(strategy = GenerationType.AUTO)
	private ID id;

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}
	
}
