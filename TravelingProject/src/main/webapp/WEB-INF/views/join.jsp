<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/Jo_Join.css">
<meta charset="UTF-8">
<title>JoinUs</title>
</head>
<body>
<div class="page">
		<div class="slideShow">
			<div><img src="" id="backImg"></div>
		</div>
		<div class="container">
			<div class="form">
				<label for="id">아이디</label>
				<input type="text" id="joinId">
				<font id="chkId" size=2></font><br>
				<label for="pw">비밀번호</label>
				<input type="password" id="joinPw" onkeyup="characterCheckPW(this)" onkeydown="characterCheckPW(this)">
				<font id="chkPwlength" size=2></font><br>
				<label for="pw">비밀번호확인</label>
				<input type="password" id="pwCheck">
				<font id="chkPwMms" size=2></font><br>
				<label for="name">이름</label>
				<input type="text" id="name">
				<label for="nickname">닉네임</label>
				<input type="text" id="aka">
				<font id="chkNickName" size=2></font><br>
				<label for="mail">이메일</label>
				<input type="email" id="mail" onkeyup="validEmail(this)" onkeydown="validEmail(this)">
				<font id="chkEmail" size=2></font><br>
				<label for="mobile">전화번호</label>
				<input
					type="tel"
					class="form-control m-input"
					name="tel"
					id="telInput"
					required
					pattern="[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}"
					maxlength="13"
					placeholder="예) 010-9999-9999" 
				/>
				<label for="birth">생년월일</label>
				<input type="date" id="birth">
				<label for="gender">성별</label>
				<div class="select">
				     <input type="radio" id="gender" name="gender" value="남"><label for="gender">남</label>
				     <input type="radio" id="gender2" name="gender" value="여"><label for="gender2">여</label>
				</div>
				<button class="joinButton" id="btnsubmit"><span>join</span></button>
				<button id="btnback"><span>login</span></button>
			</div>
		</div>
	</div>
</body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="http://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script src="js/Jo_Join.js"></script>
</html>