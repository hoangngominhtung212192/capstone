$(document).ready(function () {

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

    ajaxGetCrawlingStatus();

    ajaxGetLog();

    function ajaxGetLog() {
        $("#loading").css("display", "block");

        var listResult = [];
        var count = 0;

        $.ajax({
            type: "GET",
            url: "/gwa/api/article/crawl/getLog",
            success: function (result) {
                console.log(result);

                $("#tbody-crawl").empty();
                $.each(result, function (idx, value) {
                    listResult[count] = value;
                    count++;
                });

                // sort by date, reverse data
                for (var i = count - 1; i >= 0; i--) {
                    if (listResult[i].status == "Crawling") {
                        $("#tbody-crawl").append("<tr>\n" +
                            "<td>" + listResult[i].id + "</td>\n" +
                            "<td>" + listResult[i].crawlDateTime + "</td>\n" +
                            "<td>" + listResult[i].numberOfRecords + "</td>\n" +
                            "<td style=\"color: darkblue;\">" + listResult[i].numberOfNewRecords + "</td>\n" +
                            "<td style=\"color: red;\"><b>" + listResult[i].status + "</b></td>\n" +
                            "</tr>")
                    } else {
                        $("#tbody-crawl").append("<tr>\n" +
                            "<td>" + listResult[i].id + "</td>\n" +
                            "<td>" + listResult[i].crawlDateTime + "</td>\n" +
                            "<td>" + listResult[i].numberOfRecords + "</td>\n" +
                            "<td style=\"color: darkblue;\">" + listResult[i].numberOfNewRecords + "</td>\n" +
                            "<td style=\"color: green;\"><b>" + listResult[i].status + "</b></td>\n" +
                            "</tr>")
                    }
                }
                $("#loading").css("display", "none");
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    var crawlStatus = false;

    function ajaxGetCrawlingStatus() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/article/crawl/getStatus",
            success: function (result) {
                if (result.inProgress) {
                    $("#numOfCrawl").text(result.numberOfRecords);
                    $("#numOfNew").text(result.numberOfNewRecords);

                    console.log("Number of records: " + result.numberOfRecords + " - Number of new: " + result.numberOfNewRecords);

                    $("#switch").css("display", "block");
                    $("#lbSwitch").css("display", "block");
                    $("#crawlRecordsDiv").css("display", "block");
                    crawlStatus = true;
                } else {
                    $("#switch").css("display", "none");
                    $("#lbSwitch").css("display", "none");
                    $("#crawlRecordsDiv").css("display", "none");
                    crawlStatus = false;
                }
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    var interval;

    $("#switch").change(function (e) {

        if ($(this).prop('checked') == true) {
            $("#crawlRecordsDiv").css("display", "block")

            if (crawlStatus) {
                interval = setInterval(function () {
                    loopforever()
                }, 3000);
            }
        } else {
            $("#crawlRecordsDiv").css("display", "none");
            if (interval) {
                clearInterval(interval);
            }
        }
    });

    function loopforever() {
        if (!crawlStatus) {
            clearInterval(interval);
            window.location.href = "/gwa/admin/article/crawl";
        }

        ajaxGetCrawlingStatus();
    }

    $("#crawlBtn").click(function (e) {
        e.preventDefault();

        $("#mi-modal").modal({backdrop: 'static', keyboard: false});
        $("#modal-btn-si").on("click", function(){
            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });

        $("#modal-btn-no").on("click", function(e) {

            $("#loading").css("display", "block");

            ajaxCrawlArticle();

            $("#switch").css("display", "block");
            $("#lbSwitch").css("display", "block");
            $("#crawlRecordsDiv").css("display", "block");

            setTimeout(function () {
                ajaxGetLog();
                ajaxGetCrawlingStatus();
                $("#loading").css("display", "none");
            }, 2000);

            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });
    });

    function ajaxCrawlArticle() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/article/crawl",
            success: function (message) {
                $("#modal-header").css("display", "block");

                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function() {
                });
            },
            complete: function (xhr, txtStatus) {
                if (txtStatus == "error") {

                    var xhr_data = xhr.responseText;

                    $("#modal-header").css("display", "none");
                    $("#modal-h4").text("Oops!");
                    $("#modal-p").text(xhr_data);
                    $("#myModal").modal({backdrop: 'static', keyboard: false});
                    $("#success-btn").on("click", function() {
                    });
                }
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
                appendNotification += "<li>\n"
            } else {
                // already seen
                appendNotification += "<li style='background-color: white;'>\n"
            }

            var iconType = "<i class=\"fa fa-warning text-yellow\" style=\"color: darkred;\"></i> ";

            if (value.notificationtype.name == "Profile"){
                iconType = "<i class=\"fa fa-user-circle-o text-yellow\" style=\"color: darkred;\"></i> ";
            }else if (value.notificationtype.name == "Model") {
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
})