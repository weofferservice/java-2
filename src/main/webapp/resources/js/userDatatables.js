var ajaxUrl = "ajax/admin/users/";
var datatableApi;

function enable(checkbox) {
    var id = $(checkbox).parents("tr").attr("id");
    var checked = checkbox.checked;
    $.ajax({
        url: ajaxUrl + id + "/enable",
        type: "POST",
        data: "enabled=" + checked,
        success: function () {
            updateTable();
        }
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