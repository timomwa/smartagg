package com.pixelandtag.smartagg.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class Content extends BaseEntity<Long>{

	private static final long serialVersionUID = -3398781909133968184L;
	
	@Column(name="contenttype")
	@Enumerated(EnumType.STRING)
	private ContentType contenttype;
	
	@Column(name="creationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@PreUpdate
	@PrePersist
	public void update(){
		if(creationDate==null)
			creationDate = new Date();
	}

}
