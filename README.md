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
 * Author & Pages: [Kafka with a max page of 500](https://book-suggestion-webapp.herokuapp.com/suggestions?preference1=Author&preference2=Pages&author=Kafka&minPages=0&maxPages=500). The result set of Kafka's 3 genres and 1900-1977 is sorted by pages closest to 500.
 * Author & Year: Kafka with year of 1920. The result set of Kafka's 3 genres and 1900-1977 is sorted by years closest to 1920.
 * Author & Genre: Kafka with genre of Suspense. The result set of Kafka's 3 genres and 1900-1977 is sorted first by Suspense novels, then the other genres. If the preferred genre is not one of Kafka's genres, then no results are returned.
 * Genre & Author: TBD 
 * Genre & Year: TBD 
 * Genre & Pages: TBD
 * Year & Author: TBD 
 * Year & Genre: TBD 
 * Year & Pages: TBD 
 * Pages & Year: TBD
 * Pages & Genre: TBD
 * Pages & Author: TBD

The data source currently contains only 106 books which can be [viewed here](https://book-suggestion-webapp.herokuapp.com/books).

### App Technical Details
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
