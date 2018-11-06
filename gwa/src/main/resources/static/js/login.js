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

    authentication();

    function authentication() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {
                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);
                    var role = jsonResponse["role"].name;

                    if (role == "MEMBER") {
                        window.location.href = "/gwa/model/";
                    } else if (role == "ADMIN") {
                        window.location.href = "/gwa/admin/model/pending";
                    }
                } else {
                    console.log("Guest is accessing !");
                }

            }
        });
    }

    $("#loginBtn").click(function (event) {
        event.preventDefault();

        var check = true;

        if (!$("#username").val().trim()) {
            $("#errorusername").css("visibility", "visible");
            $("#errorusername").text("Please input empty field");
            check = false;
        } else {
            $("#errorusername").css("visibility", "hidden");
        }

        if (!$("#password").val().trim()) {
            $("#errorpassword").css("visibility", "visible");
            $("#errorpassword").text("Please input empty field");
            check = false;
        } else {
            $("#errorpassword").css("visibility", "hidden");
        }

        if (check) {
            var formLogin = {
                username: $("#username").val(),
                password: $("#password").val()
            }

            ajaxPost(formLogin);
        }

    })

    function ajaxPost(data) {
        console.log(data);

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/gwa/api/user/login",
            data: JSON.stringify(data),
            success: function (result, status) {

                // get goBack parameter value from previous page which required to login
                var goBack = getUrlParameter("goBack");
                if (goBack) {
                    history.back(goBack);
                } else {
                    if (result.role.name == "MEMBER") {
                        window.location.href = "/gwa/model/"
                    } else {
                        if (result.role.name == "ADMIN") {
                            window.location.href = "/gwa/admin/model/pending";
                        }
                    }
                }
            },
            complete: function (xhr, textStatus) {
                if (textStatus == "error") {

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);

                    $("#errorpassword").css("visibility", "visible");
                    $("#errorpassword").text(jsonResponse["message"]);
                }
            }
        });
    }
})