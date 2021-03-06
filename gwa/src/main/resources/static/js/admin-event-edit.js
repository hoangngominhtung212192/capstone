$(document).ready(function() {
    var currentPage = 1;
    var totalPage = 1;
    var $pagination = $("#pagination-event");
    var isSearch = false;
    var defaultPaginationOpts = {
        totalPages: totalPage,
// the current page that show on start
        startPage: 1,

// maximum visible pages
        visiblePages: 3,

        initiateStartPageClick: false,

// template for pagination links
        href: false,

// variable name in href template for page number
        hrefVariable: '{{number}}',

// Text labels
        first: '&laquo;',
        prev: '&lsaquo;',
        next: '&rsaquo;',
        last: '&raquo;',

// carousel-style pagination
        loop: false,

// callback function
        onPageClick: function (event, page) {
            currentPage = page;
            $('#search-result').html("");
            searchUser();
        },

// pagination Classes
        paginationClass: 'pagination',
        nextClass: 'page-item',
        prevClass: 'page-item',
        lastClass: 'page-item',
        firstClass: 'page-item',
        pageClass: 'page-item',
        activeClass: 'active',
        disabledClass: 'disabled'
    };

    // getEventData();
    $pagination.twbsPagination('destroy');
    if (totalPage > 1){
        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
            totalPages: totalPage
        }));
    }
    // searchEv();
    $("#btnSearch").click(function (event) {
        currentPage = 1;
        event.preventDefault();

        isSearch = true;
        searchUser();
        // testSchedule();

    });

    function appendResult(result){
        var searchDiv = document.getElementById("tblBody");
        while (searchDiv.firstChild) {
            searchDiv.removeChild(searchDiv.firstChild);
        }

        for (var i = 0; i < result.length; i++) {
            var row = $('<tr>\n' +
                '                    <td>' + result[i].account.username + '</td>\n' +
                '                    <td>' + result[i].date + '</td>\n' + '</tr>')
            // $('#tblBody').append(row);
            $('#tblBody').append(row);
        }
        $pagination.twbsPagination('destroy');
        if (totalPage > 1){
            $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                totalPages: totalPage,
                currentPage: currentPage,
                startPage: currentPage,

            }));
        }
    }

    function searchUser() {
        var searchValue = $("#txtSearch").val();

        $.ajax({
            type : "POST",
            url : "/gwa/api/event/searchAttendeeInEvent",
            data : {
                eventid : id,
                username : searchValue,
                pageNum : currentPage
            },
            async: false,
            success : function(result, status) {
                var data = result[1];
                totalPage = result[0];
                console.log(result);
                console.log(status);
                console.log("page num: "+currentPage);
                console.log("seach numb of pages: "+result[0]);
                // currentPage = 1;

                appendResult(data);
            },
            error : function(e) {
                $.growl.error({message: "No username found"});
                console.log("ERROR: ", e);
            }
        });
    }

    var checkImage = false;
    var imagetype;
    var imageFile;
    var formData = new FormData();
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
                }
                reader.readAsDataURL(file);

                imageFile = file;

                checkImage = true;

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

    var attendeeAmount;
    var numberOfRating;
    var numberOfStars;

    var id = getUrlParameter();
    getEventDetail(id);
    function getEventDetail(data){
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/gwa/api/event/getEvent",
            data : JSON.stringify(data),
            success : function(result, status) {
                if (result){
                    attendeeAmount = result.numberOfAttendee;
                    numberOfRating = result.numberOfRating;
                    numberOfStars = result.numberOfStars;
                    $('#hiddenEvID').val(result.id);
                    $('#txtTitle').val(result.title);
                    var loca = result.location;
                    var addr = loca.split("@")[0];
                    var latt = loca.split("@")[1];
                    var longg = loca.split("@")[2];
                    // $('#txtLocation').append(addr);
                    $('#txtNumberAtt').html("This event has "+ result.numberOfAttendee +" attendees.");
                    $('#txtLocation').val(addr);
                    $('#txtLat').val(latt);
                    $('#txtLong').val(longg);
                    $("#imgthumb").attr("src", result.thumbImage);
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
        console.log(minn +"and"+ maxx);
        if (parseInt(minn) <= 0){
            valid = false;
            $.growl.error({message: "Attendee number should be positive"});
        }
        if (parseInt(minn) > parseInt(maxx)){
            console.log("minum is higer than maxx");
            valid = false;
            $.growl.error({message: "Minimum attendee should be lower than maximum attendee"});
        }
        if (d3 < d4 && d4 < d1 && d1 < d2 ){
            console.log("valid");
            if (valid == true) {
                checkMatchingEvt(staDate, endDate);
            }
        } else {
            $.growl.error({message: "Input date is invalid!"});
        }

    })
    function checkMatchingEvt(staDate, endDate) {
        var address = $("#txtLocation").val();
        var lat = $('#txtLat').val();
        var long = $('#txtLong').val();
        var addrlocation = address + "@" + lat + "@" + long;

        $.ajax({
            type: "POST",
            url: "/gwa/api/event/checkMatchingLocaNtimeExcept",
            data: {
                id: id,
                location: addrlocation,
                staDate: staDate,
                endDate: endDate,
            },

            success:function (result, status) {
                // console.log(": "+result.length);

                if (result.length==0){
                    var curImage ;
                    if (checkImage==false){
                        curImage = $('#imgthumb').attr('src');
                        console.log("current thum: "+curImage);
                    }
                    var formEvent = {
                        // $('#hiddenEvID').val(result.id);
                        id : $("#hiddenEvID").val(),
                        title : $("#txtTitle").val(),
                        location : addrlocation,
                        description : $("#txtDescription").val(),
                        thumbImage : curImage,
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
                    $.growl.error({message: "There are events with matching location and time!!"});
                }
            },
            error:function (e) {
                // alert("err");
                console.log("error: ",e);
            }

        })
    }
    function editEvent(data) {
        console.log("editing ev")
        console.log(data);
        var valid = true;
        if (valid == true){

            $.ajax({
                type : "POST",
                contentType : "application/json",
                url : "/gwa/api/event/editEvent",
                data : JSON.stringify(data),
                success : function(result, status) {

                    if(checkImage){
                        var type = imagetype.split("/")[1];
                        formData.append("id", result.id);
                        formData.append("photoBtn", imageFile, "thumbEvtID" +result.id + "." + type);
                        ajaxImagePost(formData);
                    }
                    $("#myModal").modal({backdrop: 'static', keyboard: false});
                    $("#success-btn").on("click", function() {
                        window.location.href = "/gwa/event/detail?id="+result.id;
                    });

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

                lastPage = result.lastPage;
                renderNotification(result.notificationList, result.notSeen);
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

    function renderNotification(result, countNotSeen) {

        $.each(result, function (index, value) {

            var appendNotification = "";

            if (!value.seen) {
                // not seen yet
                appendNotification += "<li>\n"
            } else {
                // already seen
                appendNotification += "<li style='background-color: white;'>\n"
            }

            var iconType = "<i class=\"fa fa-warning text-yellow\" style=\"color: darkred;\"></i> ";

            if (value.notificationtype.name == "Profile") {
                iconType = "<i class=\"fa fa-user-circle-o text-yellow\" style=\"color: darkred;\"></i> ";
            } else if (value.notificationtype.name == "Model") {
                iconType = "<i class=\"fa fa-warning text-yellow\" style=\"color: darkred;\"></i> ";
            } else if (value.notificationtype.name == "Tradepost") {
                iconType = "<i class=\"fa fa-check-square-o text-yellow\" style=\"color: darkred;\"></i> ";
            } else if (value.notificationtype.name == "OrderSent") {
                iconType = "<i class=\"fa fa fa-paper-plane text-yellow\" style=\"color: darkred;\"></i> ";
            } else if (value.notificationtype.name == "OrderReceived") {
                iconType = "<i class=\"fa fa fa-bullhorn text-yellow\" style=\"color: darkred;\"></i> ";
            }

            appendNotification += "<a id='" + value.id + "-" + value.notificationtype.name + "-" + value.objectID +
                "' href=\"#\">\n" +
                iconType + value.description + "</a>\n" +
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

    /*  This is for firebase area */
    var config = {
        apiKey: "AIzaSyCACMwhbLcmYliWyHJgfkd8IW6oPUoupIM",
        authDomain: "gunplaworld-51eee.firebaseapp.com",
        databaseURL: "https://gunplaworld-51eee.firebaseio.com",
        projectId: "gunplaworld-51eee",
        storageBucket: "gunplaworld-51eee.appspot.com",
        messagingSenderId: "22850579681"
    };

    firebase.initializeApp(config);

    var messaging = firebase.messaging();

    navigator.serviceWorker.register("/gwa/pages/firebase-messaging-sw.js", {
        scope: "/gwa/pages/"
    }).then(function (registration) {
        messaging.useServiceWorker(registration);

        messaging.requestPermission()
            .then(function (value) {
                console.log("Have permission!");
            }).catch(function (err) {
            console.log("Error occur!", err);
        })

        messaging.onMessage(function (payload) {
            console.log('onMessage: ', payload);

            if (payload.data.accountID == account_session_id) {
                pageNumber = 1;
                $("#ul-notification").empty();
                if (payload.data.title == "Model" || payload.data.title == "Event") {
                    toastr.error(payload.data.body, payload.data.title + " Notification", {timeOut: 5000});
                } else {
                    toastr.info(payload.data.body, payload.data.title + " Notification", {timeOut: 5000});
                }
                setTimeout(function () {
                    ajaxGetAllNotification(account_session_id);
                }, 1000);
            }
        })
    })
    /* This is end of firebase  */
})