$(document).ready(function() {

    $("#submitBtn").click(function (event) {
        event.preventDefault();
        var formArticle = {
            title : $("#title").val(),
            content : CKEDITOR.instances.content.getData()
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
                console.log(result);
                console.log(status);
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