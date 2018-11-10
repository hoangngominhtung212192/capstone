$(document).ready(function() {
    var getUrlParameter = function getUrlParameter() {
        var sPageURL = window.location.href;
        console.log(sPageURL);
        var url = new URL(sPageURL);
        var c = url.searchParams.get("id");
        console.log(c);
        arId = parseInt(c);
        return arId;
    }

    var attendeeAmount;
    var numberOfRating;
    var numberOfStars;

    var id = getUrlParameter();
    getEventDetail(id);
    function getEventDetail(data){
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/gwa/api/event/getEvent",
            data : JSON.stringify(data),
            success : function(result, status) {
                if (result){
                    attendeeAmount = result.numberOfAttendee;
                    numberOfRating = result.numberOfRating;
                    numberOfStars = result.numberOfStars;
                    $('#hiddenEvID').val(result.id);
                    $('#txtTitle').val(result.title);
                    $('#txtLocation').val(result.location);
                    $('#txtDescription').val(result.description);
                    $('#txtStartDate').val(result.startDate);
                    $('#txtEndDate').val(result.endDate);
                    $('#txtRegStartDate').val(result.regDateStart);
                    $('#txtRegEndDate').val(result.regDateEnd);
                    $('#txtAttMax').val(result.maxAttendee);
                    $('#txtAttMin').val(result.minAttendee);
                    $('#txtPrice').val(result.ticketPrice);
                    $('#contentEditor').html(result.content);
                    var evStatus = result.status;
                    $("#cboStatus").val(result.status);

                    
                    
                    // title : $("#txtTitle").val(),
                    //     location : $("#txtLocation").val(),
                    //     description : $("#txtDescription").val(),
                    //     startDate : $("#txtStartDate").val(),
                    //     endDate : $("#txtEndDate").val(),
                    //     regDateStart : $("#txtRegStartDate").val(),
                    //     regDateEnd : $("#txtRegEndDate").val(),
                    //     maxAttendee : $("#txtAttMax").val(),
                    //     minAttendee : $("#txtAttMin").val(),
                    //     ticketPrice : $("#txtPrice").val(),
                    //     content : CKEDITOR.instances.contentEditor.getData(),
                    //     status : $("#cboStatus").val(),
                }
            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });

    }


    $("#btnSubmit").click(function (event) {
        event.preventDefault();

        var location = $("#txtLocation").val();
        var staDate = $("#txtStartDate").val();
        var endDate = $("#txtEndDate").val();

        checkMatchingEvt(location,staDate,endDate);

    })
    function checkMatchingEvt(location, staDate, endDate) {
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/gwa/api/event/checkMatchingLocaNtimeExcept",
            data: {
                id: id,
                location: location,
                staDate: staDate,
                endDate: endDate,
            },

            success:function (result, status) {
                console.log("result len: "+result.length);
                if (result.length==0){
                    var formEvent = {
                        // $('#hiddenEvID').val(result.id);
                        id : $("#hiddenEvID").val(),
                        title : $("#txtTitle").val(),
                        location : $("#txtLocation").val(),
                        description : $("#txtDescription").val(),
                        startDate : $("#txtStartDate").val(),
                        endDate : $("#txtEndDate").val(),
                        regDateStart : $("#txtRegStartDate").val(),
                        regDateEnd : $("#txtRegEndDate").val(),
                        maxAttendee : $("#txtAttMax").val(),
                        minAttendee : $("#txtAttMin").val(),
                        ticketPrice : $("#txtPrice").val(),
                        content : CKEDITOR.instances.contentEditor.getData(),
                        status : $("#cboStatus").val(),

                    }

                    editEvent(formEvent);
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
    function editEvent(data) {
        console.log(data);

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/gwa/api/event/editEvent",
            data : JSON.stringify(data),
            success : function(result, status) {
                alert("Event updated successfully!");
                window.location.href = "/gwa/event/detail?id=" + result.id;

            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }

})