<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.11/angular.min.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="preferences.title" text="preferences.title"/></title>
	<link rel="stylesheet" type="text/css" href="css/appstyle.css">
</head>
<body ng-app="setPreferences">
	<div ng-controller="SearchController">
	<h1><spring:message code="preferences.header" text="preferences.header"/></h1>
	<form:form action="suggestions" method="post" commandName="searchForm" acceptCharset="utf-8" ng-submit="form.$valid && submit()" name="form">
		<table border="0">
		<tr>
		<td id="divPrefs">
			<div id="divPref1">
				<spring:message code="preferences.preference" text="preferences.preference"/> 1
				<form:select path="preference1" id="prefs1" ng-change="prefChanged(prefs1);" ng-model="prefs1">
					<option value="">--- SELECT ---</option>
					<form:options items="${prefs}" />
				</form:select>
			</div>
			<div id="divPref2" ng-show="prefs1">
				<spring:message code="preferences.preference" text="preferences.preference"/> 2
				<form:select path="preference2" id="prefs2" ng-change="prefChanged(prefs2);" ng-model="prefs2" ng-disabled="!prefs1">
					<option value="">--- SELECT ---</option>
					<form:options items="${prefs}" />
				</form:select>
			</div>
		</td>
		<td id="divFields">
			<div id="divAuthor" ng-show="checkValue('Author');">
				<spring:message code="prefs.author" text="prefs.author"/>: 
				<input type="text" ng-model="author" name="author" />
			</div>
			<div id="divGenre" ng-show="checkValue('Genre');">
				<spring:message code="prefs.genre" text="prefs.genre"/>: 
				<form:select path="genre">
					<form:option value="" label="--- Select ---"/>
					<form:options items="${genreArray}" />
				</form:select>
			</div>
			<div id="divYears" ng-show="checkValue('Years');">
				<spring:message code="prefs.years" text="prefs.years"/>: 
				<spring:message code="search.from" text="search.from"/> 
				<input type="number" ng-model="minYear" ng-model-options="{updateOn: 'blur'}" 
					name="minYear" min="0" max="2015" value="0" integer />
				<spring:message code="search.to" text="search.to"/>
				<input type="number" ng-model="maxYear" ng-model-options="{updateOn: 'blur'}" 
					name="maxYear" min="0" max="2015" value="2015" integer />
				<br /> 
				<span ng-show="form.minYear.$error.integer || form.maxYear.$error.integer"><spring:message code="search.error.integer"/></span>
				<span ng-show="form.minYear.$error.min || form.maxYear.$error.min"><spring:message code="search.error.minYear"/></span>
				<span ng-show="form.minYear.$error.max || form.maxYear.$error.max"><spring:message code="search.error.maxYear"/></span>
			</div>
			<div id="divPages" ng-show="checkValue('Pages');">
				<spring:message code="prefs.pages" text="prefs.pages"/>: 
				<spring:message code="search.from" text="search.from"/>
				<input type="number" ng-model="minPages" ng-model-options="{updateOn: 'blur'}" name="minPages" integer/>
				<spring:message code="search.to" text="search.to"/>
				<input type="number" ng-model="maxPages" ng-model-options="{updateOn: 'blur'}" name="maxPages" integer />
			</div>
		</td>
		</tr>
		</table>
		<input type="submit" name="submit" ng-disabled="!form.$valid" value="<spring:message code="preferences.saveprefs" text="preferences.saveprefs"/>">
		<input type="reset" name="reset" value="<spring:message code="button.reset" text="button.reset"/>">
	</form:form>
	</div>
	<script>
	angular.module("setPreferences", []).controller("SearchController", function($scope) {
		$scope.prefChanged = function(obj) {
			// ensure we aren't setting the same preference
			if (form.prefs1.value == form.prefs2.value) {
				alert("Please choose distinct priorities.");
			}
		}
		$scope.checkValue = function(val) {
			if (form.prefs1.value == val || form.prefs2.value == val) {
				// TODO: when re-hide, erase any stored values
				return true; // hide
			} else {
				return false; // show
			}
		};
		$scope.submit = function() {
			// TODO: ensure we have items to save
			return false;
	  	};
	});
	</script>
<jsp:include page="footer.jsp" />
</body>
</html>
