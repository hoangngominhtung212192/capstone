$(document).ready(function () {

    var account_session_id;
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
        window.location.href = "/gwa/article/edit?id=" + id;
    })
    var username_session;
    authentication();
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

                    var role_session = jsonResponse["role"].name;
                    account_session_id = jsonResponse["id"];

                    // $('#hiddenLoggedUser').val(account_session_id);
                    var username = jsonResponse["username"];
                    username_session = jsonResponse["username"];
                    console.log("logegd user: "+username_session+" id "+account_session_id);
                    console.log(role_session + " " + username + " is on session!");

                    // click profile button
                    // profileClick(account_session_id);

                    // $('#hidUserID').val(account_session_id);
                    // display username, profile and logout button
                    // $("#username").text(username);
                    // $('#lblUsername').text(username);
                    // $("#username").css("display", "block");
                    $(".dropdown-menu-custom-profile").css("display", "block");
                    $(".dropdown-menu-custom-logout").css("display", "block");

                    // get current profile
                    account_profile_on_page_id = getUrlParameter('accountID');
                    ajaxPost(id);
                    // checkCurUserIsAttendee();
                } else {
                    ajaxPost(id);
                    // display login and register button
                    console.log("Guest is accessing !");
                }

            }
        });
    }


    function ajaxPost(data) {
        var displayDiv = document.getElementById("search-result");

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/gwa/api/article/getArticle",
            data : JSON.stringify(data),
            success : function(result, status) {
                console.log("id of author" + result.account.id);

                if (result){
                    console.log("userid "+ account_session_id+ " viewing article posted by id "+result.account.id);
                    if (account_session_id == result.account.id){
                        document.getElementById('btnEdit').style.display = 'block';
                    }
                    var title = result.title;
                    var date = result.date;
                    var resultcontent = result.content.toString();
                    var acontent = $('<a>lol</a><pre>' + resultcontent + '</pre>');
                    $('#author').append(result.account.username);
                    $('#title').append(title);
                    $('#date').append(date);
                    $('#content').html(result.content);
                }
            },
            error : function(e) {
                alert("no such article foudn!")
                console.log("ERROR: ", e);
            }
        });
    }
});
