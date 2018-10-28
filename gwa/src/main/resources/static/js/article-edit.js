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
    showArticle(id)

    function showArticle(data) {
        console.log("showing article with id: " + data);

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/api/user/getArticle",
            data : JSON.stringify(data),
            success : function(result, status) {
                console.log(result);
                console.log(status);
                if (result){
                    var title = result.title;
                    var date = result.date;
                    var resultcontent = result.content.toString();
                    console.log(resultcontent);
                    var acontent = $('<a>lol</a><pre>' + resultcontent + '</pre>');
                    document.getElementById("title").value = title.toString();
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
            content : CKEDITOR.instances.content.getData(),
            category : $("#category").val(),
            date : today,
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
            url : "http://localhost:8080/api/user/updateArticle",
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
