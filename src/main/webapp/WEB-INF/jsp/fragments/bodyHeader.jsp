<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<nav class="navbar navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="meals"><img src="resources/images/icon-meal.png"> <spring:message code="app.title"/></a>
        <form class="form-inline my-2">
            <a class="btn btn-info mr-1" href="users"><spring:message code="user.title"/></a>
            <a class="btn btn-primary" href="logout">
                <span class="fa fa-sign-out"></span>
            </a>
        </form>
    </div>
</nav>