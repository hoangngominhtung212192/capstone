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
})