<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <base href="${pageContext.request.contextPath}/">
    <title>
        <spring:message code="app.title"/>
    </title>

    <link rel="stylesheet" href="resources/css/style.css">
    <link rel="stylesheet" href="webjars/bootstrap/4.1.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="webjars/noty/3.1.4/demo/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="webjars/datatables/1.10.19/css/dataTables.bootstrap4.min.css">
    <link rel="stylesheet" href="webjars/noty/3.1.4/lib/noty.css"/>
    <link rel="stylesheet" href="webjars/datetimepicker/build/jquery.datetimepicker.min.css"/>

    <link rel="shortcut icon" href="resources/images/icon-meal.png">

    <%--https://stackoverflow.com/questions/436411/where-should-i-put-script-tags-in-html-markup/24070373#24070373--%>
    <script type="text/javascript" src="webjars/jquery/3.3.1-1/jquery.min.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/4.1.3/js/bootstrap.min.js" defer></script>
    <script type="text/javascript" src="webjars/datatables/1.10.19/js/jquery.dataTables.min.js" defer></script>
    <script type="text/javascript" src="webjars/datatables/1.10.19/js/dataTables.bootstrap4.min.js" defer></script>
    <script type="text/javascript" src="webjars/noty/3.1.4/lib/noty.min.js" defer></script>
    <script type="text/javascript" src="webjars/datetimepicker/build/jquery.datetimepicker.full.min.js" defer></script>
</head>