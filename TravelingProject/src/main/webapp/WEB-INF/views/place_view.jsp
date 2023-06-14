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
<title>Place_view</title>
</head>
<style>
 .section {
    margin-top: 40px;
    padding: 20px;   
  }

  table {
    width: 90%;
    border-collapse: collapse;
  }

  td {
    padding: 8px;
    border: 1px solid #ccc;
  }

  input[type="text"],
  textarea {
    width: 90%;
    padding: 8px;
    border-radius: 4px;
    border: 1px solid #ccc;
  }

  input[type="submit"],
  input[type="button"] {
    padding: 8px 16px;
    font-size: 16px;
    border-radius: 4px;
    background-color: #000;
    color: #fff;
    border: none;
    cursor: pointer;
    margin-top: 10px;
  }

  .uploaded-image {
    display: block;
    width: 200px;
    height: auto;
    margin-top:
</style>
<body>
<%@ include file="./header.jsp" %>
<div class="section">
<table>
	<tr>
		<input type=hidden name=place_seq id=place_seq value="${p.place_seq}">
		<td>지역</td>
		<td><input type=text name=city id=name value="${p.city_name}" readonly></td>			
	</tr>
	<tr>
		<td>분류</td>
		<td><input type=text name=place id=place value="${p.place_category_name}" readonly></td>	
	</tr>
	<tr>		
		<td>업체명</td>
		<td><input type=text name=name id=name value="${p.place_name}" readonly></td>		
	</tr>
	<tr>		
		<td>주소</td>
		<td><input type="text" name="address" id="address" value="${p.place_address}" readonly><br></td>	
	</tr>
	<tr>		
		<td>전화</td>
		<td><input type=text name=tel id=tel value="${p.place_tel}" readonly></td>		
	</tr>
	<tr>		
		<td>영업시간</td>
		<td><input type=text name=open id=open value="${p.place_open}" readonly></td>		
	</tr>
	<tr>		
		<td>설명</td>
		<td><textarea name=content id=content readonly>${p.place_content}</textarea></td>		
	</tr>
	<tr>
			<td>이미지보기</td>
			<td id="imageContainer">
				<c:forEach items="${fn:split(p.place_img, ',')}" var="imageUrl">
					<img src="${imageUrl}" class="uploaded-image">
				</c:forEach>
			</td>  
		</tr>
	<tr>
		<td colspan=2 align=center><input type=submit value=수정 id=btnUpdate>
		<input type=button value=삭제 id=btnDelete>
		<input type=button value=목록보기 id=btnList></td>
	</tr>
</table>
</div>
</body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
$(document)
.on('click','#btnList',function() {
	document.location = "/manage_place";	
})
.on('click','#btnDelete',function() {
	let place_seq=$('#place_seq').val();
	document.location='/delete_place/'+place_seq;
})
.on('click','#btnUpdate',function() {
	let place_seq=$('#place_seq').val();
	document.location='/place_update/'+place_seq;
})

</script>
</html>