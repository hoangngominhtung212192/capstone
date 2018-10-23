$(document).ready(function () {

    // authentication();

    function authentication() {

        $.ajax({
            type: "GET",
            url: "http://localhost:8080/api/user/checkLogin",
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
                        $("#error").text("You selected image " + value.name);
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
                            $(".files").append('<tr id="' + value.name.split(".")[0] + '" class="table-tr" style="margin-top: 10px;">' +
                                '<td><img id="' + count + '" src="' + e.target.result + '"/></td>' +
                                '<td><div class="imageName">' + value.name + '</div></td>' +
                                '<td><div class="imageSize">' + Math.round(value.size / 1024) + ' KB</div></td>' +
                                '<td class="imageType"><select id="image-type-' + count + '">\n' +
                                '<option value="Package">Package</option>\n' +
                                '<option value="Item picture">Item picture</option>\n' +
                                '<option value="Other picture">Other picture</option>\n' +
                                '<option value="Contents">Contents</option>\n' +
                                '<option value="About item">About item</option>\n' +
                                '<option value="Color">Color</option>\n' +
                                '<option value="Assembly Guide">Assembly Guide</option>\n' +
                                ' </select></td>\n' +
                                ' <td class="canceltd">\n' +
                                '<button id="cancelImage" class="btn btn-warning cancel ' + value.name.split(".")[0] + '">\n' +
                                '<i class="glyphicon glyphicon-ban-circle"></i>\n' +
                                '<span>Cancel</span>\n' +
                                '</button>\n' +
                                '<input type="checkbox" class="toggle models $$$' + value.name.split(".")[0] + '">\n' +
                                '</td>');

                            $("." + value.name.split(".")[0]).click(function (e) {
                                e.preventDefault();

                                var classess = this.className.split(" ");
                                var imageName = classess[classess.length-1];

                                remove(imageName);
                            })
                        }
                        reader.readAsDataURL(value);
                    }
                }
            }
        });
    });

    // remove image
    function remove(imageName) {
        console.log("Remove image: " + imageName);

        for (var i = 0; i < imageFiles.length; i++) {
            if (imageFiles[i].name.indexOf(imageName) == 0) {
                imageFiles.splice(i,1);
            }
        }

        $("table#listImage tr#" + imageName).remove();
    }

    $("#checkAll").change(function () {
        if (this.checked) {
            $(".models:checkbox").prop('checked', true);
        } else {
            $(".models:checkbox").prop('checked', false);
        }
    })

    $("#deleteFiles").click(function (e) {
        e.preventDefault();

        $(".models:checkbox:checked").each(function () {
            var classess = this.className.split(" ");
            var imageName = classess[classess.length-1].replaceAll("$$$", "");

            remove(imageName);
        })
    })
})