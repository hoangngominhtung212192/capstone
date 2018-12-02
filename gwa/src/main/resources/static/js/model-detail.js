$(document).ready(function () {

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

    // get modelID param
    var current_model_id = getUrlParameter('modelID');

    // process UI
    $(document).click(function (event) {
        $("#dropdown-profile").css("display", "none");
        $("#dropdown-notification").css("display", "none");
    })

    authentication();

    var account_session_id;
    var current_model_id;

    function authentication() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {
                    // username click
                    usernameClick();

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);

                    var username = jsonResponse["username"];
                    var thumbAvatar = jsonResponse["avatar"];
                    console.log(jsonResponse["role"].name + " " + username + " is on session!");
                    $("#membersince").text("Member since " + jsonResponse["createdDate"].split(" ")[0]);

                    // click profile button
                    profileClick(jsonResponse["id"]);
                    getSessionProfile(jsonResponse["id"]);
                    accountID = jsonResponse["id"];
                    account_session_id = jsonResponse["id"];
                    ajaxGetAllNotification(jsonResponse["id"]);
                    ajaxGetStatistic(jsonResponse["id"]);
                    role = jsonResponse["role"].name;

                    // display username, profile and logout button
                    if (thumbAvatar) {
                        $("#thumbAvatar-new").attr("src", thumbAvatar);
                        $("#thumbAvatar-dropdown").attr("src", thumbAvatar);
                    }

                    if (jsonResponse["role"].name == "ADMIN") {
                        $("#adminBtn").css("display", "block");
                        $("#admin-buttons").css("display", "block");
                        $("#edit-btn").click(function (e) {
                            window.location.href = "/gwa/pages/edit-model.html?modelID=" + current_model_id;
                        });
                        $("#status-tr").css("display", "block");
                    }

                } else {
                    // display login and register button
                    console.log("Guest is accessing !");
                    $("#profile-div").css("display", "none");
                    $("#loginForm").css("display", "block");
                }

                // get model detail
                ajaxGetDetail(current_model_id);
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

    var modelID;
    var accountID;

    function ajaxGetDetail(modelID) {

        $("#loading").css("display", "block");

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/getDetail?id=" + modelID,
            success: function (result) {
                console.log(result);
                setDetailInfo(result.model);
                renderImage(result.modelimageList);
                ajaxGetRelatedTradePost(result.model.name);
                ajaxGetRelatedArticle(result.model.name);
                $("#loading").css("display", "none");
            },
            complete: function (xhr, status) {
                if (status == "error") {
                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);

                    alert(jsonResponse["message"]);
                }
                $("#loading").css("display", "none");
            }
        });
    }

    function setDetailInfo(detail) {
        modelID = detail.id;
        $("#modelCode").text(detail.code);
        $("#modelName").text(detail.name);
        $("#rating-title").text(detail.name);
        if (detail.manufacturer) {
            $("#manufacturer").text(detail.manufacturer.name);
        } else {
            $("#manufacturer").text("N/A");
        }
        $("#productseries").text(detail.productseries.name);
        $("#seriestitle").text(detail.seriestitle.name);
        if (detail.releasedDate) {
            $("#releasedDate").text(detail.releasedDate);
        } else {
            $("#releasedDate").text("N/A");
        }
        $("#price").text(detail.price);
        $("#description").append(detail.description);

        if (detail.numberOfRater == 0) {
            $("#ratingValue").text("N/A")
            $("#ratingValue").css("color", "darkblue");
            $("#ratingValue").css("font-size", "16px");
            $("#icon-review").css("display", "none");
        } else {
            $("#ratingValue").css("color", "red");
            $("#ratingValue").css("font-size", "20px");

            var averageRating = Math.round(detail.numberOfRating / detail.numberOfRater);
            console.log("AverageRating: " + averageRating);
            for (var i = 0; i < averageRating; i++) {
                $("#ratingValue").append("&#9733;");
            }

            $("#numberOfRater").text("(" + detail.numberOfRater + ")");
            $("#numberOfRater").css("display", "block");
        }

        if (!accountID) {
            $("#my-rating").css("display", "none");
        } else {
            checkExistRating(detail.id, accountID);
        }

        $("#status").text(detail.status);
    }

    var packageContainer = "<div class=\"model-information-image-grid\">\n" +
        "                                    <div class=\"model-information-image-type\">\n" +
        "                                        Package\n" +
        "                                    </div>" +
        "<div class=\"model-information-image-items\">\n" +
        "                                        <div class=\"w3-content w3-display-container\"\n" +
        "                                             style=\"max-width:220px;height:100%\">\n" +
        "                                            <div style=\"width:100%; height:100%\">";
    var listPackageImage = "";

    var itemContainer = "<div class=\"model-information-image-grid\">\n" +
        "                                    <div class=\"model-information-image-type\">\n" +
        "                                        Item picture\n" +
        "                                    </div>\n" +
        "\n" +
        "                                    <div class=\"model-information-image-items\">\n" +
        "                                        <div class=\"w3-content w3-display-container\"\n" +
        "                                             style=\"max-width:220px;height:100%\">";

    var listItemImage = "";

    var otherContainer = "<div class=\"model-information-image-grid\">\n" +
        "                                    <div class=\"model-information-image-type\">\n" +
        "                                        Other picture\n" +
        "                                    </div>\n" +
        "\n" +
        "                                    <div class=\"model-information-image-items\">\n" +
        "                                        <div class=\"w3-content w3-display-container\"\n" +
        "                                             style=\"max-width:220px;height:100%\">";

    var listOtherImage = "";

    var contentsContainer = "<div class=\"model-information-image-grid\">\n" +
        "                                    <div class=\"model-information-image-type\">\n" +
        "                                        Contents\n" +
        "                                    </div>\n" +
        "\n" +
        "                                    <div class=\"model-information-image-items\">\n" +
        "                                        <div class=\"w3-content w3-display-container\"\n" +
        "                                             style=\"max-width:220px;height:100%\">";

    var listContentsImage = "";

    var aboutContainer = "<div class=\"model-information-image-grid\">\n" +
        "                                    <div class=\"model-information-image-type\">\n" +
        "                                        About item\n" +
        "                                    </div>\n" +
        "\n" +
        "                                    <div class=\"model-information-image-items\">\n" +
        "                                        <div class=\"w3-content w3-display-container\"\n" +
        "                                             style=\"max-width:220px;height:100%\">";

    var listAboutImage = "";

    var colorContainer = "<div class=\"model-information-image-grid\">\n" +
        "                                    <div class=\"model-information-image-type\">\n" +
        "                                        Color\n" +
        "                                    </div>\n" +
        "\n" +
        "                                    <div class=\"model-information-image-items\">\n" +
        "                                        <div class=\"w3-content w3-display-container\"\n" +
        "                                             style=\"max-width:220px;height:100%\">";

    var listColorImage = "";

    var assemblyContainer = "<div class=\"model-information-image-grid\">\n" +
        "                                    <div class=\"model-information-image-type\">\n" +
        "                                        Assembly guide\n" +
        "                                    </div>\n" +
        "\n" +
        "                                    <div class=\"model-information-image-items\">\n" +
        "                                        <div class=\"w3-content w3-display-container\"\n" +
        "                                             style=\"max-width:220px;height:100%\">";

    var listAssemblyImage = "";
    var thumbImage;
    var listImageSize;

    function renderImage(result) {

        listImageSize = result.length;

        if (result) {
            $.each(result, function (i, value) {
                if (value.imagetype.name == "Package") {
                    listPackageImage += "<a href='" + value.imageUrl + "'><img id='" + value.imageUrl + "' class=\"mySlides1\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\"></a>";
                    thumbImage = value.imageUrl;
                }
                if (value.imagetype.name == "Item picture") {
                    listItemImage += "<a href='" + value.imageUrl + "'><img class=\"mySlides2\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\"></a>";
                    if (!thumbImage) {
                        thumbImage = value.imageUrl;
                    }
                }
                if (value.imagetype.name == "Other picture") {
                    listOtherImage += "<a href='" + value.imageUrl + "'><img class=\"mySlides3\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\"></a>";
                    if (!thumbImage) {
                        thumbImage = value.imageUrl;
                    }
                }
                if (value.imagetype.name == "Contents") {
                    listContentsImage += "<a href='" + value.imageUrl + "'><img class=\"mySlides4\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\"></a>";
                    if (!thumbImage) {
                        thumbImage = value.imageUrl;
                    }
                }
                if (value.imagetype.name == "About item") {
                    listAboutImage += "<a href='" + value.imageUrl + "'><img class=\"mySlides5\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\"></a>";
                    if (!thumbImage) {
                        thumbImage = value.imageUrl;
                    }
                }
                if (value.imagetype.name == "Color") {
                    listColorImage += "<a href='" + value.imageUrl + "'><img class=\"mySlides6\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\"></a>";
                    if (!thumbImage) {
                        thumbImage = value.imageUrl;
                    }
                }
                if (value.imagetype.name == "Assembly guide") {
                    listAssemblyImage += "<a href='" + value.imageUrl + "'><img class=\"mySlides7\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\"></a>";
                    if (!thumbImage) {
                        thumbImage = value.imageUrl;
                    }
                }
            });

            $("#thumbImage").attr("src", thumbImage);

            if (listPackageImage) {
                packageContainer += listPackageImage;
                packageContainer += "</div>\n" +
                    "                                            <div class=\"w3-center w3-container w3-section w3-large w3-text-white w3-display-bottommiddle\"\n" +
                    "                                                 style=\"width:100%\">\n" +
                    "                                                <div class=\"w3-left w3-hover-text-khaki\" onclick=\"plusDivs1(-1)\">\n" +
                    "                                                    &#10094;\n" +
                    "                                                </div>\n" +
                    "                                                <div class=\"w3-right w3-hover-text-khaki\" onclick=\"plusDivs1(1)\">\n" +
                    "                                                    &#10095;\n" +
                    "                                                </div>\n" +
                    "                                            </div>\n" +
                    "                                        </div>\n" +
                    "                                    </div>\n" +
                    "                                </div>";
                $("#image-container").append(packageContainer);
                showDivs("mySlides1");
            }
            if (listItemImage) {
                itemContainer += listItemImage;
                itemContainer += "<div class=\"w3-center w3-container w3-section w3-large w3-text-white w3-display-bottommiddle\"\n" +
                    "                                                 style=\"width:100%\">\n" +
                    "                                                <div class=\"w3-left w3-hover-text-khaki\" onclick=\"plusDivs2(-1)\">\n" +
                    "                                                    &#10094;\n" +
                    "                                                </div>\n" +
                    "                                                <div class=\"w3-right w3-hover-text-khaki\" onclick=\"plusDivs2(1)\">\n" +
                    "                                                    &#10095;\n" +
                    "                                                </div>\n" +
                    "                                            </div>\n" +
                    "                                        </div>\n" +
                    "                                    </div>\n" +
                    "                                </div>";
                $("#image-container").append(itemContainer);
                showDivs("mySlides2");
            }
            if (listOtherImage) {
                otherContainer += listOtherImage;
                otherContainer += "<div class=\"w3-center w3-container w3-section w3-large w3-text-white w3-display-bottommiddle\"\n" +
                    "                                                 style=\"width:100%\">\n" +
                    "                                                <div class=\"w3-left w3-hover-text-khaki\" onclick=\"plusDivs3(-1)\">\n" +
                    "                                                    &#10094;\n" +
                    "                                                </div>\n" +
                    "                                                <div class=\"w3-right w3-hover-text-khaki\" onclick=\"plusDivs3(1)\">\n" +
                    "                                                    &#10095;\n" +
                    "                                                </div>\n" +
                    "                                            </div>\n" +
                    "                                        </div>\n" +
                    "                                    </div>\n" +
                    "                                </div>";
                $("#image-container").append(otherContainer);
                showDivs("mySlides3");
            }
            if (listContentsImage) {
                contentsContainer += listContentsImage;
                contentsContainer += "<div class=\"w3-center w3-container w3-section w3-large w3-text-white w3-display-bottommiddle\"\n" +
                    "                                                 style=\"width:100%\">\n" +
                    "                                                <div class=\"w3-left w3-hover-text-khaki\" onclick=\"plusDivs4(-1)\">\n" +
                    "                                                    &#10094;\n" +
                    "                                                </div>\n" +
                    "                                                <div class=\"w3-right w3-hover-text-khaki\" onclick=\"plusDivs4(1)\">\n" +
                    "                                                    &#10095;\n" +
                    "                                                </div>\n" +
                    "                                            </div>\n" +
                    "                                        </div>\n" +
                    "                                    </div>\n" +
                    "                                </div>";
                $("#image-container").append(contentsContainer);
                showDivs("mySlides4");
            }
            if (listAboutImage) {
                aboutContainer += listAboutImage;
                aboutContainer += "<div class=\"w3-center w3-container w3-section w3-large w3-text-white w3-display-bottommiddle\"\n" +
                    "                                                 style=\"width:100%\">\n" +
                    "                                                <div class=\"w3-left w3-hover-text-khaki\" onclick=\"plusDivs5(-1)\">\n" +
                    "                                                    &#10094;\n" +
                    "                                                </div>\n" +
                    "                                                <div class=\"w3-right w3-hover-text-khaki\" onclick=\"plusDivs5(1)\">\n" +
                    "                                                    &#10095;\n" +
                    "                                                </div>\n" +
                    "                                            </div>\n" +
                    "                                        </div>\n" +
                    "                                    </div>\n" +
                    "                                </div>";
                $("#image-container").append(aboutContainer);
                showDivs("mySlides5");
            }
            if (listColorImage) {
                colorContainer += listColorImage;
                colorContainer += "<div class=\"w3-center w3-container w3-section w3-large w3-text-white w3-display-bottommiddle\"\n" +
                    "                                                 style=\"width:100%\">\n" +
                    "                                                <div class=\"w3-left w3-hover-text-khaki\" onclick=\"plusDivs6(-1)\">\n" +
                    "                                                    &#10094;\n" +
                    "                                                </div>\n" +
                    "                                                <div class=\"w3-right w3-hover-text-khaki\" onclick=\"plusDivs6(1)\">\n" +
                    "                                                    &#10095;\n" +
                    "                                                </div>\n" +
                    "                                            </div>\n" +
                    "                                        </div>\n" +
                    "                                    </div>\n" +
                    "                                </div>";
                $("#image-container").append(colorContainer);
                showDivs("mySlides6");
            }
            if (listAssemblyImage) {
                assemblyContainer += listAssemblyImage;
                assemblyContainer += "<div class=\"w3-center w3-container w3-section w3-large w3-text-white w3-display-bottommiddle\"\n" +
                    "                                                 style=\"width:100%\">\n" +
                    "                                                <div class=\"w3-left w3-hover-text-khaki\" onclick=\"plusDivs7(-1)\">\n" +
                    "                                                    &#10094;\n" +
                    "                                                </div>\n" +
                    "                                                <div class=\"w3-right w3-hover-text-khaki\" onclick=\"plusDivs7(1)\">\n" +
                    "                                                    &#10095;\n" +
                    "                                                </div>\n" +
                    "                                            </div>\n" +
                    "                                        </div>\n" +
                    "                                    </div>\n" +
                    "                                </div>";
                $("#image-container").append(assemblyContainer);
                showDivs("mySlides7");
            }

            if (role != "ADMIN") {
                console.log(role);
                $('img').on("error", function () {
                    countErrorImage++;
                    checkErrorImage();
                });
            }
        }
    }

    var role;
    var countErrorImage = 0;
    var flagErrorImage = false;

    function showDivs(classSlide) {
        var i;
        var x = $("." + classSlide);
        var dots = $(".demo");
        for (i = 0; i < x.length; i++) {
            x[i].style.display = "none";
        }
        for (i = 0; i < dots.length; i++) {
            dots[i].className = dots[i].className.replace(" w3-white", "");
        }
        x[0].style.display = "block";
    }

    function checkErrorImage() {
        if (countErrorImage > (listImageSize / 3) && !flagErrorImage) {
            addNewNotification();
            ajaxUpdateStatusModel();
            flagErrorImage = true;
        }
    }

    function ajaxUpdateStatusModel() {

        $.ajax({
            type: "POST",
            url: "/gwa/api/model/updateError?modelID=" + current_model_id,
            success: function (result) {
                $("#modal-head").remove();
                $("#modal-h4").text("Oops!");
                $("#modal-p").text("This model is unavailable right now, we are sorry !");
                $("#modal-span").text("Return to Gundam Page");

                $("#success-modal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    window.location.href = "/gwa/model/";
                });
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    /* This is for rating model */
    function checkExistRating(modelID, accountID) {

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/rating/checkExist?modelID=" + modelID + "&accountID=" + accountID,
            success: function (result) {
                console.log("User " + accountID + " did rating this model? : " + result);
                if (result == "Existed") {
                    $("#my-rating").css("display", "none");
                }
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    $("#submit-rating").click(function (e) {
        e.preventDefault();

        var ratingValue = $('input[name=rating]:checked').val();
        var txtReview = $("#txtReview").val();

        if (checkValid(txtReview)) {

            $("#mi-modal").modal({backdrop: 'static', keyboard: false});
            $("#modal-btn-si").on("click", function () {
                $("#mi-modal").modal('hide');
                $("#modal-btn-no").prop("onclick", null).off("click");
                $("#modal-btn-si").prop("onclick", null).off("click");
            });

            $("#modal-btn-no").on("click", function (e) {

                console.log("Rating: " + ratingValue);
                console.log("Feedback: " + txtReview);

                var formRating = {
                    model: {
                        id: modelID
                    },
                    account: {
                        id: accountID
                    },
                    rating: ratingValue,
                    feedback: txtReview
                }

                ajaxRating(formRating);

                $("#myModal").modal('hide');

                $("#mi-modal").modal('hide');
                $("#modal-btn-no").prop("onclick", null).off("click");
                $("#modal-btn-si").prop("onclick", null).off("click");
            });
        }

        return false;
    })

    function checkValid(txtReview) {

        var check = true;

        if (!txtReview) {
            check = false;

            $("#errorFeedback").css("display", "block");
            $("#errorFeedback").text("Please input your feedback")
        } else if (txtReview.length > 250) {
            check = false;

            $("#errorFeedback").css("display", "block");
            $("#errorFeedback").text("Maximum length: 250 characters");
        } else {
            $("#errorFeedback").css("display", "none");
        }

        if ($('input[name=rating]:checked').length == 0) {
            check = false;

            $("#errorRating").css("display", "block");
            $("#errorRating").text("Please rate this model");
        } else {
            $("#errorRating").css("display", "none");
        }

        return check;
    }

    function ajaxRating(data) {

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/gwa/api/model/rating/create",
            data: JSON.stringify(data),
            success: function (result) {
                console.log(result);

                $("#success-modal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    window.location.href = "/gwa/pages/modeldetail.html?modelID=" + current_model_id;
                });
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    // begin get all rating area
    var pageNumber = 1;
    var lastPage;

    function ajaxGetAllRating() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/rating/getAll?pageNumber=" + pageNumber + "&modelID=" + current_model_id,
            success: function (result) {
                lastPage = result[0];
                renderModelRatings(result[1]);

                console.log(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    $("#scrollTable").scroll(function () {
        if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
            if (pageNumber < lastPage) {
                pageNumber += 1;

                $("#loading").css("display", "block");

                setTimeout(function () {
                    ajaxGetAllRating();

                    $("#loading").css("display", "none");
                }, 400);
            }
        }
    });

    function renderModelRatings(result) {

        $.each(result, function (i, value) {
            var appendReview = "";

            appendReview += "<tr>\n" +
                "<td style=\"vertical-align: top; padding-top: 10px;\">\n" +
                "<img src=\"" + value.account.avatar + "\" onerror=\"this.src='/gwa/img/avatar_2x.png'\"/>\n" +
                " </td>\n" +
                "<td class=\"review-info\">\n" +
                "<a href=\"/gwa/pages/profile.html?accountID=" + value.account.id + "\">" + value.account.username +
                "</a>\n";

            appendReview += "<span class=\"reputation-star-rating\">";

            for (var j = 0; j < value.rating; j++) {
                appendReview += "&#9733;";
            }

            appendReview += "</span>\n" +
                "                                <span class=\"ratingDate\">(" + value.date + ")</span>\n" +
                "                                <br/>\n" +
                "                                <div class=\"review-info-text\">\n" + value.feedback +
                "                                </div>\n" +
                "                            </td>\n" +
                "                        </tr>";

            $("#reviewList").append(appendReview);
        });
    }

    $("#icon-review").click(function (e) {
        $("#reviewList").empty();
        pageNumber = 1;

        ajaxGetAllRating();
    });
    // end get all rating

    ajaxGetTop5Rating();

    function ajaxGetTop5Rating() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/getTop5Rating",
            success: function (result) {
                if (result.length > 0) {
                    console.log("Top 5 rating: " + result);
                    renderTop5Rating(result);
                }
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function renderTop5Rating(result) {
        var appendTop5 = "";
        appendTop5 += "<div class=\"ul-category-title-content\">\n" +
            "                <ul class=\"ul-category-contents\">\n" +
            "                <li class=\"lead-top-five\">\n" +
            "                <strong>Model\n" +
            "                </strong></li>\n" +
            "            </ul>\n" +
            "            </div>";

        $.each(result, function (idx, value) {

            appendTop5 += "<div class=\"image-top-five\">\n" +
                "                <a href='/gwa/pages/modeldetail.html?modelID=" + value.id + "'>" +
                "<img class=\"image-top-five-items\" src=\"" + value.thumbImage + "\"/>" +
                "</a>\n" +
                "                </div>";

        });

        $("#right-container").append(appendTop5);
    }

    function ajaxGetRelatedTradePost(modelName) {
        $.ajax({
            type: "GET",
            url: "/gwa/api/model/getRelatedTradePost?modelName=" + modelName,
            success: function (result) {
                if (result.length > 0) {
                    console.log("Top 5 related trade post: " + result);
                    renderRelatedTradePost(result);
                }
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function renderRelatedTradePost(result) {
        var appendRelatedTrade = "";
        appendRelatedTrade += "<div class=\"ul-category-title-content\">\n" +
            "                    <ul class=\"ul-category-contents\">\n" +
            "                        <li class=\"lead-top-five\">\n" +
            "                            <strong>Related Trade Post\n" +
            "                            </strong></li>\n" +
            "                    </ul>\n" +
            "                </div>";

        $.each(result, function (idx, value) {

            appendRelatedTrade += "<div class=\"words-top-five\">\n" +
                "                    <a href=\"/gwa/trade-market/view-trade?tradepostId=" + value.id +"\">" + value.title + "</a>\n" +
                "                </div>";

        });

        $("#right-container").append(appendRelatedTrade);
    }

    function ajaxGetRelatedArticle(modelName) {
        $.ajax({
            type: "GET",
            url: "/gwa/api/model/getTop5Article?modelName=" + modelName,
            success: function (result) {
                if (result.length > 0) {
                    console.log("Top 5 related article: " + result);
                    renderRelatedArticle(result);
                }
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function renderRelatedArticle(result) {
        var appendRelatedArticle = "";
        appendRelatedArticle += "<div class=\"ul-category-title-content\">\n" +
            "                    <ul class=\"ul-category-contents\">\n" +
            "                        <li class=\"lead-top-five\">\n" +
            "                            <strong>Related Article\n" +
            "                            </strong></li>\n" +
            "                    </ul>\n" +
            "                </div>";

        $.each(result, function (idx, value) {

            appendRelatedArticle += "<div class=\"words-top-five\">\n" +
                "                    <a href=\"/gwa/article/detail?id=" + value.id + "\">" + value.title + "</a>\n" +
                "                </div>";

        });

        $("#right-container").append(appendRelatedArticle);
    }

    /* This is for left nav-bar */
    ajaxGetProductseries();
    ajaxGetSeriestitle();

    function ajaxGetProductseries() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/getAllProductseries",
            success: function (result) {
                console.log(result);

                renderProductSeries(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function renderProductSeries(result) {

        $.each(result, function (key, entry) {
            $("#product-series-ul").append("<a class=\"title-li-a\" href=\"/gwa/pages/model.html?categoryName=" +
                entry.name + "&categoryType=ProductSeries\">\n" +
                "                            <li class=\"title-li\">" + entry.name + "</li>\n" +
                "                        </a>");
        })
    }

    function ajaxGetSeriestitle() {

        $("#cbo-seriestitle").append($('<option></option>').attr('value', 'All').text('All'));

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/getAllSeriestitle",
            success: function (result) {
                console.log(result);

                renderSeriesTitle(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function renderSeriesTitle(result) {

        $.each(result, function (key, entry) {
            $("#series-title-ul").append("<a class=\"title-li-a\" href=\"/gwa/pages/model.html?categoryName=" +
                entry.name + "&categoryType=SeriesTitle\">\n" +
                "                            <li class=\"title-li\">" + entry.name + "</li>\n" +
                "                        </a>");
        })
    }

    /* End left nav-bar */

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

        var formNotification = {
            description: "Model " + current_model_id + " has error loading image!!",
            objectID: current_model_id,
            account: {
                id: 3
            },
            notificationtype: {
                id: 2
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

    $("#deleteBtn").click(function (e) {
        e.preventDefault();

        $("#mi-modal").modal({backdrop: 'static', keyboard: false});
        $("#modal-btn-si").on("click", function () {
            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });

        $("#modal-btn-no").on("click", function (e) {
            ajaxDeleteModel();

            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });
    });

    function ajaxDeleteModel() {

        $.ajax({
            type: "POST",
            url: "/gwa/api/model/delete?modelID=" + modelID,
            success: function (result) {
                alert("Delete successfully !");
                window.location.href = "/gwa/admin/model/manage";
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

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