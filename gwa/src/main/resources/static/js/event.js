$(document).ready(function () {
    var currentStatus = $( "#cboStatus option:selected").val();

    var currentSortType = $("#cbSortType option:selected").val();

    var currentPage = 1;
    var totalPage = 1;
    var $pagination = $("#pagination-event");
    var isSearch = false;
    var currentSearchTab = 1;
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
        console.log("clicked on page: "+page)
        currentPage = page;
        $('#search-result').html("");
        if (currentSearchTab == 1) {
            searchEv();
        }
        if (currentSearchTab == 2) {
            getMyEv();
        }
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

    searchEv();
    // getEventData();
    $pagination.twbsPagination('destroy');
    if (totalPage > 1){
        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
            totalPages: totalPage
        }));
    }

    $("#btnSearch").click(function (event) {
        currentSearchTab = 1;
        currentPage = 1;
        event.preventDefault();
        var searchDiv = document.getElementById("search-result");
        while (searchDiv.firstChild) {
            searchDiv.removeChild(searchDiv.firstChild);
        };
        isSearch = true;
        searchEv();
        $('#lblMessage').css("display", "none");

    });


    function searchEv() {

        var searchValue = $("#txtSearch").val();

        console.log("searchvalue: "+searchValue);
        console.log("sorttype: "+$("#cbSortType option:selected").val());
        $.ajax({
            type : "POST",
            url : "/gwa/api/event/searchEventByStatusAndPage",
            data : {
                title : searchValue,
                status : $( "#cboStatus option:selected").val(),
                sorttype : $("#cbSortType option:selected").val(),
                pageNum : currentPage
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

                appendResult(data);
            },
            error : function(e) {
                alert("No event with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }

    function appendResult(result){
        var searchDiv = document.getElementById("search-result");
        while (searchDiv.firstChild) {
            searchDiv.removeChild(searchDiv.firstChild);
        }

        for (var i = 0; i < result.length; i++) {
            var article = $('<div class="single-blog-post d-flex row">\n' +
                '<div class="post-thumb col-sm-3" style="max-height: inherit;">\n' +
                '<img src="'+result[i].thumbImage+'" alt=""><br/>\n' +
                '</div>'+
                '                                <div class="post-data col-sm-9" style="max-height: inherit;    ">\n' +
                '                                    <div class="post-meta">\n' +
                '                                        <a class="post-title" href="/gwa/event/detail?id=' + result[i].id +'">\n' +
                '                                            <h6>' + result[i].title + '</h6>\n' +
                '                                        </a>\n' +
                '<p class="post-date"><span>From: '+result[i].startDate+'</span> to <span>'+result[i].endDate+'</span></p>'+
                '<p><span>Ticket price: '+result[i].ticketPrice+'</span></p>\n' +
                '<p class="post-excerp" style="margin-bottom: 0px">'+result[i].description+'</p>'+
                '                                    </div>\n' +
                '                                </div>\n' +
                '                            </div>' +
                '<hr />');
            $('#search-result').append(article);
        }
        $pagination.twbsPagination('destroy');
        if (totalPage > 1){
            $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                totalPages: totalPage,
                currentPage: currentPage,
                startPage: currentPage,

            }));
        }
    }
    $("#btnGetEventList").click(function (event) {
        currentSearchTab = 1;
        event.preventDefault();
        $('#searchDiv').css("display", "block");
        $('#cboStatus').css("display", "inline-block");
        $('#btnGetEventList').addClass("active");
        $('#btnGetRegEvnts').removeClass("active");

        // var searchDiv = document.getElementById("search-result");
        // while (searchDiv.firstChild) {
        //     searchDiv.removeChild(searchDiv.firstChild);
        // }
        isSearch = true;
        currentPage = 1;
        searchEv();
    })
    $("#btnGetRegEvnts").click(function (event) {
        currentSearchTab = 3;
        event.preventDefault();
        $('#searchDiv').css("display", "none");
        $('#cboStatus').css("display", "none");
        $('#btnGetEventList').removeClass("active");
        $('#btnGetRegEvnts').addClass("active");

        var searchDiv = document.getElementById("search-result");
        while (searchDiv.firstChild) {
            searchDiv.removeChild(searchDiv.firstChild);
        }
        isSearch = true;
        currentPage = 1;
        getMyEv();
    })

    function getMyEv(){
        $.ajax({
            type : "POST",
            url : "/gwa/api/event/getMyListEvent",
            data : {
                accountID : account_session_id,
                sorttype : $("#cbSortType option:selected").val(),
                pageNum : currentPage
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

                appendResult(data);
            },
            error : function(e) {
                console.log("ERROR: ", e);
            }
        });
    }

    /*   Begin authentication and notification  */
    // process UI
    $(document).click(function (event) {
        $("#dropdown-profile").css("display", "none");
        $("#dropdown-notification").css("display", "none");
    });
    var currentAddress = "";
    // autoGetYourLocation();
    function autoGetYourLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(showPosition);
        } else {
            alert("no geolocation");
            $.growl.notice({title: "Location Error", message: "Geolocation is not supported by this browser."});
        }
    }

    var addressWithLatLong;
    $("#btnNearbyEvents").click(function (event) {
        currentSearchTab = 2;
        $('#cboStatus').css("display", "none");
        event.preventDefault();
        // showPosition();

        autoGetYourLocation();
        $('#lblMessage').css("display", "block");

    })

    function showPosition(position) {
        var lat = position.coords.latitude;
        var lng = position.coords.longitude;
        var google_map_pos = new google.maps.LatLng(lat, lng);

        /* Use Geocoder to get address */
        var google_maps_geocoder = new google.maps.Geocoder();
        google_maps_geocoder.geocode(
            {'latLng': google_map_pos},
            function (results, status) {
                if (status == google.maps.GeocoderStatus.OK && results[0]) {
                    currentAddress = results[0].formatted_address;
                    // console.log(results[0].formatted_address);
                    $('#txtMyLocation').append(currentAddress);
                    currentAddress += "@" + lat;
                    currentAddress += "@" + lng;
                    getNearEvents();

                }
            }
        );
    }
    function getNearEvents(){

        console.log("getting nearby events "+currentAddress);
        $.ajax({
            type : "POST",
            url : "/gwa/api/event/getNearbyEvent",
            data : {
                location : currentAddress,
                range : 10000,
                sorttype : $("#cbSortType option:selected").val(),
                pageNum : currentPage
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

                appendResult(data);
            },
            error : function(e) {
                console.log("ERROR: ", e);
            }
        });
    }

    // document.getElementById("cbSortType").addEventListener("change", );
    $("#cbSortType").on('change', function () {
        searchCurrentType(currentSearchTab);
    });
    $("#cboStatus").on('change', function () {
        searchCurrentType(currentSearchTab);
    });
    // document.getElementById("cboStatus").addEventListener("change", searchCurrentType(currentSearchTab));

    function searchCurrentType(type){
        if (type == 1){
            console.log("SEARCHING type1");
            searchEv();
        }
        if (type == 2){
            console.log("NEAR EVEtype2");
            getNearEvents();
        }
        if (type == 3){
            console.log("MY EVEtype3");
            getMyEv();
        }
    }

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

                lastPage = result.lastPage;
                renderNotification(result.notificationList, result.notSeen);
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

    function renderNotification(result, countNotSeen) {

        $.each(result, function (index, value) {

            var appendNotification = "";

            if (!value.seen) {
                // not seen yet
                appendNotification += "<li>\n"
            } else {
                // already seen
                appendNotification += "<li style='background-color: white;'>\n"
            }

            var iconType = "<i class=\"fa fa-warning text-yellow\" style=\"color: darkred;\"></i> ";

            if (value.notificationtype.name == "Profile") {
                iconType = "<i class=\"fa fa-user-circle-o text-yellow\" style=\"color: darkred;\"></i> ";
            } else if (value.notificationtype.name == "Model") {
                iconType = "<i class=\"fa fa-warning text-yellow\" style=\"color: darkred;\"></i> ";
            } else if (value.notificationtype.name == "Tradepost") {
                iconType = "<i class=\"fa fa-check-square-o text-yellow\" style=\"color: darkred;\"></i> ";
            } else if (value.notificationtype.name == "OrderSent") {
                iconType = "<i class=\"fa fa fa-paper-plane text-yellow\" style=\"color: darkred;\"></i> ";
            } else if (value.notificationtype.name == "OrderReceived") {
                iconType = "<i class=\"fa fa fa-bullhorn text-yellow\" style=\"color: darkred;\"></i> ";
            }

            appendNotification += "<a id='" + value.id + "-" + value.notificationtype.name + "-" + value.objectID +
                "' href=\"#\">\n" +
                iconType + value.description + "</a>\n" +
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
                } else if (type == "Event") {
                    window.location.href = "/gwa/event/detail?id=" + objectID;
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
    /*   End authentication and notification  */

    /*  This is for firebase area */
    var config = {
        apiKey: "AIzaSyCACMwhbLcmYliWyHJgfkd8IW6oPUoupIM",
        authDomain: "gunplaworld-51eee.firebaseapp.com",
        databaseURL: "https://gunplaworld-51eee.firebaseio.com",
        projectId: "gunplaworld-51eee",
        storageBucket: "gunplaworld-51eee.appspot.com",
        messagingSenderId: "22850579681"
    };

    firebase.initializeApp(config);

    var messaging = firebase.messaging();

    navigator.serviceWorker.register("/gwa/pages/firebase-messaging-sw.js", {
        scope: "/gwa/pages/"
    }).then(function (registration) {
        messaging.useServiceWorker(registration);

        messaging.requestPermission()
            .then(function (value) {
                console.log("Have permission!");
            }).catch(function (err) {
            console.log("Error occur!", err);
        })

        messaging.onMessage(function (payload) {
            console.log('onMessage: ', payload);

            pageNumber = 1;
            $("#ul-notification").empty();
            if (payload.data.title == "Model" || payload.data.title == "Event") {
                toastr.error(payload.data.body, payload.data.title + " Notification", {timeOut: 5000});
            } else {
                toastr.info(payload.data.body, payload.data.title + " Notification", {timeOut: 5000});
            }
            setTimeout(function () {
                ajaxGetAllNotification(account_session_id);
            }, 1000);
        })
    })
    /* This is end of firebase  */
})