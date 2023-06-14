<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/Jo_Login.css">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<meta charset="UTF-8">
<title>login</title>
</head>
<body>
	<div class="page">
		<div class="slideShow">
			<div><img src="" id="backImg"></div>
		</div>
		<div class="container">
			<div class="form">
				<label for="id">아이디</label>
				<input type="text" id="loginID">
				<label for="password">비밀번호</label>
				<div class="password-container">
					<input type="password" id="loginPW">
					<img src="/img/eyeOpen.png" id="eyeOpen" onclick="togglePasswordVisibility()">
				</div><br>
				<button class="loginButton"><span>Login</span></button><br><br>
				<p class="forgot" align="center"><span id="findId">아이디찾기</span>&nbsp;&nbsp;/&nbsp;&nbsp;<span id="findPw">비밀번호찾기</span>&nbsp;&nbsp;/&nbsp;&nbsp;<a href="#" id="joinLink">회원가입</a></p>
			</div>
			<div class="formTitle">
				<img src="/img/로고.png" id="logoImg">
				<button id="btnShow">시작하기</button>
			</div>
		</div>
	</div>
<div class=IdDlg id="IdDlg" style="display:none;">
	<div class=divclass>
	   <div class="textForm"> 
	   <p class="sign" align="center"></p>
	       <input name="findId" id=findIdName type="text" class="findId" placeholder="Name">
	   </div>
	   <div class="textForm">
	        <input name="findEm" id=findEm type="text" class="findEm" placeholder="E-mail">
	   </div>
	   <div class="textForm1">
	        <button id=btnfind class=btnfind>아이디 찾기</button>
	   </div>
   </div>
</div>

<div class=PwDlg id="PwDlg" style="display:none;">
	<div class=divclass>
	   <div class="textForm"> 
	   <p class="sign" align="center"></p>
	       <input name="findId" id=pwdid type="text" class="findId" placeholder="Id">
	   </div>
	   <div class="textForm">
	        <input name="findEm" id=pwdem type="text" class="findEm" placeholder="E-mail">
	   </div>
	   <div class="textForm">
	        <input name="findNm" id=pwdnm type="text" class="findNm" placeholder="Name">
	   </div>
	   <div class="textForm1">
	        <button id=btnFindPw class=btnfind>비밀번호 찾기</button>
	   </div>
   </div>
</div>
</body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="http://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script src="js/Jo_Login.js"></script>
</html>