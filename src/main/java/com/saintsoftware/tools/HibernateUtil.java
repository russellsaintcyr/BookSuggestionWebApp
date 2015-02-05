package com.saintsoftware.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class HibernateUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			createDatabase();
			Configuration hibConfiguration = new Configuration().configure("hibernate.cfg.h2.xml");
			@SuppressWarnings("deprecation")
			SessionFactory sessionFactory = hibConfiguration.buildSessionFactory();
			return sessionFactory;
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed: " + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
 
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
 
	public static void shutdown() {
		getSessionFactory().close();
	}
	
	private static void createDatabase() throws SQLException {
		String jdbcURL = "jdbc:h2:mem:books";
		String csvPath = "classpath:Books.csv";
		logger.debug("Creating in-memory database at " + jdbcURL);
		Connection connection = DriverManager.getConnection(jdbcURL);
		Statement stmnt = connection.createStatement();
		try {
			logger.debug("Reading in data from " + csvPath);
			stmnt.execute("CREATE TABLE BOOKS AS SELECT * FROM CSVREAD('" + csvPath + "', null, 'fieldSeparator=' || CHAR(9));");
			logger.debug("Database created");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmnt.close();
			// don't close the connection or else the in-memory db will disappear
		}
	}
 
}