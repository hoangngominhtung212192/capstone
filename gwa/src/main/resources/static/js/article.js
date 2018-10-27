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

        var displayDiv = document.getElementById("search-result");

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/api/user/getAllArticle",
            success : function(result, status) {
                console.log(result);
                console.log(status);
                for (var i in result){
                    var article = $('<div class="single-blog-post small-featured-post d-flex">\n' +
                        '                                <div class="post-data">\n' +
                        '                                    <a href="#" class="post-catagory">' + result[i].category + '</a>\n' +
                        '                                    <div class="post-meta">\n' +
                        '                                        <a href="#" class="post-title">\n' +
                        '                                            <h6><a href="/article/detail?id=' + result[i].id +'">' + result[i].title + '</a></h6>\n' +
                        '                                        </a>\n' +
                        '                                        <p class="post-date">' + result[i].date + '</p>\n' +
                        '                                        <p >Posted by: </p>\n' +
                        '                                    </div>\n' +
                        '                                </div>\n' +
                        '                            </div>');

                    $('#search-result').append(article);
                }

            },
            error : function(e) {
                alert("No article with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }
    $("#btnSearch").click(function (event) {
        event.preventDefault();
        var searchDiv = document.getElementById("search-result");
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
                    var article = $('<div class="single-blog-post small-featured-post d-flex">\n' +
                        '                                <div class="post-data">\n' +
                        '                                    <a href="#" class="post-catagory">' + result[i].category + '</a>\n' +
                        '                                    <div class="post-meta">\n' +
                        '                                        <a href="#" class="post-title">\n' +
                        '                                            <h6><a href="/article/detail?id=' + result[i].id +'">' + result[i].title + '</a></h6>\n' +
                        '                                        </a>\n' +
                        '                                        <p class="post-date">' + result[i].date + '</p>\n' +
                        '                                        <p >Posted by: </p>\n' +
                        '                                    </div>\n' +
                        '                                </div>\n' +
                        '                            </div>');

                    $('#search-result').append(article);
                }

            },
            error : function(e) {
                alert("No article with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }
})