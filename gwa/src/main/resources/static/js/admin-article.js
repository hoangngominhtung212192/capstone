$(document).ready(function () {
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
                    window.location.href = "/login";
                }

            }
        });
    }

    getAllArticle();

    function getAllArticle() {
        console.log("getting all article");
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/api/user/getAllArticle",
            success : function(result, status) {
                console.log(result);
                console.log(status);
                for (var i in result){
                    var row = $('<tr>\n' +
                        '                    <td>' + result[i].id + '</td>\n' +
                        '                    <td>' + result[i].title + '</td>\n' +
                        '                    <td>' + result[i].accountID.id + '</td>\n' +
                        '                    <td>' + result[i].date + '</td>\n' +
                        '                    <td>' + result[i].category + '</td>\n' +
                        '                    <td>' + result[i].approvalStatus + '</td>\n' +
                        '                    <td></td>\n' +
                        '                  </tr>')
                    $('#tblBody').append(row);
                }
                // for (var i in result){
                //     $('#search-result').append(article);
                // }

            },
            error : function(e) {
                alert("No article with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }

    $("#btnSearch").click(function (event) {
        event.preventDefault();
        var searchDiv = document.getElementById("tblBody");
        while (searchDiv.firstChild) {
            searchDiv.removeChild(searchDiv.firstChild);
        }
        var searchValue = document.getElementById("txtSearch").value;
        ajaxPost(searchValue);
    })

    function ajaxPost(data) {
        console.log(data);
        var displayDiv = document.getElementById("search-result");

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/api/user/searchArticle",
            data : data.toString(),
            success : function(result, status) {
                console.log(result);
                console.log(status);
                for (var i in result){
                    var row = $('<tr>\n' +
                        '                    <td>' + result[i].id + '</td>\n' +
                        '                    <td>' + result[i].title + '</td>\n' +
                        '                    <td>' + result[i].accountID + '</td>\n' +
                        '                    <td>' + result[i].date + '</td>\n' +
                        '                    <td>' + result[i].category + '</td>\n' +
                        '                    <td>' + result[i].approvalStatus + '</td>\n' +
                        '                    <td></td>\n' +
                        '                  </tr>')
                    $('#tblBody').append(row);
                }
                // for (var i in result){
                //     $('#search-result').append(article);
                // }

            },
            error : function(e) {
                alert("No article with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }
})