$(document).ready(function () {
    function authentication() {

        $.ajax({
            type: "GET",
            url: "http://localhost:8080/gwa/api/user/checkLogin",
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
                }

            }
        });
    }

    getAllProposal();



    function getAllProposal() {
        console.log("getting all proposal");
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/gwa/api/proposal/getProposalList",
            success : function(result, status) {
                console.log(result);
                console.log(status);
                for (var i in result){

                    var row = $('<tr>\n' +
                        '                    <td>' + result[i].eventTitle + '</td>\n' +
                        '                    <td>' + result[i].account.username + '</td>\n' +
                        '                    <td><a href="/gwa/admin/proposal/detail?id=' + result[i].id + '">Detail</a></td>\n' +
                        '                  </tr>')
                    $('#tblBody').append(row);
                    // document.getElementById("idx" + result[i].id ).options[tus].selected = 'selected';
                }
            },
            error : function(e) {
                alert("No proposal found!");
                console.log("ERROR: ", e);
            }
        });
    }




})