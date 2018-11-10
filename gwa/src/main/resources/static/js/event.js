
$(document).ready(function () {
    var currentStatus = $( "#cboStatus option:selected").val();

    var currentSortType = $("#cbSortType option:selected").val();

    var currentPage = 1;
    var totalPage = 5;
    var $pagination = $("#pagination-event");
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
            getEventData();
            // if(isSearch){
            //     myTradeData = searchMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType,searchValue);
            // }else {
            //     myTradeData = loadMyTradeData(myTradeAccountId,currentTabSelected,currentPage,currentSortType);
            // }
            // renderRecord();
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
    getEventData();
    $pagination.twbsPagination('destroy');
    if (totalPage > 1){
        $pagination.twbsPagination($.extend({}, defaultPaginationOpts, {
            totalPages: totalPage
        }));
    }
    function getEventData() {
        $.ajax({
            type : "POST",
            url : "http://localhost:8080/gwa/api/event/getEventByStatusAndPage",
            data : {
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

                for (var i in data){
                    appendResult(data[i]);
                }


            },
            error : function(e) {
                alert("No event with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }



    function appendResult(result){
        var article = $('<div class="single-blog-post featured-post d-flex">\n' +
            '                                <div class="post-data">\n' +
            '                                    <a href="#" class="post-catagory">EVENT</a>\n' +
            '                                    <div class="post-meta">\n' +
            '                                        <a class="post-title" href="/gwa/event/detail?id=' + result.id +'">\n' +
            '                                            <h6>' + result.title + '</h6>\n' +
            '                                        </a>\n' +
            '<p class="post-date"><span>From: '+result.startDate+'</span> to <span>'+result.endDate+'</span></p>'+
            '                                        <p><span>Ticket price: '+result.ticketPrice+'</span></p>\n' +
            '<p>Location: <span>'+result.location+'</span></p>'+
            '                                    </div>\n' +
            '                                </div>\n' +
            '                            </div>' +
            '<hr />');

        $('#search-result').append(article);
    }
    function getAllEvent() {

        var displayDiv = document.getElementById("search-result");

        $.ajax({
            type : "POST",
            url : "http://localhost:8080/gwa/api/event/getAllEvent",
            success : function(result, status) {
                console.log(result);
                console.log(status);
                for (var i in result){
                    appendResult(result[i]);
                }

            },
            error : function(e) {
                alert("No event with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }
    $("#btnSearch").click(function (event) {
        event.preventDefault();
        var searchDiv = document.getElementById("search-result");
        while (searchDiv.firstChild) {
            searchDiv.removeChild(searchDiv.firstChild);
        }
        // var searchValue = document.getElementById("txtSearch").value;
        searchEv();
    });

    function searchEv() {
        var searchValue = $("#txtSearch").val();
        var displayDiv = document.getElementById("search-result");

        console.log("searchvalue: "+searchValue);
        console.log("sorttype: "+currentSortType);
        $.ajax({
            type : "POST",
            url : "http://localhost:8080/gwa/api/event/searchEventByStatusAndPage",
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

                for (var i in data){
                    appendResult(data[i]);
                }


            },
            error : function(e) {
                alert("No event with matching title found!");
                console.log("ERROR: ", e);
            }
        });
    }



})