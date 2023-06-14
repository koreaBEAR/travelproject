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
<title>Manage_place</title>
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

#tblplace {
	border-collapse: collapse;
	width: 100%;
	margin: 10px;
}

table tr[name=place]:hover {
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

#btnDelete {
	background-color: #000;
	color: #ffffff;
	border: none;
	padding: 6px 16px;
	font-size: 12px;
	font-weight: bold;
	border-radius: 4px;
	cursor: pointer;
	float: right;
}

.btnInsert {
	background-color: #000;
	color: #ffffff;
	border: none;
	text-decoration: none;
	padding: 6px 16px;
	font-size: 15px;
	font-weight: bold;
	border-radius: 4px;
	cursor: pointer;
	float: right;
}
</style>
<%@ include file="./header.jsp" %>
<body>	

	<div class="nav">
	<h2><a href ="/manage_member" class="index">회원관리</a></h2>
	<h2><a href ="/manage_place" class="index">업체관리</a></h2>
	<h2><a href ="/manage_help" class="index">문의관리</a></h2>
	</div>

	<div class="section">
	<form method="get" action="/place/search">
		<select name=type id="searchForm" >
			<option value="b.city_name|| c.place_category_name|| a.place_name|| a.place_address|| a.place_tel">전체</option>
			<option value="b.city_name">지역</option>
			<option value="c.place_category_name">분류</option>
			<option value="a.place_name">업체명</option>
			<option value="a.place_address">주소</option>
			<option value="a.place_tel">전화</option>
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
	<button id="btnDelete" value=>삭제</button>
	
	<table border=1 id="tblplace">
		<tr>
			<th><input type="checkbox" id="checkAll" class="checkGroup"></th>
			<th>NO</th>
			<th>지역</th>
			<th>분류</th>
			<th>업체명</th>
			<th>주소</th>
			<th>전화</th>
		</tr>
		<c:forEach items="${placeList}" var="p">
			<tr name=place>
				<td><input type="checkbox" id="checkbox" class="checkGroup" value=${p.place_seq} ></td>
				<td>${p.place_seq}</td>
				<td>${p.city_name}</td>
				<td>${p.place_category_name}</td>
				<td>${p.place_name}</td>
				<td>${p.place_address}</td>
				<td>${p.place_tel}</td>
			</tr>
		</c:forEach>
	</table>
	
	<a href='/place_insert' class="btnInsert">업체등록</a>
	
	
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

</body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.3.0/jquery.form.min.js"></script>
<script>
$(document)
.on('click', '#btnDelete', function() {
	var placeSeqs = [];            //console.log(placeSeqs);
    $('input.checkGroup:checked').each(function() {
        placeSeqs.push($(this).val());
    });

    if (placeSeqs.length === 0) {
        alert('선택된 항목이 없습니다.');
        return;
    }
	if (!confirm('정말로 삭제하시겠습니까?')) {
		return;
	}
	
	str = ''
	for(i=0; i<placeSeqs.length; i++){
		str += ","+placeSeqs[i];
	}
    $.ajax({
        url: '/place_delete',
        type: 'post',
        dataType: 'text',
        data: { placeSeqs : str }, 
        success: function(data) { 
            
        },
        complete: function() { 
            document.location = "/manage_place";
        }
        
    });
})


.on('click', '#checkAll', function() {
	$(".checkGroup").prop("checked", this.checked);
	
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

.on('click', '#tblplace tr:gt(0) td:not(:first-child)', function() {
   let place_seq = $(this).closest('tr').find('td').eq(1).text();
   window.location.href = "/place_view/" + place_seq;
});

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