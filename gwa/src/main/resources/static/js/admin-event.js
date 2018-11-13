$(document).ready(function () {


    getAllEvent();



    function getAllEvent() {
        console.log("getting all event");
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/gwa/api/event/getAllEvent",
            success : function(result, status) {
                console.log(result);
                console.log(status);
                for (var i in result){

                    var tus = parseInt(result[i].approvalStatus) - 1;
                    var row = $('<tr>\n' +
                        '                    <td>' + result[i].title + '</td>\n' +
                        '                    <td>' + result[i].description + '</td>\n' +
                        '                    <td>' + result[i].startDate + '</td>\n' +
                        '                    <td>' + result[i].endDate + '</td>\n' +
                        '                    <td>' + result[i].ticketPrice + '</td>\n' +
                        '                    <td><div class="styled-select slate">\n' +
                        '                   <select onchange="approvalChange(value,' + result[i].id + ' )" id="idx' + result[i].id + '" class="editable-select">\n' +
                        '                   <option value="Active">Active</option>\n' +
                        '                   <option value="Inactive">Inactive</option>\n' +
                        '                   <option value="Finished">Finished</option>\n' +
                        '                   <option value="Cancelled">Cancelled</option>\n' +
                        '                   </select></td>\n' +
                        '<td><a href="/gwa/event/detail?id=' + result[i].id + '">Link</a> / <a href="/gwa/admin/event/edit?id=' + result[i].id + '">Edit</a> </td>\n' +
                        '                  </tr>')
                    $('#tblBody').append(row);
                    // document.getElementById("idx" + result[i].id ).options[tus].selected = 'selected';
                }
            },
            error : function(e) {
                alert("No evnt found!");
                console.log("ERROR: ", e);
            }
        });
    }




    //search function
    $("#btnSearch").click(function (event) {
        event.preventDefault();
        var searchDiv = document.getElementById("tblBody");
        while (searchDiv.firstChild) {
            searchDiv.removeChild(searchDiv.firstChild);
        }
        var searchValue = document.getElementById("txtSearch").value;
        if (searchValue.length == 0){
            getAllEvent();
        } else {
            searchArticle(searchValue);
        }

    })

    function searchArticle(data) {
        console.log(data);
        var displayDiv = document.getElementById("search-result");

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/api/user/searchArticle",
            data : data.toString(),
            success : function(result, status) {
                console.log(result);
                console.log(status);
                console.log("first res: "+ result[0].account.id );
                for (var i in result){
                    var tus = parseInt(result[i].approvalStatus) - 1;
                    var row = $('<tr>\n' +
                        '                    <td>' + result[i].id + '</td>\n' +
                        '                    <td>' + result[i].title + '</td>\n' +
                        '                    <td>' + result[i].account.id + '</td>\n' +
                        '                    <td>' + result[i].date + '</td>\n' +
                        '                    <td>' + result[i].category + '</td>\n' +
                        '                    <td><div class="styled-select slate">\n' +
                        '                   <select onchange="approvalChange(value,' + result[i].id + ' )" id="idx' + result[i].id + '" class="editable-select">\n' +
                        '                   <option value="1">APPROVED</option>\n' +
                        '                   <option value="2">DISAPPROVED</option>\n' +
                        '                   <option value="3">PENDING</option>\n' +
                        '                   </select></td>\n' +
                        '                    <td><a href="/admin/article/edit?id=' + result[i].id + '">Read more...</a></td>\n' +
                        '                  </tr>')
                    $('#tblBody').append(row);
                    document.getElementById("idx" + result[i].id ).options[tus].selected = 'selected';
                }
            },
            error : function(e) {
                alert("No article with matching title found!");
                console.log("ERROR: ", e);
            }
        });
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