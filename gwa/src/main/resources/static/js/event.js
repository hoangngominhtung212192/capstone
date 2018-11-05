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

    getAllEvent();

    function appendResult(result){
        var article = $('<div class="single-blog-post featured-post d-flex">\n' +
            '                                <div class="post-data">\n' +
            '                                    <a href="#" class="post-catagory">EVENT</a>\n' +
            '                                    <div class="post-meta">\n' +
            '                                        <a class="post-title" href="/event/detail?id=' + result.id +'">\n' +
            '                                            <h6>' + result.title + '</h6>\n' +
            '                                        </a>\n' +
            '<p class="post-date"><span>From:'+result.startDate+'</span> to <span>'+result.endDate+'</span></p>'+
            '                                        <p><span>Ticket price: '+result.ticketPrice+'</span></p>\n' +
            '                                    </div>\n' +
            '                                </div>\n' +
            '                            </div>' +
            '<hr />');

        $('#search-result').append(article);
    }
    function getAllEvent() {

        var displayDiv = document.getElementById("search-result");

        $.ajax({
            type : "POST",
            url : "http://localhost:8080/api/event/getAllEvent",
            success : function(result, status) {
                console.log(result);
                console.log(status);
                for (var i in result){
                    appendResult(result[i]);
                }

            },
            error : function(e) {
                alert("No event with matching title found!");
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
            url : "http://localhost:8080/api/event/searchEvent",
            data : data.toString(),
            success : function(result, status) {
                console.log(result);
                console.log(status);
                for (var i in result){
                    appendResult(result[i]);
                }

            },
            error : function(e) {
                alert("No event with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }
})