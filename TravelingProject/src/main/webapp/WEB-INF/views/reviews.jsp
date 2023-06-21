<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>place</title>
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/css/review.css">
</head>
<body>
<!-- 메인헤더 -->
<%@ include file="./header.jsp" %>


    <div>지역 설정하는 select    //    검색창 들어가는 자리</div>

<!-- 정렬 카테고리 -->
    <div class="sort">
        <select>
          <option value="best">인기순</option>
          <option value="ascorder">오름차순</option>
          <option value="descorder">내림차순</option>
        </select>
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