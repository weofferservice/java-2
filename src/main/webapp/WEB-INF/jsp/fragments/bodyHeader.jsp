<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set value="${requestScope['javax.servlet.forward.request_uri']}" var="currentPage"/>
<c:set value="${pageContext.response.locale}" var="localeCode"/>

<nav class="navbar navbar-expand-md navbar-dark bg-dark py-0">
    <div class="container">
        <a class="navbar-brand" href="meals"><img src="resources/images/icon-meal.png"> <spring:message code="app.title"/></a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <sec:authorize access="isAuthenticated()">
                        <form:form class="form-inline my-2" method="POST" action="logout">
                            <sec:authorize access="hasRole('ROLE_ADMIN')">
                                <a class="btn btn-info mr-1" href="users"><spring:message code="user.title"/></a>
                            </sec:authorize>
                            <a class="btn btn-info mr-1" href="profile">
                                <spring:message code="app.profile"/>
                                <sec:authentication property="principal.userTo.name"/>
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <span class="fa fa-sign-out"></span>
                            </button>
                        </form:form>
                    </sec:authorize>
                    <sec:authorize access="isAnonymous()">
                        <form:form class="form-inline my-2" method="POST" action="spring_security_check" id="form-login">
                            <input class="form-control mr-1" type="text" name="username" placeholder="email">
                            <input class="form-control mr-1" type="password" name="password" placeholder="password">
                            <button class="btn btn-success" type="submit">
                                <span class="fa fa-sign-in"></span>
                            </button>
                        </form:form>
                    </sec:authorize>
                </li>
                <li class="nav-item dropdown">
                    <a class="dropdown-toggle nav-link my-1 ml-2" data-toggle="dropdown">
                        ${localeCode}
                    </a>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="${currentPage}?lang=en">English</a>
                        <a class="dropdown-item" href="${currentPage}?lang=ru">Русский</a>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</nav>
<script type="text/javascript">
    const localeCode = "${localeCode}";
</script>