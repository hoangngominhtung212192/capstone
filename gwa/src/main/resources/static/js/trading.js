var tradingData = [];
var isSearch = false;
var searchValue = "";
var isLocationSearch = false;
var addressSearch = "";
var rangeValue = 0;
var currentAddress = "";
var currentSortType = 1;
var currentTabSelected = "all";
var currentPage = 1;
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
        if (isSearch) {
            loadSearchData();
        } else {
            loadTradingData();
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
    $(".notice-section").hide();
    loadTradingData();
    $pagination.twbsPagination('destroy');
    if (totalPage > 1) {
        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
            totalPages: totalPage
        }));
    }
    renderRecord();
    autoGetYourLocation();
    <!---->
    var slider = $("#distance");
    var output = $("#distanceValue");
    output.text(slider.val());

    slider.on('input', function () {
        output.text(slider.val());
    });
    <!-- End range slide -->


});

function changeTab(ele) {
    currentTabSelected = $(ele).attr("data-tradeType");
    currentPage = 1;
    if (isLocationSearch) {
        loadLocationSearchData();
    } else {
        loadSearchData();
    }

    $pagination.twbsPagination('destroy');
    if (totalPage > 1) {
        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
            totalPages: totalPage
        }));
    }
    renderRecord();
}

$("#sortTypeSelect").change(function () {
    currentSortType = $("#sortTypeSelect").val();
    if (isSearch) {
        if (isLocationSearch) {
            loadLocationSearchData();
        } else {
            loadSearchData();
        }

    } else {
        loadTradingData();
    }
    renderRecord();
    $.growl.notice({title: "Trading", message: "Sorting by " + $("option[value='" + currentSortType + "']").text()});

});
$("#inputSearch").on('keyup', function (e) {
    if (e.keyCode == 13) {
        console.log("Enter");
        searchValue = $("#inputSearch").val();
        if (searchValue === "") {
            if (isSearch === true) {
                $(".notice-section").hide();
                currentPage = 1;
                loadTradingData();
                $pagination.twbsPagination('destroy');
                if (totalPage > 1) {
                    $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                        totalPages: totalPage
                    }));
                }
                renderRecord();
                isSearch = false;
            }
        } else {
            currentPage = 1;
            if (isLocationSearch) {
                rangeValue = $("#distance").val();
                addressSearch = $("#locationInput").val();
                loadLocationSearchData();
                $("#noticeTitle").html("You are searching with keyword: " + searchValue + " - Range: " + rangeValue + "KM");
            } else {
                loadSearchData();
                $("#noticeTitle").html("You are searching with keyword: " + searchValue);
            }
            $pagination.twbsPagination('destroy');
            if (totalPage > 1) {
                $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                    totalPages: totalPage
                }));
            }
            renderRecord();
            isSearch = true;
            $(".notice-section").show();
            $("#noticeContent").html("Clear your searchbox and enter to remove search.");
        }
    }
});
$("#searchWithLocation").click(function () {
    if ($(this).is(":checked")) {
        waitingDialog.show('Getting your location...', {dialogSize: '', progressType: 'info'});
        setTimeout(function () {
            waitingDialog.hide();
            $("#locationInput").val(currentAddress);
        }, 1000);
        isLocationSearch = true;

    }
    else if ($(this).is(":not(:checked)")) {
        isLocationSearch = false;
    }
});

function loadSearchData() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/tradepost/search-trade-listing",
        async: false,
        data: {
            tradeType: currentTabSelected,
            pageNumber: currentPage,
            sortType: currentSortType,
            keyword: searchValue
        },
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                console.log(jsonResponse);
                tradingData = jsonResponse[1];
                if (tradingData != "") {
                    totalPage = jsonResponse[0];
                } else {
                    totalPage = 0;
                }

            } else {
                tradingData = null;
                console.log("Trade post not found!");
            }
        }
    });
}

function loadLocationSearchData() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/tradepost/search-location-trade-listing",
        async: false,
        data: {
            tradeType: currentTabSelected,
            sortType: currentSortType,
            keyword: searchValue,
            location: addressSearch,
            range: rangeValue * 1000
        },
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                console.log(jsonResponse);
                tradingData = jsonResponse[1];
                if (tradingData != "") {
                    totalPage = jsonResponse[0];
                } else {
                    totalPage = 0;
                }

            } else {
                tradingData = null;
                console.log("Trade post not found!");
            }
        }
    });
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
                tradingData = jsonResponse[1];
                if (tradingData != "") {
                    totalPage = jsonResponse[0];
                } else {
                    totalPage = 0;
                }
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
            var addressBtn = $('<a href="#directionModal" data-title="tooltip" data-placement="top" data-address="' + postData["location"] + '" ' +
                '                                       title="' + postData["location"] + '" data-toggle="modal"><i class="fa fa-map-marker"></i></a>');
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

