<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <jsp:useBean id="meal" type="org.zcorp.java2.model.Meal" scope="request"/>
    <h3><spring:message code="${meal.isNew() ? 'meal.add' : 'meal.edit'}"/></h3>
    <hr>
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
