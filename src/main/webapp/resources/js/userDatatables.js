const ajaxUrl = "ajax/admin/users/";
let datatableApi;

function updateTable() {
    $.get(ajaxUrl, updateTableByData);
}

function enable(checkbox, id) {
    const checked = checkbox.checked;
    // https://stackoverflow.com/questions/22213495/jquery-post-done-and-success/22213543#22213543
    $.ajax({
        url: ajaxUrl + id,
        type: "POST",
        data: "enabled=" + checked,
    }).done(function () {
        updateTable();
        successNoty(checked ? "common.enabled" : "common.disabled");
    }).fail(function () {
        $(checkbox).prop("checked", !checked);
    });
}

// $(document).ready(function () {
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
                "data": "name"
            },
            {
                "data": "email",
                "render": function (data, type, row) {
                    if (type === "display") {
                        return "<a href='mailto:" + data + "'>" + data + "</a>";
                    }
                    return data;
                }
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled",
                "render": function (data, type, row) {
                    if (type === "display") {
                        return "<input type='checkbox' " + (data ? "checked" : "") + " onclick='enable(this, " + row.id + ")'>";
                    }
                    return data;
                }
            },
            {
                "data": "registered",
                "render": function (date, type, row) {
                    if (type === "display") {
                        return date.substring(0, 10);
                    }
                    return date;
                }
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
                "asc"
            ]
        ],
        "createdRow": function (row, data, dataIndex) {
            if (!data.enabled) {
                $(row).attr("data-userEnabled", false);
            }
        },
        "initComplete": init
    });
});