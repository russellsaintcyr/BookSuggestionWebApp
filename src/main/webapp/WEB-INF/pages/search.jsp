<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html ng-app="bookSuggestionApp">
<head>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.11/angular.min.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="search.title"/></title>
	<link rel="stylesheet" type="text/css" href="css/appstyle.css">
</head>
<body>
	<div ng-controller="SearchController">
		<h1><spring:message code="search.header" text="search.header"/></h1>
		 <form:form action="searchresults" method="post" commandName="searchForm" acceptCharset="utf-8" ng-submit="submit()" name="myForm">
		<div id="divAuthor">
			<spring:message code="search.authorName" text="search.authorName"/>: 
			<input type="text" ng-model="author" name="author" username />
		</div>
		<div id="divGenre">
			<spring:message code="search.genre" text="search.genre"/>:
			<form:select path="genre">
				<form:option value="" label="--- Select ---"/>
				<form:options items="${genreArray}" />
			</form:select>
		</div>
		<div id="divYears">
			<spring:message code="search.year" text="search.year"/>: 
			<spring:message code="search.from" text="search.from"/> 
			<input type="number" ng-model="minYear" ng-model-options="{updateOn: 'blur'}" 
				name="minYear" min="0" max="2015" value="1000" integer />
			<spring:message code="search.to" text="search.to"/>
			<input type="number" ng-model="maxYear" ng-model-options="{updateOn: 'blur'}" 
				name="maxYear" min="0" max="2015" value="2015" integer />
			<br /> 
			<span ng-show="form.minYear.$error.integer || form.maxYear.$error.integer"><spring:message code="search.error.integer"/></span>
			<span ng-show="form.minYear.$error.min || form.maxYear.$error.min"><spring:message code="search.error.minYear"/></span>
			<span ng-show="form.minYear.$error.max || form.maxYear.$error.max"><spring:message code="search.error.maxYear"/></span>
		</div>
		<div id="divPageCount">
			<spring:message code="search.pages" text="search.pages"/>:
			<spring:message code="search.from" text="search.from"/>
			<input type="number" ng-model="minPages" ng-model-options="{updateOn: 'blur'}" name="minPages" integer />
			<spring:message code="search.to" text="search.to"/>
			<input type="number" ng-model="maxPages" ng-model-options="{updateOn: 'blur'}" name="maxPages" integer />
		</div>
		<div id="divRatings">
			<spring:message code="search.rating" text="search.rating"/>:
			<spring:message code="search.from" text="search.from"/>
			<input type="number" ng-model="minRating" ng-model-options="{updateOn: 'blur'}" name="minRating" integer />
			<spring:message code="search.to" text="search.to"/>
			<input type="number" ng-model="maxRating" ng-model-options="{updateOn: 'blur'}" name="maxRating" integer />
		</div>
		<input type="submit" name="submit" value="<spring:message code="search.submit"/>">
		</form:form>
	</div>
	<script>
		angular.module("bookSuggestionApp", []).controller("SearchController",
				function($scope) {
				  $scope.submit = function() {
					// ensure we have non-blank values for numeric inputs 
					if (myForm.minYear.value == "") myForm.minYear.value = "0";
					if (myForm.maxYear.value == "") myForm.maxYear.value = "2015";
					if (myForm.minPages.value == "") myForm.minPages.value = "0";
					if (myForm.maxPages.value == "") myForm.maxPages.value = "1000";
					if (myForm.minRating.value == "") myForm.minRating.value = "1";
					if (myForm.maxRating.value == "") myForm.maxRating.value = "5";
				  };
				});
	</script>
<jsp:include page="footer.jsp" />
</body>
</html>
