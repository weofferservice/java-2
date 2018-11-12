<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="resources/js/datatablesUtil.js" defer></script>
<script type="text/javascript" src="resources/js/mealDatatables.js" defer></script>
<script type="text/javascript" src="resources/js/mealDatetimepicker.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>
<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center"><spring:message code="meal.title"/></h3>
        <%--https://getbootstrap.com/docs/4.1/components/card/--%>
        <div class="card">
            <div class="card-body py-0 border">
                <form id="filter" class="my-0">
                    <div class="row">
                        <div class="offset-2 col-6">
                            <div class="form-group">
                                <label for="startDate" class="col-form-label"><spring:message code="meal.fromdate"/>:</label>
                                <input type="text" class="form-control col-6" id="startDate" name="startDate">

                                <label for="endDate" class="col-form-label"><spring:message code="meal.todate"/>:</label>
                                <input type="text" class="form-control col-6" id="endDate" name="endDate">
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="form-group">
                                <label for="startTime" class="col-form-label"><spring:message code="meal.fromtime"/>:</label>
                                <input type="text" class="form-control col-6" id="startTime" name="startTime">

                                <label for="endTime" class="col-form-label"><spring:message code="meal.totime"/>:</label>
                                <input type="text" class="form-control col-6" id="endTime" name="endTime">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="card-footer text-right">
                <button type="button" class="btn btn-danger" onclick="resetFilter()">
                    <span class="fa fa-remove"></span>
                    <spring:message code="common.reset"/>
                </button>
                <button type="button" class="btn btn-primary" onclick="updateTable()">
                    <span class="fa fa-filter"></span>
                    <spring:message code="common.filter"/>
                </button>
            </div>
        </div>
        <button class="btn btn-primary" onclick="add()">
            <span class="fa fa-plus"></span>
            <spring:message code="common.add"/>
        </button>
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
            </tbody>
        </table>
    </div>
</div>

<div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="modalTitle"><spring:message code="meal.add"/></h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" id="id" name="id">

                    <div class="form-group">
                        <label for="dateTime" class="col-form-label"><spring:message code="meal.datetime"/></label>
                        <input type="text" class="form-control" id="dateTime" name="dateTime">
                    </div>

                    <div class="form-group">
                        <label for="description" class="col-form-label"><spring:message code="meal.description"/></label>
                        <input type="text" class="form-control" id="description" name="description"
                               placeholder="<spring:message code="meal.description"/>">
                    </div>

                    <div class="form-group">
                        <label for="calories" class="col-form-label"><spring:message code="meal.calories"/></label>
                        <input type="number" class="form-control" id="calories" name="calories" placeholder="1000">
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
<script type="text/javascript">
    <jsp:include page="fragments/i18n.jsp"/>

    i18n["addTitle"] = '<spring:message code="meal.add"/>';
    i18n["editTitle"] = '<spring:message code="meal.edit"/>';
</script>
</html>