var myTradeData = [];
var currentSortType = 1 ;
var currentTabSelected = "approved";
var currentPage = 1;
var myTradeAccountId;
var totalPage = 1;
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
        myTradeData = loadMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType);
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
    authentication();
});
function changeTab(ele) {
    currentTabSelected = $(ele).attr("data-status");
    currentPage =1;
    myTradeData = loadMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType);
    renderRecord();
    $pagination.twbsPagination('destroy');
    $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
        totalPages: totalPage
    }));
}
$("#sortTypeSelect").change(function () {
    currentSortType = $("#sortTypeSelect").val();
    myTradeData = loadMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType);
    renderRecord();
    $.growl.notice({title: "My trade", message: "Sorting by " + $("option[value='"+currentSortType+"']").text()});

});
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
                myTradeAccountId = jsonResponse["id"];

                if (role == "MEMBER" || role == "BUYERSELLER") {
                    myTradeData = loadMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType);
                    renderRecord();
                    $("#tradepostContainerDiv").show();
                    $("#searchDiv").show();
                    $("#paginationDiv").show();
                    $pagination.twbsPagination('destroy');
                    $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                        totalPages: totalPage
                    }));
                    
                } else if (role == "ADMIN") {
                    $(".notice-section").show();
                    $("#noticeTitle").html("Opps! You are administrator, why you stay here...");
                    $("#noticeContent").html("Click <a href='/admin'>[HERE]</a> to back to your site.");
                }
            } else {
                $(".notice-section").show();
                $("#noticeTitle").html("Opps! You need login to stay here!");
                $("#noticeContent").html("Click <a href='/login'>[HERE]</a> to login.");
                console.log("Guest is accessing !");
            }
        }
    });

}

<!-- Load My Trade Data-->
function loadMyTradeData(accountId, status, pageNumber, sortType) {
    var result;
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/tradepost/get-my-trade",
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
                console.log(jsonResponse);
                result = jsonResponse;
                totalPage = jsonResponse[0]["totalPage"];
            } else {
                result = null;
                console.log("Trade post not found!");
            }
        }
    });
    return result;
}
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
            var itemImgLink = $('<a href="/trade-market/view-trade?tradepostId=' + postData["id"] + '"></a>');
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
            var itemTitleLink = $('<a href="/trade-market/view-trade?tradepostId=' + postData["id"] + '"></a>');
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
            var editButton = $('<a class="edit-item" href="/trade-market/edit-trade?tradepostID=' +postData["id"]+ '" data-title="tooltip" data-placement="top" ' +
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

function updateQuantity(tradepostId) {
    var updateQuantityVal = $("#updateQuantityValue").val();
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/tradepost/update-quantity",
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
function deleteTradePost(tradepostId) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/tradepost/delete-trade-post",
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

<!-- Tooltip -->
$('[data-title="tooltip"]').tooltip();

<!-- Tab panel  -->
$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
   $(e.target).parent().addClass("active");// newly activated tab
    $(e.relatedTarget).parent().removeClass("active"); // previous active tab
});
/* MODAL PASS VALUE */
$('#deleteModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var id = button.data('id') ;
    var modal = $(this);
    modal.find('.modal-footer #deleteTradePostBtn').attr('onclick','deleteTradePost('+ id +');');
});
$('#updateQuantityModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var quantity = button.data('quantity');
    var id = button.data('id');
    var modal = $(this);
    modal.find('.modal-body input').val(quantity);
    modal.find('.modal-footer #updateQuantityBtn').attr('onclick','updateQuantity('+ id +');');
    modal.find('.modal-footer #updateQuantityBtn').attr('disabled','disabled');
});
$('#viewReasonModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var reason = button.data('reason') ;
    var modal = $(this);
    modal.find('.modal-body').text(reason);
});
$("#updateQuantityValue").keyup(function () {
   if ($(this).val() !== $("#oldQuantityValue").val() && $(this).val() >= 0 && $(this).val() <= 1000){
        $("#updateQuantityBtn").removeAttr("disabled");
   }else {
       $("#updateQuantityBtn").attr("disabled","disabled");
   }
});