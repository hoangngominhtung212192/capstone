var myTradeData = [];
var currentSortType = 1 ;
var currentTabSelected = "approved";
var currentPage = 1;
var myTradeAccountId;
var totalPage = 1;
var searchValue = "";
var isSearch = false;
var $pagination = $("#pagination-mytrade");
var defaultPaginationOpts = {
    totalPages: totalPage,
// the current page that show on start
    startPage: 1,

// maximum visible pages
    visiblePages: 7,

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
        if(isSearch){
            myTradeData = searchMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType,searchValue);
        }else {
            myTradeData = loadMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType);
        }
        renderRecord();
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
$(document).ready(function () {
    $("#tradepostContainerDiv").hide();
    $("#searchDiv").hide();
    $(".notice-section").hide();
    $("#paginationDiv").hide();


    /*   Begin authentication and notification  */
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
            url: "http://localhost:8080/gwa/api/user/checkLogin",
            // async: false,
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


                    var role = jsonResponse["role"].name;
                    myTradeAccountId = jsonResponse["id"];

                    if (role == "BUYERSELLER") {
                        myTradeData = loadMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType);
                        renderRecord();
                        $("#tradepostContainerDiv").show();
                        $("#searchDiv").show();
                        $("#paginationDiv").show();
                        $pagination.twbsPagination('destroy');
                        if (totalPage > 1){
                            $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                                totalPages: totalPage
                            }));
                        }

                    } else if (role == "ADMIN") {

                        $("#adminBtn").css("display", "block");

                        $(".notice-section").show();
                        $("#noticeTitle").html("Opps! You are administrator, why you stay here...");
                        $("#noticeContent").html("Click <a href='/gwa/admin'>[HERE]</a> to back to your site.");
                    }else if (role == "MEMBER") {
                        $(".notice-section").show();
                        $("#noticeTitle").html("You are not buyer or seller so you can't not stay here...");
                        $("#noticeContent").html("Try post a trade and waiting administrator approve to become buyer/seller.");
                    }
                } else {
                    $(".notice-section").show();
                    $("#noticeTitle").html("Opps! You need login to stay here!");
                    $("#noticeContent").html("Click <a href='/gwa/login'>[HERE]</a> to login.");


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
});

/* ADD NEW NOTIFICATION FUNCTION */
function addNewNotification(desc,objectId,account,notiType) {

    var formNotification = {
        description: desc,
        objectID: objectId,
        account: {
            id: account // to admin
        },
        notificationtype: {
            id: notiType
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
            // console.log(result);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}
/*END ADD NEW NOTIFICATION FUNCTION */

/* Handle change tab event */
function changeTab(ele) {
    currentTabSelected = $(ele).attr("data-status");
    currentPage =1;
    if(isSearch){
        myTradeData = searchMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType,searchValue);
    }else {
        myTradeData = loadMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType);
    }
    renderRecord();
    $pagination.twbsPagination('destroy');
    if (totalPage > 1){
        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
            totalPages: totalPage
        }));
    }
}
/* End Handle change tab event */

/* Handle change Sorting event */
$("#sortTypeSelect").change(function () {
    currentSortType = $("#sortTypeSelect").val();
    if(isSearch){
        myTradeData = searchMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType,searchValue);
    }else {
        myTradeData = loadMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType);
    }
    renderRecord();
    $.growl.notice({title: "My trade", message: "Sorting by " + $("option[value='"+currentSortType+"']").text()});

});
/* END Handle change Sorting event */

/* Handle ENTER KEY in search input event */
$("#inputSearch").on('keyup', function (e) {
    if (e.keyCode == 13) {
        // console.log("Enter");
        searchValue = $("#inputSearch").val();
        if (searchValue === ""){
            if (isSearch === true){
                $(".notice-section").hide();
                currentPage = 1;
                myTradeData = loadMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType);
                $pagination.twbsPagination('destroy');
                if (totalPage > 1){
                    $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                        totalPages: totalPage
                    }));
                }
                renderRecord();
                isSearch = false;
            }
        }else {
            currentPage = 1;
            myTradeData = searchMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType,searchValue);
            $pagination.twbsPagination('destroy');
            if (totalPage > 1){
                $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                    totalPages: totalPage
                }));
            }
            renderRecord();
            isSearch = true;
            $(".notice-section").show();
            $("#noticeTitle").html("You are searching with keyword: " + searchValue);
            $("#noticeContent").html("Clear your searchbox and enter to remove search.");
        }
    }
});
/* END Handle ENTER KEY in search input event */


/* Load my trade data*/
function loadMyTradeData(accountId, status, pageNumber, sortType) {
    var result;
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/gwa/api/tradepost/get-my-trade",
        data: {
            accountId : accountId,
            status : status,
            pageNumber: pageNumber,
            sortType: sortType
        },
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                // console.log(jsonResponse);
                result = jsonResponse["data"];
                if (result != ""){
                    totalPage = jsonResponse["totalPage"];
                }else {
                    totalPage = 0;
                }
            } else {
                result = null;
                // console.log("Trade post not found!");
            }
        }
    });
    return result;
}
/* END Load my trade data */

/* Load Search result */
function searchMyTradeData(accountId, status, pageNumber, sortType, keyword) {
    var result;
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/gwa/api/tradepost/search-my-trade",
        data: {
            accountId : accountId,
            status : status,
            pageNumber: pageNumber,
            sortType: sortType,
            keyword: keyword
        },
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                // console.log(jsonResponse);
                result = jsonResponse["data"];
                if (result != ""){
                    totalPage = jsonResponse["totalPage"];
                }else {
                    totalPage = 0;
                }
            } else {
                result = null;
                // console.log("Trade post not found!");
            }
        }
    });
    return result;
}
/* END Load Search result */

/* Render Data */
function renderRecord() {
    var tabContentDiv;
    if(currentTabSelected === "approved"){
        tabContentDiv = $("#ontrade");
    }else if(currentTabSelected === "pending"){
        tabContentDiv = $("#pendingtrade");
    }if(currentTabSelected === "declined"){
        tabContentDiv = $("#rejectedtrade");
    }
    if (myTradeData.length <= 0) {
        tabContentDiv.html("<h4>No record.</h4>")
    }else {
        tabContentDiv.html("");
        for (var i = 0; i < myTradeData.length; i++) {
            rowData = myTradeData[i];
            postData = rowData["myTradePost"];
            //Div Record Row
            var itemWrap = $('<div class="tradeitem row"></div>');
            /* Thumbnail Div contain */
            var itemImgBoxWrap = $('<div class="item-image-box col-sm-4"></div>');
            var itemImgBox = $('<div class="item-image"></div>');
            var itemImgLink = $('<a href="/gwa/trade-market/view-trade?tradepostId=' + postData["id"] + '"></a>');
            var itemImgSrc = $('<img src="' + rowData["thumbnail"] + '" alt="Image" class="img-responsive"/>');
            itemImgLink.html(itemImgSrc);
            itemImgBox.html(itemImgLink);
            itemImgBoxWrap.html(itemImgBox);
            /* End Thumbnail Div contain */

            /* Item info Div contain */
            var itemInfoBoxWrap = $('<div class="item-info col-sm-8"></div>');

            /* Item info Box */
            var itemInfoBox = $('<div class="tradeinfo"></div>');
            var isNegotiableText = (postData["priceNegotiable"] === 1) ? "(Negotiable)": "";
            var itemPrice = $('<h3 class="item-price">$' + postData["price"] + '<span>' + isNegotiableText + '</span></h3>');
            var itemTitle = $('<h4 class="item-title"></h4>');
            var itemTitleLink = $('<a href="/gwa/trade-market/view-trade?tradepostId=' + postData["id"] + '"></a>');
            itemTitleLink.html(postData["title"]);
            itemTitle.html(itemTitleLink);
            var itemCat = $('<div class="item-cat"></div>');
            itemCat.html('<span>' + postData["brand"] + '</span> / <span>' + postData["model"] + '</span>');
            itemInfoBox.append(itemPrice);
            itemInfoBox.append(itemTitle);
            itemInfoBox.append(itemCat);
            /* End Item info Box */

            /* Item info Meta */
            var itemMetaBox = $('<div class="trademeta"></div>');

            /* Item info Meta Content */
            var metaContent = $('<div class="meta-content"></div>');
            var metaDate = $('<span class="dated">Posted On: <span>' + postData["postedDate"] + '</span></span>');
            var numOfRequest = rowData["numOfOnPaymentRequest"] + rowData["numOfPendingRequest"] + rowData["numOfSucceedRequest"];
            metaContent.append(metaDate);
            if(currentTabSelected === "approved"){
                if (numOfRequest > 0){
                    metaContent.append($('<span class="number-request">Request: </span>'));
                    metaContent.append($('<span>' + rowData["numOfPendingRequest"] + '</span>'));
                    metaContent.append($('<span class="number-payment">On Payment: </span>'));
                    metaContent.append($('<span>' + rowData["numOfOnPaymentRequest"] + '</span>'));
                    metaContent.append($('<span class="number-succed">Succeed: </span>'));
                    metaContent.append($('<span>' + rowData["numOfSucceedRequest"] + '</span>'));
                }else {
                    metaContent.append($('<span class="no-trade">No trading activity</span>'));
                }
            }else if(currentTabSelected === "pending"){
                metaContent.append($('<span class="pending-trade">Waiting approval</span>'));

            }else if(currentTabSelected === "declined"){
                var viewRejectBtn = $('<span class="rejected-trade">Rejected</span>');
                viewRejectBtn.append($('<a href="#" data-toggle="modal" data-target="#viewReasonModal" data-reason="'+ postData["rejectReason"] +'"><i class="fa fa-book"></i></a>'));
                metaContent.append(viewRejectBtn);
            }

            /* End Item info Meta Content */

            /* Item info Meta button action */
            var metaAction = $('<div class="user-option pull-right"></div>');
            var editButton = $('<a class="edit-item" href="/gwa/trade-market/edit-trade?tradepostID=' +postData["id"]+ '" data-title="tooltip" data-placement="top" ' +
                '                                   title="Edit this trade"><i class="fa fa-pencil"></i></a>');
            var delButton = $('<a class="delete-item" href="#" data-title="tooltip" data-placement="top" ' +
                '                                   title="Delete this trade" data-id="'+postData["id"]+'" ' +
                '                                   data-toggle="modal" data-target="#deleteModal"><i class="fa fa-times"></i></a>');
            var upQuantityBtn = $('<a class="edit-item" href="#" data-title="tooltip" data-placement="top" ' +
                '                                   title="Update Quantity" ' +
                '                                   data-toggle="modal" data-target="#updateQuantityModal" ' +
                '                                   data-id="'+postData["id"]+'" data-quantity="'+postData["quantity"]+'"> ' +
                '                                    <i class="fa fa-cart-plus"></i></a>');
            if(currentTabSelected === "approved"){
                if (numOfRequest > 0){
                    metaAction.append(upQuantityBtn);
                }else {
                    metaAction.append(editButton);
                    metaAction.append(delButton);
                }
            }else if(currentTabSelected === "pending"){
                metaAction.append(editButton);
                metaAction.append(delButton);
            }else if(currentTabSelected === "declined"){
                metaAction.append(editButton);
                metaAction.append(delButton);
            }
            /* End Item info Meta button action */

            itemMetaBox.append(metaContent);
            itemMetaBox.append(metaAction);
            /* End Item info Meta */

            itemInfoBoxWrap.append(itemInfoBox);
            itemInfoBoxWrap.append(itemMetaBox);
            /* End Item info Div contain */


            itemWrap.append(itemImgBoxWrap);
            itemWrap.append(itemInfoBoxWrap);
            tabContentDiv.append(itemWrap);
        }
    }
    
}
/* END Render Data */

/* AJAX call for update quantity function */
function updateQuantity(tradepostId) {
    var updateQuantityVal = $("#updateQuantityValue").val();
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/gwa/api/tradepost/update-quantity",
        data: {
            tradepostId : tradepostId,
            newQuantity : updateQuantityVal
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            myTradeData = loadMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType);
            renderRecord();
            $.growl.notice({title: txtStatus, message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#updateQuantityModal').modal('hide');
}
/* END AJAX call for update quantity function */

/* AJAX call for delete tradepost function */
function deleteTradePost(tradepostId) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/gwa/api/tradepost/delete-trade-post",
        data: {
            tradepostId : tradepostId
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            myTradeData = loadMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType);
            renderRecord();
            $.growl.notice({title: txtStatus, message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#deleteModal').modal('hide');
}
/* END AJAX call for update quantity function */

/* Call tooltip jquery */
$('[data-title="tooltip"]').tooltip();

/* handle tab-content display event */

$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
   $(e.target).parent().addClass("active");// newly activated tab
    $(e.relatedTarget).parent().removeClass("active"); // previous active tab
});

/* Handle delete modal open event */
$('#deleteModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var id = button.data('id') ;
    var modal = $(this);
    modal.find('.modal-footer #deleteTradePostBtn').attr('onclick','deleteTradePost('+ id +');');
});
/* Handle update quantity modal open event */
$('#updateQuantityModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var quantity = button.data('quantity');
    var id = button.data('id');
    var modal = $(this);
    modal.find('.modal-body input').val(quantity);
    modal.find('.modal-footer #updateQuantityBtn').attr('onclick','updateQuantity('+ id +');');
    modal.find('.modal-footer #updateQuantityBtn').attr('disabled','disabled');
});

/* Handle view reason modal open event */
$('#viewReasonModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var reason = button.data('reason') ;
    var modal = $(this);
    modal.find('.modal-body').text(reason);
});

/* Handle update quantity input on keyup event */
$("#updateQuantityValue").change(function () {
   if ($(this).val() !== $("#oldQuantityValue").val() && $(this).val() >= 0 && $(this).val() <= 1000){
        $("#updateQuantityBtn").removeAttr("disabled");
   }else {
       $("#updateQuantityBtn").attr("disabled","disabled");
   }
});