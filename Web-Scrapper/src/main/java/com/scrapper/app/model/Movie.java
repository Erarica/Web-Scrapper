package com.scrapper.app.model;

public class Movie {

	private String title;
	private String rating;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRating() {
		return rating;
	}

	public Movie(String title, String rating) {
		super();
		this.title = title;
		this.rating = rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

}
