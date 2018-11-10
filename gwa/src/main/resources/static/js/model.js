$(document).ready(function () {

    function getUrlParameter(sParam) {
        var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');

            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : sParameterName[1];
            }
        }
    };

    // get all dropdown values
    getAllDropdownValues();

    function getDataByCategory() {
        var categoryName = getUrlParameter("categoryName");
        var type = getUrlParameter("categoryType");

        if (categoryName && type) {
            console.log(categoryName);
            console.log(type);

            if (type == "SeriesTitle") {
                $("#cbo-seriestitle").val(categoryName);
            } else {
                $("#cbo-productseries").val(categoryName);
            }

            // update records
            updateFilter();
            search(1, "");
        }
    }

    // begin authentication area
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
                    ajaxGetStatistic(jsonResponse["id"]);
                    account_session_id = jsonResponse["id"];
                    ajaxGetAllNotification(jsonResponse["id"]);

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

    // end authentication area

    function getAllDropdownValues() {
        $("#loading").css("display", "block");
        ajaxGetManufacturer();
        ajaxGetProductseries();
        ajaxGetSeriestitle();
        setTimeout(function () {
            getDataByCategory();
            $("#loading").css("display", "none");
        }, 400);
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

                renderProductSeries(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function renderProductSeries(result) {

        $.each(result, function (key, entry) {
            $("#product-series-ul").append("<a class=\"title-li-a\" href=\"/gwa/pages/model.html?categoryName=" +
                entry.name + "&categoryType=ProductSeries\">\n" +
                "                            <li class=\"title-li\">" + entry.name + "</li>\n" +
                "                        </a>");
        })
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

                renderSeriesTitle(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function renderSeriesTitle(result) {

        $.each(result, function (key, entry) {
            $("#series-title-ul").append("<a class=\"title-li-a\" href=\"/gwa/pages/model.html?categoryName=" +
                entry.name + "&categoryType=SeriesTitle\">\n" +
                "                            <li class=\"title-li\">" + entry.name + "</li>\n" +
                "                        </a>");
        })
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
            searchValue: txtSearchValue,
            productseries: filterProductseries,
            seriestitle: filterSeriestitle,
            price: filterPrice,
            manufacturer: filterManufacturer,
            pagination: {
                currentPage: pageNumber,
                type: type
            },
            orderBy: filterOrderBy,
            cending: filterCending
        }

        ajaxSearchModel(formSearch);
    }

    // on document load
    updateFilter();
    search(1, "");

    $("#searchBtn").click(function (e) {
        e.preventDefault();

        // clear session
        sessionStorage.removeItem('data');
        sessionArr = [];
        console.log("Clear session");

        updateFilter();
        search(1, "");
    });

    function ajaxSearchModel(data) {

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/gwa/api/model/search",
            data: JSON.stringify(data),
            success: function (result) {
                console.log(result);
                // reset content div
                $("#pagination-content").empty();
                $("#ul-records-container").empty();
                $("#no-records").css("display", "none");

                if (result.modelDTOList.length > 0) {
                    pagination(result.pagination);
                }

                renderData(result.modelDTOList);

                // push to array
                sessionArr.push(result);
                // save to session
                sessionStorage.setItem('data', JSON.stringify(sessionArr));

                // get next by page's data
                calculateNextByPage(result.pagination.beginPage, result.pagination.lastPage, result.pagination.currentPage);
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

                    // check exist in session storage
                    // if exist
                    if (checkExistInSessionStorage(index)) {
                        console.log("Current page " + index + " existed in sessionStorage - Data in session storage: ");
                        console.log(global_value);

                        // reset pagination and data records
                        $("#pagination-content").empty();
                        $("#ul-records-container").empty();
                        $("#no-records").css("display", "none");

                        pagination(global_value.pagination);
                        renderData(global_value.modelDTOList);

                        // get next by page's data
                        calculateNextByPage(global_value.pagination.beginPage, global_value.pagination.lastPage,
                            global_value.pagination.currentPage);
                    } else {
                        console.log("Current page " + index + " not exist in sessionStorage!!!");
                        search(index, "");
                    }
                });
            }
        }

        $("#next").click(function (e) {
            e.preventDefault();

            if (checkExistInSessionStorage(value.currentPage + 1)) {
                console.log("Current page " + (value.currentPage + 1) + " existed in sessionStorage - Data in session storage: ");
                console.log(global_value);

                // reset pagination and data records
                $("#pagination-content").empty();
                $("#ul-records-container").empty();
                $("#no-records").css("display", "none");

                pagination(global_value.pagination);
                renderData(global_value.modelDTOList);

                // get next by page's data
                calculateNextByPage(global_value.pagination.beginPage, global_value.pagination.lastPage,
                    global_value.pagination.currentPage);
            } else {
                console.log("Current page " + (value.currentPage + 1) + " not exist in sessionStorage!!!");
                search(value.currentPage, "Next");
            }

        });

        $("#last").click(function (e) {
            e.preventDefault();

            if (checkExistInSessionStorage(value.lastPage)) {
                console.log("Current page " + value.lastPage + " existed in sessionStorage - Data in session storage: ");
                console.log(global_value);

                // reset pagination and data records
                $("#pagination-content").empty();
                $("#ul-records-container").empty();
                $("#no-records").css("display", "none");

                pagination(global_value.pagination);
                renderData(global_value.modelDTOList);

                // get next by page's data
                calculateNextByPage(global_value.pagination.beginPage, global_value.pagination.lastPage,
                    global_value.pagination.currentPage);
            } else {
                console.log("Current page " + value.lastPage + " not exist in sessionStorage");
                search(value.currentPage, "Last");
            }
        });

        $("#first").click(function (e) {
            e.preventDefault();

            if (checkExistInSessionStorage(1)) {
                console.log("Current page " + 1 + " existed in sessionStorage - Data in session storage: ");
                console.log(global_value);

                // reset pagination and data records
                $("#pagination-content").empty();
                $("#ul-records-container").empty();
                $("#no-records").css("display", "none");

                pagination(global_value.pagination);
                renderData(global_value.modelDTOList);

                // get next by page's data
                calculateNextByPage(global_value.pagination.beginPage, global_value.pagination.lastPage,
                    global_value.pagination.currentPage);
            } else {
                console.log("Current page " + 1 + " not exist in sessionStorage");
                search(value.currentPage, "First");
            }
        });

        $("#prev").click(function (e) {
            e.preventDefault();

            if (checkExistInSessionStorage(value.currentPage - 1)) {
                console.log("Current page " + (value.currentPage - 1) + " existed in sessionStorage - Data in session storage: ");
                console.log(global_value);

                // reset pagination and data records
                $("#pagination-content").empty();
                $("#ul-records-container").empty();
                $("#no-records").css("display", "none");

                pagination(global_value.pagination);
                renderData(global_value.modelDTOList);

                // get next by page's data
                calculateNextByPage(global_value.pagination.beginPage, global_value.pagination.lastPage,
                    global_value.pagination.currentPage);
            } else {
                console.log("Current page " + (value.currentPage - 1) + " not exist in sessionStorage");
                search(value.currentPage, "Prev");
            }
        });
    };

    $("#cbo-order-1").on('change', function () {
        // clear session
        sessionStorage.removeItem('data');
        sessionArr = [];
        console.log("Clear session");

        filterOrderBy = this.value;
        search(1, "");
    });

    $("#cbo-order-2").on('change', function () {
        // clear session
        sessionStorage.removeItem('data');
        sessionArr = [];
        console.log("Clear session");

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

    /*  This is for sessionStorage area */
    var sessionArr = [];
    // on reload page
    sessionStorage.removeItem('data');
    // for current value exist in sessionStorage
    var global_value;

    function checkExistInSessionStorage(currentPage) {
        // get data from sessionStorage and parse to JSON
        var data = JSON.parse(sessionStorage.getItem('data'));

        var found = false;

        if (data) {
            $.each(data, function (index, value) {
                if (value.pagination.currentPage == currentPage) {
                    global_value = value;
                    found = true;
                }
            })
        }

        return found;
    }

    function calculateNextByPage(beginPage, lastPage, pageNumber) {
        if ((pageNumber - 1) >= 1) {
            if (!checkExistInSessionStorage(pageNumber - 1)) {
                console.log("Page next by left: " + (pageNumber - 1) + " --> doesn't exist in sessionStorage!!!");
                searchNextBy(pageNumber - 1);
            } else {
                console.log("Page next by left: " + (pageNumber - 1) + " --> existed in sessionStorage!!!");
            }
        }
        if ((pageNumber + 1) <= lastPage) {
            if (!checkExistInSessionStorage(pageNumber + 1)) {
                console.log("Page next by right: " + (pageNumber + 1) + " --> doesn't exist in sessionStorage!!!");
                searchNextBy(pageNumber + 1);
            } else {
                console.log("Page next by right: " + (pageNumber + 1) + " --> existed in sessionStorage!!!");
            }
        }
    }

    function searchNextBy(pageNumber) {
        console.log("Ajax call to get page: " + pageNumber);

        var formSearch = {
            searchValue: txtSearchValue,
            productseries: filterProductseries,
            seriestitle: filterSeriestitle,
            price: filterPrice,
            manufacturer: filterManufacturer,
            pagination: {
                currentPage: pageNumber,
                type: ""
            },
            orderBy: filterOrderBy,
            cending: filterCending
        }

        ajaxCallNextByPage(formSearch);
    }

    function ajaxCallNextByPage(data) {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/gwa/api/model/search",
            data: JSON.stringify(data),
            success: function (result) {

                // push to array
                sessionArr.push(result);
                // save to session
                sessionStorage.setItem('data', JSON.stringify(sessionArr));
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    /*  End sessionStorage area */
})