$(document).ready(function() {
    authentication()
    var loggedUsername;
    function authentication() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/gwa/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {
                    // username click
                    // usernameClick();

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);

                    role_session = jsonResponse["role"].name;
                    account_session_id = jsonResponse["id"];
                    // userid = jsonResponse["id"];

                    var username = jsonResponse["username"];
                    loggedUsername = jsonResponse["username"];

                    username_session = jsonResponse["username"];
                    console.log("logegd user: "+username_session);
                    console.log(role_session + " " + username + " is on session!");

                    // click profile button
                    // profileClick(account_session_id);

                    $('#hidUserID').val(account_session_id);
                    // display username, profile and logout button
                    $("#username").text(username);
                    $('#lblUsername').text(username);
                    $("#username").css("display", "block");
                    $(".dropdown-menu-custom-profile").css("display", "block");
                    $(".dropdown-menu-custom-logout").css("display", "block");

                    // get current prof

                } else {
                    // display login and register button
                    console.log("Guest is accessing !");
                    window.location.href = "/gwa/login";
                }

            }
        });
    }
    $("#btnSubmit").click(function (event) {
        event.preventDefault();
        var formData = {

        }

        createProposal();

    })
    function createProposal() {
        console.log();

        $.ajax({
            type : "POST",
            // contentType : "application/json",
            url : "http://localhost:8080/gwa/api/proposal/createProposal",
            data : {
                username : loggedUsername,
                eventTitle : $('#txtTitle').val(),
                description : $('#txtDescription').val(),
                location : $('#txtLocation').val(),
                prize : $('#txtPrize').val(),
                startDate : $('#txtDate').val(),
                content : CKEDITOR.instances.contentEditor.getData(),
                ticketPrice : $('#txtPrice').val()

            },
            success : function(result, status) {
                alert("Proposal sent. Thank you for your input!");
                window.location.href = "/gwa/event";

            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }
})