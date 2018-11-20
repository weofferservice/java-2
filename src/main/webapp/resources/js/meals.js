const ajaxUrl = "ajax/profile/meals/";
let datatableApi;

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

function initDatetimepickers() {
    const startDate = $('#startDate');
    const endDate = $('#endDate');
    const dateParams = {
        timepicker: false,
        format: 'Y-m-d',
        mask: true
    };
    startDate.datetimepicker(Object.assign(dateParams, {
        onShow: function (ct) {
            this.setOptions({
                maxDate: endDate.val() ? endDate.val() : false
            })
        }
    }));
    endDate.datetimepicker(Object.assign(dateParams, {
        onShow: function (ct) {
            this.setOptions({
                minDate: startDate.val() ? startDate.val() : false
            })
        }
    }));

    $('#startTime, #endTime').datetimepicker({
        datepicker: false,
        format: 'H:i',
        mask: true
    });

    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i',
        mask: true
    });

    $("#filter")[0].reset();
}

$(function () {
    $.ajaxSetup({
        // https://api.jquery.com/jQuery.ajax/#using-converters
        converters: {
            "text json": function (stringData) {
                const json = JSON.parse(stringData);
                $(json).each(function () {
                    this.dateTime = this.dateTime.substr(0, 16).replace('T', ' ');
                });
                return json;
            }
        }
    });

    datatableApi = $("#datatable").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
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
        "initComplete": init
    });

    initDatetimepickers();
});