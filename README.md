[![Build Status](https://travis-ci.org/weofferservice/java-2.svg?branch=master)](https://travis-ci.org/weofferservice/java-2)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b5a1805dfd534962875800fb53b2d7ac)](https://www.codacy.com/project/weofferservice/java-2/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=weofferservice/java-2&amp;utm_campaign=Badge_Grade_Dashboard)

# java-2
Java-проект для портфолио - 2

# Стек технологий

Web-cлой (front-end):
- [Bootstrap](https://getbootstrap.com/)
- [jQuery](https://jquery.com/)
- [jQuery DataTables plugin](https://datatables.net/)
- [JavaScript Notifications Noty](https://ned.im/noty/#/)
- [WebJars](https://www.webjars.org/)

Web-cлой (back-end):
- [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc)
- [JSP](https://ru.wikipedia.org/wiki/JavaServer_Pages)
- [JSTL](https://ru.wikipedia.org/wiki/JavaServer_Pages_Standard_Tag_Library)
- [JSON Jackson](https://github.com/FasterXML/jackson)

Data-слой (back-end):
- [Spring Jdbc Template](https://docs.spring.io/spring/docs/3.1.x/spring-framework-reference/html/jdbc.html) 
- [Hibernate ORM](https://hibernate.org/orm/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

Валидация данных (back-end):
- [Hibernate Validator](https://hibernate.org/validator/)

Кеширование данных в data-слое (back-end):
- [EHCACHE](https://www.ehcache.org/)

Security-"слой" (back-end):
- [Spring Security](https://spring.io/projects/spring-security)

Логирование (back-end):
- [SLF4J](https://www.slf4j.org/)
- [logback](https://logback.qos.ch/)

Тестирование:
- [JUnit](https://junit.org/junit5/)
- [Hamcrest](https://hamcrest.org/JavaHamcrest/)
- [Spring Test](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html)
- [Spring Security Test](https://spring.io/blog/2014/05/07/preview-spring-security-test-method-security)

Контейнер сервлетов:
- [Apache Tomcat](https://tomcat.apache.org/)

БД:
- [PostgreSQL](https://www.postgresql.org/)
- [HSQLDB](http://hsqldb.org/)

# Общее описание

Java Enterprise проект с регистрацией/авторизацией. Администратор может управлять пользователями,
а пользователи могут управлять своим профилем и данными через UI (по AJAX) и по REST с базовой авторизацией.
Весь REST-интерфейс покрыт JUnit тестами, используя Spring MVC Test и Spring Security Test.
