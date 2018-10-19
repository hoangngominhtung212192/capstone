$(document).ready(function() {

    authentication();

    function authentication() {

        $.ajax({
            type : "GET",
            url : "http://localhost:8080/api/user/getUsername",
            complete : function(xhr, status) {

                if (status == "success") {
                    console.log("User " + xhr.responseText + " login!");

                    $("#username").text(xhr.responseText)
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