$(document).ready(function () {
    authentication()
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

    function authentication() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/gwa/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {
                    // username click
                    // usernameClick();

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);

                    role_session = jsonResponse["role"].name;
                    account_session_id = jsonResponse["id"];

                    var username = jsonResponse["username"];
                    username_session = jsonResponse["username"];
                    console.log("logegd user: "+username_session);
                    console.log(role_session + " " + username + " is on session!");

                    // click profile button
                    // profileClick(account_session_id);

                    $('#hidUserID').val(account_session_id);
                    // display username, profile and logout button
                    $("#username").text(username);
                    $('#lblUsername').text(username);
                    $("#username").css("display", "block");
                    $(".dropdown-menu-custom-profile").css("display", "block");
                    $(".dropdown-menu-custom-logout").css("display", "block");

                    // get current profile
                    account_profile_on_page_id = getUrlParameter('accountID');

                    checkCurUserIsAttendee();
                } else {
                    // display login and register button
                    console.log("Guest is accessing !");
                }

            }
        });
    }
    //check if user is logged in, redirect if not
    function checkAuth() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/gwa/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {
                    $("#confi-modal").modal('show');
                } else {
                    alert("You will be redirected to the login page!")
                    console.log("Guest is accessing !");
                    window.location.href = "/login";
                }

            }
        });
    }

    //check if user is attending this event
    var curAttendeeID
    function checkCurUserIsAttendee() {
        var curEvent = id;
        var curUser = parseInt($('#hidUserID').val());

        console.log(curEvent +" and "+curUser);
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/gwa/api/event/getAttendeeInEvent",
            data :{
              eventid: curEvent,
              userid: account_session_id,
            },
            success: function (result, status) {
                document.getElementById('feedbackArea').style.display = 'block';
                if (result.rating>0){
                    myRating.setRating(result.rating);
                    $('#txtFeedback').append(result.feedback);
                    $('#txtFeedback').prop('disabled', true);
                    $('#btnSubmitFeedback').hide();
                    $('#lblFeedback1').text("Your rating: ");
                    $('#lblFeedback2').text("Your feedback");

                }
                $('#btnRegister').hide();
                document.getElementById('userAttendInfo').style.display = 'block';
                $('#lblRegisUsername').append(username_session);
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
            url: "http://localhost:8080/gwa/api/event/feedbackEvent",
            data :{
                eventid: curEvent,
                attendeeid: curAtt,
                rating: parseInt(myRating.getRating()),
                feedback: $('textarea#txtFeedback').val()
            },
            success: function (result, status) {
                alert("Feedback sent! Thank you for your time.")
            },
            error : function(e) {
                alert("feedback failed");

                console.log( e);
            }
        });
    });
    $("#btnRegister").on("click", function () {
        checkAuth()
    });
    getRatedAttendeeEvent(id);
    function getRatedAttendeeEvent(id) {
        // var displayDiv = document.getElementById("FeedbackList");
        console.log("searchin rater by id: "+id);
        $.ajax({
            type : "POST",
            // contentType : "application/json",
            url : "http://localhost:8080/gwa/api/event/getListAttendeeInEvent",
            data : {
                eventid : id
            },
            success : function(result, status) {
                console.log("found evn raters:" +result.length);
                if (result){
                    for (var i in result) {
                        var arater = $('<div class="panel">\n' +
                            '                    <h5 id="raterName" >'+result[i].account.username+'</h5>\n' +
                            '                    <div id="raterNo'+result[i].id+'"></div>\n' +
                            '                    <p id="raterFeedback">'+result[i].feedback+'</p>\n' +
                            '                </div>' +
                            '<hr/>');

                        $('#raterArea').append(arater);
                        var raterRating = raterJs( {
                            element:document.querySelector("#raterNo"+result[i].id),
                            rateCallback:function rateCallback(rating, done) {
                                // this.setRating(rating);
                                done();

                            },
                            showToolTip: false,
                            readOnly: true
                        });
                        raterRating.setRating(result[i].rating);
                    }

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
            url : "http://localhost:8080/gwa/api/event/getEvent",
            data : JSON.stringify(data),
            success : function(result, status) {

                if (result){
                    var title = result.title;
                    console.log(result.maxAttendee);
                    console.log(result.numberOfAttendee);
                    var remaininT = parseInt(result.maxAttendee) - parseInt(result.numberOfAttendee);

                    console.log('tikets: ' + remaininT + typeof remaininT);
                    $('#regStDate').append(result.regDateStart);
                    $('#regEnDate').append(result.regDateEnd);
                    var resRegStart = Date.parse(result.regDateStart);
                    var resRegEnd = Date.parse(result.regDateEnd);
                    var tttoday = Date.parse(today);
                    if (tttoday <= resRegEnd && tttoday >= resRegStart) {
                        console.log("today is within reg date");
                        document.getElementById('btnRegister').style.display = 'block';
                    } else{
                        console.log("today is not within reg date");
                        document.getElementById('btnRegister').style.display = 'none';
                    }
                    $('#stDate').append(result.startDate);
                    $('#enDate').append(result.endDate);
                    var totalStars = result.numberOfStars;
                    var timeRated = result.numberOfRating;
                    var stars = Math.floor(totalStars/timeRated);
                    if (timeRated>0){
                        $('#txtCurRat').append(stars);
                        evRating.setRating(stars);
                    } else {
                        $('#ratingDiv').hide();
                    }
                    console.log("Stars: "+stars)

                    $('#hidID').val(result.id);
                    curEvnId = result.id;
                    console.log("ev id is "+curEvnId);
                    // $('#lblUsername').append(username_session.toString());
                    $('#lblTimeRated').append(result.numberOfRating);
                    $('#lblTodayDate').append(today.toString());
                    $('#txtLocation').append(result.location);
                    $('#lblRemaining').append(remaininT);
                    $('#title').append(title);
                    $('#content').html(result.content);
                }
            },
            error : function(e) {
                alert("Error!")
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
            url : "http://localhost:8080/gwa/api/event/getRemainingSlots",
            data: {
                eventid : eventid
            },
            success : function(result, status) {
                console.log('rmnslots: '+result);
                var iamount = parseInt(amount);
                if (result<amount){
                    alert('There are not enough tickets! Please change the amount of tickets.');
                } else{
                    registerAtt();
                }
            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });

        // registerAtt(formArticle);
    })


    function registerAtt() {

            $.ajax({
                type : "POST",
                url : "http://localhost:8080/gwa/api/event/registerEvent",
                data: {
                    eventid : parseInt($("#hidID").val()),
                    userid : account_session_id,
                    amount : parseInt($("#txtNum").val()),
                    date : today.toString()
                },
                success : function(result, status) {
                    alert("Registered successfully!")
                    location.reload(true);
                    console.log(result);
                    console.log(status);
                },
                error : function(e) {
                    alert("Error!")
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

});
