$(document).ready(function() {

    authentication();

    function authentication() {

        $.ajax({
            type : "GET",
            url : "http://localhost:8080/api/user/checkLogin",
            complete : function(xhr, status) {

                if (status == "success") {
                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);
                    var role = jsonResponse["role"].name;
                    var username = jsonResponse["username"];
                    var accountID = jsonResponse["id"];

                    console.log(role + " " + username + " login!");

                    // click profile button
                    profileClick(accountID);

                    $("#username").text(username)
                    $("#username").css("display", "block");
                    $(".dropdown-menu-custom-profile").css("display", "block");
                    $(".dropdown-menu-custom-logout").css("display", "block");
                } else {
                    console.log("Guest is accessing !");
                    $("#loginbtn").css("display", "block");
                    $("#register").css("display", "block");
                }

            }
        });
    }

    $("#logout").click(function (event) {
        console.log("aaaaaaaaa");

        event.preventDefault();

        ajaxLogout();
    })

    function ajaxLogout() {
        $.ajax({
            type : "GET",
            url : "http://localhost:8080/api/user/logout",
            success : function(result) {
                window.location.href = "http://localhost:8080/login";
            }
        });
    }

    function profileClick(accountID) {
        $("#profile").click(function (event) {
            window.location.href = "/pages/profile.html?accountID=" + accountID;
        })
    }
})