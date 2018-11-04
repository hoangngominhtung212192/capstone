$(document).ready(function () {

    // process UI
    $(document).click(function (event) {
        $("#dropdown-profile").css("display", "none");
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
            url: "/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {
                    // username click
                    usernameClick();

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);

                    role_session = jsonResponse["role"].name;
                    account_session_id = jsonResponse["id"];

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
                    window.location.href = "/login";
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

                var displayUsername;

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

    // get current selected profile
    function getProfile() {

        $.ajax({
            type: "POST",
            url: "/api/user/profile?accountID=" + account_profile_on_page_id,
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

                // authorization
                authorization();

                console.log(result);
                setField(result);
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
            window.location.href = "/pages/profile.html?accountID=" + accountID;
        })
    }

    // authorization for process UI
    function authorization() {

        if (account_profile_on_page_id == account_session_id) {
            $("#editBtn").css("display", "block");
        } else {
            if (role_session != "ADMIN") {
                // render non-own profile area

            }
        }
        if (role_session == "ADMIN") {
            $("#editBtn").css("display", "block");

            if (current_profile_status == "Banned") {
                $("#unbanBtn").css("display", "block");
            } else {
                $("#banBtn").css("display", "block");
            }
        }
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
                avatar: avatar
            }

            var formData = new FormData();
            if (checkImage) {
                var type = imagetype.split("/")[1];
                // $("#photoBtn").get(0).files[0]
                if (imageFile) {
                    formData.append("photoBtn", imageFile, username + "." + type);
                }
                ajaxPost(formProfile, formData);
            } else {
                ajaxPost(formProfile, null);
            }
        }
    });

    function ajaxPost(data, images) {

        $.ajax({
            type: "POST",
            contentType: "application/json",
            processData: false,
            url: "/api/user/profile/update",
            data: JSON.stringify(data),
            success: function (result) {

                if (images != null) {
                    images.append("id", result.account.id);
                    ajaxImagePost(images);
                }

                alert("Updated profile successfully !");
                window.location.href = "/pages/profile.html?accountID=" + result.account.id;
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
            url: "/api/user/profile/update/image",
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
            }
            $("#loading").css("display", "none");
        }, 500);

        $("#usernameTitle").text(result.account.username);

        $("#saveBtn").css("display", "none");
        $("#resetBtn").css("display", "none");

        $("#photoBtn").css("display", "none");
        $("#photoTitle").css("display", "none");
        $("#upload").css("display", "none");
    }

    // birthday datepicker
    $(function () {
        $("#datepicker").datepicker({
            autoclose: true,
            todayHighlight: true
        }).datepicker('update', new Date());
    });
})