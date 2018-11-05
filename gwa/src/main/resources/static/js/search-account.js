$(document).ready(function () {

    ajaxGetAllAccount(1, "");

    function ajaxGetAllAccount(pageNumber, type) {
        $.ajax({
            type: "GET",
            url: "/gwa/api/user/getAllAccount?pageNumber=" + pageNumber + "&type=" + type,
            success: function (result) {
                if (result) {
                    console.log(result);
                    // reset data and pagination
                    $("#pagination-content").empty();
                    $("#accountBody").empty();
                    pagination(result[0]);
                    renderData(result[1]);
                }
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
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
            $("#accountBody").append("<tr>\n" +
                "                                    <td class=\"accountID\">" + value.id + "</td>\n" +
                "                                    <td class=\"fullname\">" + value.lastmame + " " +  value.middlename
                + " " + value.firstname + "</td>\n" +
                "                                    <td class=\"usernameTD\"><a href=\"/gwa/pages/profile.html?accountID="
                + value.id + "\">" + value.username + "</a></td>\n" +
                "                                    <td class=\"emailTD\">" + value.email +"</td>\n" +
                "                                    <td class=\"datetimeTD\">" + value.createdDate + "</td>\n" +
                "                                    <td class=\"accountStatus\">" + value.status + "</td>\n" +
                "                                    <td class=\"accountRating\">0</td>\n" +
                "                                    <td class=\"addressTD\">" + value.address + "</td>\n" +
                "                                </tr>");
        });
    }

})