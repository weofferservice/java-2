<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<nav class="navbar navbar-dark bg-dark">
    <div class="container">
        <div class="navbar-brand">
            <img src="resources/images/icon-meal.png"> <spring:message code="app.title"/>
        </div>
        <form class="form-inline my-2" method="POST" action="spring_security_check" id="form-login">
            <input class="form-control mr-1" type="text" name="username" placeholder="email">
            <input class="form-control mr-1" type="password" name="password" placeholder="password">
            <button class="btn btn-success" type="submit">
                <span class="fa fa-sign-in"></span>
            </button>
        </form>
    </div>
</nav>

<div class="jumbotron pt-0">
    <div class="container">
        <c:if test="${param.error}">
            <div class="error">
                ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
            </div>
        </c:if>
        <br>
        <div class="text-center">
            <button type="button" class="btn btn-lg btn-primary" onclick="setCredentialsAndSubmit('user@yandex.ru', 'password')">
                <spring:message code="app.login"/> User
            </button>
            <button type="button" class="btn btn-lg btn-primary" onclick="setCredentialsAndSubmit('admin@gmail.com', 'admin')">
                <spring:message code="app.login"/> Admin
            </button>
        </div>
        <br>
        <br>
        <div class="lead text-justify">
            Стек технологий:
            <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc" target="_blank">Spring MVC</a>,
            <a href="https://spring.io/projects/spring-data-jpa" target="_blank">Spring Data JPA</a>,
            <a href="https://spring.io/projects/spring-security" target="_blank">Spring Security</a>,
            <a href="https://hibernate.org/orm/" target="_blank">Hibernate ORM</a>,
            <a href="https://hibernate.org/validator/" target="_blank">Hibernate Validator</a>,
            <a href="https://github.com/FasterXML/jackson" target="_blank">JSON Jackson</a>,
            <a href="https://ru.wikipedia.org/wiki/JavaServer_Pages" target="_blank">JSP</a>,
            <a href="https://ru.wikipedia.org/wiki/JavaServer_Pages_Standard_Tag_Library" target="_blank">JSTL</a>,
            <a href="https://www.webjars.org/" target="_blank">WebJars</a>,
            <a href="https://getbootstrap.com/" target="_blank">Bootstrap</a>,
            <a href="https://jquery.com/" target="_blank">jQuery</a>,
            <a href="https://datatables.net/" target="_blank">jQuery DataTables plugin</a>,
            <a href="https://ned.im/noty/#/" target="_blank">JavaScript Notifications Noty</a>,
            <a href="https://www.slf4j.org/" target="_blank">SLF4J</a>,
            <a href="https://www.ehcache.org/" target="_blank">EHCACHE</a>,
            <a href="https://junit.org/junit5/" target="_blank">JUnit</a>,
            <a href="https://hamcrest.org/JavaHamcrest/" target="_blank">Hamcrest</a>,
            <a href="https://spring.io/blog/2014/05/07/preview-spring-security-test-method-security" target="_blank">Spring Security Test</a>,
            <a href="https://tomcat.apache.org/" target="_blank">Apache Tomcat</a>,
            <a href="https://www.postgresql.org/" target="_blank">PostgreSQL</a>.
        </div>
    </div>
</div>
<div class="container">
    <div class="lead text-justify">
        <a href="https://github.com/weofferservice/java-2" target="_blank">Java Enterprise проект</a> с регистрацией/авторизацией
        и интерфейсом на основе ролей (USER, ADMIN). Администратор может создавать/редактировать/удалять пользователей,
        а пользователи - управлять своим профилем и данными (дата/время приема пищи, блюдо, калорийность) через UI (по AJAX)
        и по REST-интерфейсу с базовой авторизацией. Возможна фильтрация данных по дате/времени, при этом цвет записи
        таблицы еды зависит от того, превышает ли сумма калорий за день норму (редактируемый параметр в профиле пользователя).
        Весь REST-интерфейс покрывается JUnit тестами, используя Spring MVC Test и Spring Security Test.
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
<script type="text/javascript">
    function setCredentialsAndSubmit(username, password) {
        const form = $('#form-login');
        form.find('input[name="username"]').val(username);
        form.find('input[name="password"]').val(password);
        form.submit();
    }
</script>
</body>
</html>