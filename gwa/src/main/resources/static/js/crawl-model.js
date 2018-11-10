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

    ajaxGetCrawlingStatus();

    ajaxGetLog();

    function ajaxGetLog() {
        $("#loading").css("display", "block");

        var listResult = [];
        var count = 0;

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/crawl/getLog",
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
            url: "/gwa/api/model/crawl/getStatus",
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
            window.location.href = "/gwa/admin/model/crawl";
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

            ajaxCrawlModel();

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

    function ajaxCrawlModel() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/crawl",
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

})