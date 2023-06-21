<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>place</title>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/css/review.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
</head>
<style>
span[name='pageNum'] {
	cursor: pointer;
	padding: 10px;
	border: 1px solid #a14a1c;
	margin: 0 4px;
	color: #a14a1c;
	text-decoration: none;
	transition: background-color .3s;
	border-radius: 20px;
}

span[name='pageNum']:hover {
	font-weight: bold;
	background-color: #ddd;
}

span[name='pageNum'].current {
	font-weight: bold;
	background-color: #ddd;
}

.h1 {
	margin-top: 13%;
	text-align: center;
	font-size: 28px;
}
/* 검색창 */
.placeSelect {
	display: flex;
	justify-content: center;
	align-items: center;
}

#Placekeyword {
	height: 45px;
	width: 45%;
	/* You can adjust this to change the width of the input element */
}

/* 맛집,숙소,명소 네비 */
.nav-tab {
	display: flex;
	justify-content: center;
}

.nav-tab ul {
	list-style-type: none;
	margin: 0;
	padding: 0;
	overflow: hidden;
}

.nav-tab li {
	float: left;
	border: 1px solid #ddd;
}

.nav-tab li a {
	display: block;
	color: black;
	text-align: center;
	padding: 20px 20px;
	text-decoration: none;
}

.nav-tab li a:hover {
	background-color: #ddd;
}

.nav-tab li .active {
	background-color: #4CAF50;
	color: white;
}

.dropdown {
	display: flex;
	justify-content: flex-end;
	margin-right: 5%;
}
</style>
<body>
<!-- 메인헤더 -->
<%@ include file="./header.jsp" %>

	<div class="h1"><h1>𝑃𝑙𝑎𝑐𝑒 𝑅𝑒𝑣𝑖𝑒𝑤</h1></div>
	
<!--리뷰 업체검색 -->	
    <div class="placeSelect">
    <input type="text" id="Placekeyword" name="Placekeyword" placeholder="Search..." />   
    </div>
    
<!-- 지역별 네비 탭 -->    
<div class="nav-tab">
    <ul>
        <li class="active"><a href="#tab1">맛집</a></li>
        <li><a href="#tab2">숙소</a></li>
        <li><a href="#tab3">명소</a></li>
    </ul>
</div>

<!-- 정렬 카테고리 -->
	<div class="dropdown">
		<button class="btn btn-secondary dropdown-toggle" type="button"
			data-bs-toggle="dropdown" aria-expanded="false">지역별 조회하기</button>
		<ul class="city dropdown-menu">
			<li><a class="dropdown-item" href="#">가평</a></li>
			<li><a class="dropdown-item" href="#">강릉</a></li>
			<li><a class="dropdown-item" href="#">거제/통영</a></li>
			<li><a class="dropdown-item" href="#">경주</a></li>
			<li><a class="dropdown-item" href="#">대전</a></li>
			<li><a class="dropdown-item" href="#">부산</a></li>
			<li><a class="dropdown-item" href="#">서울</a></li>
			<li><a class="dropdown-item" href="#">여수</a></li>
			<li><a class="dropdown-item" href="#">인천</a></li>
			<li><a class="dropdown-item" href="#">전주</a></li>
			<li><a class="dropdown-item" href="#">제주</a></li>
			<li><a class="dropdown-item" href="#">제천</a></li>
		</ul>
		<button class="btn btn-secondary dropdown-toggle" type="button"
			data-bs-toggle="dropdown" aria-expanded="false">정렬 조회</button>
		<ul class="city dropdown-menu">
			<li><a class="dropdown-item" href="#">인기순</a></li>
			<li><a class="dropdown-item" href="#">오름차순</a></li>
			<li><a class="dropdown-item" href="#">내림차순</a></li>
		</ul>
	</div>
	
	<!--     modal -->
<%@ include file="./placeModal.jsp" %>
<!-- 관광명소 div -->
   <section class="placeList" id="placeList"></section>
    <div class=viewDivFooter>
    <span name=pageNum>1</span>
    </div>
<!-- 푸터 -->
<%@ include file="./footer.jsp" %> 
    </body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="http://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script src="js/reviews.js"></script>
</html>