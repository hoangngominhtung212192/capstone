$(document).ready(function () {

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
    });

    function ajaxCrawlModel() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/crawl",
            success: function (message) {
                alert(message);
            },
            complete: function (xhr, txtStatus) {
                if (txtStatus == "error") {

                    var xhr_data = xhr.responseText;

                    alert(xhr_data);
                }
            }
        });
    }
})