<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="java-2" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<div class="jumbotron pt-4">
    <div class="container">
        <div class="row">
            <div class="offset-3 col-5">
                <%--@elvariable id="userTo" type="org.zcorp.java2.to.UserTo"--%>
                <h3><spring:message code="app.profile"/> ${userTo.name}</h3>
                <form:form class="form-group" modelAttribute="userTo" method="POST" action="profile"
                           charset="utf-8" accept-charset="UTF-8">

                    <java-2:inputField labelCode="user.name" name="name"/>
                    <java-2:inputField labelCode="user.email" name="email"/>
                    <java-2:inputField labelCode="user.password" name="password" inputType="password"/>
                    <java-2:inputField labelCode="user.caloriesPerDay" name="caloriesPerDay" inputType="number"/>

                    <div class="text-right">
                        <button type="submit" class="btn btn-primary">
                            <span class="fa fa-check"></span>
                            <spring:message code="common.save"/>
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>