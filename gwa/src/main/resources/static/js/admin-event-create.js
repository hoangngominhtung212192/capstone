$(document).ready(function() {
    //image
    var checkImage = false;
    var imagetype;
    var imageFile;

    $("#photoBtn").change(function (e) {
        console.log($("#photoBtn").val());

        for (var i = 0; i < e.originalEvent.srcElement.files.length; i++) {

            var file = e.originalEvent.srcElement.files[i];
            imagetype = file.type;
            var match = ["image/jpeg", "image/png", "image/jpg"];
            if (!((imagetype == match[0]) || (imagetype == match[1]) || (imagetype == match[2]))) {
                checkImage = false;
                alert("select img pls");
                // $("#imgError").css("display", "block");
                // $("#imgError").text("Please select image only");
            } else {
                var reader = new FileReader();
                reader.onloadend = function () {
                    $("#imgthumb").attr("src", reader.result);
                    // $("#avatar").css("height", "202px");
                    // $("#avatar").css("width", "202px");
                }
                reader.readAsDataURL(file);

                imageFile = file;

                checkImage = true;

                // $("#imgError").css("display", "none");
                // $("#imgError").text("");
            }

        }
    });

    function ajaxImagePost(formData) {
        console.log("updating image for "+formData)
        $.ajax({
            type: "POST",
            contentType: false,
            processData: false,
            url: "/gwa/api/event/uploadEventImage",
            data: formData,
            success: function (result) {
                console.log(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        })
    }
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
            url : "/gwa/api/proposal/getProposalByID",
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


    var formData = new FormData();
    $("#submitBtn").click(function (event) {
        event.preventDefault();

        var location = $("#txtLocation").val();
        var staDate = $("#txtStartDate").val();
        var endDate = $("#txtEndDate").val();

        var regstaDate = $('#txtRegStartDate').val();
        var regendDate = $('#txtRegEndDate').val();
        var d1 = Date.parse(staDate);
        var d2 = Date.parse(endDate);
        var d3 = Date.parse(regstaDate);
        var d4 = Date.parse(regendDate);
        var valid = true;
        if ($("#txtTitle").val() == ""){
            valid = false;
            $.growl.error({message: "Please enter title"});
        }
        if ($("#txtLocation").val() == ""){
            valid = false;
            $.growl.error({message: "Please enter location"});
        }
        if ($("#txtDescription").val() == ""){
            valid = false;
            $.growl.error({message: "Please enter description"});
        }
        if ($("#txtAttMax").val() == ""){
            valid = false;
            $.growl.error({message: "Please enter max attendee"});
        }
        if ($("#txtAttMin").val() == ""){
            valid = false;
            $.growl.error({message: "Please enter min attendee"});
        }
        if ($("#txtPrice").val() == ""){
            valid = false;
            $.growl.error({message: "Please enter price"});
        }
        if ($('#imgthumb').attr("src") == "#"){
            valid = false;
            $.growl.error({message: "Please select a thumbnail image"});
        }
        var minn = $("#txtAttMin").val();
        var maxx = $("#txtAttMax").val();
        if (minn > maxx){
            console.log("minum is higer than maxx");
            valid = false;
            $.growl.error({message: "Minimum attendee should be lower than maximum attendee"});
        }
        if (d3 < d4 && d4 < d1 && d1 < d2 ){
            console.log("valid");
            if (valid == true){
                checkMatchingEvt(staDate,endDate);
            }
        } else {
            $.growl.error({message: "Input date is invalid!"});
        }
    })
    function checkMatchingEvt(staDate, endDate) {

        var address = $("#txtLocation").val();
        if (address != ""){
            var lat = $('#txtLat').val();
            var long = $('#txtLong').val();
            var addrlocation = address + "@" + lat + "@" + long;

            $.ajax({
                type: "POST",
                url: "/gwa/api/event/checkMatchingLocaNtime",
                data: {
                    location: addrlocation,
                    staDate: staDate,
                    endDate: endDate,
                },

                success:function (result, status) {
                    console.log("result len: "+result.length);
                    if (result.length==0){
                        var formEvent = {
                            title : $("#txtTitle").val(),
                            location : addrlocation,
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
                        $.growl.error({message: "There are events with matching location and time!!"});
                    }
                },
                error:function (e) {
                    console.log("error: ",e);
                }

            })
        } else{
            console.log("location is null");
        }

    }
    function createEvent(data) {
        // formData.append("photoBtn", imageFile, "thumbEvt#"+$('#txtTitle').val() + "." + type);
        console.log(data);
        var valid = true;
        if (valid == true){
            $.ajax({
                type : "POST",
                contentType : "application/json",
                url : "/gwa/api/event/createEvent",
                data : JSON.stringify(data),
                success : function(result, status) {
                    console.log("event created!");
                    var type = imagetype.split("/")[1];
                    formData.append("id", result.id);
                    formData.append("photoBtn", imageFile, "thumbEvtID" +result.id + "." + type);
                    ajaxImagePost(formData);
                    $("#myModal").modal({backdrop: 'static', keyboard: false});
                    $("#success-btn").on("click", function() {
                        window.location.href = "/gwa/event/detail?id="+result.id;
                    });
                    // alert("Event created successfully!");
                    // window.location.href = "/gwa/event/detail?id=" + result.id;

                },
                error : function(e) {
                    alert("Error!")
                    console.log("ERROR: ", e);
                }
            });
        }

    }

    /* Begin authentication & notification */
    authentication();

    var createdDate;
    var account_session_id;

    function authentication() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/user/checkLogin",
            complete: function (xhr, status) {
                if (status == "success") {

                    var xhr_data = xhr.responseText;
                    console.log(xhr_data);
                    var jsonResponse = JSON.parse(xhr_data);

                    var role_session = jsonResponse["role"].name;
                    account_session_id = jsonResponse["id"];

                    if (role_session != "ADMIN") {
                        window.location.href = "/gwa/403";
                    } else {
                        console.log(role_session + " " + jsonResponse["username"] + " is on session!");
                        $("#profileBtn").attr("href", "/gwa/pages/profile.html?accountID=" + jsonResponse["id"]);
                        $("#user-out-avatar").attr("src", jsonResponse["avatar"]);
                        $("#user-in-avatar").attr("src", jsonResponse["avatar"]);
                        $("#left-avatar").attr("src", jsonResponse["avatar"]);

                        createdDate = jsonResponse["createdDate"].split(" ")[0];
                        getSessionProfile(jsonResponse["id"]);
                        ajaxGetAllNotification(jsonResponse["id"]);
                    }

                } else {
                    window.location.href = "/gwa/login";
                }

            }
        });
    }

    // get session account's profile
    function getSessionProfile(id) {

        $.ajax({
            type: "POST",
            url: "/gwa/api/user/profile?accountID=" + id,
            success: function (result) {
                //get selected profile's account status

                var displayUsername = "";

                if (result.middleName) {
                    displayUsername += result.lastName + ' ' + result.middleName + ' ' + result.firstName;
                } else {
                    displayUsername += result.lastName + ' ' + result.firstName;
                }

                $("#user-in-name").text(displayUsername);
                $("#user-out-name").text(displayUsername);
                $("#user-in-name").append("<small>Member since " + createdDate + "</small>");

                $("#left-name").text(displayUsername);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    $("#signoutBtn").click(function (e) {
        e.preventDefault();

        $("#loading").css("display", "block");

        setTimeout(function () {
            $("#loading").css("display", "none");

            ajaxLogout();
        }, 300);
    })

    function ajaxLogout() {
        $.ajax({
            type: "GET",
            url: "/gwa/api/user/logout",
            success: function (result) {
                window.location.href = "/gwa/login";
            }
        });
    }

    /*   This is for notification area   */
    var pageNumber = 1;
    var lastPage;

    function ajaxGetAllNotification(accountID) {

        $.ajax({
            type: "GET",
            url: "/gwa/api/notification/getAll?pageNumber=" + pageNumber + "&accountID=" + accountID,
            success: function (result) {
                console.log(result);

                lastPage = result[0];
                renderNotification(result[1]);
            }
        })
    }

    // scroll to bottom event
    $("#ul-notification").scroll(function () {
        if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
            if (pageNumber < lastPage) {
                pageNumber += 1;

                $("#loading").css("display", "block");

                setTimeout(function () {
                    ajaxGetAllNotification(account_session_id);

                    $("#loading").css("display", "none");
                }, 400);
            }
        }
    });

    var countNotSeen = 0;

    function renderNotification(result) {

        $.each(result, function (index, value) {

            var appendNotification = "";

            if (!value.seen) {
                // not seen yet
                countNotSeen++;
                appendNotification += "<li style='background-color: lightgoldenrodyellow;'>\n"
            } else {
                // already seen
                appendNotification += "<li style='background-color: white;'>\n"
            }

            appendNotification += "<a id='" + value.id + "-" + value.notificationtype.name + "-" + value.objectID +
                "' href=\"#\">\n" +
                "<i class=\"fa fa-warning text-yellow\" style=\"color: darkred;\"></i> " + value.description + "</a>\n" +
                "</li>";

            $("#ul-notification").append(appendNotification);

            // click event
            $("a[id='" + value.id + "-" + value.notificationtype.name + "-" + value.objectID + "'").click(function (e) {
                e.preventDefault();

                // get parameters saved in attribute id of current notification
                var notificationID = $(this).attr('id').split("-")[0];
                var type = $(this).attr('id').split("-")[1];
                var objectID = $(this).attr('id').split("-")[2];

                // log to console
                console.log("Notification ID: " + notificationID);
                console.log("Type: " + type);
                console.log("ObjectID: " + objectID);

                // set seen status to 0 --> means user has seen this current notification
                ajaxUpdateNotificationStatus(notificationID);

                if (type == "Profile") {
                    window.location.href = "/gwa/pages/profile.html?accountID=" + objectID;
                } else if (type == "Model") {
                    window.location.href = "/gwa/pages/modeldetail.html?modelID=" + objectID;
                } else if (type == "Tradepost") {
                    window.location.href = "/gwa/trade-market/view-trade?tradepostId=" + objectID;
                } else if (type == "OrderSent") {
                    window.location.href = "/gwa/trade-market/my-order";
                } else if (type == "OrderReceived") {
                    window.location.href = "/gwa/trade-market/view-trade?tradepostId=" + objectID;
                } else if (type == "Article") {
                    window.location.href = "/gwa/article/detail?id=" + objectID;
                } else if (type == "Event") {
                    window.location.href = "/gwa/event/detail?id=" + objectID;
                }
            });
        });

        if (countNotSeen == 1 || countNotSeen == 0) {
            $("#notice-new").text("You have " + countNotSeen + " new notification");
        } else {
            // plural
            $("#notice-new").text("You have " + countNotSeen + " new notifications");
        }

        if (countNotSeen == 0) {
            $("#numberOfNew").text("");
        } else {
            $("#numberOfNew").text(countNotSeen);
        }

    }

    function addNewNotification() {
        var description = $("#txtReason").val();

        var formNotification = {
            description: description,
            objectID: account_profile_on_page_id,
            account: {
                id: account_profile_on_page_id
            },
            notificationtype: {
                id: 1
            }
        }

        ajaxPostNewNotification(formNotification);
    }

    function ajaxPostNewNotification(data) {

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/gwa/api/notification/addNew",
            data: JSON.stringify(data),
            success: function (result) {
                console.log(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function ajaxUpdateNotificationStatus(notificationID) {

        $.ajax({
            type: "POST",
            url: "/gwa/api/notification/update?notificationID=" + notificationID,
            success: function (result) {
                console.log(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }
    /* End notification */
    /* End authentication & notification */

})