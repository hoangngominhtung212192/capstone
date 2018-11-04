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

                    var tus = parseInt(result[i].approvalStatus) - 1;
                    var row = $('<tr>\n' +
                        '                    <td>' + result[i].id + '</td>\n' +
                        '                    <td>' + result[i].title + '</td>\n' +
                        '                    <td>' + result[i].account.id + '</td>\n' +
                        '                    <td>' + result[i].date + '</td>\n' +
                        '                    <td>' + result[i].category + '</td>\n' +
                        '                    <td><div class="styled-select slate">\n' +
                        '                   <select onchange="approvalChange(value,' + result[i].id + ' )" id="idx' + result[i].id + '" class="editable-select">\n' +
                        '                   <option value="1">APPROVED</option>\n' +
                        '                   <option value="2">DISAPPROVED</option>\n' +
                        '                   <option value="3">PENDING</option>\n' +
                        '                   </select></td>\n' +
                        '                    <td><a href="/admin/article/edit?id=' + result[i].id + '">Read more...</a></td>\n' +
                        '                  </tr>')
                    $('#tblBody').append(row);
                    document.getElementById("idx" + result[i].id ).options[tus].selected = 'selected';
                }
            },
            error : function(e) {
                alert("No article with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }




    //search function
    $("#btnSearch").click(function (event) {
        event.preventDefault();
        var searchDiv = document.getElementById("tblBody");
        while (searchDiv.firstChild) {
            searchDiv.removeChild(searchDiv.firstChild);
        }
        var searchValue = document.getElementById("txtSearch").value;
        if (searchValue.length == 0){
            getAllArticle();
        } else {
            searchArticle(searchValue);
        }

    })

    function searchArticle(data) {
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
                console.log("first res: "+ result[0].account.id );
                for (var i in result){
                    var tus = parseInt(result[i].approvalStatus) - 1;
                    var row = $('<tr>\n' +
                        '                    <td>' + result[i].id + '</td>\n' +
                        '                    <td>' + result[i].title + '</td>\n' +
                        '                    <td>' + result[i].account.id + '</td>\n' +
                        '                    <td>' + result[i].date + '</td>\n' +
                        '                    <td>' + result[i].category + '</td>\n' +
                        '                    <td><div class="styled-select slate">\n' +
                        '                   <select onchange="approvalChange(value,' + result[i].id + ' )" id="idx' + result[i].id + '" class="editable-select">\n' +
                        '                   <option value="1">APPROVED</option>\n' +
                        '                   <option value="2">DISAPPROVED</option>\n' +
                        '                   <option value="3">PENDING</option>\n' +
                        '                   </select></td>\n' +
                        '                    <td><a href="/admin/article/edit?id=' + result[i].id + '">Read more...</a></td>\n' +
                        '                  </tr>')
                    $('#tblBody').append(row);
                    document.getElementById("idx" + result[i].id ).options[tus].selected = 'selected';
                }
            },
            error : function(e) {
                alert("No article with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }
})