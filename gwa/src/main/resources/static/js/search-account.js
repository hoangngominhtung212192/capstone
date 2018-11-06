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

    ajaxGetAllAccount(1, "");

    var txtSearch;
    var orderBy;

    function ajaxGetAllAccount(pageNumber, type) {
        $("#loading").css("display", "block");

        txtSearch = $("#txtSearch").val();
        orderBy = $("select[id='cbo-orderBy'] option:selected").text();

        if (!txtSearch) {
            txtSearch = "";
        }

        setTimeout(function () {
            $.ajax({
                type: "GET",
                url: "/gwa/api/user/searchAccount?pageNumber=" + pageNumber + "&type=" + type + "&txtSearch=" + txtSearch +
                "&orderBy=" + orderBy,
                success: function (result) {
                    if (result) {
                        console.log(result);
                        // reset data and pagination
                        $("#pagination-content").empty();
                        $("#accountBody").empty();
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

                    ajaxGetAllAccount(index, "");
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
            var appendBody = "";

            appendBody += "<tr>\n" +
                "                                    <td class=\"accountID\">" + value.id + "</td>\n" +
                "                                    <td class=\"fullname\">";

            if (value.middlename) {
                appendBody += value.lastname + " " + value.middlename + " " + value.firstname;
            } else {
                appendBody += value.lastname + " " + value.firstname;
            }

            appendBody += "</td>\n" +
                "                                    <td class=\"usernameTD\"><a href=\"/gwa/pages/profile.html?accountID="
                + value.id + "\">" + value.username + "</a></td>\n" +
                "                                    <td class=\"emailTD\">" + value.email + "</td>\n" +
                "                                    <td class=\"datetimeTD\">" + value.createdDate + "</td>\n" +
                "                                    <td class=\"accountStatus\">" + value.status + "</td>\n" +
                "                                    <td class=\"addressTD\">" + value.address + "</td>\n" +
                "                                </tr>";

            $("#accountBody").append(appendBody);
        });
    }

    $("#search-btn").click(function (e) {
        e.preventDefault();

        ajaxGetAllAccount(1, "");
    })

    $("#cbo-orderBy").on('change', function () {
        ajaxGetAllAccount(1, "");
    });

})