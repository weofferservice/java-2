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
        $(checkbox).closest("tr").attr("data-userEnabled", checked);
        successNoty(checked ? "Enabled" : "Disabled");
    }).fail(function () {
        $(checkbox).prop("checked", !checked);
    });
}

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
});