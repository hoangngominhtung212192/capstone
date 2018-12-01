$(document).ready(function () {
    var checkImage = false;
    var imagetype;
    var imageFile;
    var formData = new FormData();
    $("#photoBtn").change(function (e) {
        console.log($("#photoBtn").val());

        for (var i = 0; i < e.originalEvent.srcElement.files.length; i++) {

            var file = e.originalEvent.srcElement.files[i];
            imagetype = file.type;
            var match = ["image/jpeg", "image/png", "image/jpg"];
            if (!((imagetype == match[0]) || (imagetype == match[1]) || (imagetype == match[2]))) {
                checkImage = false;
                alert("select img pls");
                // $("#imgError").css("display", "block");
                // $("#imgError").text("Please select image only");
            } else {
                var reader = new FileReader();
                reader.onloadend = function () {
                    $("#imgthumb").attr("src", reader.result);
                    // $("#avatar").css("height", "202px");
                    // $("#avatar").css("width", "202px");
                }
                reader.readAsDataURL(file);

                imageFile = file;

                checkImage = true;

                // $("#imgError").css("display", "none");
                // $("#imgError").text("");
            }

        }
    });

    function ajaxImagePost(formData) {
        console.log("updating image for "+formData)
        $.ajax({
            type: "POST",
            contentType: false,
            processData: false,
            url: "/gwa/api/article/uploadArticleImage",
            data: formData,
            success: function (result) {
                console.log(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        })
    }


    var getUrlParameter = function getUrlParameter() {
        var sPageURL = window.location.href;
        console.log(sPageURL);
        var url = new URL(sPageURL);
        var c = url.searchParams.get("id");
        console.log(c);
        arId = parseInt(c);
        return arId;
    }
    var id = getUrlParameter();

    showArticle(id);
    var articleAuthorID;
    var articleurl;

    function showArticle(data) {
        console.log("showing article with id: " + data);

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/gwa/api/article/getArticle",
            data : JSON.stringify(data),
            success : function(result, status) {
                console.log(result);
                console.log(status);
                if (result){
                    articleurl = "/gwa/article/detail?id="+result.id;
                    articleAuthorID = result.account.id;
                    $('#cboStatus').val(result.approvalStatus);
                    $("#imgthumb").attr("src", result.thumbImage);
                    $('#cboCate').val(result.category);
                    $('#txtTitle').val(result.title);
                    $('#txtDescription').val(result.description);
                    $('#date').append(result.date);
                    $('#contentEditor').html(result.content);
                    $('#author').append(result.account.username);
                }
            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }


    $("#btnSubmit").click(function (event) {
        var valid = true;
        if ($('#txtTitle').val() == "") {
            valid = false;
            $.growl.error({message: "Please enter title"});
        }
        if ($('#txtDescription').val() == ""){
            valid = false;
            $.growl.error({message: "Please enter article's description"});
        }
        if (CKEDITOR.instances.contentEditor.getData() == ""){
            valid = false;
            $.growl.error({message: "Please enter article's content"});
        }
        if ($('#imgthumb').attr("src") == "#"){
            valid = false;
            $.growl.error({message: "Please select a thumbnail image"});
        }
        if (valid == true) {
            event.preventDefault();
            var today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth() + 1; //January is 0!
            var yyyy = today.getFullYear();

            if (dd < 10) {
                dd = '0' + dd
            }

            if (mm < 10) {
                mm = '0' + mm
            }

            today = yyyy + "/" + mm + "/" + dd;
            var curImage ;
            if (!checkImage){
                curImage = $('#imgthumb').attr('src');
            }
            var formArticle = {
                id: id,
                title: $("#txtTitle").val(),
                content: CKEDITOR.instances.contentEditor.getData(),
                thumbImage : curImage,
                description: $('#txtDescription').val(),
                category: $("#cboCate").val(),
                modifiedDate: today,
                date: $('#lblDate').val(),
                approvalStatus: $("#cboStatus :selected").val(),
            }

            updateArticle(formArticle);
        }
    })


    var modalConfirm = function(callback){

        $("#deleteBtn").on("click", function(){
            $("#confi-modal").modal('show');
        });

        $("#modal-btn-yes").on("click", function(){
            callback(true);
            $("#confi-modal").modal('hide');
        });

        $("#modal-btn-no").on("click", function(){
            callback(false);
            $("#confi-modal").modal('hide');
        });
    };

    modalConfirm(function(confirm){
        if(confirm){
            deleteArticle();
            console.log("yes");
        }else{
            console.log("no");
        }
    });

    function deleteArticle() {
        console.log("delete article id "+id);
        $.ajax({
            type : "GET",
            url: "/gwa/api/article/deleteArticle?id=" + id,
            success : function(result, status) {
                $("#lblModalMessage").html("Article deleted");
                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function() {
                    window.location.href = "/gwa/admin/article";
                });

            },
            error : function(e) {
                alert("Error! Delete article failed!")
                console.log("ERROR: ", e);
            }
        });
    }

    var notidescription;
    function updateArticle(data) {
        console.log("updating")
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/gwa/api/article/updateArticle",
            data : JSON.stringify(data),
            success : function(result, status) {
                if(checkImage){
                    var type = imagetype.split("/")[1];
                    formData.append("id", result.id);

                    formData.append("photoBtn", imageFile, "thumbArtID" +result.id + "." + type);
                    ajaxImagePost(formData);
                }

                if (result.approvalStatus == "Approved"){
                    notidescription = "Your article was approved!"
                    console.log("sending approval noti");
                    addNewNotification();
                } else if(result.approvalStatus == "Disapproved"){
                    notidescription = "Your article was disapproved!"
                    addNewNotification();
                }
                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function() {
                    window.location.href = "/gwa/admin/article";
                });
            },
            error : function(e) {
                alert("Error! Update article failed!")
                console.log("ERROR: ", e);
            }
        });
    }
    /* Begin authentication & notification */
    authentication();

    var createdDate;
    var account_session_id;

    function authentication() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/user/checkLogin",
            complete: function (xhr, status) {
                if (status == "success") {

                    var xhr_data = xhr.responseText;
                    console.log(xhr_data);
                    var jsonResponse = JSON.parse(xhr_data);

                    var role_session = jsonResponse["role"].name;
                    account_session_id = jsonResponse["id"];

                    if (role_session != "ADMIN") {
                        window.location.href = "/gwa/403";
                    } else {
                        console.log(role_session + " " + jsonResponse["username"] + " is on session!");
                        $("#profileBtn").attr("href", "/gwa/pages/profile.html?accountID=" + jsonResponse["id"]);
                        $("#user-out-avatar").attr("src", jsonResponse["avatar"]);
                        $("#user-in-avatar").attr("src", jsonResponse["avatar"]);
                        $("#left-avatar").attr("src", jsonResponse["avatar"]);

                        createdDate = jsonResponse["createdDate"].split(" ")[0];
                        getSessionProfile(jsonResponse["id"]);
                        ajaxGetAllNotification(jsonResponse["id"]);
                    }

                } else {
                    window.location.href = "/gwa/login";
                }

            }
        });
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

                $("#user-in-name").text(displayUsername);
                $("#user-out-name").text(displayUsername);
                $("#user-in-name").append("<small>Member since " + createdDate + "</small>");

                $("#left-name").text(displayUsername);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    $("#signoutBtn").click(function (e) {
        e.preventDefault();

        $("#loading").css("display", "block");

        setTimeout(function () {
            $("#loading").css("display", "none");

            ajaxLogout();
        }, 300);
    })

    function ajaxLogout() {
        $.ajax({
            type: "GET",
            url: "/gwa/api/user/logout",
            success: function (result) {
                window.location.href = "/gwa/login";
            }
        });
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
        var description = notidescription;

        var formNotification = {
            description: description,
            objectID: id,
            account: {
                id: articleAuthorID
            },
            notificationtype: {
                id: 6
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
    /* End authentication & notification */

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

            pageNumber = 1;
            $("#ul-notification").empty();
            ajaxGetAllNotification(account_session_id);
            if (payload.notification.title == "Model" || payload.notification.title == "Event") {
                toastr.error(payload.notification.body, payload.notification.title + " Notification", {timeOut: 5000});
            } else {
                toastr.info(payload.notification.body, payload.notification.title + " Notification", {timeOut: 5000});
            }
        })
    })
    /* This is end of firebase  */
})
