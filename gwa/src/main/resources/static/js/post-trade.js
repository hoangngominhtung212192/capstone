var tradepost_data_edit; //Load data cua form edit
var imageListFileUp = []; //Luu String Json cua form hinh, load nhung hinh da lua trong database len
var FiletoSubmit = []; //Lua nhung link hinh de lua vao database
var fileSelected = 0; //Bo dem so file da dc select vao form
var authorStatus;// 1 = Yes , 0 = No //Kiem tra bai nay co thuoc quyen so hua cua login session ko
var notFoundStatus;// 1 = notfound , 0 = found
var ImageNamePrefix;
var currentAccountName;
var currentTradePostID = -1;

$(document).ready(function () {
    if (checkValidRequest()) { //Kiem tra duong link co dung ko
        notFoundStatus = 0;
        authentication(); //Tien hanh lay user session va kiem tra
        $.fileup({
            url: "/gwa/uploadFile", //Link Ajax call len server
            inputID: 'upload-2', //ID form up hinh
            queueID: 'upload-2-queue',
            dropzoneID: '', //Setting ten div chua vua drop de load file
            files: imageListFileUp, //Load hinh san co tu database len
            fieldName: 'file',
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
                // '            <span class="fileup-upload" onclick="$.fileup(\'[INPUT_ID]\', \'upload\', \'[FILE_NUM]\');">[UPLOAD]</span>\n' +
                // '            <span class="fileup-abort" onclick="$.fileup(\'[INPUT_ID]\', \'abort, \'[FILE_NUM]\');" style="display:none">[ABORT]</span>\n' +
                '        </div>\n' +
                '        <div class="fileup-result"></div>\n' +
                '        <div class="fileup-progress">\n' +
                '            <div class="fileup-progress-bar"></div>\n' +
                '        </div>\n' +
                '    </div>\n' +
                '    <div class="fileup-clear"></div>\n' +
                '</div>',
            onSelect: function (file) {
                fileSelected++;// Khi them 1 hinh thi tang bo dem len
                $('#multiple button').show();
            },
            onRemove: function (file, total, file_number) {//Event khi xoa 1 tam hinh tren giao dien
                fileSelected--;// Moi lan xoa 1 hinh thi tru bo dem di 1

                if (file === '*' || total === 1) {//Khi nhan xoa het tat ca / xoa toi hinh cuoi cung
                    $('#multiple button').hide();//an nut remove all
                    fileSelected = 0; //Dat lai bo dem file da chon
                }

                //Kiem tra file da lua tren database chua
                if (file.file.saved){
                    //Neu co thi go khoi danh sach link hinh anh
                    for (var i = 0; i < FiletoSubmit.length; i++) {
                        if(FiletoSubmit[i].split("downloadFile/")[1].split(".")[0].split(ImageNamePrefix)[1] == file_number){//lay id cua tam anh == so thu tu file tren form
                            FiletoSubmit.splice(i,1);// go tam hinh co so thu tu = id tam hinh
                        }
                    }
                }

            },
            onSuccess: function (response, file_number, file) {
                $.growl.notice({title: "Upload success!", message: file.name});
                FiletoSubmit.push(JSON.parse(response)['fileDownloadUri']);
            },
            onError: function (event, file, file_number) {
                var textErr = "";
                if (event === "files_limit") {
                    textErr = "The number of selected file exceeds the limit(5)";
                }
                if (event === "file_type") {
                    textErr = "File " + file.name + " is not image file types";
                }
                if (event === "file_duplicate") {
                    textErr = "File " + file.name + " is duplicated";
                }
                if (event === "size_limit") {
                    textErr = "File " + file.name + " is exceeds the size limit.";
                }
                $.growl.error({title: "Upload Image Error: ", message: textErr});
            }

        });
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

function ajaxSubmitForm(form) {
    var formObj = {};
    new FormData(form).forEach(function (value, key) {
        formObj[key] = value;
    });
    var url = $(form).attr("action");
    var formDataJson = JSON.stringify(formObj);
    console.log(formDataJson);
    $.ajax({
        url: url,
        type: "POST",
        async: false,
        data: formDataJson,
        contentType: "application/json",
        success: function (result, txtStatus, xhr) {
            currentTradePostID = result;
            $.growl.notice({title: txtStatus, message: "Your trade post has been submited."});
            $("#tradePostDiv").hide();
            $("#noticeTitle").html("Trade post has been submited.").css("color", "green");
            $("#noticeContent").html("Redirecting to your trade post list page...");
            setTimeout(function () {
                window.location.href = "/gwa/trade-market/my-trade#pendingtrade";
            }, 3000);
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: "Something is wrong when submit your trade post. Please contact Administrator for more information."});
        }
    });

}
function ajaxUploadImageList() {
    ImageNamePrefix = "tradepost_"+ currentAccountName + "_" + currentTradePostID + "_";
    waitingDialog.show('Uploading image...', {dialogSize: '', progressType: 'info'});
    $.fileup.updatePrefixName('upload-2', ImageNamePrefix);
    $.fileup('upload-2', 'upload', '*');
    setTimeout(function () {
        waitingDialog.hide();
    }, 2000);
}
function ajaxUpdateImagesToDatabase(tradepostId, images) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/gwa/api/tradepost/update-images-to-database",
        data: {
            tradepostId: tradepostId,
            images: images
        },
        async: false,
        success: function (result, txtStatus, xhr) {
            $.growl.notice({message: result});
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({title: textStatus, message: xhr.responseText});
        }
    });

}




function authentication() {
    $("#tradePostDiv").hide();
    var noticeTitle = "";
    var noticeContent = "";
    $.ajax({
        type: "GET",
<<<<<<< HEAD
=======
        async: false,
>>>>>>> 9f3e4c3605978ec38067b2f5ebd7439dd01c14a3
        url: "http://localhost:8080/gwa/api/user/checkLogin",
        complete: function (xhr, status) {

            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                var role = jsonResponse["role"].name;
                var username = jsonResponse["username"];
                var id = jsonResponse["id"];

                currentAccountName = username;

                if (role == "MEMBER" || role == "BUYERSELLER") {
                    if (checkEditRequest()) {
                        checkAuthorization(id);
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
                    noticeContent = 'Click <a href="/gwa/admin">[HERE]</a> to back to your site.';
                    $("#tradePostDiv").hide();
                }
                console.log("Login as " + role);
            } else {
                $("#noticeTitle").css("color", "red");
                noticeTitle = "Opps! You need login to stay here!";
                noticeContent = 'Click <a href="/gwa/login?goBack=1">[HERE]</a> to login.';
                $("#tradePostDiv").hide();
                console.log("Guest is accessing !");
            }
            $("#noticeTitle").html(noticeTitle);
            $("#noticeContent").html(noticeContent);

        }
    });

}



function checkAuthorization(userId) {
    var tradepostID = getUrlParameter("tradepostID");
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/gwa/api/tradepost/get-trade-post-edit-form-data?tradepostID=" + tradepostID,
        async: false,
        complete: function (xhr, status) {
            if (status == "success") {
                var xhr_data = xhr.responseText;
                var jsonResponse = JSON.parse(xhr_data);
                var userIdPost = jsonResponse["traderId"];
                if (userId == userIdPost) {//This login user owner this post
                    authorStatus = 1;
                    tradepost_data_edit = jsonResponse;
                    currentTradePostID = jsonResponse["tradeId"];
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

function loadEditForm(editformData) {
    ImageNamePrefix = "tradepost_" + currentAccountName + "_" + currentTradePostID + "_";
    console.log(ImageNamePrefix);
    //SET EDIT API TO FORM
    $("#tradepostForm").attr("action", "/gwa/api/tradepost/edit-trade-post");
    //SET NAME OF BUTTOM
    $("#submitTradeBtn").html("Save Your Trade");

    //SET TRADE POST DATA
    $("#tradeId").val(editformData["tradeId"]);
    if (editformData["tradeType"] === "sell") {
        $("#tradeType-sell").prop('checked', true);
    } else {
        $("#tradeType-buy").prop('checked', true);
    }
    $("#tradeTitle").val(editformData["tradeTitle"]);
    if (editformData["tradeCondition"] === "new") {
        $("#tradeCondition-new").prop('checked', true);
    } else {
        $("#tradeCondition-used").prop('checked', true);
    }
    $("#tradePrice").attr("value", editformData["tradePrice"]);
    if (editformData["tradeNegotiable"] === "on") {
        $("#tradeNegotiable").prop('checked', true);
    }
    $("#tradeQuantity").val(editformData["tradeQuantity"]);
    $("#tradeBrand").val(editformData["tradeBrand"]);
    $("#tradeModel").val(editformData["tradeModel"]);
    $("#tradeDesc").val(editformData["tradeDesc"]);

    var imgListArr = [];
    imgListArr = editformData["imageUploadedList"];

    for (var i = 0; i < imgListArr.length; i++) {
        FiletoSubmit.push(imgListArr[i]);
        fileSelected++;
        var fileupObj = {};
        fileupObj["id"] = imgListArr[i].split("downloadFile/")[1].split(".")[0].split(ImageNamePrefix)[1];
        fileupObj["name"] = imgListArr[i].split("downloadFile/")[1];
        fileupObj["previewUrl"] = imgListArr[i];
        fileupObj["saved"] = true;
        imageListFileUp.push(fileupObj);
    }

    //Trader profile load
    $("#traderName").val(editformData["traderName"]);
    $("#traderPhone").val(editformData["traderPhone"]);
    $("#traderEmail").val(editformData["traderEmail"]);
    $("#traderAddress").val(editformData["traderAddress"]);


}

function loadProfileData(accountID) {
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
            minlength: 3,
            maxlength: 100
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
        tradeType: {
            required: true

        },
        tradeCondition: {
            required: true
        },
        sendCheck: {
            required: true
        }
    },
    messages: {
        tradeTitle: {
            required: "You missing your title here.",
            minlength: "Your title too short, request at least 3 characters.",
            maxlength: "Your title too long, request maximum 100 characters."
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
        }
    },
    submitHandler: function (form) {
        if (fileSelected < 1) {
            alert("You must select at least 1 image");
            return;
        }
        ajaxSubmitForm(form);
        ajaxUploadImageList();
        ajaxUpdateImagesToDatabase(currentTradePostID, FiletoSubmit);
    }
});
function checkEditRequest() {
    var uriPath = window.location.pathname;
    if (uriPath == "/gwa/trade-market/edit-trade") {
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
                $("#traderAddress").val(results[0].formatted_address);

            }
        }
    );
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




