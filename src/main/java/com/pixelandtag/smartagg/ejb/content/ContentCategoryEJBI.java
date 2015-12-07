package com.pixelandtag.smartagg.ejb.content;

import java.util.List;

import com.pixelandtag.smartagg.api.content.ContentException;
import com.pixelandtag.smartagg.entities.ContentCategory;

public interface ContentCategoryEJBI {
	
	public List<ContentCategory> getallCategories() throws ContentException;
}
