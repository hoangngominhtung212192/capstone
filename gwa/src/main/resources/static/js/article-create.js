$(document).ready(function() {

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
            title : $("#title").val(),
            content : CKEDITOR.instances.content.getData(),
            category : $("#category").val(),
            date : today,
            approvalStatus : 'userpending'
        }

        ajaxPost(formArticle);
    })

    function ajaxPost(data) {
        console.log(data);

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/api/user/createArticle",
            data : JSON.stringify(data),
            success : function(result, status) {
                alert("Article created successfully!");
                window.location.href = "detail?id=" + result.id;

            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }
})