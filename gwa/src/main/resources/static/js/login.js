$(document).ready(function () {

    authentication();

    function authentication() {

        $.ajax({
            type: "GET",
            url: "http://localhost:8080/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {
                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);
                    var role = jsonResponse["role"].name;

                    if (role == "MEMBER") {
                        window.location.href = "/model/"
                    } else if (role == "ADMIN") {
                        window.location.href = "/admin/model/create";
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
            url: "/api/user/login",
            data: JSON.stringify(data),
            success: function (result, status) {
                if (result.role.name == "MEMBER") {
                    window.location.href = "/model/"
                } else {
                    if (result.role.name == "ADMIN") {
                        window.location.href = "/model/create"
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