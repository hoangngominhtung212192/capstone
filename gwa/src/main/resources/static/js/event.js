// $(document).ready(function () {
var currentStatus = $( "#cboStatus option:selected").val();

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
        console.log("clicked on page: "+page)
        currentPage = page;
        $('#search-result').html("");
        searchEv();
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
    searchEv();
    // $pagination.twbsPagination('destroy');
    // if (totalPage > 1){
    //     $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
    //         totalPages: totalPage
    //     }));
    // }

    $("#btnSearch").click(function (event) {
        currentPage = 1;
        event.preventDefault();
        var searchDiv = document.getElementById("search-result");
        while (searchDiv.firstChild) {
            searchDiv.removeChild(searchDiv.firstChild);
        };
        isSearch = true;
        searchEv();

    });

    function searchEv() {
        var searchValue = $("#txtSearch").val();

        console.log("searchvalue: "+searchValue);
        console.log("sorttype: "+$("#cbSortType option:selected").val());
        $.ajax({
            type : "POST",
            url : "http://localhost:8080/gwa/api/event/searchEventByStatusAndPage",
            data : {
                title : searchValue,
                status : $( "#cboStatus option:selected").val(),
                sorttype : $("#cbSortType option:selected").val(),
                pageNum : currentPage
            },
            async: false,
            success : function(result, status) {
                var data = result[1];
                totalPage = result[0];
                console.log(result);
                console.log(status);
                console.log("page num: "+currentPage);
                console.log("seach numb of pages: "+result[0]);
                // currentPage = 1;

                appendResult(data);
            },
            error : function(e) {
                alert("No event with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }

function appendResult(result){

    for (var i = 0; i < result.length; i++) {
        var article = $('<div class="single-blog-post featured-post d-flex">\n' +
            '                                <div class="post-data">\n' +
            '                                    <a href="#" class="post-catagory">EVENT</a>\n' +
            '                                    <div class="post-meta">\n' +
            '                                        <a class="post-title" href="/gwa/event/detail?id=' + result[i].id +'">\n' +
            '                                            <h6>' + result[i].title + '</h6>\n' +
            '                                        </a>\n' +
            '<p class="post-date"><span>From: '+result[i].startDate+'</span> to <span>'+result[i].endDate+'</span></p>'+
            '                                        <p><span>Ticket price: '+result[i].ticketPrice+'</span></p>\n' +
            '<p>Location: <span>'+result[i].location+'</span></p>'+
            '                                    </div>\n' +
            '                                </div>\n' +
            '                            </div>' +
            '<hr />');
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


// })