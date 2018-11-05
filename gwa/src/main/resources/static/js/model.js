$(document).ready(function () {

    // process UI
    $(document).click(function (event) {
        $("#dropdown-profile").css("display", "none");
        $("#dropdown-notification").css("display", "none");
    })

    authentication();

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
                    $("#membersince").text("Member since "  + jsonResponse["createdDate"].split(" ")[0]);

                    // click profile button
                    profileClick(jsonResponse["id"]);
                    getSessionProfile(jsonResponse["id"]);
                    ajaxGetStatistic(jsonResponse["id"]);

                    // display username, profile and logout button
                    if (thumbAvatar) {
                        $("#thumbAvatar-new").attr("src", thumbAvatar);
                        $("#thumbAvatar-dropdown").attr("src", thumbAvatar);
                    }

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
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    // logout button click event
    $("#logout-new").click(function (event) {

        event.preventDefault();

        ajaxLogout();
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

    // get all dropdown values
    getAllDropdownValues();

    function getAllDropdownValues() {
        $("#loading").css("display", "block");
        ajaxGetManufacturer();
        ajaxGetProductseries();
        ajaxGetSeriestitle();
        setTimeout(function () {
            $("#loading").css("display", "none");
        }, 300);
    }

    function ajaxGetProductseries() {

        $("#cbo-productseries").append($('<option></option>').attr('value', 'All').text('All'));

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/getAllProductseries",
            success: function (result) {
                console.log(result);

                $.each(result, function (key, entry) {
                    $("#cbo-productseries").append($('<option></option>').attr('value', entry.name).text(entry.name));
                })
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function ajaxGetSeriestitle() {

        $("#cbo-seriestitle").append($('<option></option>').attr('value', 'All').text('All'));

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/getAllSeriestitle",
            success: function (result) {
                console.log(result);

                $.each(result, function (key, entry) {
                    $("#cbo-seriestitle").append($('<option></option>').attr('value', entry.name).text(entry.name));
                })
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function ajaxGetManufacturer() {

        $("#cbo-manufacturer").append($('<option></option>').attr('value', 'All').text('All'));

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/getAllManufacturer",
            success: function (result) {
                console.log(result);

                $.each(result, function (key, entry) {
                    $("#cbo-manufacturer").append($('<option></option>').attr('value', entry.name).text(entry.name));
                })
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    var txtSearchValue;
    var filterProductseries;
    var filterSeriestitle;
    var filterManufacturer;
    var filterPrice;
    var filterOrderBy;
    var filterCending;

    function updateFilter() {
        txtSearchValue = $("#table_filter").val();
        filterProductseries = $("select[id='cbo-productseries'] option:selected").text();
        filterSeriestitle = $("select[id='cbo-seriestitle'] option:selected").text();
        filterManufacturer = $("select[id='cbo-manufacturer'] option:selected").text();
        filterPrice = $("select[id='cbo-price'] option:selected").text();
        filterOrderBy = $("select[id='cbo-order-1'] option:selected").text();
        filterCending = $("select[id='cbo-order-2'] option:selected").text();
    }

    function search(pageNumber, type) {

        var formSearch = {
            searchValue : txtSearchValue,
            productseries : filterProductseries,
            seriestitle : filterSeriestitle,
            price : filterPrice,
            manufacturer : filterManufacturer,
            pagination : {
                currentPage : pageNumber,
                type : type
            },
            orderBy : filterOrderBy,
            cending : filterCending
        }

        ajaxSearchModel(formSearch);
    }

    // on document load
    updateFilter();
    search(1, "");

    $("#searchBtn").click(function (e) {
        e.preventDefault();

        updateFilter();
        search(1, "");
    });

    function ajaxSearchModel(data) {

        $.ajax({
           type : "POST",
            contentType: "application/json",
            url : "/gwa/api/model/search",
            data : JSON.stringify(data),
            success : function (result) {
                console.log(result);
                // reset content div
                $("#pagination-content").empty();
                $("#ul-records-container").empty();
                $("#no-records").css("display", "none");
                pagination(result.pagination);
                renderData(result.modelDTOList);
            },
            error : function (e) {
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

                    search(index, "");
                });
            }
        }

        $("#next").click(function (e) {
            e.preventDefault();

            search(value.currentPage, "Next");
        });

        $("#last").click(function (e) {
            e.preventDefault();

            search(value.currentPage, "Last");
        });

        $("#first").click(function (e) {
            e.preventDefault();

            search(value.currentPage, "First");
        });

        $("#prev").click(function (e) {
            e.preventDefault();

            search(value.currentPage, "Prev");
        });
    };

    $("#cbo-order-1").on('change', function () {
        filterOrderBy = this.value;
        search(1, "");
    });

    $("#cbo-order-2").on('change', function () {
        filterCending = this.value;
        search(1, "");
    })

    function renderData(data) {

        if (data.length > 0) {
            $.each(data, function (index, value) {
                var model = value.model;

                $("#ul-records-container").append("<li class=\"model-records-item\">\n" +
                    "                                        <div class=\"model-records-item-img\">\n" +
                    "                                            <img src=\"" + model.thumbImage + "\">\n" +
                    "                                            <span class=\"model-records-item-name\">" + model.name + "</span>\n" +
                    "                                            <div class=\"model-records-item-info\">\n" +
                    "                                                <p>" + model.name + "</p>\n" +
                    "                                                <p><b>Price: </b>" + model.price + "</p>\n" +
                    "                                                <p><b>Released: </b>" + model.releasedDate + "</p>\n" +
                    "                                                <p><b>Manufacturer: </b>" + model.manufacturer.name + "</p>\n" +
                    "                                                <p><b>Product Series: </b>" + model.productseries.name + "</p>\n" +
                    "                                                <div class=\"model-records-item-info-button\">\n" +
                    "                                                    <button id='" + model.id + "'>More Information</button>\n" +
                    "                                                </div>\n" +
                    "                                            </div>\n" +
                    "                                        </div>\n" +
                    "                                    </li>");
                $("#" + model.id).click(function (e) {
                    e.preventDefault();

                   var id = $(this).attr('id');
                   window.location.href = "/gwa/pages/modeldetail.html?modelID=" + id;
                });

            });
        } else {
            $("#no-records").css("display", "block");
        }
    }


})