$(document).ready(function() {

    // process UI
    $(document).click(function (event) {
        $(".dropdown-menu-custom-profile").css("height", "0");
        $(".dropdown-menu-custom-profile").css("border", "none");
        $(".dropdown-menu-custom-logout").css("height", "0");
        $(".dropdown-menu-custom-logout").css("border", "none");
    })

    authentication();

    function authentication() {

        $.ajax({
            type : "GET",
            url : "http://localhost:8080/api/user/checkLogin",
            complete : function(xhr, status) {

                if (status == "success") {
                    // username click
                    usernameClick();

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);
                    var role = jsonResponse["role"].name;

                    var thumbAvatar = jsonResponse["avatar"];
                    var username = jsonResponse["username"];
                    var accountID = jsonResponse["id"];

                    console.log(role + " " + username + " is on session!");

                    // click profile button
                    profileClick(accountID);

                    $("#username").text(username)
                    $("#username").css("display", "block");
                    $("#thumbAvatar").attr("src", thumbAvatar);
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

    function usernameClick() {
        $("#username").click(function (event) {
            // separate from document click
            event.stopPropagation();

            $(".dropdown-menu-custom-profile").css("height", "25px");
            $(".dropdown-menu-custom-profile").css("border", "1px solid #FF0000");
            $(".dropdown-menu-custom-logout").css("height", "25px");
            $(".dropdown-menu-custom-logout").css("border", "1px solid #FF0000");

            return false;
        })
    }
})