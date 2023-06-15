<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/gh/sunn-us/SUITE/fonts/static/woff2/SUITE.css" rel="stylesheet">
<title>문의게시판 상세페이지</title>
<link rel="stylesheet" href="/css/contactdetail.css">
</head>
<style>
</style>
</head>
<body>
<!-- 헤더 -->
<%@ include file="./header.jsp" %>

  <table id= detaillist class="tbldetail">
   <thead class="tbldetail_head">
   <tr>
   <th colspan="3"><h1>게시글 보기</h1></th>
   </tr>
   </thead>
   <tbody class="tbldetail_body" id=detailtbl>
      <input type="hidden" name="help_seq" id="help_seq" value="${details.help_seq}" readonly></tr>
      <tr><td class=tblcategory>카테고리</td><td colspan="2">${details.help_category}</td></tr>
      <tr><td class=tblcategory>제목</td><td colspan="2">${details.help_title}</td></tr>               
      <tr><td class=tblcategory>작성자</td><td colspan="2">${details.member_nickname}</td></tr>
      <input type="hidden" name="member_id" id="member_id" value="${details.member_id}" readonly>
      <tr><td class=tblcategory>내용</td><td colspan="2">${details.help_content}</td></tr>                
    
      <tr id="tblimg">
  		<td class="tblcategory">이미지</td>
  		<td colspan="2">
    	<c:forEach var="img" items="${fn:split(details.help_img, ',')}">
      		<img src="/img/contact/${img}" id="img" alt="Image" width="200" height="150" />
    	</c:forEach>
  		</td>
	  </tr>
        <tr><td class=tblcategory>작성일자</td><td colspan="2">${details.help_created}</td></tr>          
   </tbody> 
</table>

</br>
<div class='btnsort'>
<button class=boardBtn type="button" id=btnModify>수정</button>
<button class=boardBtn id=btnRemove>삭제</button>
<button class=boardBtn id=btnback>목록가기</button>
</div>
  <h3>Comment</h3>
  <div class="comment">
    <p><span class="author">관리자</span></p>
    <table>
    <tr><td>${details.help_comment}</td></tr>
    </table>
  </div>
<!-- 푸터 -->
<%@ include file="./footer.jsp" %>    
<script src="https://code.jquery.com/jquery-latest.js"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script>
$(document)

.ready(function(){

	  if ($('#member_id').val() == '<%= session.getAttribute("id") %>') {
	    $('#btnModify, #btnRemove').show();
	  } else {
	    $('#btnModify, #btnRemove').hide();
	  }
	
	  
	  let imgs = "${details.help_img}";
	  if(imgs == null || imgs == ''){
	   $('#tblimg').hide();
	  }
 	  
})

// 목록가기
.on('click','#btnback',function(){
   document.location='/contact';
})

.on('click','#btnModify', function(){
	document.location='/updatelist/'+$('#help_seq').val();
})


//리뷰삭제
.on('click','#btnRemove' ,function(){
  var isDelete = confirm('해당 게시물을 삭제하시겠습니까?');

  if(isDelete){
    $.ajax({url:'/contactdelete',type:'post', dataType:'text',
      data:{help_seq:$('#help_seq').val()}, 
      beforeSend:function(){
        if($('#help_seq').val()==''){
          alert('삭제할 게시물이 없습니다.');
          return false;
        }
      },
      success:function(data){ 
        document.location="/contact";
      }
    });
  }
});
</script>
</body>
</html>