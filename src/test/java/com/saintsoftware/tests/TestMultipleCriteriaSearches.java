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
public class TestMultipleCriteriaSearches {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

	/**
	 * 22 books returned.
	 */
    @Test
    public void authorAndPages() throws Exception {
    	// TODO: check that top result is Ovide, Les Métamorphoses
		mockMvc.perform(post("/suggestions?preference1=Author&preference2=Pages&author=Kafka&maxPages=500"))
        .andExpect(status().isOk())
		.andExpect(model().attribute("books", Matchers.hasSize(22)));
    }

	/**
	 * 22 books returned.
	 */
    @Test
    public void authorAndYear() throws Exception {
    	// TODO: check that top result is Franz Kafka, Le Procès
		mockMvc.perform(post("/suggestions?preference1=Author&preference2=Year&author=Kafka&maxYear=1920"))
        .andExpect(status().isOk())
		.andExpect(model().attribute("books", Matchers.hasSize(22)));
    }

    /**
	 * kafka genres: [Comedy, Suspense, Science Fiction]
	 * kafka years with 25y offset: min=1900; max=1977
	 * 22 books returned.
	 */
//    @Test
//    public void authorAndGenre() throws Exception {
//		mockMvc.perform(post("/suggestions?preference1=Author&preference2=Genre&author=Kafka&genre=Suspense"))
//        .andExpect(status().isOk())
//		.andExpect(model().attribute("books", Matchers.hasSize(22)));
//    }
    
}