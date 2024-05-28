package es.uma.rysd.entities;

import java.util.Calendar;
import java.util.Date;

// Class obtained when querying for a movie

public class Movie {
	public String title;
	public Integer episode_id;
	public String opening_crawl;
	public String director;
	public String producer;
	public Date release_date;
	public String[] characters;
	
	public String toString(){
		Calendar c = Calendar.getInstance();
		c.setTime(release_date);
		return title + " ("+c.get(Calendar.YEAR)+") by "+director;
	}
}
