$(document).ready(function () {
    var currentPage = 1;
    var totalPage = 1;
    var $pagination = $("#pagination-detail");
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
        prev: '❮',
        next: '❯',
        last: '&raquo;',

// carousel-style pagination
        loop: false,

// callback function
        onPageClick: function (event, page) {
            console.log("clicked on page: "+page)
            // $('#raterArea').html("");
            currentPage = page;
            getRatedAttendeeEvent(id);
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

    $pagination.twbsPagination('destroy');
    if (totalPage > 1){
        document.getElementById('paginationDiv').style.display = 'block';
        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
            totalPages: totalPage
        }));
    }



    authentication();
    var username;
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1; //January is 0!
    var yyyy = today.getFullYear();

    if(dd<10) {
        dd = '0'+dd
    }
    if(mm<10) {
        mm = '0'+mm
    }
    today = yyyy + '-' + mm + '-' + dd;
    console.log("today is "+today);

    var curEvnId;
    var role_session;
    var account_session_id;
    var username_session;


    //check if user is logged in, redirect if not
    function checkAuth() {
        $.ajax({
            type: "GET",
            url: "/gwa/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {
                    $("#confi-modal").modal('show');
                } else {
                    // $.growl.notice({message: "Redirecting ro"});
                    // alert("You will be redirected to the login page!")
                    console.log("Guest is accessing !");
                    window.location.href = "/gwa/login";
                }

            }
        });
    }

    //check if user is attending this event
    var curAttendeeID
    var eventStartDate;
    function checkCurUserIsAttendee() {
        var curEvent = id;
        var curUser = account_session_id;

        console.log(curEvent +" and "+curUser);
        $.ajax({
            type: "POST",
            url: "/gwa/api/event/getAttendeeInEvent",
            data :{
              eventid: curEvent,
              userid: account_session_id,
            },
            success: function (result, status) {
                if (Date.parse(today) >= eventStartDate){
                    console.log("today is withing starting days");
                    document.getElementById('feedbackArea').style.display = 'block';
                    if (result.rating>0){
                        myRating.setRating(result.rating);
                        myRating.set
                        $('#txtFeedback').append(result.feedback);
                        $('#txtFeedback').prop('disabled', true);
                        $('#btnSubmitFeedback').hide();
                        $('#lblFeedback1').text("Your rating: ");
                        $('#lblFeedback2').text("Your feedback");

                    }
                }
                $('#btnRegister').hide();
                document.getElementById('userAttendInfo').style.display = 'block';
                $('#lblRegisUsername').append(result.account.username);
                $('#lblRegisTicket').append(result.amount);
                $('#lblRegisDate').append(result.date);
                curAttendeeID = result.id;
            },
            error : function(e) {

                console.log("not an attendee: ", e);
            }
        });
    }
    // split url to get parameter algorithm
    var getUrlParameter = function getUrlParameter() {
        var sPageURL = window.location.href;
        console.log(sPageURL);
        var url = new URL(sPageURL);
        var c = url.searchParams.get("id");
        console.log(c);
        arId = parseInt(c);
        return arId;
    }

    var id = getUrlParameter();


    $("#btnSubmitFeedback").on("click", function () {
        var curEvent = id;
        var curAtt = curAttendeeID;

        console.log("feddback: "+$('textarea#txtFeedback').val());
        console.log("curAttendee: "+curAtt);
        $.ajax({
            type: "POST",
            url: "/gwa/api/event/feedbackEvent",
            data :{
                eventid: curEvent,
                attendeeid: curAtt,
                rating: parseInt(myRating.getRating()),
                feedback: $('textarea#txtFeedback').val(),
                ratingDate : today.toString(),
            },
            success: function (result, status) {
                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#lblModalMessage").html("Feedback sent! Thank you for your time!");
                $("#success-btn").on("click", function() {
                    window.location.href = "/gwa/event/detail?id="+result.id;
                });
            },
            error : function(e) {
                alert("feedback failed");

                console.log( e);
            }
        });
    });
    $("#btnRegister").on("click", function () {
        checkAuth();
    });
    getRatedAttendeeEvent(id);
    function getRatedAttendeeEvent(id) {
        // var displayDiv = document.getElementById("FeedbackList");
        console.log("searchin rater by id: "+id);
        $.ajax({
            type : "POST",
            url : "/gwa/api/event/getListAttendeeInEvent",
            data : {
                eventid : id,
                pageNum : currentPage
            },
            success : function(result, status) {
                var data = result[1];
                totalPage = result[0];

                console.log("found evn raters:" +result[0]);
                $('#raterArea').html("");
                var feedb = $('<h4>Feedbacks from the attendees:</h4>');
                $('#raterArea').append(feedb);
                if (data.length > 0){
                    document.getElementById('raterArea').style.display = 'block';
                    for (var i = 0; i < data.length ; i++) {
                        var arater = $('<div class="panel">\n' +
                            '                    <h6 id="raterName" >'+data[i].account.username+'<span> | ' + data[i].ratingDate+'</span></h6>\n' +
                            '                    <div id="raterNo'+data[i].id+'"></div>\n' +
                            '                    <p id="raterFeedback">'+data[i].feedback+'</p>\n' +
                            '                </div>' +
                            '<hr/>');

                        $('#raterArea').append(arater);
                        var raterRating = raterJs( {
                            element:document.querySelector("#raterNo"+data[i].id),
                            rateCallback:function rateCallback(rating, done) {
                                // this.setRating(rating);
                                done();
                            },
                            showToolTip: false,
                            readOnly: true
                        });
                        raterRating.setRating(data[i].rating);
                    }

                }
                console.log("totalo "+totalPage);
                $pagination.twbsPagination('destroy');
                if (totalPage > 1){
                    document.getElementById('paginationDiv').style.display = 'block';
                    console.log("totalp more than 1");
                    $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                        totalPages: totalPage,
                        currentPage: currentPage,
                        startPage: currentPage

                    }));
                }
            },
            error : function(e) {

                console.log("ERROR: ", e);
            }
        });
    }
    getEvent(id);
    function getEvent(data) {
        var displayDiv = document.getElementById("search-result");

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/gwa/api/event/getEvent",
            data : JSON.stringify(data),

            success : function(result, status) {

                if (result){
                    var title = result.title;
                    console.log(result.maxAttendee);
                    console.log(result.numberOfAttendee);
                    var remaininT = parseInt(result.maxAttendee) - parseInt(result.numberOfAttendee);
                    if(remaininT == 0){
                        document.getElementById('btnRegister').style.display = 'none';
                    }
                    $('#txtMax').append(result.maxAttendee);

                    console.log('tikets: ' + remaininT + typeof remaininT);
                    $('#regStDate').append(result.regDateStart);
                    eventStartDate = Date.parse(result.startDate);
                    console.log(eventStartDate);
                    $('#regEnDate').append(result.regDateEnd);
                    $('#txtMin').append(result.minAttendee);
                    // alert(result.minAttendee)
                    var resRegStart = Date.parse(result.regDateStart);
                    var resRegEnd = Date.parse(result.regDateEnd);
                    var tttoday = Date.parse(today);
                    if (tttoday <= resRegEnd && tttoday >= resRegStart) {
                        console.log("today is within reg date");
                    } else{
                        console.log("today is not within reg date");
                        document.getElementById('btnRegister').style.display = 'none';
                    }
                    $('#stDate').append(result.startDate);
                    $('#enDate').append(result.endDate);
                    var totalStars = result.numberOfStars;
                    var timeRated = result.numberOfRating;

                    if (timeRated>0){
                        var stars = totalStars/timeRated;
                        var rounded = Math.round(stars * 10)/10;
                        console.log("have rating");
                        document.getElementById('ratingDiv').style.display = 'block';
                        $('#txtCurRat').append(rounded);
                        evRating.setRating(rounded);
                    } else {
                        console.log("dont have rating");
                        document.getElementById('ratingDiv').style.display = 'none';
                        // $('#ratingDiv').hide();
                    }
                    console.log("Stars: "+rounded);
                    if (result.status == "Inactive") {
                        $('#lblEvStatus').html("This event was cancelled");
                        document.getElementById('btnRegister').style.display = 'none';
                    }
                    if (result.status == "Finished") {
                        document.getElementById('btnRegister').style.display = 'none';
                    }

                    $('#hidID').val(result.id);
                    curEvnId = result.id;
                    console.log("ev id is "+curEvnId);
                    // console.log("logged name "+loggedName);
                    if (account_session_id != null){
                        $('#lblUsername').append(loggedName);
                    }

                    $('#txtPrice').append(result.ticketPrice);
                    $('#lblTimeRated').append(result.numberOfRating);
                    $('#lblTodayDate').append(today.toString());
                    var loca = result.location;
                    var addr = loca.split("@")[0];
                    $('#txtLocation').append(addr);
                    $('#lblRemaining').append(remaininT);
                    $('#txtNumOfAttendee').append(result.numberOfAttendee);
                    $('#title').append(title);
                    $('#content').html(result.content);
                }
            },
            error : function(e) {
                alert("Error! Something went wrong!")
                console.log("ERROR: ", e);
            }
        });
    }
    var modalConfirm = function(callback) {
        $("#btnSubmitRegister").on("click", function(){
            callback(true);
            $("#confi-modal").modal('hide');
        });

    };


    $("#btnSubmitRegister").click(function (event) {
        event.preventDefault();

        var eventid = $("#hidID").val();
        var amount = $('#txtNum').val()

        var formArticle = {
            eventit : parseInt(eventid),
            userid : account_session_id,
            amount : amount,
        }
        $.ajax({
            type : "POST",
            url : "/gwa/api/event/getRemainingSlots",
            data: {
                eventid : eventid
            },
            success : function(result, status) {

                console.log('rmnslots: '+result);
                var iamount = parseInt(amount);
                if (amount>0){
                    if (result<amount){
                        $.growl.error({message: "There aren't enough tickets! Please change the number of tickets you want!"});
                    } else{
                        registerAtt();
                    }
                } else {
                    $.growl.error({message: "Please enter a positive number"});
                }

            },
            error : function(e) {
                // alert("Error!")
                console.log("ERROR: ", e);
            }
        });

        // registerAtt(formArticle);
    })


    function registerAtt() {

            $.ajax({
                type : "POST",
                url : "/gwa/api/event/registerEvent",
                data: {
                    eventid : parseInt($("#hidID").val()),
                    userid : account_session_id,
                    amount : parseInt($("#txtNum").val()),
                    date : today.toString()
                },
                success : function(result, status) {
                    $("#confi-modal").modal('hide');
                    $("#lblModalMessage").html("Registered successfully!");
                    $("#myModal").modal({backdrop: 'static', keyboard: false});
                    $("#success-btn").on("click", function() {
                        location.reload(true);
                    });
                },
                error : function(e) {
                    $.growl.error({message: "Register failed!"});
                    console.log("ERROR: ", e);
                }
            });
    }

    //get rating
    var myRating = raterJs( {
        element:document.querySelector("#rater"),
        rateCallback:function rateCallback(rating, done) {
            this.setRating(rating);
            done();
        }
    });

    var evRating = raterJs( {
        element:document.querySelector("#raterEv"),
        rateCallback:function rateCallback(rating, done) {
            // this.setRating(rating);
            done();

        },
        showToolTip: false,
        readOnly: true
    });
    $(document).click(function (event) {
        $("#dropdown-profile").css("display", "none");
        $("#dropdown-notification").css("display", "none");
    })




    function authentication() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {

                    // username click
                    usernameClick();

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);

                    username = jsonResponse["username"];
                    loggedName = jsonResponse["username"];
                    var thumbAvatar = jsonResponse["avatar"];
                    console.log(jsonResponse["role"].name + " " + username + " is on session!");
                    $("#membersince").text("Member since " + jsonResponse["createdDate"].split(" ")[0]);

                    // click profile button
                    profileClick(jsonResponse["id"]);
                    getSessionProfile(jsonResponse["id"]);
                    account_session_id = jsonResponse["id"];
                    ajaxGetAllNotification(jsonResponse["id"]);
                    ajaxGetStatistic(jsonResponse["id"]);

                    checkCurUserIsAttendee();
                    // display username, profile and logout button
                    if (thumbAvatar) {
                        $("#thumbAvatar-new").attr("src", thumbAvatar);
                        $("#thumbAvatar-dropdown").attr("src", thumbAvatar);
                    }

                    if (jsonResponse["role"].name == "ADMIN") {
                        $("#adminBtn").css("display", "block");
                    }

                } else {
                    // display login and register button
                    console.log("Guest is accessing !");
                    $("#profile-div").css("display", "none");
                    $("#loginForm").css("display", "block");
                }
            }
        });
    }

    notificationClick();

    function notificationClick() {
        $("#notification-li").click(function (event) {
            // separate from document click
            event.stopPropagation();

            $("#dropdown-notification").css("display", "block");

            return false;
        })
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

                $("#fullname-new").text(displayUsername);
                $("#fullname-dropdown").text(displayUsername);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function ajaxGetStatistic(accountID) {

        $.ajax({
            type: "GET",
            url: "/gwa/api/user/getStatistic?accountID=" + accountID,
            success: function (result) {
                // current user session's profile statistic
                $("#sell").text(result[0]);
                $("#buy").text(result[1]);
                $("#proposal").text(result[2]);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    // logout button click event
    $("#logout-new").click(function (event) {
        event.preventDefault();

        $("#loading").css("display", "block");

        setTimeout(function () {
            $("#loading").css("display", "none");

            ajaxLogout();
        }, 300);
    });

    $("#adminBtn").click(function (event) {
        event.preventDefault();

        window.location.href = "/gwa/admin/model/pending";
    });

    function ajaxLogout() {
        $.ajax({
            type: "GET",
            url: "/gwa/api/user/logout",
            success: function (result) {
                window.location.href = "/gwa/login";
            }
        });
    }

    // profile button click event
    function profileClick(accountID) {
        $("#profile-new").click(function (event) {
            window.location.href = "/gwa/pages/profile.html?accountID=" + accountID;
        })
    }

    // username click event
    function usernameClick() {
        $("#username-li").click(function (event) {
            // separate from document click
            event.stopPropagation();

            $("#dropdown-profile").css("display", "block");

            return false;
        })
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
    /*   End authentication and notification  */

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

            pageNumber = 1;
            $("#ul-notification").empty();
            ajaxGetAllNotification(account_session_id);
            if (payload.notification.title == "Model" || payload.notification.title == "Event") {
                toastr.error(payload.notification.body, payload.notification.title + " Notification", {timeOut: 5000});
            } else {
                toastr.info(payload.notification.body, payload.notification.title + " Notification", {timeOut: 5000});
            }
        })
    })
    /* This is end of firebase  */
});
