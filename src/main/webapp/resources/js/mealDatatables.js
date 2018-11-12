const ajaxUrl = "ajax/profile/meals/";
let datatableApi;

function correctDatePart(datePart) {
    return datePart < 10 ? "0" + datePart : datePart;
}

function updateTable() {
    $.ajax({
        url: ajaxUrl + "filter",
        type: "GET",
        data: $("#filter").serialize(),
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
                        const date = new Date(data);
                        const year = date.getFullYear();
                        const month = correctDatePart(date.getMonth() + 1);
                        const day = correctDatePart(date.getDate());
                        const hours = correctDatePart(date.getHours());
                        const minutes = correctDatePart(date.getMinutes());
                        return year + "-" + month + "-" + day + " " + hours + ":" + minutes;
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
        },
        "initComplete": makeEditable
    });
});