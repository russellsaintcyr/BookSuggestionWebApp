# Book Suggestion Web App

Users state what sort of books they like the most, ranking their preferences in importance. Preferences can be author name (partial name search, case-insensitive, UTF-8 support), a specific genre, a range of years, and a range of pages.

For example, users could state that they like the author Kafka, with no other preferences. Our data source is quite limited, so we have few details about Kafka to use to recommend other books. I use the author's published genres and years of publication. The data shows that Kafka wrote in the genres of Comedy, Suspense and Science Fiction, and that his books were published from 1925-1952. I then offset the year range by 25 years in either direction, to allow more flexibility in the suggestions. This returns a list of 22 books which can be [viewed here](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Author&author=Kafka). This list is sorted by rating high to low, and within the rating by author first name A-Z.

Made with:
  * Spring MVC
  * AngularJS
  * Embedded Tomcat with tomcat7-maven-plugin
  * Hibernate data modeling from a CSV source, loaded into an H2 in-memory database
  * Internationalization (i18n)

Maven command to build: `mvn package`. This will run the unit tests and create an executable jar `BookSuggestionWebApp-1.0-SNAPSHOT-war-exec.jar`. You can then run the up with one of the commands below.
```
java -jar target/BookSuggestionWebApp-1.0-SNAPSHOT-war-exec.jar
... or ...
mvn tomcat7:run
... or if you have heroku toolkit ...
foreman start
```
Test URL: https://book-suggestion-webapp.herokuapp.com.

The data source currently contains only 106 books.
