
$(document).ready(function () {
    var tradepostId =getUrlParameter("tradepostId");
    if (checkValidRequest(tradepostId)){
        loadTradePostData(tradepostId);
    }else {
        $("#noticeSection").html("<h4>Opps! 404 Page not found!</h4>");
        $("#slideSection").hide();
        $("#descBox").hide();
        console.log("Trade post not found!");
    }
    <!-- Tooltip -->
    $('[data-title="tooltip"]').tooltip();
});
function loadTradePostData(tradepostID) {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/tradepost/view-trade-post",
        data: {
            tradepostId : tradepostID
        },
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                console.log(jsonResponse);
                renderData(jsonResponse);
            } else {
                $("#noticeSection").html("<h4>Opps! 404 Page not found!</h4>");
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
    $('#breadTitle').html(" " + postData["title"]);
    $("#tradePrice").html("$" + postData["price"]);
    $("#tradeTitle").html(postData["title"]);
    $("#tradeId").html(postData["id"]);
    var dateSplit = postData["postedDate"].split(" ");
    var shortDate = dateSplit[0];
    $("#tradeDated").html(shortDate);
    $("#tradeDated").attr("title",postData["postedDate"]);
    var addressSplit = postData["location"].split(",");
    var shortAddress = addressSplit[addressSplit.length - 2] + "," + addressSplit[addressSplit.length - 1];
    $("#tradeLocation").html(shortAddress);
    $("#tradeLocation").attr("title",postData["location"]);

    $("#traderName").html(postData["account"]["username"]);
    $("#traderName").attr("title","See "+ postData["account"]["username"] + " profile");

    $("#tradeType").attr("style",(postData["tradeType"] === 1) ? "color : red" : "color : blue" );
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
        var liIndicator = $('<li data-target="#product-carousel" data-slide-to="'+i+'"></li>');
        var divImage = $('<div class="carousel-item"></div>');
        if (i===0){
            liIndicator = $('<li data-target="#product-carousel" data-slide-to="'+i+'" class="active"></li>');
            divImage = $('<div class="carousel-item active"></div>');
        }
        liIndicator.append($('<img src="' +imgList[i]+ '" alt="Carousel Thumb" class="img-responsive">'));
        divImage.append($('<img src="' +imgList[i]+ '" alt=slide"'+i+'" class="img-responsive">'));
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



