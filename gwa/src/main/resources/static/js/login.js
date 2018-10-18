$(document).ready(function() {

    getUsername();

    function getUsername() {

        $.ajax({
            type : "GET",
            url : "http://localhost:8080/api/user/getUsername",
            success : function(result, status) {

                if (result.length) {
                    window.location.href("http://localhost:8080/model/");
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
                username : $("#username").val(),
                password : $("#password").val()
            }

            ajaxPost(formLogin);
        }

    })

    function ajaxPost(data) {
        console.log(data);

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/api/user/login",
            data : JSON.stringify(data),
            success : function(role, status) {
                if (role == "MEMBER") {
                    window.location.href = "http://localhost:8080/model/"
                } else {
                    window.location.href = "http://localhost:8080/admin/model/create"
                }
            },
            complete : function(xhr, textStatus) {
                if (textStatus == "error") {
                    $("#errorpassword").css("visibility", "visible");
                    $("#errorpassword").text(xhr.responseText);
                }
            }
        });
    }
})