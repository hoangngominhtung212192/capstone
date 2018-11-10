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

    var idp = getUrlParameter();
    getProposalDetail(idp);
    function getProposalDetail(data){
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/gwa/api/proposal/getProposalByID",
            data : JSON.stringify(data),
            success : function(result, status) {
                if (result){
                    $('#linkCreate').attr("href", "/admin/event/create?id="+result.id+"");
                    $('#txtTitle').val(result.eventTitle);
                    $('#txtLocation').val(result.location);
                    $('#txtDescription').val(result.description);
                    $('#contentEditor').html(result.content);
                }
            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });

    }


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
            url: "http://localhost:8080/gwa/api/event/checkMatchingLocaNtime",
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
            url : "http://localhost:8080/gwa/api/event/createEvent",
            data : JSON.stringify(data),
            success : function(result, status) {
                alert("Event created successfully!");
                window.location.href = "/gwa/event/detail?id=" + result.id;

            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }

})