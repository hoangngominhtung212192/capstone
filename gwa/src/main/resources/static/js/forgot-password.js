$(document).ready(function () {
    authentication();

    function authentication() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {
                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);
                    var role = jsonResponse["role"].name;

                    if (role == "MEMBER") {
                        window.location.href = "/gwa/model/"
                    } else if (role == "ADMIN") {
                        window.location.href = "/gwa/admin/model/create";
                    } else if (role == "BUYERSELLER") {
                        window.location.href = "/gwa/trade-market/my-trade";
                    }
                } else {
                    console.log("Guest is accessing !");
                }

            }
        });
    }

    $("#submitBtn").click(function (event) {
        event.preventDefault();

        var check = true;

        var email = $("#email").val().trim();

        if (!email) {
            $("#erroremail").css("visibility", "visible");
            $("#erroremail").text("Please input your email");
            check = false;
        } else {
            if (!email.match("^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")) {
                $("#erroremail").css("visibility", "visible");
                $("#erroremail").text("Please input valid email");
                check = false;
            } else if (email.length > 50) {
                $("#erroremail").css("visibility", "visible");
                $("#erroremail").text("Out of range, maximum: 50 characters");
                check = false;
            } else {
                $("#erroremail").css("visibility", "hidden");
            }
        }

        if (check) {
            ajaxPost(email);
        }
    })

    function ajaxPost(email) {
        $("#loading").css("display", "block");

        $.ajax({
            type : "POST",
            url : "/gwa/api/user/forgot-password?email=" + email,
            success : function(result, status) {
                console.log(result);

                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function() {
                    window.location.href = "/gwa/login";
                });

                $("#loading").css("display", "none");
            },
            complete : function(xhr, textStatus) {
                if (textStatus == "error") {
                    $("#erroremail").css("visibility", "visible");

                    var xhr_data = xhr.responseText;
                    $("#erroremail").text(xhr_data);
                    console.log(xhr_data);
                }

                $("#loading").css("display", "none");
            }
        });
    }
})