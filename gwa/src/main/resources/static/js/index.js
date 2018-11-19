$(document).ready(function () {
    var currentPage = 1;
    searchEv();
    function searchEv() {
        console.log("getting evens")
        $.ajax({
            type : "POST",
            url : "/gwa/api/event/searchEventByStatusAndPage",
            data : {
                title : "",
                status : "Active",
                sorttype : "desc",
                pageNum : 1
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

                appendEvent(data);
            },
            error : function(e) {
                alert("No event with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }

    function appendEvent(result){
        var topEvdiv = document.getElementById("topEvents")
        var eventDiv1 = $('<div class="single-blog-post featured-post">\n' +
            '                            <div class="post-thumb">\n' +
            '<img src="'+result[0].thumbImage+'" style="width: 400px; max-height: 350px" alt="">' +
            '                            </div>\n' +
            '                            <div class="post-data">\n' +
            '                                <a href="#" class="post-title">\n' +
            '                                    <h6>'+result[0].title+'</h6>\n' +
            '                                </a>\n' +
            '                                <div class="post-meta">\n' +
            '                                    <p class="post-excerp">'+result[0].description+'</p>\n' +
            '                                </div>\n' +
            '                            </div>\n' +
            '                        </div>');
        $('#BigEv').append(eventDiv1);
        var eventDiv2 = $('<div class="single-blog-post featured-post-2">\n' +
            '                            <div class="post-thumb">\n' +
            '<img src="'+result[1].thumbImage+'" style="width: 280px; max-height: 200px" alt="">' +
            '                            </div>\n' +
            '                            <div class="post-data">\n' +
            '                                <div class="post-meta">\n' +
            '                                    <a href="#" class="post-title">\n' +
            '                                        <h6>'+result[1].title+'</h6>\n' +
            '                                    </a>\n' +
            '                                </div>\n' +
            '                            </div>\n' +
            '                        </div>');
        var eventDiv3 = $('<div class="single-blog-post featured-post-2">\n' +
            '                            <div class="post-thumb">\n' +
            '<img src="'+result[2].thumbImage+'" style="width: 280px; max-height: 200px" alt="">' +
            '                            </div>\n' +
            '                            <div class="post-data">\n' +
            '                                <div class="post-meta">\n' +
            '                                    <a href="#" class="post-title">\n' +
            '                                        <h6>'+result[2].title+'</h6>\n' +
            '                                    </a>\n' +
            '                                </div>\n' +
            '                            </div>\n' +
            '                        </div>');
        $('#smallerEvDiv').append(eventDiv2);
        $('#smallerEvDiv').append(eventDiv3);

    }




    /*   Begin authentication and notification  */
    // process UI
    $(document).click(function (event) {
        $("#dropdown-profile").css("display", "none");
        $("#dropdown-notification").css("display", "none");
    })

    authentication();

    var account_session_id;

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

                    var username = jsonResponse["username"];
                    var thumbAvatar = jsonResponse["avatar"];
                    console.log(jsonResponse["role"].name + " " + username + " is on session!");
                    $("#membersince").text("Member since " + jsonResponse["createdDate"].split(" ")[0]);

                    // click profile button
                    profileClick(jsonResponse["id"]);
                    getSessionProfile(jsonResponse["id"]);
                    account_session_id = jsonResponse["id"];
                    ajaxGetAllNotification(jsonResponse["id"]);
                    ajaxGetStatistic(jsonResponse["id"]);

                    // display username, profile and logout button
                    if (thumbAvatar) {
                        $("#thumbAvatar-new").attr("src", thumbAvatar);
                        $("#thumbAvatar-dropdown").attr("src", thumbAvatar);
                    }
                    $("#btnGetMyArticles").css("display", "block");

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
        var description = "Model " + current_model_id + " loading image 404 error!";

        var formNotification = {
            description: description,
            objectID: current_model_id,
            account: {
                id: 3 // to admin
            },
            notificationtype: {
                id: 2
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

})