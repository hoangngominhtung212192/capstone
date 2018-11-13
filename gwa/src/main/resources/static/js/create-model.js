$(document).ready(function () {

    // Begin authentication
    authentication();

    var createdDate;
    var account_session_id;

    function authentication() {

        $.ajax({
            type: "GET",
            url: "/gwa/api/user/checkLogin",
            complete: function (xhr, status) {
                if (status == "success") {

                    var xhr_data = xhr.responseText;
                    console.log(xhr_data);
                    var jsonResponse = JSON.parse(xhr_data);

                    var role_session = jsonResponse["role"].name;
                    account_session_id = jsonResponse["id"];

                    if (role_session != "ADMIN") {
                        window.location.href = "/gwa/403";
                    } else {
                        console.log(role_session + " " + jsonResponse["username"] + " is on session!");
                        $("#profileBtn").attr("href", "/gwa/pages/profile.html?accountID=" + jsonResponse["id"]);
                        $("#user-out-avatar").attr("src", jsonResponse["avatar"]);
                        $("#user-in-avatar").attr("src", jsonResponse["avatar"]);
                        $("#left-avatar").attr("src", jsonResponse["avatar"]);

                        createdDate = jsonResponse["createdDate"].split(" ")[0];
                        getSessionProfile(jsonResponse["id"]);
                        ajaxGetAllNotification(jsonResponse["id"]);
                    }

                } else {
                    window.location.href = "/gwa/login";
                }

            }
        });
    }

    // get session account's profile
    function getSessionProfile(id) {

        $.ajax({
            type: "POST",
            url: "/gwa/api/user/profile?accountID=" + id,
            success: function (result) {
                //get selected profile's account status

                var displayUsername = "";

                if (result.middleName) {
                    displayUsername += result.lastName + ' ' + result.middleName + ' ' + result.firstName;
                } else {
                    displayUsername += result.lastName + ' ' + result.firstName;
                }

                $("#user-in-name").text(displayUsername);
                $("#user-out-name").text(displayUsername);
                $("#user-in-name").append("<small>Member since " + createdDate + "</small>");

                $("#left-name").text(displayUsername);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    $("#signoutBtn").click(function (e) {
        e.preventDefault();

        $("#loading").css("display", "block");

        setTimeout(function () {
            $("#loading").css("display", "none");

            ajaxLogout();
        }, 300);
    })

    function ajaxLogout() {
        $.ajax({
            type: "GET",
            url: "/gwa/api/user/logout",
            success: function (result) {
                window.location.href = "/gwa/login";
            }
        });
    }
    // End authentication

    getAllDropdownValues();

    function getAllDropdownValues() {
        $("#loading").css("display", "block");
        ajaxGetManufacturer();
        ajaxGetProductseries();
        ajaxGetSeriestitle();
        setTimeout(function () {
            $("#loading").css("display", "none");
        }, 300);
    }

    function ajaxGetProductseries() {

        var first = false;

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/getAllProductseries",
            success: function (result) {
                console.log(result);

                $.each(result, function (key, entry) {
                    $("#cbo-productseries").append($('<option></option>').attr('value', entry.name).text(entry.name));
                    // first option
                    if (!first) {
                        $("#txtProductseries").attr('value', entry.name);
                        first = true;
                    }
                })
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function ajaxGetSeriestitle() {

        var first = false;

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/getAllSeriestitle",
            success: function (result) {
                console.log(result);

                $.each(result, function (key, entry) {
                    $("#cbo-seriestitle").append($('<option></option>').attr('value', entry.name).text(entry.name));
                    // first option
                    if (!first) {
                        $("#txtSeriestitle").attr('value', entry.name);
                        first = true;
                    }
                })
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function ajaxGetManufacturer() {

        var first = false;

        $.ajax({
            type: "GET",
            url: "/gwa/api/model/getAllManufacturer",
            success: function (result) {
                console.log(result);

                $.each(result, function (key, entry) {
                    $("#cbo-manufacturer").append($('<option></option>').attr('value', entry.name).text(entry.name));
                    if (!first) {
                        $("#txtManufacturer").attr('value', entry.name);
                        first = true;
                    }
                })
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    var count = 0;
    var totalSize = 0;
    var imageFiles = [];

    // upload file event
    $("#files").change(function (e) {

        var checkExist = false;
        // hide error
        $("#tr-error").css("display", "none");
        $("#error").text("");

        $.each(e.target.files, function (index, value) {
            // reset variable
            checkExist = false;
            console.log("Upload file: " + value.name);

            // for check file extension
            var match = ["image/jpeg", "image/png", "image/jpg"];

            // get image type
            var imagetype = value.type;
            // check file extension
            if (!((imagetype == match[0]) || (imagetype == match[1]) || (imagetype == match[2]))) {
                $("#error").css("display", "block");
                $("#error").text("Please select [jpeg/png/jpg] only");
            } else {
                // check exist image in list
                for (var i = 0; i < imageFiles.length && !checkExist; i++) {
                    // if exist
                    if (value.name == imageFiles[i].name) {
                        $("#error").css("display", "block");
                        $("#error").text("You already selected image " + value.name);
                        checkExist = true;
                    }
                }

                if (!checkExist) {
                    // check totalSize
                    totalSize += value.size;
                    // maximum total size: 5MB
                    if (totalSize > 5242880) {
                        totalSize -= value.size;
                        $("#error").css("display", "block");
                        $("#error").text("Maximum size for uploading: 5MB. Your current size: " + totalSize);
                    } else { // if not over reach max size
                        // display current total size
                        if (Math.round((totalSize/1024)/1024) == 0) {
                            $("#totalSize").text("Total: 1 MB");
                        } else {
                            $("#totalSize").text("Total: " + Math.round((totalSize/1024)/1024) + " MB");
                        }

                        // add current file image to list
                        imageFiles[count] = value;
                        // increase count
                        count++;

                        // preview image with file reader
                        var reader = new FileReader();
                        reader.onload = function (e) {
                            $(".files").append('<tr id="' + value.name + '" class="table-tr" style="margin-top: 10px;">' +
                                '<td><img id="' + count + '" src="' + e.target.result + '"/></td>' +
                                '<td><div class="imageName">' + value.name + '</div></td>' +
                                '<td><div class="imageSize">' + Math.round(value.size / 1024) + ' KB</div></td>' +
                                '<td class="imageType"><select id="image-type-' + value.name + '">\n' +
                                '<option value="Package">Package</option>\n' +
                                '<option value="Item picture">Item picture</option>\n' +
                                '<option value="Other picture">Other picture</option>\n' +
                                '<option value="Contents">Contents</option>\n' +
                                '<option value="About item">About item</option>\n' +
                                '<option value="Color">Color</option>\n' +
                                '<option value="Assembly Guide">Assembly Guide</option>\n' +
                                ' </select></td>\n' +
                                ' <td class="canceltd">\n' +
                                '<button id="$$$' + value.name + '" style="transition: none;\n' +
                                '    color: white;\n' +
                                '    width: 85px;\n' +
                                '    font-size: 14px;\n' +
                                '    height: 34px;\n' +
                                '    padding-top: 5px;\n' +
                                '    background-color: #ffc107;\n' +
                                '    border-color: #ffc107;" ' +
                                'class="high-cancel btn cancel ' + value.name + '">\n' +
                                '<i class="glyphicon glyphicon-ban-circle"></i>\n' +
                                '<span>Cancel</span>\n' +
                                '</button>\n' +
                                '<input id="$$' + value.name + '" type="checkbox" class="toggle models">\n' +
                                '</td>');

                            $("button[id='$$$" + value.name + "']").click(function (e) {
                                e.preventDefault();

                                var imageName = $(this).attr('id').replace('$$$', '');

                                remove(imageName);
                            })
                        }
                        reader.readAsDataURL(value);
                    }
                }
            }
        });

        $("#files").val('');
    });

    // remove image
    function remove(imageName) {
        console.log("Remove image: " + imageName);

        for (var i = 0; i < imageFiles.length; i++) {
            if (imageFiles[i].name.indexOf(imageName) == 0) {
                // subtract image size
                totalSize -= imageFiles[i].size;
                imageFiles.splice(i,1);
                // subtract list length
                count--;
            }
        }

        $("table#listImage tr[id='" + imageName + "']").remove();
    }

    $("#checkAll").change(function () {
        if (this.checked) {
            $(".models:checkbox").prop('checked', true);
        } else {
            $(".models:checkbox").prop('checked', false);
        }
    });

    $("#deleteFiles").click(function (e) {
        e.preventDefault();

        $(".models:checkbox:checked").each(function () {
            var imageName = $(this).attr('id').replace('$$', '');

            remove(imageName);
        })
    });

    $("#save").click(function (e) {
        e.preventDefault();
        $("#serverErr").css("display", "none");

        var code = $("#txtCode").val().trim();
        var name = $("#txtName").val().trim();
        var productseries = $("#txtProductseries").val().trim();
        var seriestitle = $("#txtSeriestitle").val().trim();
        var manufac = $("#txtManufacturer").val().trim();
        var price = $("#txtPrice").val().trim();
        var status = $("#cbo-status").find(":selected").text();
        var description = $("#txtDescription").val();

        if (checkValidForm(code, name, productseries, seriestitle, price)) {

            var formModel = {
                code : code,
                name : name,
                productseries : {
                    name : productseries
                },
                seriestitle : {
                    name : seriestitle
                },
                manufacturer : {
                    name : manufac
                },
                price : price,
                status: status,
                description : description
            }

            $("#mi-modal").modal({backdrop: 'static', keyboard: false});
            $("#modal-btn-si").on("click", function(){
                $("#mi-modal").modal('hide');
                $("#modal-btn-no").prop("onclick", null).off("click");
                $("#modal-btn-si").prop("onclick", null).off("click");
            });

            $("#modal-btn-no").on("click", function(e) {

                if (imageFiles.length > 0) {
                    var formData = new FormData();

                    $.each(imageFiles, function (idx, value) {
                        var type = value.type.split("/")[1];

                        formData.append("files", value, code + idx + "." + type);
                        formData.append("imagetypes", $("select[id='image-type-" + value.name + "'] option:selected").text());
                    });

                    ajaxPostModel(formModel, formData);
                } else {
                    ajaxPostModel(formModel, null);
                }

                $("#mi-modal").modal('hide');
                $("#modal-btn-no").prop("onclick", null).off("click");
                $("#modal-btn-si").prop("onclick", null).off("click");
            });

        }
    });

    function ajaxPostModel(data, images) {

        $.ajax({
           type: "POST",
            contentType: "application/json",
            processData: false,
            url: "/gwa/api/model/create",
            data: JSON.stringify(data),
            success: function (result) {
               console.log(result);

               if (images) {
                   images.append("id", result.id);

                   ajaxImagePost(images);
               } else {
                   $("#myModal").modal({backdrop: 'static', keyboard: false});
                   $("#success-btn").on("click", function() {
                       window.location.href = "/gwa/pages/modeldetail.html?modelID=" + result.id;
                   });
               }
            }, 
            complete: function (xhr, txtStatus) {
                if (txtStatus == "error") {
                    $("#serverErr").css("display", "block");

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);
                    $("#serverErr").text(jsonResponse["message"]);
                }
            }
        });
    }

    function ajaxImagePost(formData) {
        $.ajax({
            type: "POST",
            contentType: false,
            processData: false,
            url: "/gwa/api/model/create/uploadImages",
            data: formData,
            success: function (result) {
                console.log(result);

                $("#myModal").modal({backdrop: 'static', keyboard: false});
                $("#success-btn").on("click", function() {
                    window.location.href = "/gwa/pages/modeldetail.html?modelID=" + result.id;
                });
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        })
    }

    function checkValidForm(code, name, productseries, seriestitle, price) {
        var check = true;

        if (!code) {
            $("#errorcode").css("display", "block");
            $("#errorcode").text("Please input model code")
            check = false;
        } else {
            if (!code.match(/^\S*$/)) {
                $("#errorcode").css("display", "block");
                $("#errorcode").text("Invalid code, not allow space");
                check = false;
            } else {
                $("#errorcode").css("display", "none");
                $("#errorcode").text("");
            }
        }

        if (!name) {
            $("#errorname").css("display", "block");
            $("#errorname").text("Please input model name")
            check = false;
        } else {
            $("#errorname").css("display", "none");
            $("#errorname").text("")
        }

        if (!productseries) {
            $("#errorproductseries").css("display", "block");
            $("#errorproductseries").text("Please input or select product series");
            check = false;
        } else {
            $("#errorproductseries").css("display", "none");
            $("#errorproductseries").text("");
        }

        if (!seriestitle) {
            $("#errorseriestitle").css("display", "block");
            $("#errorseriestitle").text("Please input or select series title");
            check = false;
        } else {
            $("#errorseriestitle").css("display", "none");
            $("#errorseriestitle").text("");
        }

        return check;
    }

    $("#cancel").click(function (e) {
        e.preventDefault();

        history.back(1);
    })

    /*   This is for notification area   */
    var pageNumber = 1;
    var lastPage;

    function ajaxGetAllNotification(accountID) {

        $.ajax({
            type: "GET",
            url: "/gwa/api/notification/getAll?pageNumber=" + pageNumber + "&accountID=" + accountID,
            success: function (result) {
                console.log(result);

                lastPage = result[0];
                renderNotification(result[1]);
            }
        })
    }

    // scroll to bottom event
    $("#ul-notification").scroll(function () {
        if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
            if (pageNumber < lastPage) {
                pageNumber += 1;

                $("#loading").css("display", "block");

                setTimeout(function () {
                    ajaxGetAllNotification(account_session_id);

                    $("#loading").css("display", "none");
                }, 400);
            }
        }
    });

    var countNotSeen = 0;

    function renderNotification(result) {

        $.each(result, function (index, value) {

            var appendNotification = "";

            if (!value.seen) {
                // not seen yet
                countNotSeen++;
                appendNotification += "<li>\n"
            } else {
                // already seen
                appendNotification += "<li style='background-color: white;'>\n"
            }

            var iconType = "<i class=\"fa fa-warning text-yellow\" style=\"color: darkred;\"></i> ";

            if (value.notificationtype.name == "Profile"){
                iconType = "<i class=\"fa fa-user-circle-o text-yellow\" style=\"color: darkred;\"></i> ";
            }else if (value.notificationtype.name == "Model") {
                iconType = "<i class=\"fa fa-warning text-yellow\" style=\"color: darkred;\"></i> ";
            } else if (value.notificationtype.name == "Tradepost") {
                iconType = "<i class=\"fa fa-check-square-o text-yellow\" style=\"color: darkred;\"></i> ";
            } else if (value.notificationtype.name == "OrderSent") {
                iconType = "<i class=\"fa fa fa-paper-plane text-yellow\" style=\"color: darkred;\"></i> ";
            } else if (value.notificationtype.name == "OrderReceived") {
                iconType = "<i class=\"fa fa fa-bullhorn text-yellow\" style=\"color: darkred;\"></i> ";
            }

            appendNotification += "<a id='" + value.id + "-" + value.notificationtype.name + "-" + value.objectID +
                "' href=\"#\">\n" +
                iconType + value.description + "</a>\n" +
                "</li>";

            $("#ul-notification").append(appendNotification);

            // click event
            $("a[id='" + value.id + "-" + value.notificationtype.name + "-" + value.objectID + "'").click(function (e) {
                e.preventDefault();

                // get parameters saved in attribute id of current notification
                var notificationID = $(this).attr('id').split("-")[0];
                var type = $(this).attr('id').split("-")[1];
                var objectID = $(this).attr('id').split("-")[2];

                // log to console
                console.log("Notification ID: " + notificationID);
                console.log("Type: " + type);
                console.log("ObjectID: " + objectID);

                // set seen status to 0 --> means user has seen this current notification
                ajaxUpdateNotificationStatus(notificationID);

                if (type == "Profile") {
                    window.location.href = "/gwa/pages/profile.html?accountID=" + objectID;
                } else if (type == "Model") {
                    window.location.href = "/gwa/pages/modeldetail.html?modelID=" + objectID;
                } else if (type == "Tradepost") {
                    window.location.href = "/gwa/trade-market/view-trade?tradepostId=" + objectID;
                } else if (type == "OrderSent") {
                    window.location.href = "/gwa/trade-market/my-order";
                } else if (type == "OrderReceived") {
                    window.location.href = "/gwa/trade-market/view-trade?tradepostId=" + objectID;
                }
            });
        });

        if (countNotSeen == 1 || countNotSeen == 0) {
            $("#notice-new").text("You have " + countNotSeen + " new notification");
        } else {
            // plural
            $("#notice-new").text("You have " + countNotSeen + " new notifications");
        }

        if (countNotSeen == 0) {
            $("#numberOfNew").text("");
        } else {
            $("#numberOfNew").text(countNotSeen);
        }

    }

    function addNewNotification() {
        var description = $("#txtReason").val();

        var formNotification = {
            description: description,
            objectID: account_profile_on_page_id,
            account: {
                id: account_profile_on_page_id
            },
            notificationtype: {
                id: 1
            }
        }

        ajaxPostNewNotification(formNotification);
    }

    function ajaxPostNewNotification(data) {

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/gwa/api/notification/addNew",
            data: JSON.stringify(data),
            success: function (result) {
                console.log(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }

    function ajaxUpdateNotificationStatus(notificationID) {

        $.ajax({
            type: "POST",
            url: "/gwa/api/notification/update?notificationID=" + notificationID,
            success: function (result) {
                console.log(result);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            }
        });
    }
    /* End notification */
})