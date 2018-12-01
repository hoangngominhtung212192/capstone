$(document).ready(function () {

    // this is for authentication area
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

    // execute schedule
    var sModelInterval;
    var sTradeInterval;
    var sEventInterval;

    checkRunningSchedule();

    // reset first
    function checkRunningSchedule() {

        $("#loading").css("display", "block");

        setTimeout(function () {
            if (sModelInterval) {
                clearInterval(sModelInterval);
            }

            if (sTradeInterval) {
                clearInterval(sTradeInterval);
            }

            if (sEventInterval) {
                clearInterval(sEventInterval);
            }

            $("#startCrawlBtn").css("display", "none");
            $("#stopCrawlBtn").css("display", "none");

            $("#startUpdateTradePostBtn").css("display", "none");
            $("#stopUpdateTradePostBtn").css("display", "none");

            $("#startUpdateEventBtn").css("display", "none");
            $("#stopUpdateEventBtn").css("display", "none");

            $("#modelSDay").text("00");
            $("#modelSHour").text("00");
            $("#modelSMinute").text("00");
            $("#modelSSecond").text("00");

            $("#tradeSDay").text("00");
            $("#tradeSHour").text("00");
            $("#tradeSMinute").text("00");
            $("#tradeSSecond").text("00");

            $("#eventSDay").text("00");
            $("#eventSHour").text("00");
            $("#eventSMinute").text("00");
            $("#eventSSecond").text("00");

            $("#tbody-schedule-log").empty();

            ajaxRunningSchedule();
            ajaxGetLogSchedule();

            $("#loading").css("display", "none");
        }, 200);
    }

    function ajaxGetLogSchedule() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/schedule/getLogSchedule",
            success: function (result) {
                console.log(result);
                renderLogSchedule(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function renderLogSchedule(result) {

        $.each(result, function (index, value) {
            $("#tbody-schedule-log").append("<tr>\n" +
                "                                    <td>" + value.id + "</td>\n" +
                "                                    <td class=\"schedule-description\">" + value.description + "</td>\n" +
                "                                    <td>" + value.cycle + " hour/update</td>\n" +
                "                                    <td>" + value.date + "</td>\n" +
                "                                </tr>");
        });
    }

    function ajaxRunningSchedule() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/schedule/getListScheduleStatus",
            success: function (result) {
                processUI(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function processUI(result) {
        var modelCrawlS = result[0];
        var tradeS = result[1];
        var eventS = result[2];

        // if schedule model crawl is running
        if (modelCrawlS[0]) {
            $("#stopCrawlBtn").css("display", "block");
            $("#modelScheduleStatus").text("(Is Running)");
            $("#modelScheduleStatus").css("color", "green");
            $("#modelScheduleCycle").text(modelCrawlS[2] + " hours");

            // execute loop
            loopModelSchedule(modelCrawlS[1]);
        } else {
            $("#startCrawlBtn").css("display", "block");
            $("#modelScheduleStatus").text("(Is Stopped)");
            $("#modelScheduleStatus").css("color", "red");
            $("#modelScheduleCycle").text("N/A");
        }

        // if schedule update trade is running
        if (tradeS[0]) {
            $("#stopUpdateTradePostBtn").css("display", "block");
            $("#tradeScheduleStatus").text("(Is Running)");
            $("#tradeScheduleStatus").css("color", "green");
            $("#tradeScheduleCycle").text(tradeS[2] + " hours");

            // execute loop
            loopTradeSchedule(tradeS[1]);
        } else {
            $("#startUpdateTradePostBtn").css("display", "block");
            $("#tradeScheduleStatus").text("(Is Stopped)");
            $("#tradeScheduleStatus").css("color", "red");
            $("#tradeScheduleCycle").text("N/A");
        }

        // if schedule update event is running
        if (eventS[0]) {
            $("#stopUpdateEventBtn").css("display", "block");
            $("#eventScheduleStatus").text("(Is Running)");
            $("#eventScheduleStatus").css("color", "green");
            $("#eventScheduleCycle").text(eventS[2] + " hours");

            // execute loop
            loopEventSchedule(eventS[1]);
        } else {
            $("#startUpdateEventBtn").css("display", "block");
            $("#eventScheduleStatus").text("(Is Stopped)");
            $("#eventScheduleStatus").css("color", "red");
            $("#eventScheduleCycle").text("N/A");
        }
    }

    function loopModelSchedule(value) {

        var splitString = value.split(" ");
        var days = parseInt(splitString[0]) - 1;

        var times = splitString[1].split(":");
        var hours = parseInt(times[0]);
        var minutes = parseInt(times[1]);
        var seconds = parseInt(times[2]); // sai so

        sModelInterval = setInterval(function () {
            if (days == 0 && hours == 0 && minutes == 0 && seconds == 0) {
                checkRunningSchedule();
            } else {
                if (seconds == 0) {
                    if (minutes > 0) {
                        seconds = 60;
                        minutes--;
                    }
                }
                if (minutes == 0) {
                    if (hours > 0) {
                        minutes = 59;
                        hours--;
                    }
                }
                if (hours == 0 && minutes == 0 && seconds == 0) {
                    if (days > 0) {
                        hours = 23;
                        minutes = 59;
                        seconds = 60;
                        days--;
                    }
                }
                if (seconds > 0) {
                    seconds--;
                }
                $("#modelSDay").text(days);
                $("#modelSHour").text(hours);
                $("#modelSMinute").text(minutes);
                $("#modelSSecond").text(seconds);
            }
        }, 1000);
    }

    function loopTradeSchedule(value) {
        var splitString = value.split(" ");
        var days = parseInt(splitString[0]) - 1;

        var times = splitString[1].split(":");
        var hours = parseInt(times[0]);
        var minutes = parseInt(times[1]);
        var seconds = parseInt(times[2]); // sai so

        sTradeInterval = setInterval(function () {
            if (days == 0 && hours == 0 && minutes == 0 && seconds == 0) {
                checkRunningSchedule();
            } else {
                if (seconds == 0) {
                    if (minutes > 0) {
                        seconds = 60;
                        minutes--;
                    }
                }
                if (minutes == 0) {
                    if (hours > 0) {
                        minutes = 59;
                        hours--;
                    }
                }
                if (hours == 0 && minutes == 0 && seconds == 0) {
                    if (days > 0) {
                        hours = 23;
                        minutes = 59;
                        seconds = 60;
                        days--;
                    }
                }
                if (seconds > 0) {
                    seconds--;
                }
                $("#tradeSDay").text(days);
                $("#tradeSHour").text(hours);
                $("#tradeSMinute").text(minutes);
                $("#tradeSSecond").text(seconds);
            }
        }, 1000);
    }

    function loopEventSchedule(value) {
        var splitString = value.split(" ");
        var days = parseInt(splitString[0]) - 1;

        var times = splitString[1].split(":");
        var hours = parseInt(times[0]);
        var minutes = parseInt(times[1]);
        var seconds = parseInt(times[2]); // sai so

        sEventInterval = setInterval(function () {
            if (days == 0 && hours == 0 && minutes == 0 && seconds == 0) {
                checkRunningSchedule();
            } else {
                if (seconds == 0) {
                    if (minutes > 0) {
                        seconds = 60;
                        minutes--;
                    }
                }
                if (minutes == 0) {
                    if (hours > 0) {
                        minutes = 59;
                        hours--;
                    }
                }
                if (hours == 0 && minutes == 0 && seconds == 0) {
                    if (days > 0) {
                        hours = 23;
                        minutes = 59;
                        seconds = 60;
                        days--;
                    }
                }
                if (seconds > 0) {
                    seconds--;
                }
                $("#eventSDay").text(days);
                $("#eventSHour").text(hours);
                $("#eventSMinute").text(minutes);
                $("#eventSSecond").text(seconds);
            }
        }, 1000);
    }

    $("#startCrawlBtn").click(function (e) {
        e.preventDefault();

        $("#txtCycle").val("");
        $("#miModal").modal({backdrop: 'static', keyboard: false});
        $("#close-reason").on("click", function () {
            $("#miModal").modal('hide');
            $("#close-reason").prop("onclick", null).off("click");
            $("#submit-reason").prop("onclick", null).off("click");
        });

        $("#submit-reason").on("click", function (e) {
            // valid reason
            e.preventDefault();

            var txtCycle = $("#txtCycle").val();

            if (checkCycle(txtCycle)) {

                $("#mi-modal").modal({backdrop: 'static', keyboard: false});
                $("#modal-btn-si").on("click", function (e) {
                    $("#mi-modal").modal('hide');
                    $("#modal-btn-no").prop("onclick", null).off("click");
                    $("#modal-btn-si").prop("onclick", null).off("click");
                });

                $("#modal-btn-no").on("click", function (e) {
                    // ajax call
                    ajaxStartModelCrawlSchedule(txtCycle);
                    $("#mi-modal").modal('hide');
                    $("#modal-btn-no").prop("onclick", null).off("click");
                    $("#modal-btn-si").prop("onclick", null).off("click");
                });

                $("#miModal").modal('hide');
                $("#close-reason").prop("onclick", null).off("click");
                $("#submit-reason").prop("onclick", null).off("click");
            }

            return false;
        });
    })

    function checkCycle(number) {

        var check = true;

        if (!number) {
            check = false;

            $("#errorCycle").css("display", "block");
            $("#errorCycle").text("Please input cycle!");
        } else {
            if (!number.match("^[0-9\\.]+$")) {
                check = false;

                $("#errorCycle").css("display", "block");
                $("#errorCycle").text("Number only!!");
            } else if (number <= 0 || number > 480) {
                check = false;

                $("#errorCycle").css("display", "block");
                $("#errorCycle").text("Out of range, must be larger than 0 and lower than 20 days");
            } else {
                $("#errorCycle").css("display", "none");
            }
        }

        return check;
    }

    function ajaxStartModelCrawlSchedule(cycle) {

        $.ajax({
            type: "GET",
            url: "/gwa/api/schedule/startSModelCrawl?hours=" + cycle,
            success: function (result) {

                $("#success-p").text("Schedule of Model Crawl is running successfully!");
                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    checkRunningSchedule();
                    $("#success-btn").prop("onclick", null).off("click");
                });
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    $("#stopCrawlBtn").click(function (e) {
        e.preventDefault();

        $("#mi-modal").modal({backdrop: 'static', keyboard: false});
        $("#modal-btn-si").on("click", function (e) {
            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });

        $("#modal-btn-no").on("click", function (e) {
            // ajax call
            ajaxStopModelCrawlSchedule();
            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });
    })

    function ajaxStopModelCrawlSchedule() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/schedule/stopSModelCrawl",
            success: function (result) {

                $("#success-p").text("Schedule of Model Crawl is stopped!");
                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    checkRunningSchedule();
                    $("#success-btn").prop("onclick", null).off("click");
                });
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    // trade
    $("#startUpdateTradePostBtn").click(function (e) {
        e.preventDefault();

        $("#txtCycle").val("");
        $("#miModal").modal({backdrop: 'static', keyboard: false});
        $("#close-reason").on("click", function () {
            $("#miModal").modal('hide');
            $("#close-reason").prop("onclick", null).off("click");
            $("#submit-reason").prop("onclick", null).off("click");
        });

        $("#submit-reason").on("click", function (e) {
            // valid reason
            e.preventDefault();

            var txtCycle = $("#txtCycle").val();

            if (checkCycle(txtCycle)) {

                $("#mi-modal").modal({backdrop: 'static', keyboard: false});
                $("#modal-btn-si").on("click", function (e) {
                    $("#mi-modal").modal('hide');
                    $("#modal-btn-no").prop("onclick", null).off("click");
                    $("#modal-btn-si").prop("onclick", null).off("click");
                });

                $("#modal-btn-no").on("click", function (e) {
                    // ajax call
                    ajaxStartUpdateTradeSchedule(txtCycle);
                    $("#mi-modal").modal('hide');
                    $("#modal-btn-no").prop("onclick", null).off("click");
                    $("#modal-btn-si").prop("onclick", null).off("click");
                });

                $("#miModal").modal('hide');
                $("#close-reason").prop("onclick", null).off("click");
                $("#submit-reason").prop("onclick", null).off("click");
            }

            return false;
        });
    })

    function ajaxStartUpdateTradeSchedule(cycle) {

        $.ajax({
            type: "GET",
            url: "/gwa/api/schedule/startSUpdateTrade?hours=" + cycle,
            success: function (result) {

                $("#success-p").text("Schedule of Update Trade is running successfully!");
                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    checkRunningSchedule();
                    $("#success-btn").prop("onclick", null).off("click");
                });
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    $("#stopUpdateTradePostBtn").click(function (e) {
        e.preventDefault();

        $("#mi-modal").modal({backdrop: 'static', keyboard: false});
        $("#modal-btn-si").on("click", function (e) {
            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });

        $("#modal-btn-no").on("click", function (e) {
            // ajax call
            ajaxStopUpdateTradeSchedule();
            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });
    })

    function ajaxStopUpdateTradeSchedule() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/schedule/stopSUpdateTrade",
            success: function (result) {

                $("#success-p").text("Schedule of Update Trade is stopped!");
                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    checkRunningSchedule();
                    $("#success-btn").prop("onclick", null).off("click");
                });
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    // event
    $("#startUpdateEventBtn").click(function (e) {
        e.preventDefault();

        $("#txtCycle").val("");
        $("#miModal").modal({backdrop: 'static', keyboard: false});
        $("#close-reason").on("click", function () {
            $("#miModal").modal('hide');
            $("#close-reason").prop("onclick", null).off("click");
            $("#submit-reason").prop("onclick", null).off("click");
        });

        $("#submit-reason").on("click", function (e) {
            // valid reason
            e.preventDefault();

            var txtCycle = $("#txtCycle").val();

            if (checkCycle(txtCycle)) {

                $("#mi-modal").modal({backdrop: 'static', keyboard: false});
                $("#modal-btn-si").on("click", function (e) {
                    $("#mi-modal").modal('hide');
                    $("#modal-btn-no").prop("onclick", null).off("click");
                    $("#modal-btn-si").prop("onclick", null).off("click");
                });

                $("#modal-btn-no").on("click", function (e) {
                    // ajax call
                    ajaxStartUpdateEventSchedule(txtCycle);
                    $("#mi-modal").modal('hide');
                    $("#modal-btn-no").prop("onclick", null).off("click");
                    $("#modal-btn-si").prop("onclick", null).off("click");
                });

                $("#miModal").modal('hide');
                $("#close-reason").prop("onclick", null).off("click");
                $("#submit-reason").prop("onclick", null).off("click");
            }

            return false;
        });
    })

    function ajaxStartUpdateEventSchedule(cycle) {

        $.ajax({
            type: "GET",
            url: "/gwa/api/schedule/startSUpdateEvent?hours=" + cycle,
            success: function (result) {

                $("#success-p").text("Schedule of Update Event is running successfully!");
                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    checkRunningSchedule();
                    $("#success-btn").prop("onclick", null).off("click");
                });
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    $("#stopUpdateEventBtn").click(function (e) {
        e.preventDefault();

        $("#mi-modal").modal({backdrop: 'static', keyboard: false});
        $("#modal-btn-si").on("click", function (e) {
            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });

        $("#modal-btn-no").on("click", function (e) {
            // ajax call
            ajaxStopUpdateEventSchedule();
            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });
    })

    function ajaxStopUpdateEventSchedule() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/schedule/stopSUpdateEvent",
            success: function (result) {

                $("#success-p").text("Schedule of Update Event is stopped!");
                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    checkRunningSchedule();
                    $("#success-btn").prop("onclick", null).off("click");
                });
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

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
})