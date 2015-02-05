package com.saintsoftware.tests;

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
public class TestSingleCriteriaSearches {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

	/**
	 * Homère genres: [Comedy, Suspense, Science Fiction]
	 * Homère years with 25y offset: min=1900; max=1977
	 * 23 books returned.
	 */
    @Test
    public void authorOnlyUTF8() throws Exception {
		mockMvc.perform(post("/suggestions?preference1=Author&author=Homère"))
        .andExpect(status().isOk())
		.andExpect(model().attribute("books", Matchers.hasSize(23)));
    }

    /**
	 * kafka genres: [Comedy, Suspense, Science Fiction]
	 * kafka years with 25y offset: min=1900; max=1977
	 * 22 books returned.
	 */
    @Test
    public void authorOnly() throws Exception {
		mockMvc.perform(post("/suggestions?preference1=Author&author=Kafka"))
        .andExpect(status().isOk())
		.andExpect(model().attribute("books", Matchers.hasSize(22)));
    }
    
	/**
	 * genre: [Suspense]
	 * 16 books returned.
	 */
    @Test
    public void genreOnly() throws Exception {
		mockMvc.perform(post("/suggestions?preference1=Genre&genre=Suspense"))
        .andExpect(status().isOk())
		.andExpect(model().attribute("books", Matchers.hasSize(16)));
    }  
    
	/**
	 * years: 1900-2000 (20th century)
	 * 58 books returned.
	 */
    @Test
    public void yearOnly() throws Exception {
		mockMvc.perform(post("/suggestions?preference1=Years&minYear=1900&maxYear=2000"))
        .andExpect(status().isOk())
		.andExpect(model().attribute("books", Matchers.hasSize(58)));
    }   
    
	/**
	 * pages: 200-400
	 * 50 books returned.
	 */
    @Test
    public void pagesOnly() throws Exception {
		mockMvc.perform(post("/suggestions?preference1=Pages&minPages=200&maxPages=400"))
        .andExpect(status().isOk())
		.andExpect(model().attribute("books", Matchers.hasSize(50)));
    }       
}