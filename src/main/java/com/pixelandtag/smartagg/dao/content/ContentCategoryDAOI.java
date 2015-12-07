package com.pixelandtag.smartagg.dao.content;

import java.util.List;

import com.pixelandtag.smartagg.dao.GenericDaoI;
import com.pixelandtag.smartagg.entities.ContentCategory;

public interface ContentCategoryDAOI  extends GenericDaoI<ContentCategory,Long>{

	public List<ContentCategory> getAllCategories();

}
