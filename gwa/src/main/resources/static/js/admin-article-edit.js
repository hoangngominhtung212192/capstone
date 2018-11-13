$(document).ready(function () {

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

    showArticle(id);

    function showArticle(data) {
        console.log("showing article with id: " + data);

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "http://localhost:8080/gwa/api/article/getArticle",
            data : JSON.stringify(data),
            success : function(result, status) {
                console.log(result);
                console.log(status);
                if (result){
                    $('#cboStatus').val(result.approvalStatus);
                    $('#cboCate').val(result.category);
                    $('#txtTitle').val(result.title);
                    $('#txtDescription').val(result.description);
                    $('#date').append(result.date);
                    $('#contentEditor').html(result.content);
                    $('#author').append(result.account.username);
                }
            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }

    function getSelectIndexCate(cate){
        switch (cate) {
            case "News":
                return 1;
                break;
            case "Tutorial":
                return 2;
                break;
            case "Custom build":
                return 3;
                break;
    }}

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

        today = yyyy + "-" + mm + "-" + dd;
        var formArticle = {
            id : id,
            title : $("#txtTitle").val(),
            content : CKEDITOR.instances.contentEditor.getData(),
            description : $('#txtDescription').val(),
            category : $("#cboCate").val(),
            modifiedDate : today,
            date : $('#lblDate').val(),
            approvalStatus : $("#cboStatus :selected").val(),
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
            url : "http://localhost:8080/gwa/api/article/updateArticle",
            data : JSON.stringify(data),
            success : function(result, status) {
                console.log(result);
                console.log(status);
                alert("Article updated successfully!");
                window.location.href = "/gwa/admin/article";
            },
            error : function(e) {
                alert("Error!")
                console.log("ERROR: ", e);
            }
        });
    }
})
