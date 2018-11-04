var tradingData = [];
var currentSortType = 1;
var currentTabSelected = "all";
var currentPage = 1;
var currentAddress = "";
var totalPage = 1;
var $pagination = $("#pagination-trading");
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
        loadTradingData();
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
    loadTradingData();
    $pagination.twbsPagination('destroy');
    $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
        totalPages: totalPage
    }));
    renderRecord();
    autoGetYourLocation();
    <!---->
    var slider = $("#distance");
    var output = $("#distanceValue");
    output.text(slider.val());

    slider.change(function () {
        output.text(slider.val());
    });
    <!-- End range slide -->


});
$("#searchWithLocation").click(function () {
    if ($(this).is(":checked")) {
        waitingDialog.show('Getting your location...', {dialogSize: '', progressType: 'info'});
        setTimeout(function () {
            waitingDialog.hide();
            $("#locationInput").val(currentAddress);
        }, 1000);

    }
    else if ($(this).is(":not(:checked)")) {
    }
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
                console.log(results[0].formatted_address);

            }
        }
    );
}

function loadTradingData() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/tradepost/get-trade-listing",
        async: false,
        data: {
            tradeType: currentTabSelected,
            pageNumber: currentPage,
            sortType: currentSortType
        },
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                console.log(jsonResponse);
                tradingData = jsonResponse;
                totalPage = jsonResponse[0]["totalPage"];
            } else {
                tradingData = null;
                console.log("Trade post not found!");
            }
        }
    });
}

function renderRecord() {
    var tabContentDiv;
    if (currentTabSelected === "all") {
        tabContentDiv = $("#alltrade");
    } else if (currentTabSelected === "buy") {
        tabContentDiv = $("#buytrade");
    }
    if (currentTabSelected === "sell") {
        tabContentDiv = $("#selltrade");
    }
    tabContentDiv.html("");
    if (tradingData.length <= 0) {
        tabContentDiv.html("<h4>No record.</h4>")
    } else {
        tabContentDiv.html("");
        for (var i = 0; i < tradingData.length; i++) {
            var rowData = tradingData[i];
            var postData = rowData["tradepost"];
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
            var isNegotiableText = (postData["priceNegotiable"] === 1) ? "(Negotiable)" : "";
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
            var condition = (postData["condition"] === 1) ? " New" : " Used";
            var metaCondition = $('<a href="#" class="tag" data-title="tooltip" data-placement="top" ' +
                '                                       title="This product is ' + condition + '"><i class="fa fa-tags"></i>' + condition + '</a>');
            var tradeType = (postData["tradeType"] === 1) ? "Sell" : "Buy";
            var metaTradeType = $('<span class="' + tradeType + '-trade"><i class="fa fa-money"></i> ' + tradeType + '</span>');
            metaContent.append(metaDate);
            metaContent.append(metaCondition);
            metaContent.append(metaTradeType);

            /* End Item info Meta Content */

            /* Item info Meta button action */
            var metaAction = $('<div class="user-option pull-right"></div>');
            var addressBtn = $('<a href="#" data-title="tooltip" data-placement="top" ' +
                '                                       title="' + postData["location"] + '"><i class="fa fa-map-marker"></i></a>');
            var profileBtn = $('<a href="#" data-title="tooltip" data-placement="top" ' +
                '                                       title="View ' + postData["account"]["username"] + ' Profile"><i class="fa fa-address-card"></i></a>');

            metaAction.append(addressBtn);
            metaAction.append(profileBtn);
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
    $('[data-title="tooltip"]').tooltip();
}

function changeTab(ele) {
    currentTabSelected = $(ele).attr("data-tradeType");
    currentPage = 1;
    loadTradingData();
    $pagination.twbsPagination('destroy');
    $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
        totalPages: totalPage
    }));
    renderRecord();
}

$("#sortTypeSelect").change(function () {
    currentSortType = $("#sortTypeSelect").val();
    loadTradingData();
    renderRecord();
    $.growl.notice({title: "Trading", message: "Sorting by " + $("option[value='" + currentSortType + "']").text()});

});

<!-- Tab panel  -->
$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    $(e.target).parent().addClass("active");// newly activated tab
    $(e.relatedTarget).parent().removeClass("active"); // previous active tab
});
//Validator
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
        var addressText = $("#locationInput"),
            addressFromModelText = $("#selectAddressFull");
        setTimeout(function () {
            $("#addressModal").modal('hide');
            addressText.val(addressFromModelText.val());
        }, 500);
        $.growl.notice({title: "Select Address: ", message: addressText.val()});
    }

});

