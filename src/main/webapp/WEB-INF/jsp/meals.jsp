<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java2.zcorp.org/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>--%>
<!DOCTYPE html>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="resources/js/datatablesUtil.js" defer></script>
<script type="text/javascript" src="resources/js/mealDatatables.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>
<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center"><spring:message code="meal.title"/></h3>
        <form id="filter">
            <div class="form-row">
                <div class="form-group col-md-4">
                    <label for="startDate" class="col-form-label"><spring:message code="meal.fromdate"/>:</label>
                    <input type="date" class="form-control" id="startDate" name="startDate" value="${param.startDate}">
                </div>
                <div class="form-group col-md-4">
                    <label for="endDate" class="col-form-label"><spring:message code="meal.todate"/>:</label>
                    <input type="date" class="form-control" id="endDate" name="endDate" value="${param.endDate}">
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-4">
                    <label for="startTime" class="col-form-label"><spring:message code="meal.fromtime"/>:</label>
                    <input type="time" class="form-control" id="startTime" name="startTime" value="${param.startTime}">
                </div>
                <div class="form-group col-md-4">
                    <label for="endTime" class="col-form-label"><spring:message code="meal.totime"/>:</label>
                    <input type="time" class="form-control" id="endTime" name="endTime" value="${param.endTime}">
                </div>
            </div>
            <button type="button" class="btn btn-secondary" onclick="resetFilter()">
                <spring:message code="common.reset"/>
            </button>
            <button type="button" class="btn btn-primary" onclick="updateTable()">
                <spring:message code="common.filter"/>
            </button>
        </form>
        <hr>
        <button class="btn btn-primary" onclick="add()">
            <span class="fa fa-plus"></span>
            <spring:message code="common.add"/>
        </button>
        <hr>
        <table class="table table-striped" id="datatable">
            <thead>
                <tr>
                    <th><spring:message code="meal.datetime"/></th>
                    <th><spring:message code="meal.description"/></th>
                    <th><spring:message code="meal.calories"/></th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${meals}" var="meal">
                <jsp:useBean id="meal" scope="page" type="org.zcorp.java2.to.MealWithExceed"/>
                <tr data-mealExceed="${meal.exceed}" id="${meal.id}">
                    <td>
                            <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                            <%--<%=DateTimeUtil.toString(meal.getDateTime())%>--%>
                            <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                            ${fn:formatDateTime(meal.dateTime)}
                    </td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a><span class="fa fa-pencil"></span></a></td>
                    <td><a class="delete"><span class="fa fa-remove"></span></a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><spring:message code="meal.add"/></h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" id="id" name="id">

                    <div class="form-group">
                        <label for="dateTime" class="col-form-label"><spring:message code="meal.datetime"/></label>
                        <input type="datetime-local" class="form-control" id="dateTime" name="dateTime">
                    </div>

                    <div class="form-group">
                        <label for="description" class="col-form-label"><spring:message code="meal.description"/></label>
                        <input type="text" class="form-control" id="description" name="description"
                               placeholder="<spring:message code="meal.description"/>">
                    </div>

                    <div class="form-group">
                        <label for="calories" class="col-form-label"><spring:message code="meal.calories"/></label>
                        <input type="number" class="form-control" id="calories" name="calories"
                               placeholder="<spring:message code="meal.calories"/>">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">
                    <span class="fa fa-close"></span>
                    <spring:message code="common.cancel"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="save()">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.save"/>
                </button>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>