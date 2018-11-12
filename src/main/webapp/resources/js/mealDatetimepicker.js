const timepicker = {
    datepicker: false,
    format: "H:i",
    mask: true
};

const datepicker = {
    timepicker: false,
    format: "d.m.Y",
    mask: true
};

$(function () {
    $("#startDate").datetimepicker(datepicker);
    $("#endDate").datetimepicker(datepicker);
    $("#startTime").datetimepicker(timepicker);
    $("#endTime").datetimepicker(timepicker);
    $("#dateTime").datetimepicker({
        format: "d.m.Y H:i",
        mask: true
    });

    $("#filter")[0].reset();
});