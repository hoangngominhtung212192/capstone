var postAccount;
var loginAccount;
var loginAccountName;
var postId;
var isTrading = false;
var currentTabSelected = "pending";
var currentSortSelected = 1;
var currentPage = 1;
var totalPage = 1;
var notFound = false;
var $pagination = $("#pagination-order");
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
        loadOrderData();
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
    $("#noticeSection").hide();
    postId = getUrlParameter("tradepostId");
    if (checkValidRequest(postId)) {
        loadTradePostData();
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
                        var currentAccount = jsonResponse["id"];
                        loginAccount = currentAccount;


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
                            if (postAccount === currentAccount) {
                                $("#openSendOrderModalBtn").hide();
                                if (isTrading){
                                    $("#sendOrderHelpBlock").html("You can't send order to your self because you owned this trade.");
                                    $("#noticeSection").show();
                                    loadOrderData();
                                    $pagination.twbsPagination('destroy');
                                    if (totalPage > 1) {
                                        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                                            totalPages: totalPage
                                        }));
                                    }
                                }else {
                                    $("#sendOrderHelpBlock").html("This trade is waiting administrator approve or has been declined.");
                                }
                            } else {
                                if (isTrading){
                                    $("#noticeSection").hide();
                                }else {
                                    $("#noticeSection").html("<h4>Opps! 404 Page not found!</h4>");
                                    $("#noticeSection").show();
                                    $("#slideSection").hide();
                                    $("#descBox").hide();
                                }

                            }

                        } else if (role == "ADMIN") {

                            $("#adminBtn").css("display", "block");

                            if (isTrading){
                                $("#sendOrderHelpBlock").html("You can't send order because are administrator.");
                            }else {
                                $("#sendOrderHelpBlock").html("This post is pending approve or has been declined.");
                            }
                            $("#openSendOrderModalBtn").hide();
                            $("#noticeSection").hide();
                            // console.log("Admin is accessing !");
                        }
                    } else {
                        if (isTrading){
                            $("#sendOrderHelpBlock").html("You need login to send order.");
                            $("#openSendOrderModalBtn").attr("disabled", "true");
                            $("#noticeSection").hide();
                        }else {
                            $("#noticeSection").html("<h4>Opps! 404 Page not found!</h4>");
                            $("#noticeSection").show();
                            $("#slideSection").hide();
                            $("#descBox").hide();
                        }



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

                var iconType = "<i class=\"fa fa-warning text-yellow\" style=\"color: darkred;\"></i> ";

                if (value.notificationtype.name == "Profile"){
                    iconType = "<i class=\"fa fa-user-circle-o text-yellow\" style=\"color: darkred;\"></i> ";
                }else if (value.notificationtype.name == "Model") {
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

        if (!notFound) {

            if (!isTrading) $("#reportBtn").html("");
        }

    } else {
        notFound = true;
        $("#noticeSection").html("<h4>Opps! 404 Page not found!</h4>");
        $("#noticeSection").show();
        $("#slideSection").hide();
        $("#descBox").hide();
        console.log("Trade post not found!");
    }
    <!-- Tooltip -->
    $('[data-title="tooltip"]').tooltip();
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
            console.log(result);
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
    loadOrderData();
    $pagination.twbsPagination('destroy');
    if (totalPage > 1) {
        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
            totalPages: totalPage
        }));
    }
}

$("#sortTypeSelect").change(function () {
    currentSortType = $("#sortTypeSelect").val();
    loadOrderData();
    $.growl.notice({title: "Trading", message: "Sorting by " + $("option[value='" + currentSortType + "']").text()});

})



function loadOrderData() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/gwa/api/tradepost/get-order-by-trade-post",
        data: {
            tradepostId: postId,
            status: currentTabSelected,
            pageNumber: currentPage,
            sortType: currentSortSelected
        },
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                console.log(jsonResponse);
                totalPage = jsonResponse[0];
                var orderData = jsonResponse[2];
                    renderOrderData(orderData);
            } else {
                console.log("Trade post not found!");
            }
        }
    });
}

function renderOrderData(data) {
    var container;

    if (currentTabSelected === "pending") {
        container = $("#pendingContainer");
    } else if (currentTabSelected === "approved") {
        container = $("#paymentContainer");
    } else if (currentTabSelected === "succeed") {
        container = $("#succeedContainer");
    } else if (currentTabSelected === "cancelled") {
        container = $("#cancelledContainer");
    }

    if (data.length <= 0) {
        container.html("No activity.");
    } else {
        container.html("");
        for (var i = 0; i < data.length; i++) {
            var trHtml = $('<tr></tr>');
            var thNoHtml = $('<th scope="row">' + (i + 1) + '</th>');
            var tdUserHtml = $('<td><button type="button" class="btn btn-success" data-toggle="modal" ' +
                'data-target="#profileModal" data-userid="' + data[i]["account"]["id"] + '">' + data[i]["account"]["username"] +
                ' <i class="fa fa-eye"></i></button></td>');
            var tdOrderDateHtml = $('<td>' + data[i]["orderDate"] + '</td>');
            var tdQuantityHtml = $('<td>' + data[i]["quantity"] + '</td>');
            trHtml.append(thNoHtml);
            trHtml.append(tdUserHtml);
            trHtml.append(tdOrderDateHtml);
            if (data[i]["status"] === "pending") {
                trHtml.append(tdQuantityHtml);
                var tdActionHtml = $('<td style="text-align: right">' +
                    '<button type="button" class="btn btn-info" data-toggle="modal" data-orderid="' + data[i]["id"] + '" data-traderid="' + data[i]["account"]["id"] + '" data-target="#acceptModal">Accept</button> ' +
                    '<button type="button" class="btn btn-info" data-toggle="modal" data-orderid="' + data[i]["id"] + '" data-traderid="' + data[i]["account"]["id"] + '" data-target="#declineModal">Decline</button>' +
                    '</td>');
                trHtml.append(tdActionHtml);
            } else if (data[i]["status"] === "approved") {
                var tdPaymentDateHtml = $('<td>' + data[i]["stateSetDate"] + '</td>');
                trHtml.append(tdPaymentDateHtml);
                trHtml.append(tdQuantityHtml);
                var tdActionHtml = $('<td style="text-align: right">' +
                    '<button type="button" class="btn btn-info" data-toggle="modal" data-orderid="' + data[i]["id"] + '" data-traderid="' + data[i]["account"]["id"] + '" data-target="#doneModal">Done</button> ' +
                    '<button type="button" class="btn btn-info" data-toggle="modal" data-orderid="' + data[i]["id"] + '" data-traderid="' + data[i]["account"]["id"] + '" data-target="#cancelModal">Cancel</button>' +
                    '</td>');
                trHtml.append(tdActionHtml);
            } else if (data[i]["status"] === "succeed") {
                var tdSucceedDateHtml = $('<td>' + data[i]["stateSetDate"] + '</td>');
                trHtml.append(tdSucceedDateHtml);
                trHtml.append(tdQuantityHtml);
                var isRated = data[i]["rated"];
                var tdActionHtml;
                if (!isRated){
                    tdActionHtml = $('<td style="text-align: right">' +
                        '<button type="button" class="btn btn-info" data-toggle="modal" data-orderid="' + data[i]["id"] + '" data-traderid="' + data[i]["account"]["id"] + '" data-target="#ratingModal">Rating</button> ' +
                        '</td>');
                }else {
                    tdActionHtml = $('<td style="text-align: right">' +
                        '<button type="button" class="btn btn-success">Rated</button> ' +
                        '</td>');
                }

                trHtml.append(tdActionHtml);
            } else if (data[i]["status"] === "cancelled") {
                var tdCancelledDateHtml = $('<td>' + data[i]["stateSetDate"] + '</td>');
                trHtml.append(tdCancelledDateHtml);
                trHtml.append(tdQuantityHtml);
                var tdActionHtml = $('<td style="text-align: right">' +
                    '<button type="button" class="btn btn-info" data-toggle="modal" data-reason="' + data[i]["cancelReason"] + '" data-target="#reasonModal">Reason <i class="fa fa-eye"></i></button> ' +
                    '</td>');
                trHtml.append(tdActionHtml);
            }

            container.append(trHtml);

        }


    }


}

function loadTradePostData() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/gwa/api/tradepost/view-trade-post",
        data: {
            tradepostId: postId
        },
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                postAccount = jsonResponse["tradepost"]["account"]["id"];
                var postStatus = jsonResponse["tradepost"]["approvalStatus"];
                if (postStatus=== "approved") isTrading = true;
                console.log(jsonResponse);
                renderData(jsonResponse);
            } else {
                notFound = true;
                $("#noticeSection").html("<h4>Opps! 404 Page not found!</h4>");
                $("#noticeSection").show();
                $("#slideSection").hide();
                $("#descBox").hide();
                console.log("Trade post not found!");
            }
        }
    });
}

function renderData(data) {
    /*Render Info text */
    var postData = data["tradepost"];
    $("#tradePrice").html("$" + postData["price"]);
    $("#tradeTitle").html(postData["title"]);
    $("#tradeId").html(postData["id"]);
    var dateSplit = postData["postedDate"].split(" ");
    var shortDate = dateSplit[0];
    $("#tradeDated").html(shortDate);
    $("#tradeDated").attr("title", postData["postedDate"]);
    var addressSplit = postData["location"].split(",");
    var shortAddress = addressSplit[addressSplit.length - 2] + "," + addressSplit[addressSplit.length - 1];
    $("#tradeLocation").html(shortAddress);
    $("#tradeLocation").attr("title", postData["location"]);

    $("#traderName").html(postData["account"]["username"]);
    $("#traderName").attr("title", "See " + postData["account"]["username"] + " profile");
    $("#traderName").attr("href", "/gwa/pages/profile.html?accountID=" + postData["account"]["id"]);

    $("#tradeType").attr("style", (postData["tradeType"] === 1) ? "color : red" : "color : blue");
    $("#tradeType").html((postData["tradeType"] === 1) ? "sell" : "buy");

    $("#tradeCondition").html((postData["condition"] === 1) ? "New" : "Used");

    $("#tradeBrand").html(postData["brand"]);

    $("#tradeModel").html(postData["model"]);
    if (postData["quantity"] == 0){
        $("#tradeQuantity").html("OUT OF STOCK");
        $("#sendOrderHelpBlock").html("You ca't send order because this trade is out of stock.");
        $("#openSendOrderModalBtn").attr("disabled", "true");
    }else {
        $("#tradeQuantity").html(postData["quantity"]);
    }



    $("#tradeDesc").html("<h4>Description</h4>");
    $("#tradeDesc").append(postData["description"]);

    /*Render Images slide */
    var imgList = data["images"];
    var slideIndicator = $("#slideIndicator");
    var slideImages = $("#slideImages");
    slideIndicator.html("");
    slideImages.html("");
    for (var i = 0; i < imgList.length; i++) {
        var liIndicator = $('<li data-target="#product-carousel" data-slide-to="' + i + '"></li>');
        var divImage = $('<div class="carousel-item"></div>');
        if (i === 0) {
            liIndicator = $('<li data-target="#product-carousel" data-slide-to="' + i + '" class="active"></li>');
            divImage = $('<div class="carousel-item active"></div>');
        }
        liIndicator.append($('<img src="' + imgList[i] + '" alt="Carousel Thumb" class="img-responsive">'));
        divImage.append($('<img src="' + imgList[i] + '" alt=slide"' + i + '" class="img-responsive">'));
        slideIndicator.append(liIndicator);
        slideImages.append(divImage);

    }

    var numberOfStars = postData["numberOfStar"];
    var numberOfRaters = postData["numberOfRater"];
    $("#tradeRating").html("<strong>Rating: </strong>");
    if (numberOfRaters === 0) {
        $("#tradeRating").append("N/A");
    } else {
        // $("#tradeRating").html("");
        var numberOfFullStars = Math.floor(numberOfStars / numberOfRaters);
        var HaftStars = (numberOfStars % numberOfRaters > 0) ? 1 : 0;
        for (var i = 0; i < numberOfFullStars; i++) {
            $("#tradeRating").append('<span class="fa fa-star checked"></span>');
        }
        if (HaftStars !== 0) {
            $("#tradeRating").append('<span class="fa fa-star-half checked"></span>');
        }

    }


}

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

function checkValidRequest(Param) {
    if (Param === undefined || Param === true) {
        return false;
    }
    if (!+Param) {
        return false;
    }
    if (parseInt(Param) < 1) {
        return false;
    }
    return true;
}

function sendOrder(traderId, traderEmail, traderPhone, address, tradepostId, quantity) {
    var data = {
        traderId: traderId,
        traderEmail: traderEmail,
        traderPhone: traderPhone,
        address: address,
        tradepostId: tradepostId,
        quantity: quantity
    };
    console.log(JSON.stringify(data));
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/gwa/api/tradepost/send-order",
        data: JSON.stringify(data),
        contentType: "application/json",
        async: false,
        success: function (result, txtStatus, xhr) {
            var desc = "You received new order for trade post id=" + postId + " from user: " + loginAccountName;
            var notiType = 5;
            addNewNotification(desc, tradepostId, postAccount, notiType);
            loadTradePostData();
            $.growl.notice({title: txtStatus, message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#OrderModal').modal('hide');
}

function loadProfileData(accountID, type) {
    $.ajax({
        type: "POST",
        url: "/gwa/api/user/profile?accountID=" + accountID,
        success: function (result) {
            // console.log(result);
            //get selected profile's account status

            var traderName = result["lastName"];
            if (result["middleName"]) traderName = traderName + " " + result["middleName"];
            traderName = traderName + " " + result["firstName"];
            var traderPhone = result["tel"];
            var traderEmail = result["email"];
            var traderAddress = result["address"];
            if (type === 1) {
                $("#senderName").val(traderName);
                $("#senderPhone").val(traderPhone);
                $("#senderEmail").val(traderEmail);
                $("#senderAddress").val(traderAddress);
                var numberOfStars = result["numberOfStars"];
                var numberOfRaters = result["numberOfRaters"];
                if (numberOfRaters === 0) {
                    $("#senderRating").html("N/A");
                } else {
                    $("#senderRating").html("");
                    var numberOfFullStars = Math.floor(numberOfStars / numberOfRaters);
                    var HaftStars = (numberOfStars % numberOfRaters > 0) ? 1 : 0;
                    for (var i = 0; i < numberOfFullStars; i++) {
                        $("#senderRating").append('<span class="fa fa-star checked"></span>');
                    }
                    if (HaftStars !== 0) {
                        $("#senderRating").append('<span class="fa fa-star-half checked"></span>');
                    }

                }
            } else {
                $("#yourName").val(traderName);
                $("#yourPhone").val(traderPhone);
                $("#yourEmail").val(traderEmail);
                $("#yourAddress").val(traderAddress);
            }


        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

function acceptOrder(orderID, traderId) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/gwa/api/tradepost/accept-order",
        data: {
            orderId: orderID
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            var desc = "Your order for trade post id=" + postId + " has been accepted. Please contact owner to do the trade.";
            var notiType = 4;
            addNewNotification(desc, orderID, traderId , notiType);
            loadOrderData();
            $.growl.notice({title: txtStatus, message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#acceptModal').modal('hide');
}

function declineOrder(orderID, reason, traderId) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/gwa/api/tradepost/decline-order",
        data: {
            orderId: orderID,
            reason: reason
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            var desc = "Your order for trade post id=" + postId + " has been declined. You can see reason at My order page.";
            var notiType = 4;
            addNewNotification(desc, orderID, traderId , notiType);
            loadOrderData();
            loadTradePostData();
            $.growl.notice({title: txtStatus, message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#declineModal').modal('hide');
}

function doneOrder(orderID, traderId) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/gwa/api/tradepost/confirm-succeed-order",
        data: {
            orderId: orderID
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            var desc = "Your order for trade post id=" + postId + " has been traded successfully. Please do the rating for buyer/seller.";
            var notiType = 4;
            addNewNotification(desc, orderID, traderId , notiType);
            loadOrderData();
            $.growl.notice({title: txtStatus, message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#doneModal').modal('hide');
}

function cancelOrder(orderID, reason, traderId) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/gwa/api/tradepost/cancel-order",
        data: {
            orderId: orderID,
            reason: reason
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            var desc = "Your order for trade post id=" + postId + " has been cancelled. You can see reason at My order page.";
            var notiType = 4;
            addNewNotification(desc, orderID, traderId , notiType);
            loadOrderData();
            loadTradePostData();
            $.growl.notice({title: txtStatus, message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#cancelModal').modal('hide');
}

function reportTrade(tradepostId, reason, phone, email) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/gwa/api/tradepost/report-trade",
        data: {
            tradepostId: tradepostId,
            reason: reason,
            phone: phone,
            email: email
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            if (result.includes("reported")) {
                $.growl.warning({message: result});
            } else {
                $.growl.notice({message: result});
            }

        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#reportModal').modal('hide');
}

function ratingTrader(orderId, feedbackType, rating, comment, traderId) {
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
            var desc = "You received a rating from owner trade post id=" + postId + ". Do you rating back ?";
            var notiType = 4;
            addNewNotification(desc, orderId, traderId , notiType);
            $.growl.notice({message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#ratingModal').modal('hide');
}

/* MODAL PASS VALUE */
$('#OrderModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    loadProfileData(loginAccount, 2);
    var modal = $(this);
    modal.find('input,textarea').val('');
    modal.find('label .error').remove();
    $("#sendOrderForm").validate({
        rules: {
            orderQuantityText: {
                required: true,
                digits: true,
                min: 1,
                max: parseInt($("#tradeQuantity").html())
            },
            yourPhone: {
                required: true,
                phoneVN: true

            },
            yourAddress: {
                required: true
            }
        },
        messages: {
            orderQuantityText: {
                required: "You missing your quantity here.",
                digits: "Quantity must be positive integer.",
                min: "Quantity must be greater than zero.",
                max: "Quantity must be lower than stock"
            },
            yourPhone: {
                required: "You need provide your phone.",
                phoneVN: "Your phone is not correct."

            },
            yourAddress: {
                required: "Please select or use auto get your address."
            }
        },
        submitHandler: function (form) {
            var traderId = loginAccount;
            var traderEmail = $("#yourEmail").val();
            var traderPhone = $("#yourPhone").val();
            var address = $("#yourAddress").val();
            var tradepostId = postId;
            var quantity = $("#orderQuantityText").val();
            sendOrder(traderId, traderEmail, traderPhone, address, tradepostId, quantity);
        }
    })
});
$('#profileModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var id = button.data('userid');
    var modal = $(this);
    modal.find('input,textarea').val('');
    loadProfileData(id, 1);
    modal.find('.modal-footer .btn-primary').attr('href','/gwa/pages/profile.html?accountID=' + id);
});
$('#acceptModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var id = button.data('orderid');
    var traderId = button.data('traderid');
    var modal = $(this);
    modal.find('.modal-footer #acceptOrderBtn').attr('onclick', 'acceptOrder(' + id + ','+ traderId+ ');');
});
$('#declineModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var id = button.data('orderid');
    var traderId = button.data('traderid');
    var modal = $(this);
    modal.find('input,textarea').val('');
    modal.find('label .error').remove();
    $("#declineForm").validate({
        rules: {
            declineReasonText: {
                required: true,
                minlength: 10
            }
        },
        messages: {
            declineReasonText: {
                required: "You need tell a reason",
                minlength: "Reason too short."
            }
        },
        submitHandler: function (form) {
            var reason = $("#declineReasonText").val();
            declineOrder(id, reason, traderId);
        }
    })
});

$('#doneModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var id = button.data('orderid');
    var traderId = button.data('traderid');
    var modal = $(this);
    modal.find('.modal-footer #doneOrderBtn').attr('onclick', 'doneOrder(' + id + ',' + traderId + ');');
});
$('#cancelModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var id = button.data('orderid');
    var traderId = button.data('traderid');
    var modal = $(this);
    modal.find('input,textarea').val('');
    modal.find('label .error').remove();
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
            var reason = "CANCEL FROM OWNER: " +  $("#cancelReasonText").val();
            cancelOrder(id, reason, traderId);
        }
    })
});
$('#ratingModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var orderId = button.data('orderid');
    var traderId = button.data('traderid');
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
            ratingTrader(orderId,1,rating,comment, traderId);
        }
    })


});
$('#reasonModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var reason = button.data('reason');
    var modal = $(this);
    modal.find('.modal-body').html('<strong style="color: red">' + reason + '</strong>');
});
$('#reportModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var modal = $(this);
    modal.find('input,textarea').val('');
    modal.find('label .error').remove();
    $("#reportForm").validate({
        rules: {
            reportText: {
                required: true,
                minlength: 10
            },
            reporterPhone: {
                required: true,
                phoneVN: true
            },
            reporterEmail: {
                required: true,
                email: true
            }
        },
        messages: {
            reportText: {
                required: "You need tell a reason",
                minlength: "Reason too short."
            },
            reporterPhone: {
                required: "You need provide your phone",
                phoneVN: "Your phone is not validate."
            },
            reporterEmail: {
                required: "You need provide your email",
                email: "Your email is not validate."
            }
        },
        submitHandler: function (form) {
            var email = $("#reporterEmail").val();
            var phone = $("#reporterPhone").val();
            var reason = $("#reportText").val();
            reportTrade(postId, reason, phone, email);
        }
    })
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

$.validator.addMethod("phoneVN", function (phone_number) {
    return phone_number.match('(09|03|05|07|08)+([0-9]{8})\\b');
}, "Please specify a valid phone number");


$("#addressSelectForm").validate({
    rules: {
        provinceSel: {
            required: true
        },
        districtSel: {
            required: true
        },
        wardSel: {
            required: true
        },
        selectAddressStreetName: {
            required: true
        }
    },
    messages: {
        provinceSel: {
            required: "Please select your province"
        },
        districtSel: {
            required: "Please select your district"
        },
        wardSel: {
            required: "Please select your ward"
        },
        selectAddressStreetName: {
            required: "Please provide your street name"
        }
    },
    submitHandler: function (form) {
        setTimeout(function () {
            $("#addressModal").modal('hide');
        }, 300);
        var addressText = $("#yourAddress"),
            addressFromModelText = $("#selectAddressFull");
        addressText.val(addressFromModelText.val());
        $.growl.notice({title: "Select Address: ", message: addressText.val()});
    }

});

function autoGetYourLocation() {
    waitingDialog.show('Getting your location...', {dialogSize: '', progressType: 'info'});
    setTimeout(function () {
        waitingDialog.hide();
        getYourLocation();
    }, 2000);
    // waitingDialog.show('Dialog with callback on hidden',{onHide: function () {alert('Callback!');}});
}

function getYourLocation() {
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
                $("#yourAddress").val(results[0].formatted_address);

            }
        }
    );
}

<!-- Tab panel  -->
$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    $(e.target).parent().addClass("active");// newly activated tab
    $(e.relatedTarget).parent().removeClass("active"); // previous active tab
});

/* Modal in Modal */
$('.modal-child').on('show.bs.modal', function () {
    var modalParent = $(this).attr('data-modal-parent');
    $(modalParent).hide();
});

$('.modal-child').on('hidden.bs.modal', function () {
    var modalParent = $(this).attr('data-modal-parent');
    $(modalParent).show();
});




