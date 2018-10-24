$(document).ready(function () {
    authentication()
    function authentication() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {
                    // username click
                    usernameClick();

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);

                    role_session = jsonResponse["role"].name;
                    account_session_id = jsonResponse["id"];

                    var username = jsonResponse["username"];
                    console.log(role_session + " " + username + " is on session!");

                    // click profile button
                    profileClick(account_session_id);

                    // display username, profile and logout button
                    $("#username").text(username)
                    $("#username").css("display", "block");
                    $(".dropdown-menu-custom-profile").css("display", "block");
                    $(".dropdown-menu-custom-logout").css("display", "block");

                    // get current profile
                    account_profile_on_page_id = getUrlParameter('accountID');
                    getProfile();
                } else {
                    // display login and register button
                    console.log("Guest is accessing !");
                    // window.location.href = "/login";
                }

            }
        });
    }


    // split url to get parameter algorithm
    var getUrlParameter = function getUrlParameter() {
        var sPageURL = window.location.href;
        console.log(sPageURL);
        var url = new URL(sPageURL);
        var c = url.searchParams.get("id");
        console.log(c);
        arId = parseInt(c);
        return arId;
    }

    var id = getUrlParameter();
    $("#btnEdit").click(function (event) {
        event.preventDefault();
        window.location.href = "edit?id=" + id;
    })

    ajaxPost(id)

    function ajaxPost(data) {
        var displayDiv = document.getElementById("search-result");

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/api/user/getArticle",
            data : JSON.stringify(data),
            success : function(result, status) {
                if (result){
                    var title = result.title;
                    var date = result.date;
                    var resultcontent = result.content.toString();
                    var acontent = $('<a>lol</a><pre>' + resultcontent + '</pre>');
                    $('#title').append(title);
                    $('#date').append(date);
                    $('#content').html(result.content);
                }
            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }
})
