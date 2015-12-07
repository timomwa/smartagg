package com.pixelandtag.smartagg.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="content_category")
public class ContentCategory extends BaseEntity<Long>  {

	private static final long serialVersionUID = -2828457758452357020L;

	@Column(name="name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
