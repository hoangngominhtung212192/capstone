$(document).ready(function () {
    var getUrlParameter = function getUrlParameter() {
        var sPageURL = window.location.href;
        console.log(sPageURL);
        var url = new URL(sPageURL);
        var c = url.searchParams.get("id");
        console.log(c);
        arId = parseInt(c);
        return arId;
    }

    var propid = getUrlParameter();
    getProposalDetail(propid);
    function getProposalDetail(data){
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/gwa/api/proposal/getProposalByID",
            data : JSON.stringify(data),
            success : function(result, status) {
                if (result){
                    $('#linkCreate').attr("href", "/gwa/admin/event/create?id="+result.id+"");
                    console.log("title: "+result.eventTitle);
                    $('#txtTitle').append(result.eventTitle);
                    $('#txtPrice').append(result.ticketPrice);
                    $('#txtDate').append(result.startDate);
                    $('#txtLocation').append(result.location);
                    $('#txtDescription').append(result.description);
                    $('#txtPrize').append(result.prize);
                    $('#content').html(result.content);
                }
            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });

    }

})