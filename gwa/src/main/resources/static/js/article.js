$(document).ready(function () {
    authentication();
    var currentStatus = "Approved";

    var currentSortType = $("#cbSortType option:selected").val();

    var currentPage = 1;
    var totalPage = 1;
    var $pagination = $("#pagination-event");
    var isSearch = false;
    var defaultPaginationOpts = {
        totalPages: totalPage,
// the current page that show on start
        startPage: 1,

// maximum visible pages
        visiblePages: 3,

        initiateStartPageClick: false,

// template for pagination links
        href: false,

// variable name in href template for page number
        hrefVariable: '{{number}}',

// Text labels
        first: '&laquo;',
        prev: '❮',
        next: '❯',
        last: '&raquo;',

// carousel-style pagination
        loop: false,

// callback function
        onPageClick: function (event, page) {
            currentPage = page;
            $('#search-result').html("");
            searchArticle();
        },

// pagination Classes
        paginationClass: 'pagination',
        nextClass: 'page-item',
        prevClass: 'page-item',
        lastClass: 'page-item',
        firstClass: 'page-item',
        pageClass: 'page-item',
        activeClass: 'active',
        disabledClass: 'disabled'
    };
    $pagination.twbsPagination('destroy');
    if (totalPage > 1){
        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
            totalPages: totalPage
        }));
    }
    searchArticle();


    function appendResult(result){
        $('#search-result').html("");
        console.log("drawing");
        for (var i = 0; i < result.length; i++) {
            var article = $('<div class="single-blog-post d-flex row">\n' +
                '<div class="post-thumb col-sm-2" style="max-height: inherit;">\n' +
                '<img src="'+result[i].thumbImage+'" alt=""><br/>\n' +
                '</div>'+
                '                                <div class="post-data col-sm-10">\n' +
                '                                    <a href="#" class="post-catagory">' + result[i].category + '</a>\n' +
                '                                    <div class="post-meta">\n' +
                '                                        <a href="/gwa/article/detail?id=' + result[i].id +'" class="post-title">\n' +
                '                                            <h6>' + result[i].title + '</h6>\n' +
                '                                        </a>\n' +
                '                                        <p class="post-date">' + result[i].date + ' | Author: '+result[i].account.username+ '</p>\n' +
                '                                        <p >' + result[i].description + '</p>\n' +
                '                                    </div>\n' +
                '                                </div>\n' +
                '                            </div><hr/>');

            $('#search-result').append(article);
        }
        $pagination.twbsPagination('destroy');
        if (totalPage > 1){
            $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                totalPages: totalPage,
                currentPage: currentPage,
                startPage: currentPage

            }));
        }
    }
    function appendMyResult(result){
        for (var i = 0; i < result.length; i++) {
            var article = $('<div class="single-blog-post d-flex row">\n' +
                '<div class="post-thumb col-sm-2" style="max-height: inherit;">\n' +
                '<img src="'+result[i].thumbImage+'" alt=""><br/>\n' +
                '</div>'+
                '                                <div class="post-data col-sm-10">\n' +
                '                                    <a href="#" class="post-catagory">' + result[i].category + '</a>\n' +
                '                                    <div class="post-meta">\n' +
                '                                        <a href="/gwa/article/detail?id=' + result[i].id +'" class="post-title">\n' +
                '                                            <h6>' + result[i].title + ' <span >['+result[i].approvalStatus+']</span></h6>\n' +
                '                                        </a>\n' +
                '                                        <p class="post-date">' + result[i].date + '</p>\n' +
                '                                        <p >Author: '+result[i].account.username+ '</p>\n' +
                '                                    </div>\n' +
                '                                </div>\n' +
                '                            </div><hr/>');

            $('#search-result').append(article);
        }
        $pagination.twbsPagination('destroy');
        if (totalPage > 1){
            $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                totalPages: totalPage,
                currentPage: currentPage,
                startPage: currentPage

            }));
        }
    }
    $("#btnSearch").click(function (event) {
        event.preventDefault();
        var searchDiv = document.getElementById("search-result");
        while (searchDiv.firstChild) {
            searchDiv.removeChild(searchDiv.firstChild);
        }
        isSearch = true;
        searchArticle();
    });

    function searchArticle() {
        console.log("searching article");
        // var displayDiv = document.getElementById("search-result");
        var searchValue = $("#txtSearch").val();

        $.ajax({
            type : "POST",
            url : "/gwa/api/article/searchArticleByStatusAndPage",
            data : {
                title : searchValue,
                cate : $("#cbCateType").val(),
                status : currentStatus,
                sorttype :$("#cbSortType option:selected").val(),
                pageNum : currentPage
            },
            async: false,
            success : function(result, status) {
                var data = result[1];
                totalPage = result[0];
                console.log(result);
                console.log(status);
                console.log("seach numb of pages: "+result[0]);
                $pagination.twbsPagination('destroy');
                // if (totalPage > 1){
                    $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                        totalPages: totalPage
                    }));
                // }
                appendResult(data);
            },
            error : function(e) {
                alert("No article with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }
    $("#btnListArticle").click(function (event) {
        event.preventDefault();
        $('#searchDiv').css("display", "block");
        $('#cbCateType').css("display", "inline-block");
        $('#btnListArticle').addClass("active");
        $('#btnGetMyArticles').removeClass("active");

        isSearch = true;
        currentPage = 1;
        searchArticle();
    });
    $("#btnGetMyArticles").click(function (event) {
        event.preventDefault();
        $('#searchDiv').css("display", "none");
        $('#cbCateType').css("display", "none");
        $('#btnListArticle').removeClass("active");
        $('#btnGetMyArticles').addClass("active");

        var searchDiv = document.getElementById("search-result");
        while (searchDiv.firstChild) {
            searchDiv.removeChild(searchDiv.firstChild);
        }
        isSearch = true;
        currentPage = 1;
        getMyArticle();
    });
    function getMyArticle() {

        $.ajax({
            type : "POST",
            url : "/gwa/api/article/getMyArticle",
            data : {
                id : account_session_id,
                sorttype : currentSortType,
                pageNum : currentPage
            },
            async: false,
            success : function(result, status) {
                var data = result[1];
                totalPage = result[0];
                console.log(result);
                console.log(status);
                console.log("seach numb of pages: "+result[0]);
                $pagination.twbsPagination('destroy');
                $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                    totalPages: totalPage
                }));
                appendMyResult(data);
            },
            error : function(e) {
                alert("No event with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }

    /*   Begin authentication and notification  */
    // process UI
    $(document).click(function (event) {
        $("#dropdown-profile").css("display", "none");
        $("#dropdown-notification").css("display", "none");
    })



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
                    ajaxGetAllNotification(parseInt(jsonResponse["id"]));
                    console.log("got notification");
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
            },
            error: function (e) {
                console.log("ERROR: ", e);
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
console.log("rendering notification");
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
                } else if (type == "Tradepost") {
                    window.location.href = "/gwa/trade-market/view-trade?tradepostId=" + objectID;
                } else if (type == "OrderSent") {
                    window.location.href = "/gwa/trade-market/my-order";
                } else if (type == "OrderReceived") {
                    window.location.href = "/gwa/trade-market/view-trade?tradepostId=" + objectID;
                } else if (type == "Article") {
                    window.location.href = "/gwa/article/detail?id=" + objectID;
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