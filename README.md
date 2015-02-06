# Book Suggestion Web App

Users state what types of books they prefer, ranking their preferences by importance. Preferences can be author name (partial name search, case-insensitive, UTF-8 support), a specific genre, a range of years, and a range of pages.

### Single Preferences
Let's take an example for a user who likes the author Kafka, with no other preferences. Our data source is quite limited, so we have few details about Kafka to use to recommend other books. The app queries the author's published genres and years of publication. The data shows that Kafka wrote in the genres of Comedy, Suspense and Science Fiction, and that his books were published from 1925-1952. The app offsets the year range by 25 years in either direction, to allow more flexibility in the suggestions. This returns a list of 22 books which can be [viewed here](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Author&author=Kafka). This list is sorted by rating high to low, and sub-sorted by author's first name A-Z.

For other single-preference searches, the app does a simple search for that preference. It would be useful to have relationships between genres, i.e. recommend Fantasy books for readers who like Science Fiction. 
 * Years: [Books from the 20th century](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Years&minYear=1900&maxYear=2000)
 * Genre: [Suspense books](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Genre&genre=Drama)
 * Number of Pages: [Books under 500 pages](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Pages&minPages=0&maxPages=500)

### Multiple Preferences
For multiple preferences the logic is trickier since it depends on the unique combination of preferences. Due to the complexity of search results, currently only two preferences are supported. 
 * **Author & Pages**: The result set of the author's genres and year range, sorted by pages closest to the average of the pages entered. Example: [Kafka with a max page of 500](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Author&preference2=Pages&author=Kafka&maxPages=500)
 * **Author & Year**: The result set of the author's genres and year range, sorted by pages closest to the average of the years entered. Example: [Faulkner with year of 1900](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Author&preference2=Years&author=Faulkner&minYear=1900)
 * **Author & Genre**: The result set of the author's genres and year range, sorted first by the selected genre, then the other genres. If the preferred genre is not one of the author's genres, then no results are returned.
 * **Genre & Author**: Only show the selected genre, filtered by the author's genres and year range. Sort by rating.
 * **Pages & Author**: Only show books in the selected page range, filtered by the author's genres and year range. Sort by rating.
 * **Year & Author**: Only show books in the selected year range, filtered by the author's genres and year range. Sort by rating.
 * **Genre & Year**, **Year & Genre**: Only show books in the selected year range and genre. Sort by rating. Example: [Drama from the 20th century](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Years&preference2=Genre&genre=Drama&minYear=1900&maxYear=2000)
 * **Genre & Pages**, **Pages & Genre**: Only show the selected genre within the page count entered. Sort by rating. Example: [Action books less than 400 pages](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Genre&preference2=Pages&author=&genre=Action&maxPages=400)
 * **Year & Pages**, **Pages & Year**: Only show books in the selected year range within the page count entered. Sort by rating. Example: [Books from the year 1000 at least 800 pages](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Years&preference2=Pages&minPages=800&minYear=1000)

The data source currently contains only 106 books which can be [viewed here](https://book-suggestion-webapp.herokuapp.com/books).

### App Implementation Details
  * Spring MVC
  * AngularJS
  * Embedded Tomcat with tomcat7-maven-plugin
  * Hibernate data modeling from a CSV source, loaded into an H2 in-memory database
  * Internationalization (i18n)

### Build, Test and Execute
Maven command to build: `mvn package`. This will run the unit tests and create an executable jar `BookSuggestionWebApp-1.0-SNAPSHOT-war-exec.jar`. You can then run the app with one of the commands below.
```
java -jar target/BookSuggestionWebApp-1.0-SNAPSHOT-war-exec.jar
... or ...
mvn tomcat7:run
... or if you have the heroku toolbelt, with a PORT environment variable ...
foreman start
```
