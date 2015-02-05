<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
	<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.3.11/angular.min.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="books.title" text="books.title"/></title>
	<link rel="stylesheet" type="text/css" href="css/appstyle.css">
</head>
<body>
	<h1>${booksHeader}</h1>
	${msgBookCount}<br>
	<c:if test="${not empty books}">
 		<ol>
			<c:forEach var="book" items="${books}">
				<li><a href="searchresults?author=${book.author}">${book.author}</a>, 
				<i>${book.title}</i>, 
				<a href="searchresults?minYear=${book.year}&maxYear=${book.year}">${book.year}</a>
				(${book.pages} pages) 
				(<a href="searchresults?genre=${book.genre}">${book.genre}</a>)  
				<a href="searchresults?minRating=${book.rating}&maxRating=${book.rating}"><c:forEach begin="1" end="${book.rating}" varStatus="loop"><img src="images/star.png" border="0" width="16" height="16"/></c:forEach></a>
				</li>
			</c:forEach>
		</ol>
 	</c:if>
	<c:if test="${not empty searchCriteria}"><span id="debug">${searchCriteria}</span></c:if>
	<c:if test="${not empty debugInfo}">
		<c:forEach var="info" items="${debugInfo}">
		<br><span id="debug">${info}</span>
		</c:forEach>
	</c:if>
<jsp:include page="footer.jsp" />
</body>
</html>
