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
            } else if (username.length > 50) {
                $("#errorusername").css("visibility", "visible");
                $("#errorusername").text("Out of range, maximum: 50 characters");
                check = false;
            } else {
                $("#errorusername").css("visibility", "hidden");
            }
        }

        var firstname = $("#firstName").val().trim();

        if (!firstName) {
            $("#errorfirstname").css("visibility", "visible");
            $("#errorfirstname").text("Please input empty field");
            check = false;
        } else {
            if (!firstName.match("[A-Za-z\\s]+")) {
                $("#errorfirstname").css("visibility", "visible");
                $("#errorfirstname").text("Please input valid string");
                check = false;
            } else if (firstName.length > 50) {
                $("#errorfirstname").css("visibility", "visible");
                $("#errorfirstname").text("Out of range, maximum: 50 characters");
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
            } else if (lastname.length > 50) {
                $("#errorlastname").css("visibility", "visible");
                $("#errorlastname").text("Out of range, maximum: 50 characters");
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
            if (password.length > 50) {
                $("#errorpassword").css("visibility", "visible");
                $("#errorpassword").text("Out of range, maximum: 50 characters");
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
                $("#errorconfirmpassword").text("Confirm pass word does not match password");
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
            url : "/api/user/register",
            data : JSON.stringify(data),
            success : function(result, status) {
                alert("Register successfully !")
                console("New profile is created successfully with identifier: " + result.id);

                window.location.href = "/login";
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