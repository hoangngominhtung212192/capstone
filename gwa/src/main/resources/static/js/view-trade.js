var postAccount;
var loginAccount;
var postId;
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
        if (!notFound) {
            authentication();
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

function authentication() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/user/checkLogin",
        // async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                var role = jsonResponse["role"].name;
                var currentAccount = jsonResponse["id"];
                loginAccount = currentAccount;

                if (role == "MEMBER" || role == "BUYERSELLER") {
                    if (postAccount === currentAccount) {
                        $("#sendOrderHelpBlock").html("You can't send order to your self because you owned this trade.");
                        $("#openSendOrderModalBtn").hide();
                        $("#noticeSection").show();
                        loadOrderData();
                        $pagination.twbsPagination('destroy');
                        if (totalPage > 1) {
                            $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                                totalPages: totalPage
                            }));
                        }
                    } else {
                        $("#noticeSection").hide();
                    }

                } else if (role == "ADMIN") {
                    $("#sendOrderHelpBlock").html("You can't send order because are administrator.");
                    $("#openSendOrderModalBtn").hide();
                    $("#noticeSection").hide();
                    // console.log("Admin is accessing !");
                }
            } else {
                $("#sendOrderHelpBlock").html("You need login to send order.");
                $("#openSendOrderModalBtn").attr("disabled", "true");
                $("#noticeSection").hide();
                // console.log("Guest is accessing !");
            }
        }
    });

}

function loadOrderData() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/tradepost/get-order-by-trade-post",
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
                    '<button type="button" class="btn btn-info" data-toggle="modal" data-orderid="' + data[i]["id"] + '" data-target="#acceptModal">Accept</button> ' +
                    '<button type="button" class="btn btn-info" data-toggle="modal" data-orderid="' + data[i]["id"] + '" data-target="#declineModal">Decline</button>' +
                    '</td>');
                trHtml.append(tdActionHtml);
            } else if (data[i]["status"] === "approved") {
                var tdPaymentDateHtml = $('<td>' + data[i]["stateSetDate"] + '</td>');
                trHtml.append(tdPaymentDateHtml);
                trHtml.append(tdQuantityHtml);
                var tdActionHtml = $('<td style="text-align: right">' +
                    '<button type="button" class="btn btn-info" data-toggle="modal" data-orderid="' + data[i]["id"] + '" data-target="#doneModal">Done</button> ' +
                    '<button type="button" class="btn btn-info" data-toggle="modal" data-orderid="' + data[i]["id"] + '" data-target="#cancelModal">Cancel</button>' +
                    '</td>');
                trHtml.append(tdActionHtml);
            } else if (data[i]["status"] === "succeed") {
                var tdSucceedDateHtml = $('<td>' + data[i]["stateSetDate"] + '</td>');
                trHtml.append(tdSucceedDateHtml);
                trHtml.append(tdQuantityHtml);
                var tdActionHtml = $('<td style="text-align: right">' +
                    '<button type="button" class="btn btn-info" data-toggle="modal" data-orderid="' + data[i]["id"] + '" data-target="#ratingModal">Rating</button> ' +
                    '</td>');
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
        url: "http://localhost:8080/api/tradepost/view-trade-post",
        data: {
            tradepostId: postId
        },
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                postAccount = jsonResponse["tradepost"]["account"]["id"];
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

    $("#tradeType").attr("style", (postData["tradeType"] === 1) ? "color : red" : "color : blue");
    $("#tradeType").html((postData["tradeType"] === 1) ? "sell" : "buy");

    $("#tradeCondition").html((postData["condition"] === 1) ? "New" : "Used");

    $("#tradeBrand").html(postData["brand"]);

    $("#tradeModel").html(postData["model"]);

    $("#tradeQuantity").html(postData["quantity"]);

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

function sendOrder(traderId, traderEmail, traderPhone, address, tradepostId, quantity){
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
        url: "http://localhost:8080/api/tradepost/send-order",
        data: JSON.stringify(data),
        contentType: "application/json",
        async: false,
        success: function (result, txtStatus, xhr) {
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
        url: "/api/user/profile?accountID=" + accountID,
        success: function (result) {
            // console.log(result);
            //get selected profile's account status
            if (result["middleName"] === null) result["middleName"] = "";
            var traderName = result["firstName"]+ " " + result["middleName"] + " " + result["lastName"];
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

function acceptOrder(orderID) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/tradepost/accept-order",
        data: {
            orderId: orderID
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            loadOrderData();
            $.growl.notice({title: txtStatus, message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#acceptModal').modal('hide');
}

function declineOrder(orderID, reason) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/tradepost/decline-order",
        data: {
            orderId: orderID,
            reason: reason
        },
        async: false,
        success: function (result, txtStatus, xhr) {
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

function doneOrder(orderID) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/tradepost/confirm-succeed-order",
        data: {
            orderId: orderID
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            loadOrderData();
            $.growl.notice({title: txtStatus, message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#doneModal').modal('hide');
}

function cancelOrder(orderID, reason) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/tradepost/cancel-order",
        data: {
            orderId: orderID,
            reason: reason
        },
        async: false,
        success: function (result, txtStatus, xhr) {
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
    // modal.find('.modal-footer #deleteTradePostBtn').attr('onclick','deleteTradePost('+ id +');');
});
$('#acceptModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var id = button.data('orderid');
    var modal = $(this);
    modal.find('.modal-footer #acceptOrderBtn').attr('onclick', 'acceptOrder(' + id + ');');
});
$('#declineModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var id = button.data('orderid');
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
            declineOrder(id, reason);
        }
    })
});

$('#doneModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var id = button.data('orderid');
    var modal = $(this);
    modal.find('.modal-footer #doneOrderBtn').attr('onclick', 'doneOrder(' + id + ');');
});
$('#cancelModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var id = button.data('orderid');
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
            var reason = $("#cancelReasonText").val();
            cancelOrder(id, reason);
        }
    })
});
$('#reasonModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var reason = button.data('reason');
    var modal = $(this);
    modal.find('.modal-body').html('<strong style="color: red">' + reason + '</strong>');
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




