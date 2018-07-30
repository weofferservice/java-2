<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <c:if test="${not empty meal}">
        <jsp:useBean id="meal" type="org.zcorp.java2.model.Meal" scope="request"/>
    </c:if>
    <title>${empty meal ? "Create" : "Update"} meal</title>
</head>
<body>
    <h1>${empty meal ? "Create" : "Update"} meal</h1>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        <label>
            Date & Time:
            <input type="datetime-local" name="datetime" value="${meal.dateTime}">
        </label>
        <br>
        <label>
            Description:
            <input type="text" name="description" value="${meal.description}">
        </label>
        <br>
        <label>
            Calories:
            <input type="number" name="calories" value="${meal.calories}">
        </label>
        <br>
        <button type="submit">${empty meal ? "Save" : "Update"}</button>
        <button type="reset" onclick="window.history.back()">Cancel</button>
    </form>
</body>
</html>
