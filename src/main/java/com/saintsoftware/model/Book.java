package com.saintsoftware.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "Books" )
public class Book {
	private String ID;
	private String title;
    private String author;
    private String genre;
    private int rating;
    private int pages;
    private int year;
    public static enum GENRES {
		Action, Adventure, Comedy, Drama, History, Horror, Science_Fiction, Suspense, Thriller
    }
    
	public Book() {}
	
	public Book(Object[] objBook) {
		this.title = (String) objBook[2];
		this.author = (String) objBook[3];
		this.genre = (String) objBook[4];
		this.rating = (int) objBook[5];
		this.pages = (int) objBook[6];
		this.year = (int) objBook[7];
	}
	/**
	 * @return the iD
	 */
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public String getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}
	/**
	 * @return the name
	 */
	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}
	/**
	 * @param name the name to set
	 */
	public void setTitle(String name) {
		this.title = name;
	}
	/**
	 * @return the author
	 */
	@Column(name = "AUTHOR")
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the genre
	 */
	@Column(name = "GENRE")
	public String getGenre() {
		return genre;
	}
	/**
	 * @param genre the genre to set
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}
	/**
	 * @return the rating
	 */
	@Column(name = "RATING")
	public int getRating() {
		return rating;
	}
	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}
	/**
	 * @return the pagecount
	 */
	@Column(name = "PAGES")
	public int getPages() {
		return pages;
	}
	/**
	 * @param pagecount the pagecount to set
	 */
	public void setPages(int pagecount) {
		this.pages = pagecount;
	}
	/**
	 * @return the year
	 */
	@Column(name = "YEAR")
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	@Override
	public String toString() {
		String str = author + ", " + title + ", " + year + " (" + pages + " pp)" 
				+ " (" + genre + ") (" + rating + " ";
		str += (rating == 1) ? "star" : "stars";
		str += ")";
		return str;
	}

}
