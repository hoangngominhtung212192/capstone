$(document).ready(function () {

    function getUrlParameter(sParam) {
        var sPageURL = decodeURIComponent(window.location.search.substring(1)),
            sURLVariables = sPageURL.split('&'),
            sParameterName,
            i;

        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');

            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : sParameterName[1];
            }
        }
    };

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
                        window.location.href = "/gwa/model";
                    } else if (role == "ADMIN") {
                        window.location.href = "/gwa/admin/model/pending";
                    } else if (role == "BUYERSELLER") {
                        window.location.href = "/gwa/trade-market/my-trade";
                    }
                } else {
                    console.log("Guest is accessing !");
                }

            }
        });
    }

    $("#loginBtn").click(function (event) {
        event.preventDefault();

        $("#loading").css("display", "block");

        var check = true;

        if (!$("#username").val().trim()) {
            $("#errorusername").css("visibility", "visible");
            $("#errorusername").text("Please input empty field");
            check = false;
        } else {
            $("#errorusername").css("visibility", "hidden");
        }

        if (!$("#password").val().trim()) {
            $("#errorpassword").css("visibility", "visible");
            $("#errorpassword").text("Please input empty field");
            check = false;
        } else {
            $("#errorpassword").css("visibility", "hidden");
        }

        if (check) {
            var formLogin = {
                username: $("#username").val(),
                password: $("#password").val()
            }

            ajaxPost(formLogin);
        }

    })

    function ajaxPost(data) {
        console.log(data);

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/gwa/api/user/login",
            data: JSON.stringify(data),
            success: function (result, status) {

                console.log(result);

                /*  This is for firebase area */
                var config = {
                    apiKey: "AIzaSyCACMwhbLcmYliWyHJgfkd8IW6oPUoupIM",
                    authDomain: "gunplaworld-51eee.firebaseapp.com",
                    databaseURL: "https://gunplaworld-51eee.firebaseio.com",
                    projectId: "gunplaworld-51eee",
                    storageBucket: "gunplaworld-51eee.appspot.com",
                    messagingSenderId: "22850579681"
                };

                firebase.initializeApp(config);

                var messaging = firebase.messaging();

                if ('serviceWorker' in navigator) {
                    navigator.serviceWorker.register("/gwa/pages/firebase-messaging-sw.js", {
                        scope: "/gwa/pages/"
                    }).then(function (registration) {
                        messaging.useServiceWorker(registration);

                        messaging.requestPermission()
                            .then(function (value) {
                                console.log("Have permission!");
                                return messaging.getToken();
                            }).then(function (token) {
                            ajaxAddToken(result.id, token);

                            $("#loading").css("display", "none");

                            // get goBack parameter value from previous page which required to login
                            var goBack = getUrlParameter("goBack");
                            if (goBack) {
                                window.location.reload(history.back(goBack));
                            } else {
                                if (result.role.name == "MEMBER") {
                                    window.location.href = "/gwa/model"
                                } else {
                                    if (result.role.name == "ADMIN") {
                                        window.location.href = "/gwa/admin/model/pending";
                                    } else if (result.role.name == "BUYERSELLER") {
                                        window.location.href = "/gwa/trade-market/my-trade";
                                    }
                                }
                            }
                            // end redirect page
                        }).catch(function (err) {
                            console.log(err);
                        })
                    })
                }

                /* This is end of firebase  */

            },
            complete: function (xhr, textStatus) {
                if (textStatus == "error") {

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);

                    $("#errorpassword").css("visibility", "visible");
                    $("#errorpassword").text(jsonResponse["message"]);
                }
            }
        });
    }

    function ajaxAddToken(accountID, token) {

        $.ajax({
            type: "POST",
            url: "/gwa/api/user/addToken?accountID=" + accountID + "&token=" + token,
            success: function (result) {
                console.log(result);
            }
        });
    }
})