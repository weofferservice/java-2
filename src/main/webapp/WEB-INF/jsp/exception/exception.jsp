<%@ page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<jsp:include page="../fragments/headTag.jsp"/>
<body>
<jsp:include page="../fragments/bodyHeader.jsp"/>
<div class="jumbotron">
    <div class="container text-center">
        <h3>${typeMessage}</h3>
        <div class="text-left error">
            ${message}
        </div>
    </div>
</div>
<!--
<c:forEach var="stackTraceElement" items="${exception.stackTrace}">
    ${stackTraceElement}
</c:forEach>
-->
<jsp:include page="../fragments/footer.jsp"/>
</body>
</html>