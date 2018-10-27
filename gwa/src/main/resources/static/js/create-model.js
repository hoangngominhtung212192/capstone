$(document).ready(function () {

    // authentication();

    getAllDropdownValues();

    function authentication() {

        $.ajax({
            type: "GET",
            url: "/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);

                    var role_session = jsonResponse["role"].name;

                    if (role_session != "ADMIN") {
                        alert("Access denied !")
                        window.location.href = "/model/";
                    } else {
                        var username = jsonResponse["username"];
                        var thumbAvatar = jsonResponse["avatar"];
                        console.log(role_session + " " + username + " is on session!");
                    }

                } else {
                    alert("Please login as administrator to continue !")
                    window.location.href = "/login";
                }

            }
        });
    }

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
            url: "/api/model/getAllProductseries",
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
            url: "/api/model/getAllSeriestitle",
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
            url: "/api/model/getAllManufacturer",
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
                    // maximum total size: 200MB
                    if (totalSize > 31457280) {
                        totalSize -= value.size;
                        $("#error").css("display", "block");
                        $("#error").text("Maximum size for uploading: 30MB. Your current size: " + totalSize);
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
                                '    padding-top: 5px;" ' +
                                'class="high-cancel btn btn-warning cancel ' + value.name + '">\n' +
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

            $("#mi-modal").modal('show');
            $("#modal-btn-si").on("click", function(){
                $("#mi-modal").modal('hide');
            });

            $("#modal-btn-no").on("click", function(){
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
            });
        }
    });

    function ajaxPostModel(data, images) {
        $.ajax({
           type: "POST",
            contentType: "application/json",
            processData: false,
            url: "/api/model/create",
            data: JSON.stringify(data),
            success: function (result) {
               console.log(result);

               if (images) {
                   images.append("id", result.id);

                   ajaxImagePost(images);
               }
            }, 
            complete: function (xhr, txtStatus) {
                if (txtStatus == "error") {
                    $("#error").css("display", "block");

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);
                    $("#error").text(jsonResponse["message"]);
                }
            }
        });
    }

    function ajaxImagePost(formData) {
        $.ajax({
            type: "POST",
            contentType: false,
            processData: false,
            url: "/api/model/create/uploadImages",
            data: formData,
            success: function (result) {
                console.log(result);

                alert("Created model successfully!")
                window.location.href = "/model/modeldetail";
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
})