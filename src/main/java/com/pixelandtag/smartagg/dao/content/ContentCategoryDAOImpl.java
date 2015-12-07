package com.pixelandtag.smartagg.dao.content;

import java.util.ArrayList;
import java.util.List;

import com.pixelandtag.smartagg.dao.GenericDaoImpl;
import com.pixelandtag.smartagg.entities.ContentCategory;

public class ContentCategoryDAOImpl extends GenericDaoImpl<ContentCategory ,Long> implements ContentCategoryDAOI  {

	public List<ContentCategory> getAllCategories(){
		List<ContentCategory> categories = getStaticCategories();//new ArrayList<ContentCategory>();
		
		return categories;
	}

	private List<ContentCategory> getStaticCategories() {
		List<ContentCategory> categories = new ArrayList<ContentCategory>();
		ContentCategory news = new ContentCategory();
		news.setId(100L);
		news.setName("News");
		
		ContentCategory sports = new ContentCategory();
		sports.setId(200L);
		sports.setName("Sports");
		
		ContentCategory jokes = new ContentCategory();
		jokes.setId(300L);
		jokes.setName("Jokes");
		
		ContentCategory horoscope = new ContentCategory();
		horoscope.setId(400L);
		horoscope.setName("Horoscope");
		
		ContentCategory wallpapers = new ContentCategory();
		wallpapers.setId(500L);
		wallpapers.setName("Wallpapers");
		
		ContentCategory ringtones = new ContentCategory();
		ringtones.setId(600L);
		ringtones.setName("Ringtones");
		
		categories.add(ringtones);
		categories.add(wallpapers);
		categories.add(horoscope);
		categories.add(jokes);
		categories.add(sports);
		categories.add(news);
		
		return categories;
	}

}
