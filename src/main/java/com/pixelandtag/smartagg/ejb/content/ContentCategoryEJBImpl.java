package com.pixelandtag.smartagg.ejb.content;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import com.pixelandtag.smartagg.api.content.ContentException;
import com.pixelandtag.smartagg.dao.content.ContentCategoryDAOI;
import com.pixelandtag.smartagg.entities.ContentCategory;

@Stateless
@Remote
public class ContentCategoryEJBImpl implements ContentCategoryEJBI {

	@PersistenceContext(unitName = "smartpu")
	protected EntityManager em;
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Inject
	private ContentCategoryDAOI contentcategoryDAO;
	
	public List<ContentCategory> getallCategories() throws ContentException{
		return contentcategoryDAO.getAllCategories();
	}

}
