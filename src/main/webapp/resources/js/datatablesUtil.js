let form;

function padStartDatePart(datePart) {
    return datePart < 10 ? "0" + datePart : datePart;
}

function reformatDateTimeFromISO(dateTimeString) {
    const date = new Date(dateTimeString);
    const year = date.getFullYear();
    const month = padStartDatePart(date.getMonth() + 1);
    const day = padStartDatePart(date.getDate());
    const hours = padStartDatePart(date.getHours());
    const minutes = padStartDatePart(date.getMinutes());
    return day + "." + month + "." + year + " " + hours + ":" + minutes;
}

function makeEditable() {
    form = $('#detailsForm');

    $(document).ajaxError(function (event, jqXHR, options, jsExc) {
        failNoty(jqXHR);
    });

    // solve problem with cache in IE: https://stackoverflow.com/questions/4303829/how-to-prevent-a-jquery-ajax-request-from-caching-in-internet-explorer/4303862#4303862
    $.ajaxSetup({
        cache: false,
        converters: {
            "text json": function (text) {
                const json = JSON.parse(text);
                if (json.dateTime) {
                    json.dateTime = reformatDateTimeFromISO(json.dateTime);
                }
                return json;
            }
        }
    });
}

function add() {
    $("#modalTitle").html(i18n["addTitle"]);
    form.find(":input").val("");
    $("#editRow").modal();
}

function updateRow(id) {
    $("#modalTitle").html(i18n["editTitle"]);
    $.get(ajaxUrl + id, function (data) {
        $.each(data, function (key, value) {
            form.find("input[name='" + key + "']").val(value);
        });
        $('#editRow').modal();
    });
}

function deleteRow(id) {
    $.ajax({
        url: ajaxUrl + id,
        type: "DELETE"
    }).done(function () {
        updateTable();
        successNoty("common.deleted");
    });
}

function updateTableByData(data) {
    datatableApi.clear().rows.add(data).draw();
}

function save() {
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        updateTable();
        successNoty("common.saved");
    });
}

let failedNote;

function closeNoty() {
    if (failedNote) {
        failedNote.close();
        failedNote = undefined;
    }
}

function successNoty(key) {
    closeNoty();
    new Noty({
        text: "<span class='fa fa-lg fa-check'></span> &nbsp;" + i18n[key],
        type: 'success',
        layout: "bottomRight",
        timeout: 1000
    }).show();
}

function failNoty(jqXHR) {
    closeNoty();
    failedNote = new Noty({
        text: "<span class='fa fa-lg fa-exclamation-circle'></span> &nbsp;" + i18n["common.errorStatus"] + ": " +
                                jqXHR.status + (jqXHR.responseJSON ? "<br>" + jqXHR.responseJSON : ""),
        type: "error",
        layout: "bottomRight"
    }).show();
}

function renderEditBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='updateRow(" + row.id + ")'><span class='fa fa-pencil'></span></a>";
    }
}

function renderDeleteBtn(data, type, row) {
    if (type === "display") {
        return "<a onclick='deleteRow(" + row.id + ")'><span class='fa fa-remove'></span></a>";
    }
}