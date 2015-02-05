package com.saintsoftware.model;

import java.util.Arrays;

public class SearchCriteria {
    private String author;
    private String genre;
    private String minRating;
    private String maxRating;
    private String minPages;
    private String maxPages;
    private String minYear;
    private String maxYear;
    private Preferences preference1;
    private Preferences preference2;
    private String[] debugInfo = new String[0];
    public static enum Preferences {Author, Genre, Years, Pages};
	/**
	 * @return the author
	 */
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
	 * @return the minRating
	 */
	public String getMinRating() {
		return minRating;
	}
	/**
	 * @param minRating the minRating to set
	 */
	public void setMinRating(String minRating) {
		this.minRating = minRating;
	}
	/**
	 * @return the maxRating
	 */
	public String getMaxRating() {
		return maxRating;
	}
	/**
	 * @param maxRating the maxRating to set
	 */
	public void setMaxRating(String maxRating) {
		this.maxRating = maxRating;
	}
	/**
	 * @return the minYear
	 */
	public String getMinYear() {
		return minYear;
	}
	/**
	 * @param minYear the minYear to set
	 */
	public void setMinYear(String minYear) {
		this.minYear = minYear;
	}
	/**
	 * @return the maxYear
	 */
	public String getMaxYear() {
		return maxYear;
	}
	/**
	 * @param maxYear the maxYear to set
	 */
	public void setMaxYear(String maxYear) {
		this.maxYear = maxYear;
	}
	/**
	 * @return the pagecount
	 */
	public String getMinPages() {
		return minPages;
	}
	/**
	 * @param pagecount the pagecount to set
	 */
	public void setMinPages(String pagecount) {
		this.minPages = pagecount;
	}
	/**
	 * @return the maxPages
	 */
	public String getMaxPages() {
		return maxPages;
	}
	/**
	 * @param maxPages the maxPages to set
	 */
	public void setMaxPages(String maxPages) {
		this.maxPages = maxPages;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Preferences:");
		sb.append(" {author=" + author + "}");
		sb.append(" {genre=" + genre + "}");
		sb.append(" {rating=" + minRating + "-" + maxRating + "}");
		sb.append(" {years=" + minYear + "-" + maxYear + "}");
		sb.append(" {pages=" + minPages + "-" + maxPages + "}");
		sb.append(" {pref1=" + preference1 + "}");
		sb.append(" {pref2=" + preference2 + "}");
		return sb.toString();
	}
	/**
	 * @return the preference1
	 */
	public Preferences getPreference1() {
		return preference1;
	}
	/**
	 * @param preference1 the preference1 to set
	 */
	public void setPreference1(Preferences preference1) {
		this.preference1 = preference1;
	}
	/**
	 * @return the preference2
	 */
	public Preferences getPreference2() {
		return preference2;
	}
	/**
	 * @param preference2 the preference2 to set
	 */
	public void setPreference2(Preferences preference2) {
		this.preference2 = preference2;
	}
	/**
	 * @return the debugInfo
	 */
	public String[] getDebugInfo() {
		return debugInfo;
	}
	/**
	 * @param debugInfo the debugInfo to set
	 */
	public void appendDebugInfo(String strDebugInfo) {
		// increment old array by 1
		String[] newArr = Arrays.copyOf(debugInfo, debugInfo.length +1);
		// add element to new array
		newArr[debugInfo.length] = strDebugInfo;
		// overwrite old array
		this.debugInfo = newArr;
	}
	public Preferences[] getPreferences() {
		return SearchCriteria.Preferences.values();
	}
}
