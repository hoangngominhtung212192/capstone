var tradepost_data_edit; //Load data cua form edit
var imageListFileUp = []; //Luu String Json cua form hinh, load nhung hinh da lua trong database len
var FiletoSubmit = []; //Lua nhung link hinh de lua vao database
var fileSelected = 0; //Bo dem so file da dc select vao form
var authorStatus;// 1 = Yes , 0 = No //Kiem tra bai nay co thuoc quyen so hua cua login session ko
var notFoundStatus;// 1 = notfound , 0 = found
var ImageNamePrefix;
var currentAccountName;
var currentTradePostID = -1;
var loginAccountId;

$(document).ready(function () {
    if (checkValidRequest()) { //Kiem tra duong link co dung ko
        notFoundStatus = 0;

        /*   Begin authentication and notification  */
        // process UI

        $(document).click(function (event) {
            $("#dropdown-profile").css("display", "none");
            $("#dropdown-notification").css("display", "none");
        })


        authentication();

        var account_session_id;

        function authentication() {
            $("#tradePostDiv").hide();
            var noticeTitle = "";
            var noticeContent = "";
            $.ajax({
                type: "GET",
                url: "/gwa/api/user/checkLogin",
                complete: function (xhr, status) {

                    if (status == "success") {
                        // username click
                        usernameClick();

                        var xhr_data = xhr.responseText;
                        var jsonResponse = JSON.parse(xhr_data);

                        var role = jsonResponse["role"].name;
                        var username = jsonResponse["username"];
                        var id = jsonResponse["id"];
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

                        loginAccountId = id;
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
                            fileupInit();
                        } else if (role == "ADMIN") {

                            $("#adminBtn").css("display", "block");

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
                        $("#profile-div").css("display", "none");
                        $("#loginForm").css("display", "block");
                    }
                    $("#noticeTitle").html(noticeTitle);
                    $("#noticeContent").html(noticeContent);

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

        function fileupInit() {
            /* FILE UP JQUERY INIT */
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
                    if (file.file.saved) {
                        //Neu co thi go khoi danh sach link hinh anh
                        for (var i = 0; i < FiletoSubmit.length; i++) {
                            if (FiletoSubmit[i].split("downloadFile/")[1].split(".")[0].split(ImageNamePrefix)[1] == file_number) {//lay id cua tam anh == so thu tu file tren form
                                FiletoSubmit.splice(i, 1);// go tam hinh co so thu tu = id tam hinh
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

function ajaxSubmitForm(form) {
    var formObj = {};
    new FormData(form).forEach(function (value, key) {
        formObj[key] = value;
    });
    var url = $(form).attr("action");
    var formDataJson = JSON.stringify(formObj);
    // console.log(formDataJson);
    $.ajax({
        url: url,
        type: "POST",
        async: false,
        data: formDataJson,
        contentType: "application/json",
        success: function (result, txtStatus, xhr) {
            currentTradePostID = result;
            var desc = "You just submit your trade post with id=" + result + ". Please wait administrator approve it.";
            var notiType = 3;
            addNewNotification(desc,result,loginAccountId,notiType);

            $.growl.notice({title: txtStatus, message: "Your trade post has been submited."});
            $("#tradePostDiv").hide();
            $("#noticeTitle").html("Trade post has been submited.").css("color", "green");
            $("#noticeContent").html("Redirecting to your trade post list page...");
            setTimeout(function () {
                window.location.href = "/gwa/trade-market/my-trade#pendingtrade";
            }, 3000);
        },
        error: function (xhr, textStatus, errorThrown) {
            $.growl.error({
                title: textStatus,
                message: "Something is wrong when submit your trade post. Please contact Administrator for more information."
            });
        }
    });

}

function ajaxUploadImageList() {
    ImageNamePrefix = "tradepost_" + currentAccountName + "_" + currentTradePostID + "_";

    $.fileup.updatePrefixName('upload-2', ImageNamePrefix);
    $.fileup('upload-2', 'upload', '*');

}

function ajaxUpdateImagesToDatabase(tradepostId, images) {
    $.ajax({
        type: "POST",
        url: "/gwa/api/tradepost/update-images-to-database",
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


function checkAuthorization(userId) {
    var tradepostID = getUrlParameter("tradepostID");
    $.ajax({
        type: "GET",
        url: "/gwa/api/tradepost/get-trade-post-edit-form-data?tradepostID=" + tradepostID,
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
    // console.log(editformData);
    ImageNamePrefix = "tradepost_" + currentAccountName + "_" + currentTradePostID + "_";
    // console.log(ImageNamePrefix);
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
    $("#traderLatlng").val(editformData["traderLatlng"]);


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
            addressToLatlng(traderAddress);
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
        addressToLatlng(addressText.val())
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
        waitingDialog.show('Submiting your trade post...', {dialogSize: '', progressType: 'info'});
        setTimeout(function () {
            ajaxSubmitForm(form);
            ajaxUploadImageList();
            ajaxUpdateImagesToDatabase(currentTradePostID, FiletoSubmit);
            waitingDialog.hide();
        }, 1000);

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

function addressToLatlng(address) {
    var geocoder = new google.maps.Geocoder();

    geocoder.geocode( { 'address': address}, function(results, status) {

        if (status == google.maps.GeocoderStatus.OK) {
            var latitude = results[0].geometry.location.lat();
            var longitude = results[0].geometry.location.lng();
            var result = latitude + "," + longitude;
            console.log(result);
            $("#traderLatlng").val(result);
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
    var google_map_pos = new google.maps.LatLng(lat, lng);

    /* Use Geocoder to get address */
    var google_maps_geocoder = new google.maps.Geocoder();
    google_maps_geocoder.geocode(
        {'latLng': google_map_pos},
        function (results, status) {
            if (status == google.maps.GeocoderStatus.OK && results[0]) {
                $("#traderAddress").val(results[0].formatted_address);
                // console.log(lat + "," + lng);
                $("#traderLatlng").val(lat + "," + lng);

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




