<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <h2>
        <c:choose>
            <c:when test="${action == 'create'}">
                <spring:message code="meal.add"/>
            </c:when>
            <c:otherwise>
                <spring:message code="meal.edit"/>
            </c:otherwise>
        </c:choose>
    </h2>
    <hr>
    <jsp:useBean id="meal" type="org.zcorp.java2.model.Meal" scope="request"/>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meal.datetime"/>:</dt>
            <dd><input type="datetime-local" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size="40" name="description" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit">
            <spring:message code="common.save"/>
        </button>
        <button onclick="window.history.back()" type="button">
            <spring:message code="common.cancel"/>
        </button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
