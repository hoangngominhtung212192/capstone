$(document).ready(function () {
    var currentStatus = "approved";

    var currentSortType = $("#cbSortType option:selected").val();

    var currentPage = 1;
    var totalPage = 1;
    var $pagination = $("#pagination-event");
    var isSearch = false;
    var defaultPaginationOpts = {
        totalPages: totalPage,
// the current page that show on start
        startPage: 1,

// maximum visible pages
        visiblePages: 3,

        initiateStartPageClick: false,

// template for pagination links
        href: false,

// variable name in href template for page number
        hrefVariable: '{{number}}',

// Text labels
        first: '&laquo;',
        prev: '❮',
        next: '❯',
        last: '&raquo;',

// carousel-style pagination
        loop: false,

// callback function
        onPageClick: function (event, page) {
            currentPage = page;
            $('#search-result').html("");
            searchArticle();
        },

// pagination Classes
        paginationClass: 'pagination',
        nextClass: 'page-item',
        prevClass: 'page-item',
        lastClass: 'page-item',
        firstClass: 'page-item',
        pageClass: 'page-item',
        activeClass: 'active',
        disabledClass: 'disabled'
    };
    $pagination.twbsPagination('destroy');
    if (totalPage > 1){
        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
            totalPages: totalPage
        }));
    }


    function authentication() {

        $.ajax({
            type: "GET",
            url: "http://localhost:8080/gwa/api/user/checkLogin",
            complete: function (xhr, status) {

                if (status == "success") {
                    // username click
                    usernameClick();

                    var xhr_data = xhr.responseText;
                    var jsonResponse = JSON.parse(xhr_data);

                    role_session = jsonResponse["role"].name;
                    account_session_id = jsonResponse["id"];

                    var username = jsonResponse["username"];
                    console.log(role_session + " " + username + " is on session!");

                    // click profile button
                    profileClick(account_session_id);

                    // display username, profile and logout button
                    $("#username").text(username)
                    $("#username").css("display", "block");
                    $(".dropdown-menu-custom-profile").css("display", "block");
                    $(".dropdown-menu-custom-logout").css("display", "block");

                    // get current profile
                    account_profile_on_page_id = getUrlParameter('accountID');
                    getProfile();
                } else {
                    // display login and register button
                    console.log("Guest is accessing !");
                    window.location.href = "/login";
                }

            }
        });
    }
    function appendResult(result){
        for (var i = 0; i < result.length; i++) {
            var article = $('<div class="single-blog-post small-featured-post d-flex">\n' +
                '                                <div class="post-data">\n' +
                '                                    <a href="#" class="post-catagory">' + result[i].category + '</a>\n' +
                '                                    <div class="post-meta">\n' +
                '                                        <a href="/gwa/article/detail?id=' + result[i].id +'" class="post-title">\n' +
                '                                            <h6>' + result[i].title + '</h6>\n' +
                '                                        </a>\n' +
                '                                        <p class="post-date">' + result[i].date + '</p>\n' +
                '                                        <p >Posted by: </p>\n' +
                '                                    </div>\n' +
                '                                </div>\n' +
                '                            </div>');

            $('#search-result').append(article);
        }
        $pagination.twbsPagination('destroy');
        if (totalPage > 1){
            $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                totalPages: totalPage,
                currentPage: currentPage,
                startPage: currentPage,

            }));
        }
    }
    $("#btnSearch").click(function (event) {
        event.preventDefault();
        var searchDiv = document.getElementById("search-result");
        while (searchDiv.firstChild) {
            searchDiv.removeChild(searchDiv.firstChild);
        }
        isSearch = true;
        searchArticle();
    })

    function searchArticle() {
        // console.log(data);
        var displayDiv = document.getElementById("search-result");
        var searchValue = $("#txtSearch").val();

        $.ajax({
            type : "POST",
            url : "http://localhost:8080/gwa/api/article/searchArticleByStatusAndPage",
            data : {
                title : searchValue,
                status : currentStatus,
                sorttype : currentSortType,
                pageNum : currentPage
            },
            async: false,
            success : function(result, status) {
                var data = result[1];
                totalPage = result[0];
                console.log(result);
                console.log(status);
                console.log("seach numb of pages: "+result[0]);
                $pagination.twbsPagination('destroy');
                // if (totalPage > 1){
                    $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
                        totalPages: totalPage
                    }));
                // }
                appendResult(data);
            },
            error : function(e) {
                alert("No event with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }

})