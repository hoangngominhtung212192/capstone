var tradepost_data_edit;
var authorStatus;// 1 = Yes , 0 = No
var notFoundStatus;// 1 = notfound , 0 = found

$(document).ready(function () {
    if (checkValidRequest()) {
        notFoundStatus = 0;
        console.log("valid request");
        authentication();
    }
    else {
        notFoundStatus = 1;
        $("#noticeTitle").css("color", "red");
        $("#noticeTitle").html("Opps! [ 404 - Not found ] You go wrong way, Please go back!");
        $("#tradePostDiv").hide();
        console.log("Error Request");
    }
    $('[data-title="tooltip"]').tooltip();
});

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

function checkEditRequest() {
    var uriPath = window.location.pathname;
    if (uriPath == "/trade-market/edit-trade") {
        return true;
    }
    return false;
}

function checkValidEditRequest() {
    var editPram = getUrlParameter("tradepostID");
    // console.log(editPram);
    if (editPram === undefined || editPram === true) {
        return false;
    }
    if (!+editPram) {
        return false;
    }
    if (parseInt(editPram) < 1) {
        return false;
    }
    return true;
}

function checkValidRequest() {
    if (!checkEditRequest()) {
        return true;
    } else {
        if (checkValidEditRequest()) {
            return true;
        }
    }
    return false;
}

function checkAuthorization(userId) {
    var tradepostID = getUrlParameter("tradepostID");
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/tradepost/get-trade-post-edit-form-data?tradepostID=" + tradepostID,
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                // console.log(jsonResponse);
                var userIdPost = jsonResponse["traderId"];
                if (userId == userIdPost) {//This login user owner this post
                    authorStatus = 1;
                    tradepost_data_edit = jsonResponse;
                    console.log("Authorizationed");
                } else {
                    authorStatus = 0;
                    console.log("Not Author");
                }
            } else {
                notFoundStatus = 1;
                console.log("Trade post not found!");
            }
        }
    });
}


function authentication() {
    $("#tradePostDiv").hide();
    var noticeTitle = "";
    var noticeContent = "";
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/user/checkLogin",
        complete: function (xhr, status) {

            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                var role = jsonResponse["role"].name;
                var username = jsonResponse["username"];
                var id = jsonResponse["id"];
                // console.log(jsonResponse);

                if (role == "MEMBER" || role == "BUYERSELLER") {
                    if (checkEditRequest()) {
                        checkAuthorization(id);
                        // console.log(authorStatus);
                        if (notFoundStatus === 1) {
                            noticeTitle = "Opps! [ 404 - Not found ] You go wrong way, Please go back!";
                            $("#noticeTitle").css("color", "red");
                            $("#tradePostDiv").hide();
                        } else {
                            if (authorStatus == 1) {
                                loadEditForm(tradepost_data_edit);
                                noticeTitle = "Edit your trade post"
                                $("#noticeTitle").css("color", "green");
                                $("#tradePostDiv").show();
                            } else {
                                noticeTitle = "Opps! You dont have permission to edit this trade post!"
                                $("#noticeTitle").css("color", "orange");
                                $("#tradePostDiv").hide();
                            }
                        }
                    } else {
                        noticeTitle = "Post your new trade";
                        $("#noticeTitle").css("color", "green");
                        loadProfileData(id);
                        $("#tradePostDiv").show();
                    }
                } else if (role == "ADMIN") {
                    noticeTitle = "Opps! You are administrator, why you stay here...";
                    $("#noticeTitle").css("color", "orange");
                    noticeContent = 'Click <a href="/admin">[HERE]</a> to back to your site.';
                    $("#tradePostDiv").hide();
                }
                console.log("Login as " + role);
            } else {
                $("#noticeTitle").css("color", "red");
                noticeTitle = "Opps! You need login to stay here!";
                noticeContent = 'Click <a href="/login">[HERE]</a> to login.';
                $("#tradePostDiv").hide();
                console.log("Guest is accessing !");
            }
            $("#noticeTitle").html(noticeTitle);
            $("#noticeContent").html(noticeContent);

        }
    });

}
function loadEditForm(editformData) {
    console.log(editformData);
    //SET EDIT API TO FORM
    $("#tradepostForm").attr("action", "/api/tradepost/edit-trade-post");
    //SET NAME OF BUTTOM
    $("#submitTradeBtn").html("Save Your Trade");

    //SET TRADE POST DATA
    $("#tradeId").val(editformData["tradeId"]);
    if (editformData["tradeType"] === "sell"){
        $("#tradeType-sell").prop('checked', true);
    }else {
        $("#tradeType-buy").prop('checked', true);
    }
    $("#tradeTitle").val(editformData["tradeTitle"]);
    if (editformData["tradeCondition"] === "new"){
        $("#tradeCondition-new").prop('checked', true);
    }else {
        $("#tradeCondition-used").prop('checked', true);
    }
    $("#tradePrice").attr("value", editformData["tradePrice"]);
    if (editformData["tradeNegotiable"] === "on"){
        $("#tradeNegotiable").prop('checked', true);
    }
    $("#tradeQuantity").val(editformData["tradeQuantity"]);
    $("#tradeBrand").val(editformData["tradeBrand"]);
    $("#tradeModel").val(editformData["tradeModel"]);
    $("#tradeDesc").val(editformData["tradeDesc"]);
    var imgListArr = [];
    imgListArr = editformData["imageUploadedList"];
    $("#imageUploadedList").val(JSON.stringify(imgListArr));

    //Trader profile load
    $("#traderName").val(editformData["traderName"]);
    $("#traderPhone").val(editformData["traderPhone"]);
    $("#traderEmail").val(editformData["traderEmail"]);
    $("#traderAddress").val(editformData["traderAddress"]);



}

function loadProfileData(accountID) {
    $.ajax({
        type: "POST",
        url: "/api/user/profile?accountID=" + accountID,
        success: function (result) {
            // console.log(result);
            //get selected profile's account status
            var traderName = result["firstName"].trim() + " " + result["middleName"].trim() + " " + result["lastName"].trim();
            var traderPhone = result["tel"].trim();
            var traderEmail = result["email"];
            var traderAddress = result["address"];
            $("#traderName").val(traderName);
            $("#traderPhone").val(traderPhone);
            $("#traderEmail").val(traderEmail);
            $("#traderAddress").val(traderAddress);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

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
    var google_map_pos = new google.maps.LatLng( lat, lng );

    /* Use Geocoder to get address */
    var google_maps_geocoder = new google.maps.Geocoder();
    google_maps_geocoder.geocode(
        { 'latLng': google_map_pos },
        function( results, status ) {
            if ( status == google.maps.GeocoderStatus.OK && results[0] ) {
                $("#traderAddress").val(results[0].formatted_address);

            }
        }
    );
}


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
        var addressText = $("#traderAddress"),
            addressFromModelText = $("#selectAddressFull");
        addressText.val(addressFromModelText.val());
        $.growl.notice({title: "Select Address: ", message: addressText.val()});
    }

});
$("#tradepostForm").validate({
    ignore: [],
    rules: {
        tradeTitle: {
            required: true,
            minlength: 3
        },
        tradePrice: {
            required: true,
            number: true
        },
        tradeQuantity: {
            required: true,
            digits: true,
            min: 1,
            max: 1000
        },
        tradeBrand: {
            required: true,
            minlength: 3
        },
        tradeDesc: {
            required: true,
            minlength: 10
        },
        traderName: {
            required: true,
            minlength: 2
        },
        traderEmail: {
            required: true,
            email: true
        },
        traderPhone: {
            required: true,
            phoneVN: true

        },
        traderAddress: {
            required: true
        },
        tradeType123: {
            required: true

        },
        tradeCondition: {
            required: true
        },
        sendCheck: {
            required: true
        },
        imageUploadedList: {
            required: true
        }
    },
    messages: {
        tradeTitle: {
            required: "You missing your title here.",
            minlength: "Your title too short, request at least 3 characters."
        },
        tradePrice: {
            required: "You missing your price here.",
            number: "Your price can't be string, price must be number."
        },
        tradeQuantity: {
            required: "You missing your quantity here.",
            digits: "Quantity must be positive integer.",
            min: "Quantity must be greater than zero.",
            max: "Quantity must be lower or equal 1000."
        },
        tradeBrand: {
            required: "You missing your brand here.",
            minlength: "Your brand too short, request at least 3 characters."
        },
        tradeDesc: {
            required: "You missing your description here",
            minlength: "Description too short, you need write something to let they know about your product."
        },
        traderName: {
            required: "Let they know your name to make a trade.",
            minlength: "Your name too short. Please use full name."
        },
        traderEmail: {
            required: "You need provide your email.",
            email: "Your email is not correct."
        },
        traderPhone: {
            required: "You need provide your phone.",
            phoneVN: "Your phone is not correct."

        },
        traderAddress: {
            required: "Please select or use auto get your address."
        }
        ,
        tradeType: {
            required: "You must choose type of your trade."
        },
        tradeCondition: {
            required: "You must choose your product condition."
        },
        sendCheck: {
            required: "You must accept our Terms of Use and Privacy Policy to post your trade"
        },
        imageUploadedList: {
            required: "You must upload at least 1 images for your trade."
        }
    },
    submitHandler: function (form) {
        var formObj = {};
        new FormData(form).forEach(function (value, key) {
            formObj[key] = value;
            if (key === "imageUploadedList") {
                formObj[key] = JSON.parse(value);
            }
        });
        var url = $(form).attr("action");
        var formDataJson = JSON.stringify(formObj);
        // console.log(formDataJson);
        $.ajax({
            url: url,
            type: "POST",
            data: formDataJson,
            contentType: "application/json",
            success: function (result, txtStatus, xhr) {
                $.growl.notice({title: txtStatus, message: result});
                $("#tradePostDiv").hide();
                $("#noticeTitle").html("Trade post has been submited.").css("color", "green");
                $("#noticeContent").html("Redirecting to your trade post list page...");
                setTimeout(function () {
                    window.location.href = "/trade-market/my-trade";
                }, 3000);
            },
            error: function (xhr, textStatus, errorThrown) {
                $.growl.error({title: textStatus, message: xhr.responseText});
            }
        });
    }
});
var countFileSuccess = 0;
var countFileSelect = 0;
var FiletoSubmit = [];
$.fileup({
    url: "/uploadFile",
    inputID: 'upload-2',
    queueID: 'upload-2-queue',
    dropzoneID: '',
    files: [],
    fieldName: 'file',
    extraFields: {},
    lang: 'en',
    sizeLimit: 2500000,
    filesLimit: 5,
    method: 'post',
    timeout: null,
    autostart: false,
    templateFile: '<div id="fileup-[INPUT_ID]-[FILE_NUM]" class="fileup-file [TYPE]">\n' +
        '    <div class="fileup-preview">\n' +
        '        <img src="[PREVIEW_SRC]" alt="[NAME]"/>\n' +
        '    </div>\n' +
        '    <div class="fileup-container">\n' +
        '        <div class="fileup-description">\n' +
        '            <span class="fileup-name">[NAME]</span> (<span class="fileup-size">[SIZE_HUMAN]</span>)\n' +
        '        </div>\n' +
        '        <div class="fileup-controls">\n' +
        '            <span class="fileup-remove" id="remove-[FILE_NUM]" onclick="$.fileup(\'[INPUT_ID]\', \'remove\', \'[FILE_NUM]\');" title="[REMOVE]"></span>\n' +
        '            <span class="fileup-upload" onclick="$.fileup(\'[INPUT_ID]\', \'upload\', \'[FILE_NUM]\');">[UPLOAD]</span>\n' +
        '            <span class="fileup-abort" onclick="$.fileup(\'[INPUT_ID]\', \'abort, \'[FILE_NUM]\');" style="display:none">[ABORT]</span>\n' +
        '        </div>\n' +
        '        <div class="fileup-result"></div>\n' +
        '        <div class="fileup-progress">\n' +
        '            <div class="fileup-progress-bar"></div>\n' +
        '        </div>\n' +
        '    </div>\n' +
        '    <div class="fileup-clear"></div>\n' +
        '</div>',
    onSelect: function (file) {
        countFileSelect++;
        $('#multiple .control-button').show();
        console.log(countFileSelect);
        if (countFileSuccess > 0) {
            $('#multiple .removeall').hide();
        }
    },
    onRemove: function (file, total) {
        countFileSelect--;
        if (file === '*' || total === 1) {
            $('#multiple .control-button').hide();
        }
        if (countFileSelect === countFileSuccess) {
            $('#multiple .control-button').hide();
        }
    },
    onSuccess: function (response, file_number, file) {
        countFileSuccess++;
        $.growl.notice({title: "Upload success!", message: file.name});
        $("#remove-" + file_number).hide();
        if (countFileSelect === countFileSuccess) {
            $('#multiple .control-button').hide();
        }
        if (countFileSuccess > 0) {
            $('#multiple .removeall').hide();
        }
        FiletoSubmit.push(JSON.parse(response)['fileDownloadUri']);

        $('#imageUploadedList').val(JSON.stringify(FiletoSubmit));
        console.log($('#imageUploadedList').val);
    },
    onError: function (event, file, file_number) {
        var textErr = "";
        if (event === "files_limit") {
            textErr = "The number of selected file exceeds the limit(5)";
        }
        if (event === "file_type") {
            textErr = "File " + file.name + " is not image file types";
        }
        $.growl.error({title: "Upload error: ", message: textErr});
    }

});

