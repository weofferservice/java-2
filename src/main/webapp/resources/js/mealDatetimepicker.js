$(function () {
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
});