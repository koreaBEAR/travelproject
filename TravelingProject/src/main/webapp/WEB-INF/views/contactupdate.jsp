<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/gh/sunn-us/SUITE/fonts/static/woff2/SUITE.css" rel="stylesheet">
	<title>게시판 글수정</title>
	<link rel="stylesheet" href="/css/contactwrite.css">
</head>
<body>
<!-- 헤더 -->
<%@ include file="./header.jsp" %>

<form action="contactupate" method="post" enctype="multipart/form-data"> 
<table id=updatelist border=1>
	<tr><td>카테고리</td><td>
	     <select id="mySelect" name="help_category">
    		<option value="수정요청" ${updatelists.help_category == '수정요청' ? 'selected' : ''}>수정요청</option>
    		<option value="Q&A" ${updatelists.help_category == 'Q&A' ? 'selected' : ''}>Q&A</option>
		 </select></td></tr>
    <tr><td>제목</td><td><input class=contborder type=text id=help_title name=help_title  value='${updatelists.help_title}'>
                    <input type=hidden id=help_seq name=help_seq value='${updatelists.help_seq}' readonly >
                    <input type=hidden id= member_id name=member_id  readonly ></td></tr>
    <tr><td>내용</td><td><textarea class=contborder name="help_content" id="help_content">${updatelists.help_content}</textarea></td></tr>
    <tr>
    <td>이미지</td>
    <td>
    	<input type=text id="showimg"  value="${updatelists.help_img}">
        <input type="hidden" id="old_img" name="old_img" value="${updatelists.help_img}">
        <input class="contborder" type="file" id="help_img" name="help_img" multiple>
    </td>
    </tr>
    <tr><td>비밀글 설정</td><td><input type=radio name="passwrite" value="open">공개글
                           <input type=radio name="passwrite" value="secret">비밀글</td></tr>
    <tr><td>비밀번호</td><td><input class=contborder type=password id=help_password name=help_password placeholder="비밀번호를 입력하세요"></td></tr>
    <tr><td colspan="2"><button class="btn" id=btnRegi>등록</button>
            <button type="button" class="btn" id="btncancel">취소</button></td></tr>				
    
</table>
</form>
</body>
<script src="https://code.jquery.com/jquery-latest.js"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script>
$(document).ready(function() {
	
	$('#btncancel').click(function(){
	    document.location= '/contact';
	});

	let help_category = $('#mySelect').val();
    $('#help_category').val(help_category);
    
    $('#mySelect').change(function() {        
        help_category = $(this).val();
        $('#help_category').val(help_category);
    });

    // 패스워드 공개/비공개 설정
	$('input[type=radio][name=passwrite]').change(function() {
        if (this.value == 'open') {
            $('#help_password').val('');
            $('#help_password').prop('readonly', true);
            $('#help_password').prop('placeholder', '작성불가');
        }
        else if (this.value == 'secret') {
            $('#help_password').prop('readonly', false);
            $('#help_password').prop('placeholder', '비밀번호를 입력하세요');
        }
    });
	
    $('#help_img').change(function() {
        // If files were selected
        if(this.files.length > 0) {
            var fileNames = [];
            for(var i=0; i<this.files.length; i++) {
                fileNames.push(this.files[i].name);
            }
            // Join file names and set as value of "showimg"
            $('#showimg').val(fileNames.join(", "));
        } else {
            // If no file was selected, set "showimg" value back to original value
            $('#showimg').val($('#old_img').val());
        }
    });
    
    
// 등록 버튼
    $('#btnRegi').click(function(e){
        e.preventDefault();
        
        let help_password = $('#help_password').val();
        if ($('input[type=radio][name=passwrite]:checked').val() == 'open') {
            help_password = ""; 
        }
        // 비번 공백시 리턴펄스
        if ($('input[type=radio][name=passwrite]:checked').val() == 'secret' && help_password === '') {
            alert('비밀번호를 입력하세요.');
            return false;
        }
        // 비번설정 자릿수 설정
        if ($('input[type=radio][name=passwrite]:checked').val() == 'secret') {
            if (!/^\d{4}$/.test(help_password)) {
                alert('비밀번호는 숫자만 입력 가능하며, 4자리만 가능합니다.');
                $('#help_password').val('');
                return false; 
            }
        }      
        
        // 제목, 내용 공백체크
        if($('#help_title').val()=='') {
            alert('제목을 입력해주세요');
            return false;
        } 
        if($('#help_content').val()==''){
            alert('내용을 입력해주세요.');
            return false;
        }
        // 비밀번호 체크여부
        if (!$('input[type=radio][name=passwrite]').is(':checked')) {
            alert('비밀번호 여부를 체크해주세요.');
            return false;
        }
        
        let formData = new FormData($('form')[0]);    

     // AJAX Form Submission
     $.ajax({
         url:'/contactupate',
         processData: false,
         contentType: false,
         data: formData,
         type:'post', 
         
         success:function(data){
             if(data == "ok"){
                 alert('update성공')
                 document.location= '/contact';
             }else{
                 alert('update실패')
             }
         }
     });
    }); 
});

</script>
</html>