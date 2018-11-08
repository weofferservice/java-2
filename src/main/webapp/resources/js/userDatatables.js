const ajaxUrl = "ajax/admin/users/";
let datatableApi;

function enable(checkbox) {
    const id = $(checkbox).parents("tr").attr("id");
    const checked = checkbox.checked;
    $.ajax({
        url: ajaxUrl + id + "/enable",
        type: "POST",
        data: "enabled=" + checked,
    }).done(function () {
        updateTable();
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