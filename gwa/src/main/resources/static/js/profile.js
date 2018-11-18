$(document).ready(function () {

    // process UI
    $(document).click(function (event) {
        $("#dropdown-profile").css("display", "none");
        $("#dropdown-notification").css("display", "none");
    })

    var account_profile_on_page_id;
    var account_session_id;
    var role_session;
    var current_role;
    var current_profile_status;
    var profile_id;
    var username;
    var checkImage = false;
    var imagetype;
    var avatar;
    var preEmail;
    var preFirstName;
    var preMiddleName;
    var preLastName;
    var preMobile;
    var preBirthday;
    var preAddress;
    var joinDate;
    var accountStatus;
    var numberOfRaters;
    var numberOfStars;

    // authentication
    authentication();

    // split url to get parameter algorithm
    var getUrlParameter = function getUrlParameter(sParam) {
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

                    role_session = jsonResponse["role"].name;
                    account_session_id = jsonResponse["id"];
                    ajaxGetAllNotification(jsonResponse["id"]);
                    joinDate = jsonResponse["createdDate"];
                    accountStatus = jsonResponse["status"];
                    $("#membersince").text("Member since " + jsonResponse["createdDate"].split(" ")[0]);

                    var username = jsonResponse["username"];
                    var thumbAvatar = jsonResponse["avatar"];
                    console.log(role_session + " " + username + " is on session!");

                    // click profile button
                    profileClick(account_session_id);

                    // display fullname
                    getSessionProfile(account_session_id);


                    // display username, profile and logout button
                    if (thumbAvatar) {
                        $("#thumbAvatar-new").attr("src", thumbAvatar);
                        $("#thumbAvatar-dropdown").attr("src", thumbAvatar);
                    }

                    if (jsonResponse["role"].name == "ADMIN") {
                        $("#adminBtn").css("display", "block");
                    }

                    // get current profile
                    account_profile_on_page_id = getUrlParameter('accountID');
                    getProfile();
                } else {
                    // display login and register button
                    console.log("Guest is accessing !");
                    window.location.href = "/gwa/login?goBack=1";
                }

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

    function ajaxGetStatistic() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/user/getStatistic?accountID=" + account_profile_on_page_id,
            success: function (result) {
                // profile statistic
                $("#sell-statistic").text(result[0]);
                $("#buy-statistic").text(result[1]);
                $("#proposal-statistic").text(result[2]);

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

    // get current selected profile
    function getProfile() {

        $.ajax({
            type: "POST",
            url: "/gwa/api/user/profile?accountID=" + account_profile_on_page_id,
            success: function (result) {
                //get selected profile's account status
                current_profile_status = result.account.status;

                current_role = result.account.role.name;
                profile_id = result.id;
                username = result.account.username;
                avatar = result.avatar;
                preAddress = result.address;
                preBirthday = result.birthday;
                preEmail = result.email;
                preFirstName = result.firstName;
                preMiddleName = result.middleName;
                preLastName = result.lastName;
                preMobile = result.tel;
                numberOfRaters = result.numberOfRaters;
                numberOfStars = result.numberOfStars;

                // authorization
                authorization();

                console.log(result);
                setField(result);
                renderStatistic(result.numberOfRaters, result.numberOfStars);
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

    // username click event
    function usernameClick() {
        $("#username-li").click(function (event) {
            // separate from document click
            event.stopPropagation();

            $("#dropdown-profile").css("display", "block");

            return false;
        })
    }

    // profile button click event
    function profileClick(accountID) {
        $("#profile-new").click(function (event) {
            window.location.href = "/gwa/pages/profile.html?accountID=" + accountID;
        })
    }

    // authorization for process UI
    function authorization() {

        if (account_profile_on_page_id == account_session_id) {
            $("#editBtn").css("display", "block");
        } else {
            if (role_session != "ADMIN") {
                // render non-own profile area
                renderNonOwnProfile();
                // get top ranking users
                getTopRanking();
            }
        }
        if (role_session == "ADMIN") {
            $("#editBtn").css("display", "block");

            if (account_profile_on_page_id != account_session_id) {
                if (current_profile_status == "Banned") {
                    $("#unbanBtn").css("display", "block");
                } else {
                    $("#banBtn").css("display", "block");
                }
            }
        }

        // if there is at least one rating about current profile
        if (numberOfRaters != 0) {
            $("#review-div").css("display", "block");

            $("#icon-review").click(function (e) {
                $("#reviewList").empty();
                pageNumber = 1;

                ajaxGetAllRating();
            });

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
            })
        }
    }

    function renderStatistic(numberOfRaters, numberOfStars) {
        $("#reputation").empty();
        $("#numberOfRaters").empty();

        if (numberOfRaters == 0) {
            $("#reputation").text("N/A");
            $("#reputation").css("color", "#212529");
            $("#reputation").css("font-size", "15px");
        } else {
            $("#numberOfRaters").text("(" + numberOfRaters + ")");
            var rating = Math.round(numberOfStars / numberOfRaters);

            for (var i = 0; i < rating; i++) {
                $("#reputation").append("&#9733;");
            }
        }

        // get statistic (buy & sell & proposal count)
        ajaxGetStatistic();
    }

    function renderNonOwnProfile() {
        $("#username-area").remove();
        $("#authorization-area").remove();
        $("#ownprofile-area").remove();

        var appendNonOwn = "";

        appendNonOwn += "<div class=\"col-sm-6\" style=\"background-color: white; padding-top: 20px;\n" +
            "                       margin-left: 20px; padding-bottom: 20px; border: 1px solid #f0f0f0; box-shadow: 0px 0px 5px #aaa;\n" +
            "                            height: 400px;margin-top: 50px;\">\n" +
            "                <div class=\"container\">\n" +
            "                    <form class=\"form\" id=\"profileForm\">\n" +
            "                        <div class=\"row\">\n" +
            "\n" +
            "                            <div class=\"col-sm-12\">\n" +
            "                                <div class=\"form-group\">\n" +
            "                                    <div class=\"col-xs-6\">\n" +
            "                                        <h4 class=\"about-title\">About</h4>\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "                                <br/>\n" +
            "                            </div>\n" +
            "\n" +
            "                            <div class=\"col-sm-6\">\n" +
            "                                <div class=\"form-group\">\n" +
            "                                    <div class=\"col-xs-6\">\n" +
            "                                        <h5>Full name</h5>\n" +
            "                                        <span style=\"color: indigo; font-size: 15px;\">";

        if (preMiddleName) {
            appendNonOwn += preLastName + " " + preMiddleName + " " + preFirstName;
        } else {
            appendNonOwn += preLastName + " " + preFirstName;
        }

        appendNonOwn += "</span>\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "\n" +
            "                                <div class=\"form-group\">\n" +
            "                                    <div class=\"col-xs-6\">\n" +
            "                                        <h5>Email</h5>\n" +
            "                                        <span style=\"color: indigo; font-size: 15px;\">";

        appendNonOwn += "N/A";

        appendNonOwn += "</span>\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "\n" +
            "                                <div class=\"form-group\">\n" +
            "                                    <div class=\"col-xs-6\">\n" +
            "                                        <h5>Birthday</h5>\n" +
            "                                        <span style=\"color: indigo; font-size: 15px;\">";

        if (preBirthday) {
            appendNonOwn += preBirthday;
        } else {
            appendNonOwn += "N/A";
        }

        appendNonOwn += "</span>\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "\n" +
            "                                <div id=\"review-div\" class=\"form-group\" style=\"margin-top: 35px; display: none;\">\n" +
            "                                    <div class=\"col-xs-6\">\n" +
            "                                        <img id=\"icon-review\" style=\"margin-left: 0px; float: left; margin-right: 8px;\"\n" +
            "                                             src=\"/gwa/img/icon-review.png\" data-toggle=\"modal\"\n" +
            "                                             data-target=\"#reviewModel\">\n" +
            "                                        <span style=\"text-decoration: underline; float: left;\">Exchange evaluation</span>\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                            <div class=\"col-sm-6\">\n" +
            "                                <div class=\"form-group\">\n" +
            "                                    <div class=\"col-xs-6\">\n" +
            "                                        <h5>Address</h5>\n" +
            "                                        <span style=\"color: indigo; font-size: 15px;\">";

        if (preAddress) {
            appendNonOwn += preAddress;
        } else {
            appendNonOwn += "N/A";
        }

        appendNonOwn += "</span>\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "\n" +
            "                                <div class=\"form-group\">\n" +
            "                                    <div class=\"col-xs-6\">\n" +
            "                                        <h5>Tel</h5>\n" +
            "                                        <span style=\"color: indigo; font-size: 15px;\">";


        appendNonOwn += "N/A";

        appendNonOwn += "</span>\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "\n" +
            "                                <div class=\"form-group\">\n" +
            "                                    <div class=\"col-xs-6\">\n" +
            "                                        <h5>Join Date</h5>\n" +
            "<span style=\"color: indigo; font-size: 15px;\">" + joinDate + "</span>\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "\n" +
            "                                <div class=\"form-group\">\n" +
            "                                    <div class=\"col-xs-6\">\n" +
            "                                        <h5>Status</h5>\n" +
            "                                        <span style=\"color: green; font-size: 15px;\">" + accountStatus + "</span>\n" +
            "                                    </div>\n" +
            "                                </div>\n" +
            "                            </div>\n" +
            "                        </div>\n" +
            "                    </form>\n" +
            "                    <!--    end form    -->\n" +
            "                </div>\n" +
            "            </div>";

        $("#row-area").append(appendNonOwn);
    }

    $("#editBtn").click(function (event) {
        $("#firstName").removeAttr('disabled');
        $("#firstName").css("width", "100%");

        $("#email").removeAttr('disabled');
        $("#email").css("width", "100%");

        $("#middleName").removeAttr('disabled');
        $("#middleName").css("width", "100%");

        $("#address").removeAttr('disabled');
        $("#address").css("width", "100%");

        $("#lastName").removeAttr('disabled');
        $("#lastName").css("width", "100%");

        $("#birthday").removeAttr('disabled');
        $("#birthday").css("width", "100%");

        $("#mobile").removeAttr('disabled');
        $("#mobile").css("width", "100%");

        $("#saveBtn").css("display", "block");
        $("#resetBtn").css("display", "block");

        $("#photoTitle").css("display", "block");
        $("#upload").css("display", "block");
    });

    $("#resetBtn").click(function (event) {
        event.preventDefault();

        $("#error").css("display", "none");

        $("#firstName").val(preFirstName);
        if (preMiddleName == null) {
            $("#middleName").val("");
        } else {
            $("#middleName").val(preMiddleName);
        }
        $("#lastName").val(preLastName);
        $("#email").val(preEmail);
        if (preAddress == null) {
            $("#address").val("");
        } else {
            $("#address").val(preAddress);
        }
        if (preBirthday == null) {
            $("#birthday").val("");
        } else {
            $("#birthday").val(preBirthday);
        }
        if (preMobile == null) {
            $("#mobile").val("");
        } else {
            $("#mobile").val(preMobile);
        }
        if (avatar != null) {
            $("#avatar").attr("src", avatar);
        }

        $("#username").text(username);

        $("#saveBtn").css("display", "none");
        $("#resetBtn").css("display", "none");

        $("#photoBtn").css("display", "none");
        $("#photoTitle").css("display", "none");
        $("#upload").css("display", "none");

        $("#firstName").attr('disabled', true);
        $("#firstName").css("width", "280px");

        $("#email").attr('disabled', true);

        $("#middleName").attr('disabled', true);
        $("#middleName").css("width", "280px");

        $("#address").attr('disabled', true);

        $("#lastName").attr('disabled', true);
        $("#lastName").css("width", "280px");

        $("#birthday").attr('disabled', true);

        $("#mobile").attr('disabled', true);
        $("#mobile").css("width", "250px");
    })

    var imageFile;

    $("#photoBtn").change(function (e) {
        console.log($("#photoBtn").val());

        for (var i = 0; i < e.originalEvent.srcElement.files.length; i++) {

            var file = e.originalEvent.srcElement.files[i];
            imagetype = file.type;
            var match = ["image/jpeg", "image/png", "image/jpg"];
            if (!((imagetype == match[0]) || (imagetype == match[1]) || (imagetype == match[2]))) {
                checkImage = false;
                $("#imgError").css("display", "block");
                $("#imgError").text("Please select image only");
            } else {
                var reader = new FileReader();
                reader.onloadend = function () {
                    $("#avatar").attr("src", reader.result);
                    $("#avatar").css("height", "202px");
                    $("#avatar").css("width", "202px");
                }
                reader.readAsDataURL(file);

                imageFile = file;

                checkImage = true;
                $("#imgError").css("display", "none");
                $("#imgError").text("");
            }

        }
    });

    function checkValidForm(firstname, middlename, lastname, email, birthday, mobile) {
        var check = true;

        if (!firstname) {
            $("#errorfirstname").css("display", "block");
            $("#errorfirstname").text("Please input your first name");
            check = false;
        } else {
            if (!firstname.match("[A-Za-z\\s]+")) {
                $("#errorfirstname").css("display", "block");
                $("#errorfirstname").text("Characters and space only");
                check = false;
            } else if (firstname.length > 50 || firstname.length < 2) {
                $("#errorfirstname").css("display", "block");
                $("#errorfirstname").text("Out of range, minimum: 2 letters, maximum: 50 letters");
                check = false;
            } else {
                $("#errorfirstname").css("display", "none");
            }
        }

        if (middlename) {
            if (!middlename.match("[A-Za-z\\s]+")) {
                $("#errormiddlename").css("display", "block");
                $("#errormiddlename").text("Characters and space only");
                check = false;
            } else if (middlename.length > 50 || middlename.length < 2) {
                $("#errormiddlename").css("display", "block");
                $("#errormiddlename").text("Out of range, minimum: 2 letters, maximum: 50 letters");
                check = false;
            } else {
                $("#errormiddlename").css("display", "none");
            }
        }

        if (!lastname) {
            $("#errorlastname").css("display", "block");
            $("#errorlastname").text("Please input your last name");
            check = false;
        } else {
            if (!lastname.match("[A-Za-z\\s]+")) {
                $("#errorlastname").css("display", "block");
                $("#errorlastname").text("Characters and space only");
                check = false;
            } else if (lastname.length > 50 || lastname.length < 2) {
                $("#errorlastname").css("display", "block");
                $("#errorlastname").text("Out of range, minimum: 2 letters, maximum: 50 letters");
                check = false;
            } else {
                $("#errorlastname").css("display", "none");
            }
        }

        if (!email) {
            $("#erroremail").css("display", "block");
            $("#erroremail").text("Please input your email");
            check = false;
        } else {
            if (!email.match("^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) {
                $("#erroremail").css("display", "block");
                $("#erroremail").text("Please input valid email");
                check = false;
            } else if (email.length > 50) {
                $("#erroremail").css("display", "block");
                $("#erroremail").text("Out of range, maximum: 50 characters");
                check = false;
            } else {
                $("#erroremail").css("display", "none");
            }
        }

        if (birthday) {
            if (!birthday.match("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$")) {
                $("#errorbirthday").css("display", "block");
                $("#errorbirthday").text("Please input valid date");
                check = false;
            } else {
                $("#errorbirthday").css("display", "none");
            }
        }

        if (mobile) {
            if (!mobile.match("[0-9]+$")) {
                $("#errormobile").css("display", "block");
                $("#errormobile").text("Please input valid mobile number");
                check = false;
            } else {
                if (mobile.length != 10) {
                    $("#errormobile").css("display", "block");
                    $("#errormobile").text("Mobile number is a string of 10 digits");
                    check = false;
                }
                else {
                    $("#errormobile").css("display", "none");
                }
            }
        }

        return check;
    }

    $("#profileForm").submit(function (event) {
        event.preventDefault();

        var firstname = $("#firstName").val().trim();
        var middlename = $("#middleName").val().trim();
        var lastname = $("#lastName").val().trim();
        var email = $("#email").val().trim();
        var address = $("#address").val().trim();
        var birthday = $("#birthday").val().trim();
        var mobile = $("#mobile").val().trim();

        if (checkValidForm(firstname, middlename, lastname, email, birthday, mobile)) {

            $("#mi-modal").modal({backdrop: 'static', keyboard: false});
            $("#modal-btn-si").on("click", function () {
                $("#mi-modal").modal('hide');
                $("#modal-btn-no").prop("onclick", null).off("click");
                $("#modal-btn-si").prop("onclick", null).off("click");
            });

            $("#modal-btn-no").on("click", function (e) {

                var formProfile = {
                    id: profile_id,
                    firstName: firstname,
                    middleName: middlename,
                    lastName: lastname,
                    email: email,
                    birthday: birthday,
                    tel: mobile,
                    accountID: account_profile_on_page_id,
                    address: address,
                    avatar: avatar,
                    numberOfRaters: numberOfRaters,
                    numberOfStars: numberOfStars
                }

                var formData = new FormData();
                if (checkImage) {
                    var type = imagetype.split("/")[1];
                    // $("#photoBtn").get(0).files[0]
                    if (imageFile) {
                        formData.append("photoBtn", imageFile, username + "." + type);
                    }

                    // admin role, input reason of modification user's profile
                    // if admin's profile, ignore this
                    if (role_session == "ADMIN" && account_session_id != account_profile_on_page_id) {
                        $("#myModal").modal({backdrop: 'static', keyboard: false});
                        $("#close-reason").on("click", function () {
                            $("#myModal").modal('hide');
                            $("#close-reason").prop("onclick", null).off("click");
                            $("#submit-reason").prop("onclick", null).off("click");
                        });

                        $("#submit-reason").on("click", function (e) {
                            // valid reason
                            e.preventDefault();

                            if (checkValidReason($("#txtReason").val())) {
                                addNewNotification();
                                ajaxPost(formProfile, formData);

                                $("#myModal").modal('hide');
                                $("#close-reason").prop("onclick", null).off("click");
                                $("#submit-reason").prop("onclick", null).off("click");
                            }

                            return false;
                        });
                    } else {
                        // member role
                        ajaxPost(formProfile, formData);
                    }
                } else { // don't update image
                    // admin role, input reason of modification user's profile
                    // if admin's profile, ignore this
                    if (role_session == "ADMIN" && account_session_id != account_profile_on_page_id) {
                        $("#myModal").modal({backdrop: 'static', keyboard: false});
                        $("#close-reason").on("click", function () {
                            $("#myModal").modal('hide');
                            $("#close-reason").prop("onclick", null).off("click");
                            $("#submit-reason").prop("onclick", null).off("click");
                        });

                        $("#submit-reason").on("click", function () {
                            // valid reason
                            e.preventDefault();

                            if (checkValidReason($("#txtReason").val())) {
                                addNewNotification();
                                ajaxPost(formProfile, null);

                                $("#myModal").modal('hide');
                                $("#close-reason").prop("onclick", null).off("click");
                                $("#submit-reason").prop("onclick", null).off("click");
                            }

                            return false;
                        });
                    } else {
                        ajaxPost(formProfile, null);
                    }
                }

                $("#mi-modal").modal('hide');
                $("#modal-btn-no").prop("onclick", null).off("click");
                $("#modal-btn-si").prop("onclick", null).off("click");
            });
        }
    });

    function ajaxPost(data, images) {

        $.ajax({
            type: "POST",
            contentType: "application/json",
            processData: false,
            url: "/gwa/api/user/profile/update",
            data: JSON.stringify(data),
            success: function (result) {

                if (images != null) {
                    images.append("id", result.account.id);
                    ajaxImagePost(images);
                }

                $("#success-modal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    window.location.href = "/gwa/pages/profile.html?accountID=" + result.account.id;
                });
            },
            complete: function (xhr, textStatus) {
                if (textStatus == "error") {
                    $("#error").css("display", "block");

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);
                    $("#error").text(jsonResponse["message"]);
                }
            }
        });
    }

    function ajaxImagePost(formData) {
        $.ajax({
            type: "POST",
            contentType: false,
            processData: false,
            url: "/gwa/api/user/profile/update/image",
            data: formData,
            success: function (result) {
                console.log(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        })
    }

    function setField(result) {
        $("#error").css("display", "none");

        $("#firstName").val(result.firstName);
        if (result.middleName == null) {
            $("#middleName").val("");
        } else {
            $("#middleName").val(result.middleName);
        }
        $("#lastName").val(result.lastName);
        $("#email").val(result.email);
        if (result.address == null) {
            $("#address").val("");
        } else {
            $("#address").val(result.address);
        }
        if (result.birthday == null) {
            $("#birthday").val("");
        } else {
            $("#birthday").val(result.birthday);
        }
        if (result.tel == null) {
            $("#mobile").val("");
        } else {
            $("#mobile").val(result.tel);
        }

        $("#loading").css("display", "block");
        setTimeout(function () {
            if (result.avatar != null) {
                $("#avatar").attr("src", result.avatar);
                $("#hrefAvatar").attr("href", result.avatar);
            }
            $("#loading").css("display", "none");
        }, 500);

        $("#usernameTitle").text(result.account.username);
        $("#status").val(result.account.status);

        $("#saveBtn").css("display", "none");
        $("#resetBtn").css("display", "none");

        $("#photoBtn").css("display", "none");
        $("#photoTitle").css("display", "none");
        $("#upload").css("display", "none");
    }

    function checkValidReason(value) {
        var check = true;

        $("#errorReason").css("display", "none");

        if (!value) {
            check = false;
            $("#errorReason").css("display", "block");
            $("#errorReason").text("Please input reason")
        } else {
            if (value.length > 100) {
                check = false;
                $("#errorReason").css("display", "block");
                $("#errorReason").text("Maximum length: 100")
            }
        }

        return check;
    }

    // birthday datepicker
    $(function () {
        $("#datepicker").datepicker({
            autoclose: true,
            todayHighlight: true
        }).datepicker('update', new Date());
    });

    function getTopRanking() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/user/getTopRanking",
            success: function (result) {
                console.log("List of top ranking records: " + result);

                if (result.length > 0) {
                    renderTopRanking(result);
                }
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function renderTopRanking(result) {

        var appendTopRanking = "";

        appendTopRanking += "<div class=\"col-sm-2\" style=\"background-color: white; padding-top: 20px; margin-left: 20px;\n" +
            "            padding-bottom: 20px; border: 1px solid #f0f0f0; box-shadow: 0px 0px 5px #aaa;" +
            "padding-right: 0px;\">\n" +
            "            <div class=\"top-five-title-div\" style=\"margin-bottom: 20px;\">\n" +
            "            <strong class=\"top-five-title\">TOP Ranking</strong>\n" +
            "            </div>\n" +
            "<div class=\"ranking-area\" style=\"float: left;width: 100%;overflow-y: auto;height: 400px;\">";

        $.each(result, function (i, value) {
            if (value.rank != 0) {
                if (value.rank == 1) {
                    appendTopRanking += "            <div class=\"ranking-container\">\n" +
                        "            <img src=\"/gwa/img/1st.png\" class=\"rankNumber\"/>\n" +
                        "            <a href='/gwa/pages/profile.html?accountID=" + value.account.id + "' class=\"ranking-username\">";
                } else if (value.rank == 2) {
                    appendTopRanking += "            <div class=\"ranking-container\">\n" +
                        "            <img src=\"/gwa/img/2nd.jpg\" class=\"rankNumber\"/>\n" +
                        "            <a href='/gwa/pages/profile.html?accountID=" + value.account.id + "' class=\"ranking-username\">";
                } else if (value.rank == 3) {
                    appendTopRanking += "            <div class=\"ranking-container\">\n" +
                        "            <img src=\"/gwa/img/3rd.jpg\" class=\"rankNumber\"/>\n" +
                        "            <a href='/gwa/pages/profile.html?accountID=" + value.account.id + "' class=\"ranking-username\">";
                } else {
                    appendTopRanking += "            <div class=\"ranking-container\">\n" +
                        "            <span class=\"normal-rankNumber\">" + value.rank + "th</span>\n" +
                        "            <a href='/gwa/pages/profile.html?accountID=" + value.account.id + "' class=\"ranking-username\">";
                }

                if (value.middleName) {
                    appendTopRanking += value.lastName + " " + value.middleName + " " + value.firstName;
                } else {
                    appendTopRanking += value.lastName + " " + value.firstName;
                }

                appendTopRanking += "</a></div>";
            }
        });

        // close col-sm-2 div
        appendTopRanking += "</div></div>";
        $("#row-area").append(appendTopRanking);
    }

    /*  Get all user's feedback and rating area */
    var pageNumber = 1;
    var lastPage;

    function ajaxGetAllRating() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/user/rating/getAll?pageNumber=" + pageNumber + "&accountID=" + account_profile_on_page_id,
            success: function (result) {
                lastPage = result[0];
                renderUserRating(result[1]);

                console.log(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function renderUserRating(result) {

        $.each(result, function (i, value) {
            var appendReview = "";

            appendReview += "<tr>\n" +
                "<td style=\"vertical-align: top; padding-top: 10px;\">\n" +
                "<img src=\"" + value.fromUser.avatar + "\" onerror=\"this.src='/gwa/img/avatar_2x.png'\"/>\n" +
                " </td>\n" +
                "<td class=\"review-info\">\n" +
                "<a href=\"/gwa/pages/profile.html?accountID=" + value.fromUser.id + "\">" + value.fromUser.username +
                "</a>\n";

            appendReview += "<span class=\"reputation-star-rating\">";

            for (var j = 0; j < value.rating; j++) {
                appendReview += "&#9733;";
            }

            appendReview += "</span>\n" +
                "                                <span class=\"ratingDate\">(" + value.ratingDate + ")</span>\n" +
                "                                <br/>\n" +
                "                                <div class=\"review-info-text\">\n" + value.comment +
                "                                </div>\n" +
                "                            </td>\n" +
                "                        </tr>";

            $("#reviewList").append(appendReview);
        });
    }

    /*  End feedback and rating  */

    /*   This is for notification area   */
    var pageNumber = 1;
    var lastPage;

    function ajaxGetAllNotification(accountID) {

        $.ajax({
            type: "GET",
            url: "/gwa/api/notification/getAll?pageNumber=" + pageNumber + "&accountID=" + accountID,
            success: function (result) {
                console.log(result);

                lastPage = result[0];
                renderNotification(result[1]);
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

    var countNotSeen = 0;

    function renderNotification(result) {

        $.each(result, function (index, value) {

            var appendNotification = "";

            if (!value.seen) {
                // not seen yet
                countNotSeen++;
                appendNotification += "<li>\n"
            } else {
                // already seen
                appendNotification += "<li style='background-color: white;'>\n"
            }

            var iconType = "<i class=\"fa fa-warning text-yellow\" style=\"color: darkred;\"></i> ";

            if (value.notificationtype.name == "Profile"){
                iconType = "<i class=\"fa fa-user-circle-o text-yellow\" style=\"color: darkred;\"></i> ";
            }else if (value.notificationtype.name == "Model") {
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

    // ban account
    $("#banBtn").click(function (e) {
        e.preventDefault();

        $("#mi-modal").modal({backdrop: 'static', keyboard: false});
        $("#modal-btn-si").on("click", function () {
            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });

        $("#modal-btn-no").on("click", function (e) {
            ajaxBanAccount();

            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });
    })

    function ajaxBanAccount() {

        $.ajax({
            type: "POST",
            url: "/gwa/api/user/ban?accountID=" + account_profile_on_page_id,
            success: function () {
                $("#success-modal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    window.location.href = "/gwa/pages/profile.html?accountID=" + account_profile_on_page_id;
                });
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    // unban account
    $("#unbanBtn").click(function (e) {
        e.preventDefault();

        $("#mi-modal").modal({backdrop: 'static', keyboard: false});
        $("#modal-btn-si").on("click", function () {
            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });

        $("#modal-btn-no").on("click", function (e) {
            ajaxUnbanAccount();

            $("#mi-modal").modal('hide');
            $("#modal-btn-no").prop("onclick", null).off("click");
            $("#modal-btn-si").prop("onclick", null).off("click");
        });
    })

    function ajaxUnbanAccount() {

        $.ajax({
            type: "POST",
            url: "/gwa/api/user/unban?accountID=" + account_profile_on_page_id,
            success: function () {
                $("#success-modal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function () {
                    window.location.href = "/gwa/pages/profile.html?accountID=" + account_profile_on_page_id;
                });
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }
})