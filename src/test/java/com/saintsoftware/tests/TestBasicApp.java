package com.saintsoftware.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:mvc-dispatcher-servlet.xml")
public class TestBasicApp {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getRoot() throws Exception {
        mockMvc.perform(get("/"))
        .andExpect(status().isOk());
    }

    @Test
    public void getSearch() throws Exception {
        mockMvc.perform(get("/search"))
        .andExpect(status().isOk());
    }

    @Test
    public void getBook() throws Exception {
        mockMvc.perform(get("/books"))
        .andExpect(status().isOk());
    }

	/**
	 * test for 58 books from the 20th century
	 */
    @Test
    public void searchYears() throws Exception {
		mockMvc.perform(post("/searchresults?minYear=1900&maxYear=1999"))
        .andExpect(status().isOk())
		.andExpect(model().attribute("books", Matchers.hasSize(58)));
    }

    /**
	 * test for 9 books for author "homère"
	 */
    @Test
    public void searchAuthors() throws Exception {
		mockMvc.perform(post("/searchresults?author=homère"))
        .andExpect(status().isOk())
		.andExpect(model().attribute("books", Matchers.hasSize(9)));
    }

	/**
	 * test for 16 books for genre "Suspense"
	 */
    @Test
    public void searchGenres() throws Exception {
		mockMvc.perform(post("/searchresults?genre=Suspense"))
        .andExpect(status().isOk())
		.andExpect(model().attribute("books", Matchers.hasSize(16)));
    }

	/**
	 * test for 33 books having 200-300 pages
	 */
    @Test
    public void searchPages() throws Exception {
		mockMvc.perform(post("/searchresults?minPages=200&maxPages=300"))
        .andExpect(status().isOk())
		.andExpect(model().attribute("books", Matchers.hasSize(33)));
    }

	/**
	 * test for 17 books having a 4-star rating
	 */
    @Test
    public void searchRatings() throws Exception {
		mockMvc.perform(post("/searchresults?minRating=4&maxRating=4"))
        .andExpect(status().isOk())
		.andExpect(model().attribute("books", Matchers.hasSize(17)));
    }
    
}