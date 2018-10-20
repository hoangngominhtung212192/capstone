$(document).ready(function () {

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

    var accountID = getUrlParameter('accountID');
    getProfile();

    function getProfile() {

        $.ajax({
            type : "GET",
            url : "http://localhost:8080/api/user/profile?accountID=" + accountID,
            success : function(result) {
                console.log(result);
            },
            error : function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function profileClick(accountID) {
        $("#profile").click(function (event) {
            window.location.href = "/pages/profile.html?accountID=" + accountID;
        })
    }
})