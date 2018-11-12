const ajaxUrl = "ajax/profile/meals/";
let datatableApi;

function reformatDateTimeToISO(dateTimeString) {
    if (dateTimeString === "") {
        return "";
    }
    const dateTimeParts = dateTimeString.split(" ");
    const date = reformatDateToISO(dateTimeParts[0]);
    const time = dateTimeParts[1];
    return date + "T" + time;
}

function reformatDateToISO(dateString) {
    if (dateString === "") {
        return "";
    }
    const dateParts = dateString.split(".");
    const year = dateParts[2];
    const month = dateParts[1];
    const day = dateParts[0];
    return year + "-" + month + "-" + day;
}

function serializeFilterData() {
    const startDate = reformatDateToISO($("#startDate").val());
    const endDate = reformatDateToISO($("#endDate").val());
    const startTime = $("#startTime").val();
    const endTime = $("#endTime").val();
    return "startDate=" + startDate + "&endDate=" + endDate + "&startTime=" + startTime + "&endTime=" + endTime;
}

function updateTable() {
    $.ajax({
        url: ajaxUrl + "filter",
        type: "GET",
        data: serializeFilterData(),
        success: updateTableByData
    });
}

function resetFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

$(function () {
    datatableApi = $("#datatable").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime",
                "render": function (data, type, row) {
                    if (type === "display") {
                        return reformatDateTimeFromISO(data, true);
                    }
                    return data;
                }
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "",
                "orderable": false,
                "render": renderEditBtn
            },
            {
                "defaultContent": "",
                "orderable": false,
                "render": renderDeleteBtn
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ],
        "createdRow": function (row, data, dataIndex) {
            $(row).attr("data-mealExceed", data.exceed);
        }
    });

    makeEditable();

    form.serialize = function () {
        const id = $("#id").val();
        const dateTime = reformatDateTimeToISO($("#dateTime").val());
        const description = $("#description").val();
        const calories = $("#calories").val();
        return "id=" + id + "&dateTime=" + dateTime + "&description=" + description + "&calories=" + calories;
    };
});