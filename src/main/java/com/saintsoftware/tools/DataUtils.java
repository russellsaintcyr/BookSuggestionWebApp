package com.saintsoftware.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saintsoftware.model.Book;
import com.saintsoftware.model.SearchCriteria;

@SuppressWarnings("unchecked")
public class DataUtils {

	private final static Logger logger = LoggerFactory.getLogger(DataUtils.class);
	private final static int yearOffset = 25;

	public static void main(String[] args) {
		try {
			testGetSuggestions();
			// Hibernate: select this_.ID as ID1_0_0_, this_.AUTHOR as AUTHOR2_0_0_, this_.GENRE as GENRE3_0_0_, this_.PAGES as PAGES4_0_0_, this_.RATING as RATING5_0_0_, this_.TITLE as TITLE6_0_0_, this_.YEAR as YEAR7_0_0_ from Books this_ where this_.GENRE in (?, ?, ?) and this_.YEAR between ? and ?
			// String sql = "select ABS(pages - 500) AS diff,ID,title,author,genre,rating, pages,year from Book where GENRE in ('Comedy', 'Suspense', 'Science Fiction') and YEAR between 1900 and 1970 ORDER BY diff asc";
			// utils.testQuery(sql);
		} finally {
			// close your thread when testing!
			HibernateUtil.shutdown();
		}
	}

	@SuppressWarnings("unused")
	private void testQuery(String qs) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Object[]> results = session.createQuery(qs).list();
		List<Book> books = convertToBookList(results);
		if (books != null) {
			for (Book book : books) {
				logger.debug(book.toString());
			}
			logger.debug(results.size() + " books returned.");
		}
	}

	private static void testGetSuggestions() {
		SearchCriteria sc = new SearchCriteria();
		sc.setPreference1(SearchCriteria.Preferences.Genre);
		//sc.setPreference2(SearchCriteria.Preferences.Pages);
		//sc.setMinPages("800");
		//sc.setMaxPages("500");
		sc.setGenre(Book.GENRES.Science_Fiction.toString());
		List<Book> books = getSuggestions(sc);
		if (books != null) {
			for (Book book : books) {
				logger.debug(book.toString());
			}
			logger.debug(books.size() + " books returned.");
		} else {
			logger.debug("0 books returned.");
		}
		// close your thread when testing!
		HibernateUtil.shutdown();
	}

	public static List<Book> searchBooks(SearchCriteria sc) {
		logger.debug(sc.toString());
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> books = null;
		try {
			Criteria criteria = session.createCriteria(Book.class);
			// add the search criteria
			addCriteriaAuthor(criteria, sc);
			addCriteriaGenre(criteria, sc);
			addCriteriaRating(criteria, sc);
			addCriteriaYears(criteria, sc);
			addCriteriaPages(criteria, sc);
			// order and return results
			books = defaultSortedList(criteria, sc);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (books != null) logger.debug("# of filtered books: " + books.size());
 		return books;
	}
	
	public static List<Book> getAllBooks() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Book> books = null;
		try {
			books = session.createQuery("from Book order by Rating desc, Author asc, Title asc").list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		if (books != null) logger.debug("# of total books: " + books.size());
 		return books;
	}

	public static List<Book> getSuggestions(SearchCriteria sc) {
		// return if no first preference
		if (sc.getPreference1() == null) {
			debug(sc, "getSuggestions called without preference1");
			return null;
		}
		// create Criteria
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(Book.class);
		// Author = highest priority
		if (sc.getPreference1().equals(SearchCriteria.Preferences.Author)) {
			// ensure we have author search data
			if (sc.getAuthor() == null || sc.getAuthor().trim().equals("")) {
				debug(sc, "preference1=author but author is empty");
			} else {
				List<String> authorGenres = getAuthorGenres(sc, session);
				// exit if no results
				if (authorGenres.size() == 0) return null;
				List<Integer> authorYears = getAuthorYears(sc, session);
				int authorMinYear = Collections.min(authorYears) - yearOffset; 
				int authorMaxYear = Collections.max(authorYears) + yearOffset;
				debug(sc, "Author years with " + yearOffset + "-year offset: " + "min=" + authorMinYear + ", max=" + authorMaxYear);
				// start filtering the data
				criteria.add(Restrictions.in("genre", authorGenres));
				criteria.add(Restrictions.between("year", authorMinYear, authorMaxYear));
				// handle single criteria search
				if (sc.getPreference2() == null) {
					return defaultSortedList(criteria, sc);
				} else {
					if (sc.getPreference2().equals(SearchCriteria.Preferences.Genre)) {
						debug(sc, "Author+Genre to be implemented.");
					} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Pages)) {
						// build custom sql due to ABS() query in select
						List<Book> books = getCustomQueryList("pages", Integer.parseInt(sc.getMaxPages()), 
								authorGenres, authorMinYear, authorMaxYear, session);
						debug(sc,"Ordered by pages closest to " + sc.getMaxPages());
						return books;
					} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Years)) {
						// ensure we have at least 1 year
						if (sc.getMinYear() == null && sc.getMaxYear() == null) { 
							debug(sc, "getMinYear and getMaxYear are null");
						} else {
							// if we have min and max years, get difference between year range
							int yearMiddle = 0;
							if (sc.getMinYear() != null && !sc.getMinYear().trim().equals("") 
									&& sc.getMaxYear() != null && !sc.getMaxYear().trim().equals("")) {
								yearMiddle = Integer.parseInt(sc.getMinYear()) + Integer.parseInt(sc.getMaxYear())/2;
							// otherwise just use the single min or max year
							} else if (sc.getMinYear() != null && !sc.getMinYear().trim().equals("")) {
								yearMiddle = Integer.parseInt(sc.getMinYear());
							} else if (sc.getMaxYear() != null && !sc.getMaxYear().trim().equals("")) {
								yearMiddle = Integer.parseInt(sc.getMaxYear());
							}
							// build custom sql due to ABS() query in select
							List<Book> books = getCustomQueryList("year", yearMiddle, authorGenres, authorMinYear, 
									authorMaxYear, session);
							debug(sc,"Ordered by years closest to " + yearMiddle);
							return books;
						}
					}
				}
			}
		// Genre = highest priority
		} else if (sc.getPreference1().equals(SearchCriteria.Preferences.Genre)) {
			// ensure we have search data
			if (sc.getGenre() == null || sc.getGenre().trim().equals("")) {
				debug(sc, "preference1=genre but genre is empty");
			} else {
				// handle single criteria search
				if (sc.getPreference2() == null) {
					addCriteriaGenre(criteria, sc);
					return defaultSortedList(criteria, sc);
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Author)) {
					debug(sc, "Genre+Author to be implemented.");
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Pages)) {
					return getPagesAndGenre(criteria, sc);
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Years)) {
					return getYearAndGenre(criteria, sc);
				}
			}
		// Pages = highest priority
		} else if (sc.getPreference1().equals(SearchCriteria.Preferences.Pages)) {
			// ensure we have search data
			if (sc.getMinPages() == null && sc.getMaxPages() == null) {
				debug(sc, "preference1=Pages but minPage and maxPage are empty");
			} else {
				if (sc.getPreference2() == null) {
					addCriteriaPages(criteria, sc);
					return defaultSortedList(criteria, sc);
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Author)) {
					debug(sc, "Pages+Author to be implemented.");
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Years)) {
					return getYearAndPages(criteria, sc);
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Genre)) {
					return getPagesAndGenre(criteria, sc);
				}
			}
		// Years = highest priority
		} else if (sc.getPreference1().equals(SearchCriteria.Preferences.Years)) {
			// ensure we have search data
			if (sc.getMinYear() == null && sc.getMaxYear() == null) {
				debug(sc, "preference1=year but minYear and maxYear are empty");
			} else {
				// handle single criteria search
				if (sc.getPreference2() == null) {
					addCriteriaYears(criteria, sc);
					return defaultSortedList(criteria, sc);
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Author)) {
					debug(sc, "Year+Author to be implemented.");
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Pages)) {
					return getYearAndPages(criteria, sc);
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Genre)) {
					return getYearAndGenre(criteria, sc);
				}
			}
		}
		// still here? return null
		return null;
	}

	private static void addCriteriaGenre(Criteria criteria, SearchCriteria sc) {
		if (sc.getGenre() != null  && !sc.getGenre().trim().equals("")) {
			// replace any underscores in the enum name
			String genre = sc.getGenre().replaceAll("_", " ");
			criteria.add(Restrictions.eq("genre", genre));
		}
	}

	private static void addCriteriaAuthor(Criteria criteria, SearchCriteria sc) {
		if (sc.getAuthor() != null && !sc.getAuthor().trim().equals("")) 
			criteria.add(Restrictions.like("author", "%" + sc.getAuthor() + "%").ignoreCase());
	}

	private static void addCriteriaRating(Criteria criteria, SearchCriteria sc) {
		if (sc.getMinRating() != null && !sc.getMinRating().trim().equals("")) 
			criteria.add( Restrictions.ge("rating", Integer.parseInt(sc.getMinRating())));
		if (sc.getMaxRating() != null && !sc.getMaxRating().trim().equals("")) 
			criteria.add( Restrictions.le("rating", Integer.parseInt(sc.getMaxRating())));
	}

	private static void addCriteriaPages(Criteria criteria, SearchCriteria sc) {
		if (sc.getMinPages() != null && !sc.getMinPages().trim().equals(""))
			criteria.add(Restrictions.ge("pages", Integer.parseInt(sc.getMinPages())));
		if (sc.getMaxPages() != null && !sc.getMaxPages().trim().equals(""))
			criteria.add(Restrictions.le("pages", Integer.parseInt(sc.getMaxPages())));
	}
	
	private static void addCriteriaYears(Criteria criteria, SearchCriteria sc) {
		if (sc.getMinYear() != null && !sc.getMinYear().trim().equals(""))
			criteria.add(Restrictions.ge("year", Integer.parseInt(sc.getMinYear())));
		if (sc.getMaxYear() != null && !sc.getMaxYear().trim().equals(""))
			criteria.add(Restrictions.le("year", Integer.parseInt(sc.getMaxYear())));
	}

	private static List<Book> getPagesAndGenre(Criteria criteria, SearchCriteria sc) {
		if (sc.getGenre() == null) { 
			debug(sc, "getGenre is null");
		} else if (sc.getMinPages() == null && sc.getMaxPages() == null) { 
			debug(sc, "getMinPages and getMaxPages are null");
		} else {
			addCriteriaGenre(criteria, sc);
			addCriteriaPages(criteria, sc);
			return defaultSortedList(criteria, sc);
		}
		return null;
	}

	private static List<Book> getYearAndPages(Criteria criteria, SearchCriteria sc) {
		if (sc.getMinYear() == null && sc.getMaxYear() == null) { 
			debug(sc, "getMinYear and getMaxYear are null");
		} else if (sc.getMinPages() == null && sc.getMaxPages() == null) { 
			debug(sc, "getMinPages and getMaxPages are null");
		} else {
			addCriteriaPages(criteria, sc);
			addCriteriaYears(criteria, sc);
			return defaultSortedList(criteria, sc);
		}
		return null;
	}

	private static List<Book> getYearAndGenre(Criteria criteria, SearchCriteria sc) {
		if (sc.getMinYear() == null && sc.getMaxYear() == null) { 
			debug(sc, "getMinYear and getMaxYear are null");
		} else if (sc.getGenre() == null) { 
			debug(sc, "getGenre is null");
		} else {
			addCriteriaGenre(criteria, sc);
			addCriteriaYears(criteria, sc);
			return defaultSortedList(criteria, sc);
		}
		return null;
	}

	/*
	 * For use when called by functions where author is most important preference
	 * */
	private static List<Book> getCustomQueryList(String diffField, int diffValue, List<String> authorGenres, 
			int authorMinYear, int authorMaxYear, Session session) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ABS(" + diffField + " - " + diffValue + ") AS diff,");
		sql.append("ID,title,author,genre,rating, pages,year from Book");
		sql.append(" where GENRE in (" + sqlSafeList(authorGenres) + ")");
		sql.append(" and YEAR between " + authorMinYear + " and " + authorMaxYear);
		sql.append(" ORDER BY diff asc");
		List<Object[]> objList = session.createQuery(sql.toString()).list();
		List<Book> books = convertToBookList(objList);
		return books;
	}
	private static String sqlSafeList(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (String genre : list) {
			sb.append("'" + genre + "',");
		}
		return sb.substring(0, sb.length()-1);
	}

	private static List<Book> convertToBookList(List<Object[]> objList) {
		List<Book> books = new ArrayList<Book>();
		if (objList != null) {
			for (Object[] objBook : objList) {
				books.add(new Book(objBook));
			}
		}
		return books;
	}

	private static List<Integer> getAuthorYears(SearchCriteria sc, Session session) {
		String qs = "select distinct year from Book where lower(author) like '%" + sc.getAuthor().toLowerCase() + "%'";
		List<Integer> authorYears = session.createQuery(qs).list();
		return authorYears;
	}

	private static List<String> getAuthorGenres(SearchCriteria sc, Session session) {
		String qs = "select distinct genre from Book where lower(author) like '%" + sc.getAuthor().toLowerCase() + "%'";
		List<String> authorGenres = session.createQuery(qs).list();
		debug(sc, "Author genres: " + authorGenres.toString());
		return authorGenres;
	}

	private static void debug(SearchCriteria sc, String debugInfo) {
		logger.debug(debugInfo);
		sc.appendDebugInfo(debugInfo);
	}

	private static List<Book> defaultSortedList(Criteria criteria, SearchCriteria sc) {
		debug(sc, "Default sorting by rating and author's first name.");
		criteria.addOrder(Property.forName("rating").desc()); // rating 5-1
		criteria.addOrder(Property.forName("author").asc()); // author A-Z
		return criteria.list();
	}

}
