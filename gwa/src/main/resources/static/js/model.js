$(document).ready(function() {

    getUsername();

    function getUsername() {

        $.ajax({
            type : "GET",
            url : "http://localhost:8080/api/user/getUsername",
            success : function(result, status) {

                if (result.length) {
                    console.log("User " + result + " login!");

                    $("#username").text(result)
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
})