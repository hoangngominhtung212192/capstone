$(document).ready(function() {

    $("#loginBtn").click(function (event) {
        event.preventDefault();

        var formLogin = {
            username : $("#username").val(),
            password : $("#password").val()
        }

        ajaxPost(formLogin);
    })

    function ajaxPost(data) {
        console.log(data);

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/api/user/login",
            data : JSON.stringify(data),
            success : function(result, status) {
                console.log(result);
                console.log(status)
            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }
})