$(document).ready(function() {



    $("#btnSubmit").click(function (event) {
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
            title : $("#txtTitle").val(),
            description : $('#txtDescription').val(),
            content : CKEDITOR.instances.contentEditor.getData(),
            category : $("#cboCate").val(),
            date : today,
            approvalStatus : $('#cboStatus').val(),
        }

        ajaxPost(formArticle);
    })

    function ajaxPost(data) {
        console.log(data);

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/gwa/api/article/createArticle",
            data : JSON.stringify(data),
            success : function(result, status) {
                alert("Article created successfully!");
                window.location.href = "/gwa/admin/article/";

            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }

})