<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ page session="true" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/gh/sunn-us/SUITE/fonts/static/woff2/SUITE.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/static/css/header.css">
<title>Manage_help</title>
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
	margin-top: 50px;
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

#tblhelp {
	border-collapse: collapse;
	width: 100%;
	margin: 10px;
}

table tr[name=help]:hover {
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

/* 답변 버튼 스타일 */
#btnAnswer {
	padding: 8px 16px;
	font-size: 14px;
	border-radius: 4px;
	background-color: #000;
	color: white;
	border: none;
	cursor: pointer;
}

/* 답변 버튼 호버 효과 */
#btnAnswer:hover {
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

.uploaded-image {
	width: 100px; /* Adjust the width as desired */
	height: auto;
	display: inline-block;
	margin-right: 10px; /* Adjust the margin as desired */
}

#helpDlg {
	display: none;
	position: relative;
	background-color: #fff;
	padding: 20px;
	border: 1px solid #ccc;
	border-radius: 4px;
	width: 500px;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
	position: relative;
}

#helpDlg textarea {
	width: 100%;
	height: 100px;
	padding: 5px;
	border: 1px solid #ccc;
	border-radius: 4px;
	resize: vertical;
}

#helpDlg button {
	margin-top: 10px;
	padding: 8px 16px;
	background-color: #000;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	margin-left: 10px;
}

#helpDlg button:hover {
	background-color: #213555;
}

#helpDlg div:last-child {
	display: flex;
	justify-content: flex-end;
	margin-top: 10px;
	width: 100%;
}

.ui-dialog-titlebar-close {
  background-color: #213555;
  border-radius: 4px;
  position: absolute;
  opacity: 0.8;
  right: 0;
  color: white;
}
</style>
<body>
<%@ include file="./header.jsp" %>

	<div class="nav">
	<h2><a href ="/manage_member" class="index">회원관리</a></h2>
	<h2><a href ="/manage_place" class="index">업체관리</a></h2>
	<h2><a href ="/manage_help" class="index">문의관리</a></h2>
	</div>
	
	<div class="section">
	<form method="get" action="/help/search">
		<select name=type id="searchForm" >
			<option value="help_category||help_title||member_id||help_content">전체</option>
			<option value="help_category">카테고리</option>
			<option value="help_title">제목</option>
			<option value="member_id">아이디</option>
			<option value="help_content">내용</option>			
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
	
	<table border=1 id="tblhelp">
		<tr>
			<th>NO</th>
			<th>카테고리</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>상태</th>
			<th style=display:none>내용</th>
			<th style=display:none>이미지</th>
			<th>답변</th>
		</tr>
		<c:forEach items="${helpList}" var="h">
			<tr name=help>
				<td>${h.help_seq}</td>
				<td>${h.help_category}</td>
				<td>${h.help_title}</td>
				<td>${h.member_id}</td>
				<td>${h.help_created}</td>
				<td>${h.help_complete}</td>
				<td style=display:none>${h.help_content}</td>
				<td style=display:none>${h.help_img}</td>
				<c:if test ="${h.help_complete == '대기중'}">
				<td><button id="btnAnswer" value="${h.help_seq}">답변</button></td>
				</c:if>
				<c:if test ="${h.help_complete != '대기중'}">
				<td></td>
				</c:if>
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

<div id="helpDlg" style="display:none;">
	<div id="help_category"></div>
	<div id="help_title"></div>
	<div id="member_id"></div>
	<div id="help_created"></div>
	<div id="help_content"></div>
	<div id="help_img"></div>
    <div><textarea name=comment id=comment></textarea></div>
    <div><button id="btnUpload" >등록</button><button id="btnCancel">취소</button></div>   
</div> 
 

</body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script>
$(document)
.on('click','#btnAnswer',function(){
	let help_seq =$(this).val();
	$('#btnUpload').val(help_seq); 

	let tr = $(this).closest('tr'); 
    let help_category = tr.find('td:eq(1)').text(); 
    let help_title = tr.find('td:eq(2)').text(); 
    let member_id = tr.find('td:eq(3)').text(); 
    let help_created = tr.find('td:eq(4)').text(); 
    let help_content = tr.find('td:eq(6)').text(); 
	let help_img = tr.find('td:eq(7)').text(); 
	let images = help_img.split(',');
 
    $('#help_category').text(help_category);
    $('#help_title').text(help_title);
    $('#member_id').text(member_id);
    $('#help_created').text(help_created);
    $('#help_content').text(help_content);
    let imageUrl = '';
    for (let i = 0; i < images.length; i++) {
    	imageUrl += '<img src="/img/contact/' + images[i] + '" class="uploaded-image">';
    }
    $('#help_img').html(imageUrl);
    //$('#help_img').html('<img src="' + help_img + '" class="uploaded-image">');
	console.log(imageUrl);	 
	//console.log(help_seq);
    $('#helpDlg').dialog({
        'beforeClose':function(e,u){
        	//$('#comment').val('');
    },
        title:'',
        closeText: "X",
        modal:true,
        width:'500px'
    })     
})

.on('click', '#btnCancel', function() {
    $('#helpDlg').dialog('close');
})


.on('click', '#btnUpload', function() {
	let help_seq =$(this).val();
	let help_complete ='답변완료';
	$.ajax({
		url : '/comment_insert',
		type : 'post',
		dataType : 'text',
		data : { comment:$('#comment').val(),help_complete :help_complete, help_seq : help_seq },
        beforeSend: function() {
        	 if ($('#comment').val() == '') {
        	        alert('답변을 작성해주세요');
        	        return false; 
        	    }},
		success : function(data) {	
			document.location = "/manage_help";	
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