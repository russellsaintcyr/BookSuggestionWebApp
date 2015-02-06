package com.saintsoftware.controller;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.saintsoftware.model.Book;
import com.saintsoftware.model.Book.GENRES;
import com.saintsoftware.model.SearchCriteria;
import com.saintsoftware.tools.DataUtils;

@Controller
public class BaseController {

	@Autowired
	private MessageSource messageSource;
	
	private static final String VIEW_BASIC_SEARCH = "search";
	private static final String VIEW_BOOKS = "books";
	private static final String VIEW_SET_PREFERENCES = "preferences";
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);


	// default to view for setting preferences
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String defaultView(ModelMap model, Locale locale) {
		return viewSetPreferences(model, locale);
	}

	// view for setting preferences
	@RequestMapping(value = "/preferences")
	public String viewSetPreferences(ModelMap model, Locale locale) {
        SearchCriteria sc = new SearchCriteria();     
		model.addAttribute("mapPrefs", sc.getPreferences());
        model.put("searchForm", sc);
		model.addAttribute("genreArray", Book.GENRES.values());	
		return VIEW_SET_PREFERENCES;
	}
	
	@RequestMapping(value = "/search")
	public String viewBasicSearch(ModelMap model) {
        SearchCriteria sc = new SearchCriteria();     
        model.put("searchForm", sc);
		model.addAttribute("genreArray", Book.GENRES.values());	
		return VIEW_BASIC_SEARCH;
	}

    @RequestMapping(value = "/suggestions")
    public String getSuggestions(@ModelAttribute("searchForm") SearchCriteria sc, 
    		ModelMap model, Locale locale) {
		List<Book> books = DataUtils.getSuggestions(sc);
		model.addAttribute("booksHeader", messageSource.getMessage("books.header.suggestions", null, "books.header.suggestions", locale));
		return viewBooks(model, sc, locale, books);
    }

    @RequestMapping(value = "/searchresults")
    public String searchBooks(@ModelAttribute("searchForm") SearchCriteria sc, 
    		ModelMap model, Locale locale) {
		List<Book> books = DataUtils.searchBooks(sc);
		model.addAttribute("booksHeader", messageSource.getMessage("books.header.searchresults", null, "books.header.searchresults", locale));
		return viewBooks(model, sc, locale, books);
    }

    @RequestMapping(value = "/books")
	public String showAllBooks(ModelMap model, Locale locale) {
		List<Book> books = DataUtils.getAllBooks();
		model.addAttribute("booksHeader", messageSource.getMessage("books.header.allbooks", null, "books.header.allbooks", locale));
		return viewBooks(model, null, locale, books);
	}
    
    public String viewBooks(ModelMap model, SearchCriteria sc, Locale locale, List<Book> books) {
		if (sc !=null) {
			model.addAttribute("searchCriteria", sc.toString());
			model.addAttribute("debugInfo", sc.getDebugInfo());
		}
		if (books == null) {
			model.addAttribute("msgBookCount", "0 " + messageSource.getMessage("books.notOneBook", null, "books.notOneBook", locale));
		} else if (books.size() == 1) {
			model.addAttribute("msgBookCount", messageSource.getMessage("books.oneBook", null, "books.oneBook", locale));
		} else {
			model.addAttribute("msgBookCount", books.size() + " " + messageSource.getMessage("books.notOneBook", null, "books.notOneBook", locale));
		}
		model.addAttribute("books", books);
		return VIEW_BOOKS;
    }
    
}
