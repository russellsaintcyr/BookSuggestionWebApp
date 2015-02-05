package com.saintsoftware.tools;

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
		SearchCriteria sc = new SearchCriteria();
		sc.setMinPages("200");
		sc.setMaxPages("400");
		sc.setPreference1(SearchCriteria.Preferences.Pages);
		// sc.setPreference2(SearchCriteria.Preferences.Years);
		List<Book> books = DataUtils.getSuggestions(sc);
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
			// filter by author and genre
			if (sc.getAuthor() != null && !sc.getAuthor().trim().equals("")) 
				criteria.add(Restrictions.like("author", "%" + sc.getAuthor() + "%").ignoreCase());
			if (sc.getGenre() != null  && !sc.getGenre().trim().equals("")) 
				criteria.add(Restrictions.eq("genre", sc.getGenre()));
			// filter by rating
			if (sc.getMinRating() != null && !sc.getMinRating().trim().equals("")) 
				criteria.add( Restrictions.ge("rating", Integer.parseInt(sc.getMinRating())));
			if (sc.getMaxRating() != null && !sc.getMaxRating().trim().equals("")) 
				criteria.add( Restrictions.le("rating", Integer.parseInt(sc.getMaxRating())));
			// filter by years
			if (sc.getMinYear() != null && !sc.getMinYear().trim().equals("")) 
				criteria.add( Restrictions.ge("year", Integer.parseInt(sc.getMinYear())));
			if (sc.getMaxYear() != null && !sc.getMaxYear().trim().equals("")) 
				criteria.add( Restrictions.le("year", Integer.parseInt(sc.getMaxYear())));
			// filter by page count
			if (sc.getMinPages() != null  && !sc.getMinPages().trim().equals("")) 
				criteria.add( Restrictions.ge("pages", Integer.parseInt(sc.getMinPages())));
			if (sc.getMaxPages() != null  && !sc.getMaxPages().trim().equals("")) 
				criteria.add( Restrictions.le("pages", Integer.parseInt(sc.getMaxPages())));
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
				int minYear = Collections.min(authorYears) - yearOffset; 
				int maxYear = Collections.max(authorYears) + yearOffset;
				debug(sc, "Author years with " + yearOffset + "-year offset: " + "min=" + minYear + ", max=" + maxYear);
				// start filtering the data
				criteria.add(Restrictions.in("genre", authorGenres));
				criteria.add(Restrictions.between("year", minYear, maxYear));
				// handle single criteria search
				if (sc.getPreference2() == null) {
					return defaultSortedList(criteria, sc);
				} else {
					// pref2 = genre
					if (sc.getPreference2().equals(SearchCriteria.Preferences.Genre)) {
						// TODO: group by genre, returning preferred genre on top, other genres after
						criteria.addOrder(Property.forName("genre").asc());
						return criteria.list();
					} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Genre)) {
						debug(sc, "Author+Genre to be implemented.");
					} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Pages)) {
						debug(sc, "Author+Pages to be implemented.");
					} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Years)) {
						debug(sc, "Author+Years to be implemented.");
					}
				}
			}
		// Genre = highest priority
		} else if (sc.getPreference1().equals(SearchCriteria.Preferences.Genre)) {
			// ensure we have search data
			if (sc.getGenre() == null || sc.getGenre().trim().equals("")) {
				debug(sc, "preference1=genre but genre is empty");
			} else {
				criteria.add(Restrictions.eq("genre", sc.getGenre()));
				// handle single criteria search
				if (sc.getPreference2() == null) {
					return defaultSortedList(criteria, sc);
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Author)) {
					debug(sc, "Genre+Author to be implemented.");
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Pages)) {
					debug(sc, "Genre+Pages to be implemented.");
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Years)) {
					debug(sc, "Genre+Years to be implemented.");
				}
			}
		// Pages = highest priority
		} else if (sc.getPreference1().equals(SearchCriteria.Preferences.Pages)) {
			// ensure we have search data
			if (sc.getMinPages() == null || sc.getMinPages().trim().equals("")) {
				debug(sc, "preference1=Pages but minPage is empty");
			} else if (sc.getMaxPages() == null || sc.getMaxPages().trim().equals("")) {
				debug(sc, "preference1=Pages but maxPage is empty");
			} else {
				criteria.add(Restrictions.between("pages", Integer.parseInt(sc.getMinPages()), 
						Integer.parseInt(sc.getMaxPages())));
				// handle single criteria search
				if (sc.getPreference2() == null) {
					return defaultSortedList(criteria, sc);
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Author)) {
					debug(sc, "Pages+Author to be implemented.");
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Years)) {
					debug(sc, "Pages+Years to be implemented.");
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Genre)) {
					debug(sc, "Pages+Genre to be implemented.");
				}
			}
		// Years = highest priority
		} else if (sc.getPreference1().equals(SearchCriteria.Preferences.Years)) {
			// ensure we have search data
			if (sc.getMinYear() == null || sc.getMinYear().trim().equals("")) {
				debug(sc, "preference1=year but minYear is empty");
			} else if (sc.getMaxYear() == null || sc.getMaxYear().trim().equals("")) {
				debug(sc, "preference1=year but maxYear is empty");
			} else {
				criteria.add(Restrictions.between("year", Integer.parseInt(sc.getMinYear()), 
						Integer.parseInt(sc.getMaxYear())));
				// handle single criteria search
				if (sc.getPreference2() == null) {
					return defaultSortedList(criteria, sc);
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Author)) {
					debug(sc, "Year+Author to be implemented.");
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Pages)) {
					debug(sc, "Year+Pages to be implemented.");
				} else if (sc.getPreference2().equals(SearchCriteria.Preferences.Genre)) {
					debug(sc, "Year+Genre to be implemented.");
				}
			}
		}
		// still here? return null
		return null;
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
