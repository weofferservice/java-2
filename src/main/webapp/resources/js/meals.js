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
    $.datetimepicker.setLocale(localeCode);

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

    const startTime = $('#startTime');
    const endTime = $('#endTime');
    const timeParams = {
        datepicker: false,
        format: 'H:i',
        mask: true
    };
    startTime.datetimepicker(Object.assign(timeParams, {
        onShow: function (ct) {
            this.setOptions({
                maxTime: endTime.val() ? endTime.val() : false
            })
        }
    }));
    endTime.datetimepicker(Object.assign(timeParams, {
        onShow: function (ct) {
            this.setOptions({
                minTime: startTime.val() ? startTime.val() : false
            })
        }
    }));

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

    datatableApi = $("#datatable").DataTable(extendDataTableOpts({
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
        }
    }));

    initDatetimepickers();
});