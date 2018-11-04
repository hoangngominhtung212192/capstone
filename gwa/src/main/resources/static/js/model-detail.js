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
    // get model detail
    var current_model_id = getUrlParameter('modelID');
    ajaxGetDetail(current_model_id);

    // process UI
    $(document).click(function (event) {
        $("#dropdown-profile").css("display", "none");
    })

    authentication();

    function authentication() {

        $.ajax({
            type: "GET",
            url: "/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {
                    // username click
                    usernameClick();

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);

                    var username = jsonResponse["username"];
                    var thumbAvatar = jsonResponse["avatar"];
                    console.log(jsonResponse["role"].name + " " + username + " is on session!");

                    // click profile button
                    profileClick(jsonResponse["id"]);
                    getSessionProfile(jsonResponse["id"]);
                    accountID = jsonResponse["id"];

                    // display username, profile and logout button
                    if (thumbAvatar) {
                        $("#thumbAvatar-new").attr("src", thumbAvatar);
                        $("#thumbAvatar-dropdown").attr("src", thumbAvatar);
                    }

                    if (jsonResponse["role"].name == "ADMIN") {
                        $("#adminBtn").css("display", "block");
                        $("#admin-buttons").css("display", "block");
                        $("#edit-btn").click(function (e) {
                            window.location.href = "/pages/edit-model.html?modelID=" + current_model_id;
                        });
                    }

                } else {
                    // display login and register button
                    console.log("Guest is accessing !");
                    $("#profile-div").css("display", "none");
                    $("#loginForm").css("display", "block");
                }

            }
        });
    }

    // get session account's profile
    function getSessionProfile(id) {

        $.ajax({
            type: "POST",
            url: "/api/user/profile?accountID=" + id,
            success: function (result) {
                //get selected profile's account status

                var displayUsername = result.lastName + ' ' + result.firstName;
                $("#fullname-new").text(displayUsername);
                $("#fullname-dropdown").text(displayUsername);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    // logout button click event
    $("#logout-new").click(function (event) {

        event.preventDefault();

        ajaxLogout();
    });

    $("#adminBtn").click(function (event) {
        event.preventDefault();

        window.location.href = "/admin/model/pending";
    });

    function ajaxLogout() {
        $.ajax({
            type: "GET",
            url: "/api/user/logout",
            success: function (result) {
                window.location.href = "/login";
            }
        });
    }

    // profile button click event
    function profileClick(accountID) {
        $("#profile-new").click(function (event) {
            window.location.href = "/pages/profile.html?accountID=" + accountID;
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
            url: "/api/model/getDetail?id=" + modelID,
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

    function renderImage(result) {

        if (result) {
            $.each(result, function (i, value) {
                if (value.imagetype.name == "Package") {
                    listPackageImage += "<img class=\"mySlides1\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\">";
                    thumbImage = value.imageUrl;
                }
                if (value.imagetype.name == "Item picture") {
                    listItemImage += "<img class=\"mySlides2\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\">";
                    if (!thumbImage) {
                        thumbImage = value.imageUrl;
                    }
                }
                if (value.imagetype.name == "Other picture") {
                    listOtherImage += "<img class=\"mySlides3\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\">";
                    if (!thumbImage) {
                        thumbImage = value.imageUrl;
                    }
                }
                if (value.imagetype.name == "Contents") {
                    listContentsImage += "<img class=\"mySlides4\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\">";
                    if (!thumbImage) {
                        thumbImage = value.imageUrl;
                    }
                }
                if (value.imagetype.name == "About item") {
                    listAboutImage += "<img class=\"mySlides5\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\">";
                    if (!thumbImage) {
                        thumbImage = value.imageUrl;
                    }
                }
                if (value.imagetype.name == "Color") {
                    listColorImage += "<img class=\"mySlides6\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\">";
                    if (!thumbImage) {
                        thumbImage = value.imageUrl;
                    }
                }
                if (value.imagetype.name == "Assembly guide") {
                    listAssemblyImage += "<img class=\"mySlides7\" src=\"" + value.imageUrl + "\"\n" +
                        "                                                 style=\"width:100%; height:100%\">";
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
        }
    }

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

    /* This is for rating model */
    function checkExistRating(modelID, accountID) {

        $.ajax({
            type: "GET",
            url: "/api/model/rating/checkExist?modelID=" + modelID + "&accountID=" + accountID,
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
        }

        return false;
    })

    function checkValid(txtReview) {

        var check = true;

        if (!txtReview) {
            check = false;

            $("#errorFeedback").css("display", "block");
            $("#errorFeedback").text("Please input your feedback")
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
            url: "/api/model/rating/create",
            data: JSON.stringify(data),
            success: function (result) {
                console.log(result);
                alert("Submit successfully");

                window.location.href = "/pages/modeldetail.html?modelID=" + current_model_id;
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    var pageNumber = 1;
    var lastPage;

    function ajaxGetAllRating() {

        $.ajax({
            type: "GET",
            url: "/api/model/rating/getAll?pageNumber=" + pageNumber + "&modelID=" + current_model_id,
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

                ajaxGetAllRating();
            }
        }
    })

    function renderModelRatings(result) {

        $.each(result, function (i, value) {
            var appendReview = "";

            appendReview += "<tr>\n" +
                "<td style=\"vertical-align: top; padding-top: 10px;\">\n" +
                "<img src=\"" + value.account.avatar + "\" onerror=\"this.src='/img/avatar_2x.png'\"/>\n" +
                " </td>\n" +
                "<td class=\"review-info\">\n" +
                "<a href=\"/pages/profile.html?accountID=" + value.account.id + "\">" + value.account.username +
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

    ajaxGetTop5Rating();

    function ajaxGetTop5Rating() {

        $.ajax({
            type: "GET",
            url: "/api/model/getTop5Rating",
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
                "                <img class=\"image-top-five-items\" src=\"" + value.thumbImage + "\"/>\n" +
                "                </div>";

        });

        $("#right-container").append(appendTop5);
    }

    function ajaxGetRelatedTradePost(modelName) {
        $.ajax({
            type: "GET",
            url: "/api/model/getRelatedTradePost?modelName=" + modelName,
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
                "                    <a href=\"#\">" + value.model + "</a>\n" +
                "                </div>";

        });

        $("#right-container").append(appendRelatedTrade);
    }

    function ajaxGetRelatedArticle(modelName) {
        $.ajax({
            type: "GET",
            url: "/api/model/getTop5Article?modelName=" + modelName,
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
                "                    <a href=\"#\">" + value.title + "</a>\n" +
                "                </div>";

        });

        $("#right-container").append(appendRelatedArticle);
    }
})