<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ page session="true" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/gh/sunn-us/SUITE/fonts/static/woff2/SUITE.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/static/css/header.css">
<title>Manage_member</title>
</head>
<style>

.nav {
	display: flex;
	position: fixed;
	justify-content: space-evenly;
	flex-direction: row;
	height: 80px;
	width: 100%;
	background-color: #fff;
	box-shadow: 0 1px 0px rgba(0, 0, 0, 0.1);
}

.nav h2 {
	margin: 0;
	padding: 10px;
	margin-top: 20px;
	margin-bottom: 30px;
}

.nav a {
	text-decoration: none;
	color: #000;
}

.section {
	margin-top: 100px;
	padding: 20px;
}

/* 폼 스타일 */
form {
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom: 20px;
	margin-top: 100px;
}

/* 셀렉트 박스 스타일 */
#searchForm {
	padding: 8px;
	font-size: 16px;
	border-radius: 4px;
	border: 1px solid #ccc;
	margin-right: 10px;
}

/* 키워드 입력 필드 스타일 */
#keyword {
	padding: 8px;
	font-size: 16px;
	border-radius: 4px;
	border: 1px solid #ccc;
	margin-right: 10px;
}

/* 검색 버튼 스타일 */
#btnsearch {
	padding: 8px 16px;
	font-size: 16px;
	border-radius: 4px;
	background-color: #000;
	color: white;
	border: none;
	cursor: pointer;
}

/* 검색 버튼 호버 효과 */
#btnsearch:hover {
	background-color: #213555;
}

#displayCount {
	padding: 4px;
	font-size: 16px;
	border-radius: 4px;
	border: 1px solid #ccc;
	margin-left: 15px;
}

/* 페이징 컨테이너 스타일 */
.pageIndex {
	display: flex;
	justify-content: center;
	margin-top: 20px;
}

/* 링크 스타일 */
.btn {
	padding: 8px 12px;
	font-size: 16px;
	border-radius: 4px;
	border: 1px solid #ccc;
	margin-right: 5px;
	text-decoration: none;
}

#tblmember {
	border-collapse: collapse;
	width: 100%;
	margin: 10px;
}

table tr[name=member]:hover {
	background-color: #DDE6ED;
	cursor: pointer;
}

th, td {
	text-align: center;
	padding: 8px;
}

th {
	background-color: black;
	color: #FFFFFF;
	font-size: 16px;
}

td {	
	height: 10px;
	color: #333333;
	font-size: 16px;
	border: 1px solid #D8C4B6;
}

#btnDelete {
	background-color: #000;
	color: #ffffff;
	border: none;
	padding: 8px 16px;
	font-size: 10px;
	font-weight: bold;
	text-transform: uppercase;
	border-radius: 4px;
	cursor: pointer;
	transition: background-color 0.3s ease;
}

#btnDelete:hover {
	background-color: #213555;
}

a.btn {
	text-decoration: none;
	color: black;
}

a.index {
	text-decoration: none;
}

.bold {
	font-weight: bold;
}
</style>
<body>	
<%@ include file="./header.jsp" %>
<%-- <c:if test="${sessionScope.member_class == 'admin'}">   --%>
	
	<div class="nav">
	<h2><a href ="/manage_member" class="index">회원관리</a></h2>
	<h2><a href ="/manage_place" class="index">업체관리</a></h2>
	<h2><a href ="/manage_help" class="index">문의관리</a></h2>
	</div>
	
	<div class="section">
	<form method="get" action="/member/search">
		<select name=type id="searchForm" >
			<option value="member_name||member_id||member_nickName||member_email||member_class">전체</option>
			<option value="member_name">이름</option>
			<option value="member_id">아이디</option>
			<option value="member_nickName">닉네임</option>
			<option value="member_email">이메일</option>
			<option value="member_class">회원등급</option>
		</select> 
		<input type="text" id="keyword" name="keyword"> 
		<input type="submit" id="btnsearch" value=검색>
	</form>
	
	
	<select id="displayCount" onchange="CountChange()">
	<option value="10" selected="selected">10</option>
	<option value="20">20</option>
	<option value="30">30</option>
	</select>	

	<span id="errorMessage"></span>
	
	<table border=1 id="tblmember" >
		<tr>
			<th>NO</th>
			<th>아이디</th>
			<th>이름</th>
			<th>닉네임</th>
			<th>성별</th>
			<th>생년월일</th>
			<th>모바일</th>
			<th>이메일</th>
			<th>등급</th>
			<th>삭제</th>
		</tr>
		<c:forEach items="${memberList}" var="m">
			<tr name=member>
				<td>${m.member_seq}</td>
				<td>${m.member_id}</td>
				<td>${m.member_name}</td>
				<td>${m.member_nickName}</td>
				<td>${m.member_gender}</td>
				<td>${m.member_birth}</td>
				<td>${m.member_mobile}</td>
				<td>${m.member_email}</td>
				<td>${m.member_class}</td>
				<td><button id="btnDelete" value="${m.member_seq}">X</button></td>
			</tr>
		</c:forEach>
	</table>
	
	
	<div class="pageIndex"> <!--페이징 -->	
		<a href="?pageNo=1&amount=${amount}" class="btn"><<</a>&nbsp
		<c:if test="${pageNo > 1}">
			<a href="?pageNo=${pageNo - 1}&amount=${amount}" class="btn"><</a>
		</c:if>
		<c:forEach var="i" begin="1" end="${endPage}">
			<a href="?pageNo=${i}&amount=${amount}" class="btn ${pageNo == i ? 'bold' : ''}">${i}</a>
		</c:forEach>
		<c:if test="${pageNo < endPage}">
			<a href="?pageNo=${pageNo + 1}&amount=${amount}" class="btn">></a>&nbsp
	  	</c:if>
		<a href="?pageNo=${endPage}&amount=${amount}" class="btn">>></a>
	</div>
	
</div>	
<%-- </c:if>
<c:if test="${sessionScope.member_class != 'admin'}">
    <%@include file="manage_error.jsp" %>
</c:if> --%>
	
</body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
$(document)
.on('click', '#btnDelete', function() {
	let member_seq = $(this).val();
	//console.log(member_seq);
	if (!confirm('정말로 삭제하시겠습니까?')) {
		return;
	}
	$.ajax({
		url : '/member_delete',
		type : 'post',
		dataType : 'text',
		data : { member_seq : member_seq},
		success : function(data) {
			document.location = "/manage_member";
		}
	})
})

.on("click",'#btnsearch', function(){
	if (!$('#searchForm').find('option:selected').val()) {
		alert("검색 기준을 선택하세요.");
		return false;
	}
	if ($("input[name='keyword']").val() == '') {
		alert("키워드를 입력하세요.");
		return false;
	}
	$('#searchForm').submit();
	})
	
.ready(function(){
   if('${errorMessage}'!='') {
	   $('#errorMessage').text('${errorMessage}');
	   $("#errorMessage").css("color", "grey");
   }
})	

$(document)
.ready(function() {
	  var urlParams = new URLSearchParams(window.location.search);
	  var amount = urlParams.get("amount");
	  if (amount) {
	    $("#displayCount").val(amount);
	  }
	});

function CountChange() {
	var selectedAmount = $("#displayCount option:selected").val();
	var url = new URL(window.location.href);
	url.searchParams.set("amount", selectedAmount);
	window.location.href = url.href;
}


</script>

</html>