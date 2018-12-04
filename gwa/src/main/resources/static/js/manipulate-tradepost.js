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

    ajaxGetAllPending(1, "");

    var txtSearch;
    var orderBy;

    function ajaxGetAllPending(pageNumber, type) {
        $("#loading").css("display", "block");

        txtSearch = $("#txtSearch").val();
        orderBy = $("select[id='cbo-orderBy'] option:selected").text();

        if (!txtSearch) {
            txtSearch = "";
        }

        setTimeout(function () {
            $.ajax({
                type: "GET",
                url: "/gwa/api/tradepost/search-pending-tradepost?pageNumber=" + pageNumber + "&type=" + type + "&txtSearch=" + txtSearch +
                    "&orderBy=" + orderBy,
                success: function (result) {
                    if (result) {
                        console.log(result);
                        // reset data and pagination
                        $("#pagination-content").empty();
                        $("#pendingBody").empty();
                        $("#no-record").css("display", "none");

                        if (result[1].length > 0) {
                            pagination(result[0]);
                            renderData(result[1]);
                        } else {
                            $("#no-record").css("display", "block");
                        }
                    }
                },
                error: function (e) {
                    console.log("ERROR: ", e);
                }
            });

            $("#loading").css("display", "none");
        }, 300);
    }

    function pagination(value) {
        var render_pagination = "";

        if (value.currentPage == 1) {
            render_pagination += "<span class=\"my-page my-active\">First</span>\n" +
                "<span class=\"my-page my-active\">Prev</span>\n";
        } else {
            render_pagination += "<span id=\"first\" class=\"my-page\">First</span>\n" +
                "<span id=\"prev\" class=\"my-page\">Prev</span>\n"
        }
        if (value.currentPage > 5) {
            render_pagination += "<span class=\"my-page\">...</span>\n";
        }
        for (var i = value.beginPage; i <= value.lastPage; i++) {
            if (i < (value.beginPage + 5)) {
                if (value.currentPage == i) {
                    render_pagination += "<span class=\"my-page my-active\">" + i + "</span>\n";
                } else {
                    render_pagination += "<span id=\"page" + i + "\" class=\"my-page\">" + i + "</span>\n"
                }
            }
        }
        if (value.lastPage >= (value.beginPage + 5)) {
            render_pagination += "<span class=\"my-page\">...</span>\n";
        }
        if (value.currentPage == value.lastPage) {
            render_pagination += "<span class=\"my-page my-active\">Next</span>\n" +
                "<span class=\"my-page my-active\">Last</span>\n";
        } else {
            render_pagination += "<span id=\"next\" class=\"my-page\">Next</span>\n" +
                "<span id=\"last\" class=\"my-page\">Last</span>\n"
        }

        $("#pagination-content").append(render_pagination);

        for (var i = value.beginPage; i <= value.lastPage; i++) {
            if (i < (value.beginPage + 5)) {
                $("#page" + i).click(function (e) {
                    e.preventDefault();

                    var index = $(this).attr('id').replace('page', '');

                    ajaxGetAllPending(index, "");
                });
            }
        }

        $("#next").click(function (e) {
            e.preventDefault();

            ajaxGetAllPending(value.currentPage, "Next");
        });

        $("#last").click(function (e) {
            e.preventDefault();

            ajaxGetAllPending(value.currentPage, "Last");
        });

        $("#first").click(function (e) {
            e.preventDefault();

            ajaxGetAllPending(value.currentPage, "First");
        });

        $("#prev").click(function (e) {
            e.preventDefault();

            ajaxGetAllPending(value.currentPage, "Prev");
        });
    };

    function renderData(data) {
        $.each(data, function (index, value) {
            $("#pendingBody").append("<tr>\n" +
                "<td>" + (index + 1) + "</td>\n" +
                "<td class=\"modelName\"><a href=\"/gwa/trade-market/view-trade?tradepostId=" + value.id + "\">" + value.title + "</a></td>\n" +
                "<td>" + value.brand + "</td>\n" +
                "<td>" + value.account.username + "</td>\n" +
                "<td>" + value.location + "</td>\n" +
                "<td>" + value.postedDate + "</td>\n" +
                "<td>" + value.approvalStatus+ "</td>\n" +
                "<td>\n" +
                "<a class='approveBtn' data-toggle='modal' data-id=\"" + value.id + "\" href=\"#acceptModal\">Approve</a> \n " +
                "<a class='declineBtn' data-toggle='modal' data-id=\"" + value.id + "\" href=\"#declineModal\">Decline</a>\n" +
                "</td>\n" +
                "</tr>");


        });

    }


    function declineTradepost(id, reason) {
        console.log("Decline: ID=" + id + " - Reason: " + reason);
        $.ajax({
            type: "POST",
            url: "/gwa/api/tradepost/decline-tradepost",
            data: {
                tradepostId: id,
                reason: reason
            },
            success: function (result) {
                var notiDesc = "Your trade post with id=" + id +" has been declined by Administrator.";
                var acc = result.account.id;
                var objectID = id;
                var notiType = 3; // Notification about tradepost;
                addNewNotification(notiDesc, objectID, acc, notiType);
                console.log("Decline model: " + result);
                $("#noticeText").html("Decline successfully.");
                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    ajaxGetAllPending(1, "");
                });

                $('body').css("padding-right", "0px");
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function approveTradepost(id) {
        $.ajax({
            type: "POST",
            url: "/gwa/api/tradepost/approve-tradepost",
            data: {
                tradepostId: id
            },
            success: function (result) {
                console.log("Approved model: " + result);
                var role = result.account.role.name;
                var acc = result.account.id;
                var notiDesc = "Your trade post with id=" + id +" has been approved by Administrator.";
                var objectID = id;
                var notiType = 3; // Notification about tradepost;
                addNewNotification(notiDesc, objectID, acc, notiType);

                $("#noticeText").html("Approve successfully.");
                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    if (role == "MEMBER"){
                        updateRole(acc, "BUYERSELLER");
                    }
                    ajaxGetAllPending(1, "");
                });

                $('body').css("padding-right", "0px");
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function updateRole(accountID, roleName){
        $.ajax({
            type: "POST",
            url: "/gwa/api/user/update-role",
            data: {
                accountID: accountID,
                roleName: roleName
            },
            success: function () {
                var acc = accountID;
                var notiDesc = "Congratulation! You just become " + roleName;
                var objectID = accountID;
                var notiType = 1; // Notification about tradepost;
                addNewNotification(notiDesc, objectID, acc, notiType);

                console.log("UPDATED ROLE: " + accountID + " - " + roleName);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    $('#acceptModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data('id');

        $("#acceptBtn").on("click", function (e) {
            approveTradepost(id);
            $("#acceptModal").modal('hide');
            $("#acceptBtn").prop("onclick", null).off("click");
        });

    })
    $('#declineModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var id = button.data('id');
        var modal = $(this);
        modal.find('input,textarea').val('');

        $("#declineBtn").on("click", function (e) {
            e.preventDefault();
            var reason = $("#declineReasonText").val();
            if (reason.length < 10){
                alert("Reason too short [Required > 10].");
            }else {
                declineTradepost(id, reason);
                $("#declineModal").modal('hide');
            }
            $("#declineBtn").prop("onclick", null).off("click");
        });


    })

    $("#search-btn").click(function (e) {
        e.preventDefault();

        ajaxGetAllPending(1, "");
    })

    $("#cbo-orderBy").on('change', function () {
        ajaxGetAllPending(1, "");
    });


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

    function addNewNotification(desc,objectId,account,notiType) {
        var description = $("#txtReason").val();

        var formNotification = {
            description: desc,
            objectID: objectId,
            account: {
                id: account
            },
            notificationtype: {
                id: notiType
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
            if (payload.data.title == "Model" || payload.data.title == "Event") {
                toastr.error(payload.data.body, payload.data.title + " Notification", {timeOut: 5000});
            } else {
                toastr.info(payload.data.body, payload.data.title + " Notification", {timeOut: 5000});
            }
            setTimeout(function () {
                ajaxGetAllNotification(account_session_id);
            }, 1000);
        })
    })
    /* This is end of firebase  */
})

