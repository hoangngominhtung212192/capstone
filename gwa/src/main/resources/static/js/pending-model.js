$(document).ready(function () {

    ajaxGetAllPending(1, "");

    function ajaxGetAllPending(pageNumber, type) {
        $.ajax({
            type: "GET",
            url: "/api/model/getAllPending?pageNumber=" + pageNumber + "&type=" + type,
            success: function (result) {
                if (result) {
                    console.log(result);
                    // reset data and pagination
                    $("#pagination-content").empty();
                    $("#pendingBody").empty();
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
                "<td class=\"modelCode\">" + value.code + "</td>\n" +
                "<td class=\"modelName\"><a href=\"#\">" + value.name + "</a></td>\n" +
                "<td class=\"productSeries\">" + value.productseries.name + "</td>\n" +
                "<td class=\"seriesTitle\">" + value.seriestitle.name + "</td>\n" +
                "<td class=\"modelManu\">" + value.manufacturer.name + "</td>\n" +
                "<td class=\"crawlDate\">" + value.createdDate + "</td>\n" +
                "<td class=\"modelStatus\">" + value.status + "</td>\n" +
                "<td>\n" +
                "<a class='approveBtn' id=\"" + value.id +"\" href=\"#\">Approve</a>\n" +
                "<a class=\"moreInfoBtn\" href=\"/model/modeldetail\">More</a>\n" +
                "</td>\n" +
                "</tr>");

            $("#" + value.id).click(function (e) {
                e.preventDefault();

                var id = $(this).attr('id');

                $("#mi-modal").modal({backdrop: 'static', keyboard: false});
                $("#modal-btn-si").on("click", function(e){
                    $("#mi-modal").modal('hide');
                    $("#modal-btn-no").prop("onclick", null).off("click");
                    $("#modal-btn-si").prop("onclick", null).off("click");
                });

                $("#modal-btn-no").on("click", function(e){
                    approveModel(id);
                    $("#mi-modal").modal('hide');
                    $("#modal-btn-no").prop("onclick", null).off("click");
                    $("#modal-btn-si").prop("onclick", null).off("click");
                });
            });
        });
    }

    function approveModel(id) {
        $.ajax({
           type: "GET",
           url: "/api/model/approve?id=" + id,
           success: function (result) {
               console.log("Approved model: " + result);
               ajaxGetAllPending(1, "");
           },
           error: function (e) {
               console.log("ERROR: ", e);
           }
        });
    }

})