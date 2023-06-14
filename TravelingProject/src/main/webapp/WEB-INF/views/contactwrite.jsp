<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true" %>
<!DOCTYPE html>

<html>
<head>
<link href="https://cdn.jsdelivr.net/gh/sunn-us/SUITE/fonts/static/woff2/SUITE.css" rel="stylesheet">
	<title>게시판 글작성</title>
	<link rel="stylesheet" href="/css/contactwrite.css">
</head>
<style>

</style>
<body>
<!-- 메인헤더 -->
<%@ include file="./header.jsp" %>

<form action="/contactinsert" method="post" enctype="multipart/form-data">                
<table id=updatelist border=1>
<!-- <h1>고객센터 문의글 작성</h1> -->
	<tr><td>카테고리</td><td>
	     <select id=mySelect>
	      <option value="선택해주세요">선택해주세요</option>
          <option value="수정요청">수정요청</option>
          <option value="Q&A">Q&A</option>
        </select></td></tr>
    <tr><td>제목</td><td><input class=contborder type=text id=help_title name=help_title placeholder="제목을 입력하세요">
    								<input type=hidden id= help_seq name=help_seq  readonly >
    								<input type=hidden id= help_category name=help_category  readonly >
    								<input type=hidden id= member_id name=member_id  readonly ></td></tr>
    <tr><td>내용</td><td><textarea class=contborder name="help_content" id="help_content" placeholder="내용을 입력하세요"></textarea></td></tr>
    <tr><td>이미지</td>
    <td><input class=contborder type="file" id=img name="img" multiple></td></tr>
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

    let help_category = '';

    // When select is focused, hide the default option
    $('#mySelect').focus(function() {
        $('option:first', this).hide();
    });

    // When select loses focus, if no option is selected, show the default option
    $('#mySelect').blur(function() {
        if (this.value == '') {
            $('option:first', this).show();
        }
    });

    $('#mySelect').change(function() {        
        help_category = $(this).val();
        $('#help_category').val(help_category);
       
    });

    // Password Field Control Handlers
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

    // Submission Button Event
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

        // 카테고리가 선택되지 않았을 경우 리턴펄스
		if ($('#mySelect option:selected').val() == "선택해주세요") {
    		alert('카테고리를 선택해주세요.');
    	return false;
		}

        // Form Field Check
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
        
        // 이미지 삽입이 안될시 null값 자동입력
		if ($('#img').val()==null) {
			$('#img').val('');
		}

        // AJAX Form Submission
        $.ajax({
            url:'/contactinsert',
            processData: false,
            contentType: false,
            data: formData,
            type:'post', 
            
            success:function(data){
                if(data == "ok"){
                    alert('insert성공')
                    document.location= '/contact';
                }else{
                    alert('insert실패')
                }
            }
        });
    });
});

</script>
</html>