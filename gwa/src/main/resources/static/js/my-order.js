var loginAccount;
var currentTabSelected = "approved";
var currentSortSelected = 1;
var currentPage = 1;
var totalPage = 1;
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
    prev: '❮',
    next: '❯',
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
    authentication();
    <!-- Tooltip -->
});
function changeTab(ele) {
    currentTabSelected = $(ele).attr("data-status");
    currentPage =1;
    loadMyOrderData();
    $pagination.twbsPagination('destroy');
    if (totalPage > 1){
        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
            totalPages: totalPage
        }));
    }
}
$("#sortTypeSelect").change(function () {
    currentSortSelected = $("#sortTypeSelect").val();
    loadMyOrderData();
    $.growl.notice({title: "My Order", message: "Sorting by " + $("option[value='"+currentSortSelected+"']").text()});

});

function authentication() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/gwa/api/user/checkLogin",
        // async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                var role = jsonResponse["role"].name;
                loginAccount = jsonResponse["id"];

                if (role == "MEMBER" || role == "BUYERSELLER") {
                    loadMyOrderData();
                    $("#myOrderContainerDiv").show();

                    $pagination.twbsPagination('destroy');
                    if (totalPage > 1){
                        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                            totalPages: totalPage
                        }));
                    }


                } else if (role == "ADMIN") {
                    $(".notice-section").show();
                    $("#noticeTitle").html("Opps! You are administrator, why you stay here...");
                    $("#noticeContent").html("Click <a href='/gwa/admin'>[HERE]</a> to back to your site.");
                }
            } else {
                $(".notice-section").show();
                $("#noticeTitle").html("Opps! You need login to stay here!");
                $("#noticeContent").html("Click <a href='/gwa/login'>[HERE]</a> to login.");
                // console.log("Guest is accessing !");
            }
        }
    });

}
function loadMyOrderData() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/gwa/api/tradepost/get-my-order",
        data: {
            accountId : loginAccount,
            status : currentTabSelected,
            pageNumber: currentPage,
            sortType: currentSortSelected
        },
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                console.log(jsonResponse);
                var data = jsonResponse[2];
                if (data != ""){
                    totalPage = jsonResponse[0];
                }else {
                    totalPage = 0;
                }
                renderData(data);
                console.log(data);
            } else {
                result = null;
                // console.log("Trade post not found!");
            }
        }
    });
}
function renderData(data) {
    var tabContentDiv;
    if(currentTabSelected === "approved"){
        tabContentDiv = $("#onpayment");
    }
    if(currentTabSelected === "pending"){
        tabContentDiv = $("#pendingorders");
    }
    if(currentTabSelected === "succeed"){
        tabContentDiv = $("#succeedorders");
    }
    if(currentTabSelected === "others"){
        tabContentDiv = $("#otherorders");
    }
    if (data.length <= 0) {
        tabContentDiv.html("<h4>No record.</h4>")
    }else {
        tabContentDiv.html("");
        for (var i = 0; i < data.length; i++) {
            var rowData = data[i];
            var status = rowData["orderStatus"];

            var itemRow = $('<div class="tradeitem row"></div>');

            //Thumbnail box
            var itemImageBox = $('<div class="item-image-box col-sm-4"></div>');
            var itemImage = $('<div class="item-image"></div>');
            var image = $('<a href="/gwa/trade-market/view-trade?tradepostId='+rowData["tradepostId"]+'"><img src="'+rowData["tradepostThumbnail"]+'" alt="Image" class="img-responsive"></a>');
            itemImage.html(image);
            itemImageBox.html(itemImage);
            itemRow.append(itemImageBox);

            //Item Info
            var itemInfo = $('<div class="item-info col-sm-8"></div>');
            //Trade info
            var tradeInfo = $('<div class="tradeinfo"></div>');
            var title = $('<h3 class="item-price">Order From: <a href="/gwa/trade-market/view-trade?tradepostId='+rowData["tradepostId"]+'">'+rowData["tradepostTitle"]+'</a></h3>');
            tradeInfo.append(title);
            var itemCat =$('<div class="item-cat"></div>');
            if(status === "declined" || status === "cancelled"){
                itemCat.append('<span>Quantity: </span><span style="color: red">'+rowData["orderQuantity"]+'</span> - ' +
                    '<span>Total: </span><span style="color: red">'+rowData["orderPay"]+'$</span><br/>' +
                    '<span>This order has been '+rowData["orderStatus"]+'.</span>');
            }else {
                itemCat.append('<span>Owner: </span><span style="color: green">'+rowData["ownerName"]+'</span><br/>');
                itemCat.append('<span>Phone: </span><span style="color: black">'+rowData["ownerPhone"]+'</span> /');
                itemCat.append('<span  style="float: right"><span>Quantity: </span><span style="color: red">'+rowData["orderQuantity"]+'</span> - <span>Total: </span><span style="color: red">'+rowData["orderPay"]+'$</span></span>');
                itemCat.append('<span>Email: </span><span style="color: black">'+rowData["ownerEmail"]+'</span><br/>');

            }
            tradeInfo.append(itemCat);
            itemInfo.append(tradeInfo);

            //Trade meta

            var tradeMeta = $('<div class="trademeta"></div>');

            var metaContent = $('<div class="meta-content"></div>');
            metaContent.append('<span class="dated">Ordered On: <a href="#">'+rowData["orderedDate"]+'</a></span>');
            if(status === "pending"){
                metaContent.append('<span class="number-succed">Waiting approve</span>');
            }
            if(status === "approved"){
                metaContent.append('<span class="number-payment">Accepted On: <a href="#">'+rowData["orderSetDate"]+'</a></span>');
            }
            if(status === "succeed"){
                metaContent.append('<span class="number-succed">Succeed On: <a href="#">'+rowData["orderSetDate"]+'</a></span>');
            }
            if(status === "declined"){
                metaContent.append('<span class="number-request">Declined On: <a href="#">'+rowData["orderSetDate"]+'</a></span>');
            }
            if(status === "cancelled"){
                metaContent.append('<span class="number-request">Cancelled On: <a href="#">'+rowData["orderSetDate"]+'</a></span>');
            }
            tradeMeta.append(metaContent);

            var metaAction = $('<div class="user-option pull-right"></div>');
            if(status === "pending"){
                var updateBtn = $('<a href="#updateModal" data-title="tooltip" data-placement="top" data-toggle="modal" ' +
                    'title="Update request" data-orderid="'+rowData["orderId"]+'" data-quantity="'+rowData["orderQuantity"]+'"><i class="fa fa-cart-plus"></i></a>');
                var cancelBtn = $('<a class="delete-item" href="#cancelModal" data-title="tooltip" data-placement="top" data-toggle="modal"  ' +
                    'title="Cancel this order" data-orderid="'+rowData["orderId"]+'"><i class="fa fa-share-square"></i></a>');
                metaAction.append(updateBtn);
                metaAction.append(cancelBtn);
            }
            if(status === "approved"){
                var directionBtn = $('<a href="#directionModal" data-title="tooltip" data-placement="top" data-toggle="modal"  ' +
                    'title="'+rowData["ownerAddress"]+'"><i class="fa fa-map-marker"></i></a>');
                var cancelBtn = $('<a class="delete-item" href="#cancelModal" data-title="tooltip" data-placement="top" data-toggle="modal"  ' +
                    'title="Cancel this order" data-orderid="'+rowData["orderId"]+'"><i class="fa fa-share-square"></i></a>');
                metaAction.append(directionBtn);
                metaAction.append(cancelBtn);
            }
            if(status === "succeed"){
                var ratingBtn;
                var isRated = rowData["rated"];
                if (!isRated){
                    ratingBtn = $('<a class="unrated-item" href="#ratingModal" data-title="tooltip" data-placement="top" data-toggle="modal" ' +
                        'title="Rating this trade"  data-orderid="'+rowData["orderId"]+'"><i class="fa fa-star"></i></a>');
                }else {
                    ratingBtn = $('<a class="rated-item" data-title="tooltip" data-placement="top" ' +
                        'title="This trade is rated"><i class="fa fa-star"></i></a>');
                }

                metaAction.append(ratingBtn);
            }
            if(status === "declined" || status === "cancelled"){
                var reasonBtn = $('<a class="delete-item" href="#reasonModal" data-title="tooltip" data-placement="top" data-toggle="modal" ' +
                    'title="View reason" data-reason="'+rowData["orderReason"]+'"><i class="fa fa-book"></i></a>');
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

function cancelOrder(orderID, reason) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/gwa/api/tradepost/cancel-order",
        data: {
            orderId: orderID,
            reason: reason
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            loadMyOrderData();
            $.growl.notice({title: txtStatus, message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });
    $('#cancelModal').modal('hide');
}
function ratingTrader(orderId, feedbackType, rating, comment) {
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
$('#ratingModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var orderId = button.data('orderid');
    var traderId = button.data('userid');
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
            ratingTrader(orderId,2,rating,comment);
        }
    })


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
