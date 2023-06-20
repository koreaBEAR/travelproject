<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Random" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<link href="https://cdn.jsdelivr.net/gh/sunn-us/SUITE/fonts/static/woff2/SUITE.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
<link rel="stylesheet" type="text/css" href="/css/main.css">
<title>Main</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
</head>
<style>
input.img-button {
    background: url("img/button_search.jpg") no-repeat;
    border: none;
    width: 45px;  
    height: 45px; 
    cursor: pointer;
    background-size: cover; 
}

.custom-close-button {
    float: right;
    margin-top: -10px; 
    margin-right: -250px; /* Adjust this value to move the button further to the right */
}

 html { 
  scroll-behavior: smooth; 
} 

</style>
<body>
<!-- 헤더 -->
<%@ include file="./header.jsp" %>

<!-- 배너 -->
<section class="banner">
    <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel" data-interval="3000">
        <ol class="carousel-indicators">
            <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
            <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
            <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
            <li data-target="#carouselExampleIndicators" data-slide-to="3"></li>
        </ol>
        <div class="carousel-inner">
            <%!
            String[] images = {"img1.jpg", "img3.jpg", "img4.jpg", "img6.jpg", "img8.jpg", "img10.jpg", "img11.jpg", "img12.jpg", "img13.jpg", "img14.jpg", "img2.jpg", "img5.jpg", "img7.jpg", "img9.jpg"};
            String[] captions = {"𝑮𝑨𝑷𝒀𝑬𝑶𝑵𝑮", "𝑮𝒀𝑬𝑶𝑵𝑮𝑱𝑼", "𝑮𝑨𝑵𝑮𝑵𝑬𝑼𝑵𝑮", "𝐁𝐔𝐒𝐀𝐍", "𝐒𝐄𝐎𝐔𝐋", "𝐒𝐄𝐎𝐔𝐋", "𝒀𝑬𝑶𝑺𝑼", "𝑱𝑬𝑱𝑼", "𝑷𝑶𝑯𝑨𝑵𝑮", "𝑮𝑬𝑶𝑱𝑬𝑫𝑶", " 𝑮𝑨𝑵𝑮𝑵𝑬𝑼𝑵𝑮", "𝑮𝒀𝑬𝑶𝑵𝑮𝑱𝑼", "𝐁𝐔𝐒𝐀𝐍", "𝐒𝐄𝐎𝐔𝐋"};
            Random rand = new Random();
            %>
            <% for (int i = 0; i < images.length; i++) { %>
            <div class="carousel-item<%= (i == 0) ? " active" : "" %>">
                <%
                String image = images[i];
                String caption = captions[i];
                %>
                <img src="img/banner/<%= image %>" class="d-block w-100" alt="Description of image" />
                <div class="carousel-caption d-none d-md-block">
                    <h5><%= caption %></h5>
                </div>
            </div>
            <% } %>
        </div>
        <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
</section>
</br>
<div id="target"></br></br>
    <h1>𝑾𝒉𝒆𝒓𝒆 𝒂𝒓𝒆 𝒚𝒐𝒖 𝒕𝒓𝒂𝒗𝒆𝒍𝒊𝒏𝒈 𝒕𝒐?</h1>
</div>
<!-- 검색창 -->
<div class="serachbox">
    <input type="text" id="keyword" name="keyword" placeholder="Search..." />
    <input type="button" id="btncitysearch" class="img-button">
</div>

<!-- 정렬 카테고리 -->
</br>
<div class="sort">
    <select id="Selectsort">
        <option value="인기순">인기순</option>
        <option value="오름차순">오름차순</option>
        <option value="내림차순">내림차순</option>
    </select>
</div>

<!-- 관광명소 div -->
<section class="tourlist" id="tourlist"></section>

<div class="moveH1"><button type="button" id=btnChoice>여행지 선택화면으로 돌아가기</button></div>

<!-- Modal -->
<div id="myModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content -->
        <div class="modal-content">
            <!-- Content dynamically added here -->
        </div>
    </div>
</div>
<!-- 푸터 -->
<%@ include file="./footer.jsp" %>
</body>
    <script src="https://code.jquery.com/jquery-latest.js"></script>
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script>
var city_num = '';
$(document)
.ready(function() {
    cityBest();

    $('#Selectsort').on('change', function() {
        var sortValue = $(this).val();
        if (sortValue === "오름차순") {
            cityAsc();
        } else if (sortValue === "내림차순") {
            cityDesc();
        } else if (sortValue === "인기순") {
            cityBest();
        }
    });

    $('#keyword').keydown(function(event) {
        if (event.keyCode === 13) {
            SearchCity();
        }
    });

    $('#btncitysearch').click(function() {
        SearchCity();
    });

    // Event delegation for dynamically created elements
    $('#tourlist').on('click', '.div1-1', function() {
        city_num = $(this).find('#city_num').val();
        console.log(city_num);
        $.ajax({
            url: '/getCityDetails/' + city_num,
            type: 'post',
            dataType: 'json',
            success: function(data) {
                $('#myModal .modal-content').empty();
                for (var i = 0; i < data.length; i++) {
                    var str = '<div class="modal-header">';
                    str += '<h4 class="modal-title">' + data[i]['city_engname'] + '</h4>';
                    str += '<h3 class="modal-title">' + data[i]['city_name'] + '</h3>'; // Add city_name
                    str += '<button type="button" class="close" data-dismiss="modal">×</button>';
                    str += '</div>';
                    str += '<div class="modal-body">';
                    str += '<img src="/img/' + data[i]['city_img'] + '" alt="City Image">';
                    str += '<i class="fa-regular fa-calendar-check fa-2xl" style="color: #050505;"></i>&nbsp;'+ data[i]['city_count'];
                    str += '<p class= "modalcontent">' + data[i]['city_content'] + '</p>';
                    str += '</div>';
                    str += '<div class="modal-footer center-button">';
                    str += '<button type="button" id="btnCreate" class="btn btn-primary">일정생성하기</button>';
                    str += '</div>';
                    $('#myModal .modal-content').append(str);
                }
                $('#myModal').modal('show');
            }
        });
    });
	
    $(document).on('click', '#btnCreate', function(){
        document.location="/schedulecreate/"+city_num;
    });
    
    $('#myModal').on('hidden.bs.modal', function() {
        $('#myModal .modal-content').empty();
    });
    
    
});

// 여행지 선택 이동 h1
const $topBtn = document.querySelector("#btnChoice");

$topBtn.onclick = () => {
const h1 = document.getElementById('target');
h1.scrollIntoView({behavior: "smooth"});

setTimeout(() => {
 window.scrollBy(0, -150);  
}, 500);  
}

function cityAsc() {
    $.ajax({
        url: '/cityasc',
        type: 'post',
        dataType: 'json',
        success: function(data) {
            $('#tourlist').empty();
            for (var i = 0; i < data.length; i++) {
                var str = '<div class="div1-1">';
                str += '<input type="hidden" id="city_num" name="city_num" value="' + data[i]['city_num'] + '">';
                str += '<div class="div_img"><img src="/img/' + data[i]['city_img'] + '" alt="City Image"></div>';
                str += '<p><span class="bold-text">' + data[i]['city_engname'] + '</span></br>' + data[i]['city_name'] + '</p></div>';

                $('#tourlist').append(str);
            }
        }
    });
}

function cityDesc() {
    $.ajax({
        url: '/citydesc',
        type: 'post',
        dataType: 'json',
        success: function(data) {
            $('#tourlist').empty();
            for (var i = 0; i < data.length; i++) {
                var str = '<div class="div1-1">';
                str += '<input type="hidden" id="city_num" name="city_num" value="' + data[i]['city_num'] + '">';
                str += '<div class="div_img"><img src="/img/' + data[i]['city_img'] + '" alt="City Image"></div>';
                str += '<p><span class="bold-text">' + data[i]['city_engname'] + '</span></br>' + data[i]['city_name'] + '</p></div>';

                $('#tourlist').append(str);
            }
        }
    });
}

function cityBest() {
    $.ajax({
        url: '/citybest',
        type: 'post',
        dataType: 'json',
        success: function(data) {
            $('#tourlist').empty();
            for (var i = 0; i < data.length; i++) {
                var str = '<div id="div1-1" class="div1-1">';
                str += '<input type="hidden" id="city_num" name="city_num" value="' + data[i]['city_num'] + '">';
                str += '<div class="div_img"><img src="/img/' + data[i]['city_img'] + '" alt="City Image"></div>';
                str += '<p><span class="bold-text">' + data[i]['city_engname'] + '</span></br>' + data[i]['city_name'] + '</p></div>';

                $('#tourlist').append(str);
            }
        }
    });
}

function SearchCity() {
    var keyword = $('#keyword').val();
    $.ajax({
        url: '/searchcity',
        type: 'post',
        dataType: 'json',
        data: {
            keyword: keyword
        },
        success: function(response) {
            var data = response; // Access the response directly
            $('#tourlist').empty();
            for (var i = 0; i < data.length; i++) {
                var str = '<div class="divtbl1"><div class="div1-1">';
                str += '<input type="hidden" id="city_num" name="city_num" value="' + data[i]['city_num'] + '">';
                str += '<div class="div_img"><img src="/img/' + data[i]['city_img'] + '" alt="City Image"></div>';
                str += '<p><span class="bold-text">' + data[i]['city_engname'] + '</span></br>' + data[i]['city_name'] + '</p></div></div>';
                $('#tourlist').append(str);
            }

        }
    });
}

</script>
</html>
