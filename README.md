# Book Suggestion Web App

Users state what sort of books they like the most, ranking their preferences in importance. Preferences can be author name (partial name search, case-insensitive, UTF-8 support), a specific genre, a range of years, and a range of pages.

For example, users could state that they like the author Kafka, with no other preferences. Our data source is quite limited, so we have few details about Kafka to use to recommend other books. I use the author's published genres and years of publication. The data shows that Kafka wrote in the genres of Comedy, Suspense and Science Fiction, and that his books were published from 1925-1952. I then offset the year range by 25 years in either direction, to allow more flexibility in the suggestions. This returns a list of 22 books which can be [viewed here](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Author&author=Kafka). This list is sorted by rating high to low, and sub-sorted by author's first name A-Z.

For other single-preference searches, the app does a simple search for that preference. It would be useful to have relationships between genres, i.e. recommend Fantasy books for readers who like Science Fiction. 
 * Years: [Books from the 20th century](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Years&minYear=1900&maxYear=2000)
 * Genre: [Suspense books](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Genre&genre=Drama)
 * Number of Pages: [Books under 500 pages](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Pages&minPages=0&maxPages=500)

For multiple preferences, the logic is trickier. TBD
 * Author & Year
 * Author & Genre
 * Author & Pages

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
Heroku URL: https://book-suggestion-webapp.herokuapp.com.

The data source currently contains only 106 books which can be [viewed here](https://book-suggestion-webapp.herokuapp.com/books).
