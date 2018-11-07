function makeEditable() {
    $(".delete").click(function () {
        deleteRow($(this).parents("tr").attr("id"));
    });

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/questions/4303829/how-to-prevent-a-jquery-ajax-request-from-caching-in-internet-explorer/4303862#4303862
    $.ajaxSetup({cache: false});
}

function add() {
    $("#detailsForm").find(":input").val("");
    $("#editRow").modal();
}

function deleteRow(id) {
    $.ajax({
        url: ajaxUrl + id,
        type: "DELETE",
        success: function () {
            updateTable();
            successNoty("Deleted");
        }
    });
}

function reloadTable(data) {
    datatableApi.clear().rows.add(data).draw();
}

function updateTable() {
    $.get(ajaxUrl, reloadTable);
}

function save() {
    var form = $("#detailsForm");
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: form.serialize(),
        success: function () {
            $("#editRow").modal("hide");
            updateTable();
            successNoty("Saved");
        }
    });
}

var failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(text) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + text,
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;Error status: " + jqXHR.status,
        type: "error",
        layout: "bottomRight"
    }).show();
}