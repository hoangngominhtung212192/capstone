$(document).ready(function() {

    $("#submitBtn").click(function (event) {
        event.preventDefault();

        var location = $("#txtLocation").val();
        var staDate = $("#txtStartDate").val();
        var endDate = $("#txtEndDate").val();

        checkMatchingEvt(location,staDate,endDate);

    })
    function checkMatchingEvt(location, staDate, endDate) {
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/event/checkMatchingLocaNtime",
            data: {
                location: location,
                staDate: staDate,
                endDate: endDate,
            },

            success:function (result, status) {
                console.log("result len: "+result.length);
                if (result.length==0){
                    var formEvent = {
                        title : $("#txtTitle").val(),
                        location : $("#txtLocation").val(),
                        startDate : $("#txtStartDate").val(),
                        endDate : $("#txtEndDate").val(),
                        maxAttendee : $("#txtAttMax").val(),
                        minAttendee : $("#txtAttMin").val(),
                        ticketPrice : $("#txtPrice").val(),
                        content : CKEDITOR.instances.contentEditor.getData(),
                        status : $("#cboStatus").val(),

                    }

                    createEvent(formEvent);
                } else{
                    alert("There are events with matching location and time!!");
                }
            },
            error:function (e) {
                alert("err");
                console.log("error: ",e);
            }

        })
    }
    function createEvent(data) {
        console.log(data);

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/api/event/createEvent",
            data : JSON.stringify(data),
            success : function(result, status) {
                alert("Event created successfully!");
                window.location.href = "detail?id=" + result.id;

            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }
})