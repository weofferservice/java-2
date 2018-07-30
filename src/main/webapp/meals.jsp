<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.zcorp.java2.util.DateTimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table>
    <tr>
        <th>ID</th>
        <th>Date & Time</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="meal" items="${mealsWithExceed}">
        <jsp:useBean id="meal" type="org.zcorp.java2.model.MealWithExceed"/>
        <tr style="color: ${meal.exceed ? "red" : "green"};">
            <td>${meal.id}</td>
            <td>${DateTimeUtil.toString(meal.dateTime)}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>