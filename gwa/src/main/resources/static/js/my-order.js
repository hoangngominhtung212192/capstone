var loginAccount;
var loginAccountName;
var currentTabSelected = "approved";
var currentSortSelected = 1;
var currentPage = 1;
var totalPage = 1;
var currentAddress;
var $pagination = $("#pagination-my-order");
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
    prev: '&lsaquo;',
    next: '&rsaquo;',
    last: '&raquo;',

// carousel-style pagination
    loop: false,

// callback function
    onPageClick: function (event, page) {
        currentPage = page;
        loadMyOrderData();
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
    $("#myOrderContainerDiv").hide();
    $(".notice-section").hide();

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
                    var role = jsonResponse["role"].name;
                    loginAccount = jsonResponse["id"];

                    var username = jsonResponse["username"];
                    loginAccountName = username;
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

                    if (role == "MEMBER" || role == "BUYERSELLER") {
                        loadMyOrderData();
                        $("#myOrderContainerDiv").show();

                        $pagination.twbsPagination('destroy');
                        if (totalPage > 1) {
                            $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                                totalPages: totalPage
                            }));
                        }


                    } else if (role == "ADMIN") {

                        $("#adminBtn").css("display", "block");

                        $(".notice-section").show();
                        $("#noticeTitle").html("Opps! You are administrator, why you stay here...");
                        $("#noticeContent").html("Click <a href='/gwa/admin'>[HERE]</a> to back to your site.");
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

            if (payload.data.accountID == account_session_id) {
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
            }
        })
    })
    /* This is end of firebase  */

    autoGetYourLocation();
    <!-- Tooltip -->
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

function changeTab(ele) {
    currentTabSelected = $(ele).attr("data-status");
    currentPage = 1;
    loadMyOrderData();
    $pagination.twbsPagination('destroy');
    if (totalPage > 1) {
        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
            totalPages: totalPage
        }));
    }
}

$("#sortTypeSelect").change(function () {
    currentSortSelected = $("#sortTypeSelect").val();
    loadMyOrderData();
    $.growl.notice({
        title: "My Order",
        message: "Sorting by " + $("option[value='" + currentSortSelected + "']").text()
    });

});



function loadMyOrderData() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/gwa/api/tradepost/get-my-order",
        data: {
            accountId: loginAccount,
            status: currentTabSelected,
            pageNumber: currentPage,
            sortType: currentSortSelected
        },
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                // console.log(jsonResponse);
                var data = jsonResponse["data"];
                if (data != "") {
                    totalPage = jsonResponse["totalPage"];
                } else {
                    totalPage = 0;
                }
                renderData(data);
                // console.log(data);
            } else {
                result = null;
                // console.log("Trade post not found!");
            }
        }
    });
}

function renderData(data) {
    var tabContentDiv;
    if (currentTabSelected === "approved") {
        tabContentDiv = $("#onpayment");
    }
    if (currentTabSelected === "pending") {
        tabContentDiv = $("#pendingorders");
    }
    if (currentTabSelected === "succeed") {
        tabContentDiv = $("#succeedorders");
    }
    if (currentTabSelected === "others") {
        tabContentDiv = $("#otherorders");
    }
    if (data.length <= 0) {
        tabContentDiv.html("<h4>No record.</h4>")
    } else {
        tabContentDiv.html("");
        for (var i = 0; i < data.length; i++) {
            var rowData = data[i];
            var status = rowData["orderStatus"];

            var itemRow = $('<div class="tradeitem row"></div>');

            //Thumbnail box
            var itemImageBox = $('<div class="item-image-box col-sm-4"></div>');
            var itemImage = $('<div class="item-image"></div>');
            var image = $('<a href="/gwa/trade-market/view-trade?tradepostId=' + rowData["tradepostId"] + '"><img src="' + rowData["tradepostThumbnail"] + '" alt="Image" class="img-responsive"></a>');
            itemImage.html(image);
            itemImageBox.html(itemImage);
            itemRow.append(itemImageBox);

            //Item Info
            var itemInfo = $('<div class="item-info col-sm-8"></div>');
            //Trade info
            var tradeInfo = $('<div class="tradeinfo"></div>');
            var title = $('<h3 class="item-price">Order From: <a href="/gwa/trade-market/view-trade?tradepostId=' + rowData["tradepostId"] + '">' + rowData["tradepostTitle"] + '</a></h3>');
            tradeInfo.append(title);
            var itemCat = $('<div class="item-cat"></div>');
            if (status === "declined" || status === "cancelled") {
                itemCat.append('<span>Quantity: </span><span style="color: red">' + rowData["orderQuantity"] + '</span> - ' +
                    '<span>Total: </span><span style="color: red">' + rowData["orderPay"] + '$</span><br/>' +
                    '<span>This order has been ' + rowData["orderStatus"] + '.</span>');
            } else {
                itemCat.append('<span>Quantity: </span><span style="color: red">' + rowData["orderQuantity"] + '</span> - <span>Total: </span><span style="color: red">' + rowData["orderPay"] + '$</span>');
            }
            tradeInfo.append(itemCat);
            itemInfo.append(tradeInfo);

            //Trade meta

            var tradeMeta = $('<div class="trademeta"></div>');

            var metaContent = $('<div class="meta-content"></div>');
            metaContent.append('<span class="dated">Ordered On: <a href="#">' + rowData["orderedDate"] + '</a></span>');
            if (status === "pending") {
                metaContent.append('<span class="number-succed">Waiting approve</span>');
            }
            if (status === "approved") {
                metaContent.append('<span class="number-payment">Accepted On: <a href="#">' + rowData["orderSetDate"] + '</a></span>');
            }
            if (status === "succeed") {
                metaContent.append('<span class="number-succed">Succeed On: <a href="#">' + rowData["orderSetDate"] + '</a></span>');
            }
            if (status === "declined") {
                metaContent.append('<span class="number-request">Declined On: <a href="#">' + rowData["orderSetDate"] + '</a></span>');
            }
            if (status === "cancelled") {
                metaContent.append('<span class="number-request">Cancelled On: <a href="#">' + rowData["orderSetDate"] + '</a></span>');
            }
            tradeMeta.append(metaContent);

            var metaAction = $('<div class="user-option pull-right"></div>');
            var viewContactBtn = $('<a href="#contactModal" data-title="tooltip" data-placement="top" data-toggle="modal"  ' +
                'title="View contact information" data-fullname="' + rowData["ownerName"] + '"  data-phone="' + rowData["ownerPhone"] + '"  data-email="' + rowData["ownerEmail"] + '"><i class="fa fa-eye"></i></a>');
            if (status === "pending") {
                var updateBtn = $('<a href="#updateModal" data-title="tooltip" data-placement="top" data-toggle="modal" ' +
                    'title="Update request" data-orderid="' + rowData["orderId"] + '" data-quantity="' + rowData["orderQuantity"] + '"><i class="fa fa-cart-plus"></i></a>');
                var cancelBtn = $('<a class="delete-item" href="#cancelModal" data-title="tooltip" data-placement="top" data-toggle="modal"  ' +
                    'title="Cancel this order" data-orderid="' + rowData["orderId"] + '"  data-ownerid="' + rowData["ownerId"] + '"   data-tradepostid="' + rowData["tradepostId"] + '"><i class="fa fa-share-square"></i></a>');
                // metaAction.append(updateBtn);
                metaAction.append(cancelBtn);
            }
            if (status === "approved") {
                var directionBtn = $('<a href="#directionModal" data-title="tooltip" data-placement="top" data-toggle="modal"  ' +
                    'title="' + rowData["ownerAddress"] + '" data-address="' + rowData["ownerAddress"] + '"><i class="fa fa-map-marker"></i></a>');
                var cancelBtn = $('<a class="delete-item" href="#cancelModal" data-title="tooltip" data-placement="top" data-toggle="modal"  ' +
                    'title="Cancel this order" data-orderid="' + rowData["orderId"] + '" data-ownerid="' + rowData["ownerId"] + '"  data-tradepostid="' + rowData["tradepostId"] + '"><i class="fa fa-share-square"></i></a>');
                metaAction.append(viewContactBtn);
                metaAction.append(directionBtn);
                metaAction.append(cancelBtn);
            }
            if (status === "succeed") {
                var ratingBtn;
                var isRated = rowData["rated"];
                if (!isRated) {
                    ratingBtn = $('<a class="unrated-item" href="#ratingModal" data-title="tooltip" data-placement="top" data-toggle="modal" ' +
                        'title="Rating this trade"  data-orderid="' + rowData["orderId"] + '" data-ownerid="' + rowData["ownerId"] + '"  data-tradepostid="' + rowData["tradepostId"] + '"><i class="fa fa-star"></i></a>');
                } else {
                    ratingBtn = $('<a class="rated-item" data-title="tooltip" data-placement="top" ' +
                        'title="This trade is rated"><i class="fa fa-star"></i></a>');
                }
                metaAction.append(viewContactBtn);
                metaAction.append(ratingBtn);
            }
            if (status === "declined" || status === "cancelled") {
                var reasonBtn = $('<a class="delete-item" href="#reasonModal" data-title="tooltip" data-placement="top" data-toggle="modal" ' +
                    'title="View reason" data-reason="' + rowData["orderReason"] + '"><i class="fa fa-book"></i></a>');
                metaAction.append(reasonBtn);
            }

            tradeMeta.append(metaAction);
            itemInfo.append(tradeMeta);
            itemRow.append(itemInfo);
            tabContentDiv.append(itemRow);
        }
    }
    $('[data-title="tooltip"]').tooltip();
}

function cancelOrder(orderID, reason, ownerId, tradepostId) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/gwa/api/tradepost/cancel-order",
        data: {
            orderId: orderID,
            reason: reason
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            var desc = loginAccountName + " has been cancelled order from trade post id=" + tradepostId;
            var notiType = 5;
            addNewNotification(desc,tradepostId, ownerId, notiType);
            loadMyOrderData();
            $.growl.notice({title: txtStatus, message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#cancelModal').modal('hide');
}

function ratingTrader(orderId, feedbackType, rating, comment, ownerId, tradepostId) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/gwa/api/tradepost/rating-trade",
        data: {
            orderId: orderId,
            feedbackType: feedbackType,
            rating: rating,
            comment: comment
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            var desc = loginAccountName + " has been rated order from trade post id=" + tradepostId;
            var notiType = 5;
            addNewNotification(desc,tradepostId, ownerId, notiType);
            loadMyOrderData();
            $.growl.notice({message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#ratingModal').modal('hide');
}

$('#cancelModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var id = button.data('orderid');
    var ownerId =button.data('ownerid');
    var tradepostId =button.data('tradepostid');
    var modal = $(this);
    modal.find('input,textarea').val('');
    modal.find('span .error').remove();
    $("#cancelForm").validate({
        rules: {
            cancelReasonText: {
                required: true,
                minlength: 10
            }
        },
        messages: {
            cancelReasonText: {
                required: "You need tell a reason",
                minlength: "Reason too short."
            }
        },
        submitHandler: function (form) {
            var reason = "CANCEL FROM TRADER: " + $("#cancelReasonText").val();
            cancelOrder(id, reason, ownerId, tradepostId);
        }
    })
});

$('#contactModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var fullname = button.data('fullname');
    var phone = button.data('phone');
    var email = button.data('email');
    var infoContainer = $('<p></p>');
    infoContainer.append($('<strong>Owner Fullname: </strong><strong style="color: blue">' + fullname + '</strong><br/>'));
    infoContainer.append($('<strong>Phone: </strong><strong style="color: blue">' + phone + '</strong><br/>'));
    infoContainer.append($('<strong>Email: </strong><strong style="color: blue">' + email + '</strong>'));
    var modal = $(this);
    modal.find('.modal-body').html(infoContainer);
});

$('#reasonModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var reason = button.data('reason');
    var modal = $(this);
    modal.find('.modal-body').html('<strong style="color: red">' + reason + '</strong>');
});
$('#ratingModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var orderId = button.data('orderid');
    var ownerId =button.data('ownerid');
    var tradepostId =button.data('tradepostid');
    var modal = $(this);
    modal.find('textarea').val('');
    $('input:radio[name="ratingStar"]').prop('checked', false);
    modal.find('label .error').remove();
    $("#ratingForm").validate({
        ignore: [],
        rules: {
            feedbackText: {
                required: true,
                minlength: 10
            },
            ratingStar: {
                required: true,
            }
        },
        messages: {
            feedbackText: {
                required: "You need tell a feedback",
                minlength: "feedback too short."
            },
            ratingStar: {
                required: "Please select your rating"
            }
        },
        submitHandler: function (form) {
            var rating = $("input:radio[name='ratingStar']:checked").val();
            var comment = $("#feedbackText").val();
            ratingTrader(orderId, 2, rating, comment, ownerId, tradepostId);
        }
    })


});
function autoGetYourLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    } else {
        $.growl.notice({title: "Location Error", message: "Geolocation is not supported by this browser."});
    }
}

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

            }
        }
    );
}
function initialize_direction(start, end) {
    var directionDisplay;
    var directionsService = new google.maps.DirectionsService();
    var direction_map;

    directionDisplay = new google.maps.DirectionsRenderer();
    var myOptions = {
        mapTypeId: google.maps.MapTypeId.ROADMAP,
    }
    direction_map = new google.maps.Map(document.getElementById("map-canvas"), myOptions);
    directionDisplay.setMap(direction_map);

    var request = {
        origin:start,
        destination:end,
        travelMode: google.maps.DirectionsTravelMode.DRIVING
    };
    directionsService.route(request, function(response, status) {
        if (status == google.maps.DirectionsStatus.OK) {
            directionDisplay.setDirections(response);
        }
    });
}
$('#directionModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var toAddress = button.data("address");
    initialize_direction(currentAddress, toAddress);


});


<!-- Tab panel  -->
$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    $(e.target).parent().addClass("active");// newly activated tab
    $(e.relatedTarget).parent().removeClass("active"); // previous active tab
});

$.validator.setDefaults({
    highlight: function (element) {
        $(element).closest('.form-control').addClass('is-invalid');
        $(element).parent().parent('.input-group').closest('.input-group').addClass('was-validated');
    },
    unhighlight: function (element) {
        $(element).closest('.form-control').removeClass('is-invalid').addClass('is-valid');
    },
    errorElement: 'span',
    errorClass: 'invalid-feedback',
    errorPlacement: function (error, element) {
        if (element.parent().parent('.input-group').length) {
            error.insertAfter(element.parent());
        } else {
            error.insertAfter(element);
        }
    }
});
