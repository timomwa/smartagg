package com.pixelandtag.smartagg.ejb.content;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

@Stateless
@Remote
public class ContentEJBImpl implements ContentEJBI {

	@PersistenceContext(unitName = "smartpu")
	protected EntityManager em;
	
	private Logger logger = Logger.getLogger(getClass());

}
