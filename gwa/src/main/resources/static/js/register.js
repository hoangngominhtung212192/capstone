$(document).ready(function () {
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
                        window.location.href = "/gwa/model/"
                    } else if (role == "ADMIN") {
                        window.location.href = "/gwa/admin/model/create";
                    } else if (role == "BUYERSELLER") {
                        window.location.href = "/gwa/trade-market/my-trade";
                    }
                } else {
                    console.log("Guest is accessing !");
                }

            }
        });
    }

    $("#registerBtn").click(function (event) {
        event.preventDefault();

        var check = true;

        var username = $("#username").val().trim();

        if (!username) {
            $("#errorusername").css("visibility", "visible");
            $("#errorusername").text("Please input empty field");
            check = false;
        } else {
            if (!username.match("[A-Za-z]+")) {
                $("#errorusername").css("visibility", "visible");
                $("#errorusername").text("Please input valid string");
                check = false;
            } else if (username.length > 50 || username.length < 3) {
                $("#errorusername").css("visibility", "visible");
                $("#errorusername").text("Out of range, maximum: 50 characters, minimum: 3 characters");
                check = false;
            } else {
                $("#errorusername").css("visibility", "hidden");
            }
        }

        var firstname = $("#firstName").val().trim();

        if (!firstname) {
            $("#errorfirstname").css("visibility", "visible");
            $("#errorfirstname").text("Please input empty field");
            check = false;
        } else {
            if (!firstname.match("[A-Za-z\\s]+")) {
                $("#errorfirstname").css("visibility", "visible");
                $("#errorfirstname").text("Please input valid string");
                check = false;
            } else if (firstname.length > 50 || firstname.length < 2) {
                $("#errorfirstname").css("visibility", "visible");
                $("#errorfirstname").text("Out of range, maximum: 50 characters, minimum: 2 characters");
                check = false;
            } else {
                $("#errorfirstname").css("visibility", "hidden");
            }
        }

        var lastname = $("#lastName").val().trim();

        if (!lastname) {
            $("#errorlastname").css("visibility", "visible");
            $("#errorlastname").text("Please input empty field");
            check = false;
        } else {
            if (!lastname.match("[A-Za-z\\s]+")) {
                $("#errorlastname").css("visibility", "visible");
                $("#errorlastname").text("Please input valid string");
                check = false;
            } else if (lastname.length > 50 || lastname.length < 2) {
                $("#errorlastname").css("visibility", "visible");
                $("#errorlastname").text("Out of range, maximum: 50 characters, minimum: 2 characters");
                check = false;
            } else {
                $("#errorlastname").css("visibility", "hidden");
            }
        }

        var email = $("#email").val().trim();

        if (!email) {
            $("#erroremail").css("visibility", "visible");
            $("#erroremail").text("Please input empty field");
            check = false;
        } else {
            if (!email.match("^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) {
                $("#erroremail").css("visibility", "visible");
                $("#erroremail").text("Please input valid email");
                check = false;
            } else if (email.length > 50) {
                $("#erroremail").css("visibility", "visible");
                $("#erroremail").text("Out of range, maximum: 50 characters");
                check = false;
            } else {
                $("#erroremail").css("visibility", "hidden");
            }
        }

        var password = $("#password").val().trim();

        if (!password) {
            $("#errorpassword").css("visibility", "visible");
            $("#errorpassword").text("Please input empty field");
            check = false;
        } else {
            if (password.length > 50 || password.length < 6) {
                $("#errorpassword").css("visibility", "visible");
                $("#errorpassword").text("Out of range, maximum: 50 characters, minimum: 6 characters");
                check = false;
            } else {
                $("#errorpassword").css("visibility", "hidden");
            }
        }

        var confirmPassword = $("#confirmPassword").val().trim();

        if (!confirmPassword) {
            $("#errorconfirmpassword").css("visibility", "visible");
            $("#errorconfirmpassword").text("Please input empty field");
            check = false;
        } else {
            if (!(confirmPassword == password)) {
                $("#errorconfirmpassword").css("visibility", "visible");
                $("#errorconfirmpassword").text("Confirm password does not match password");
                check = false;
            } else {
                $("#errorconfirmpassword").css("visibility", "hidden");
            }
        }

        if (check) {
            var formRegister = {
                username : username,
                password : password,
                firstname : firstname,
                lastname : lastname,
                email : email
            }

            ajaxPost(formRegister);
        }
    })

    function ajaxPost(data) {
        console.log(data);
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "/gwa/api/user/register",
            data : JSON.stringify(data),
            success : function(result, status) {
                console.log("New profile is created successfully with identifier: " + result.id);

                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function() {
                    window.location.href = "/gwa/login";
                });
            },
            complete : function(xhr, textStatus) {
                if (textStatus == "error") {
                    $("#error").css("visibility", "visible");

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);
                    $("#error").text(jsonResponse["message"]);
                }
            }
        });
    }

})