$(document).ready(function () {
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
    var account_session_id;
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

                    role_session = jsonResponse["role"].name;
                    account_session_id = jsonResponse["id"];

                    var username = jsonResponse["username"];
                    console.log(role_session + " " + username + " is on session!");

                    // click profile button
                    // profileClick(account_session_id);

                    // display username, profile and logout button
                    $("#username").text(username)
                    $("#username").css("display", "block");
                    $(".dropdown-menu-custom-profile").css("display", "block");
                    $(".dropdown-menu-custom-logout").css("display", "block");

                    // get current profile
                    account_profile_on_page_id = getUrlParameter('accountID');
                    // getProfile();
                    showArticle(id);
                } else {
                    showArticle(id)
                    // display login and register button
                    console.log("Guest is accessing !");
                    // window.location.href = "/login";
                }

            }
        });
    }


    function showArticle(data) {
        console.log("showing article with id: " + data);
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/gwa/api/article/getArticle",
            data : JSON.stringify(data),
            success : function(result, status) {
                console.log(result);
                console.log(status);
                if (result){
                    if(result.account.id != account_session_id){
                        window.location.href = "/gwa/article";
                    }
                    var title = result.title;
                    var date = result.date;
                    var resultcontent = result.content.toString();
                    console.log(resultcontent);
                    document.getElementById("title").value = title.toString();
                    $('#txtDescription').val(result.description);
                    $('#cboCate').val(result.category);
                    $('#author').append(result.account.username);
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
    $("#submitBtn").click(function (event) {
        event.preventDefault();
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth()+1; //January is 0!
        var yyyy = today.getFullYear();

        if(dd<10) {
            dd = '0'+dd
        }

        if(mm<10) {
            mm = '0'+mm
        }

        today = dd + '/' + mm + '/' + yyyy;
        var formArticle = {
            id : id,
            title : $("#title").val(),
            description : $('#txtDescription').val(),
            content : CKEDITOR.instances.content.getData(),
            category : $("#cboCate").val(),
            modifiedDate : today,
            approvalStatus : 'userpending',
        }

        updateArticle(formArticle);
    })


        var modalConfirm = function(callback){

            $("#deleteBtn").on("click", function(){
                $("#confi-modal").modal('show');
            });

            $("#modal-btn-yes").on("click", function(){
                callback(true);
                $("#confi-modal").modal('hide');
            });

            $("#modal-btn-no").on("click", function(){
                callback(false);
                $("#confi-modal").modal('hide');
            });
        };

        modalConfirm(function(confirm){
            if(confirm){
                console.log("yes");
            }else{
                console.log("no");
            }
        });

    function updateArticle(data) {

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/gwa/api/article/updateArticle",
            data : JSON.stringify(data),
            success : function(result, status) {
                console.log(result);
                console.log(status);
                alert("Article updated successfully!");
                window.location.href = "detail?id=" + result.id;
            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }
})
