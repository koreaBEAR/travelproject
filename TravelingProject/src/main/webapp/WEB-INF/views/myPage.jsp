<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<style>
@font-face {
    font-family: 'KCC-Ganpan';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2302@1.0/KCC-Ganpan.woff2') format('woff2');
    font-weight: normal;
    font-style: normal;
}
body {
    margin: 0;
    padding: 0;
}
h2 {
	margin: 5px 0 13px 0;
	padding: 0;
}
.Main {
    display: flex;
    align-items: center;
    justify-content: center;
    height:auto;
}
.MainContainer {
	width:100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    margin-top:5%;
}
.ImgContainer > img {
    width:80px;
    height:80px;
    border-radius: 50%;
}
.MainContent {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
.MemberInfo{
    margin-bottom:30px;
}
.ScheduleContainer {
	width: 80%;
	padding: 10px;
	margin-top:1%;
	margin-bottom:1%;
	margin-left:10%;
	display: flex;
/* 	justify-content: center; */
 	justify-content: space-between;
/* 	border:1px solid black; */
	box-shadow:2px 3px 5px 0px gray;
}
.MainSchedule{
	width:100%;
	display: grid;
	grid-template-columns: 1fr 1fr;
	grid-gap: 1px;
	padding: 2%;
}
.ScheduleImg {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.DateContainer{
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	grid-template-rows: repeat(2, 1fr);
	column-gap: 5px;
}
.DateContainer span{
	padding:5px;
}
.date{
	margin-right:5%;
}
.ButtonContainer{
	display: flex;
	justify-content: right;
	width:100%;
}

.ScheduleInfo {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.scheduleDate, .scheduleUpdated{
	font-size:15px;
}
.Modify > a:link, .Modify > a:visited {
	border-radius: 30px;
    background-color: #F8B48F;
    color : black;
    padding: 15px 25px;
    text-align: center;
    text-decoration: none;
    cursor: pointer;           
    transition-duration: 0.4s;
    display: inline-block;
}
.Modify > a:hover, .Modify > a:active {
     color: white;
}
.ButtonContainer > button{
	border-radius: 30px;
	background-color: #F8B48F;
	border: 0.5px solid #F8B48F;
	color : black;
	padding: 5px 10px;
	text-align: center;
	text-decoration: none;
	cursor: pointer;           
	transition-duration: 0.4s;
	display: inline-block;
	margin-right:5px;
}
.ButtonContainer > button:hover{
     color: white;
}

.modal {
	display: none; /* 모달을 기본적으로 숨김 */
	position: fixed; /* 위치를 고정 */
	z-index: 1; /* 다른 요소들보다 위에 표시 */
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	overflow: auto; /* 스크롤 가능하도록 */
	background-color: rgba(0, 0, 0, 0.5); /* 배경에 약간의 투명도 */
	padding:5px;
}
.infoModal {
	display: none; /* 모달을 기본적으로 숨김 */
	position: fixed; /* 위치를 고정 */
	z-index: 1; /* 다른 요소들보다 위에 표시 */
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	overflow: auto; /* 스크롤 가능하도록 */
	background-color: rgba(0, 0, 0, 0.5); /* 배경에 약간의 투명도 */
	padding:5px;
}
#pwChangeModalContent{
	position: relative;
	top: 30vh;
	left: 32vw;
	background-color: #fefefe;
	width: 30%;
	height: 35%;
}
#modifyInfoContainer{
	position: relative;
	top: 30vh;
	left: 32vw;
	background-color: #fefefe;
	width: 30%;
	height: 40%;
}
#modalInput, #infoInput{
	position: relative;
	display: flex;
	flex-direction: column;
	align-items: center;
}
.pwChangeModalInput {
	width: 179px;
	height: 25px;
}
.infoChangeInput{
	width: 179px;
	height: 25px;
}

.pwChangeModalButton {
	border-radius: 30px;
	background-color: #F8B48F;
	border: 0.5px solid #F8B48F;
	color: black;
	padding: 5px 10px;
	text-align: center;
	text-decoration: none;
	cursor: pointer;
	transition-duration: 0.4s;
	display: inline-block;
	margin: 0 5px 0 5px
}

.divPwChangeModalButton {
	position: relative;
	bottom: -6px;
	display: flex;
	justify-content: center;
}
.divInfoChangeModalButton{
	position: relative;
	margin-top:6%;
	bottom: -4px;
	display: flex;
	justify-content: center;
}

.pwChangeModalFont, .infoChangeModalFont{
	margin: 5px 0 5px 0;
	height: 16px;
}

</style>
<body>
<%@ include file="./header.jsp" %>
	<div class="Main">
		<div class="MainContainer">
			<div class="MainContent">
				<div class="MemberInfo">
					<span class="MemberId">${nickName} 님의 마이페이지 입니다.</span>
				</div>
				<div class="Modify">
					<a href="javascript:;" id="modify">개인정보 수정</a>
					<a href="javascript:;" id="change">비밀번호 변경</a>
				</div>
			</div>
			<div class="MainSchedule">
			</div>
		</div>
	</div>
	<div id="pwChangeModal" class="modal">
			<div id="pwChangeModalContent">
				<input type=hidden id=hidden value=${id}>
			<div id="modalInput">
				<h2>비밀번호 변경</h2>
				<input type="text" id="nowPw" class="pwChangeModalInput" placeholder="현재비밀번호">
				<font size=2 id="nowPwText" class="pwChangeModalFont"></font>
				<input type="text" id="changePw"  class="pwChangeModalInput" onkeyup="characterCheckPW(this)"onkeydown="characterCheckPW(this)" placeholder="새비밀번호">
				<font size=2 id="changePwText" class="pwChangeModalFont"></font>
				<input type="text" id="chkChangePw" class="pwChangeModalInput" placeholder="비밀번호확인">
				<font size=2 id="chkChangePwText" class="pwChangeModalFont"></font>
			</div>
			<div class="divPwChangeModalButton">	
				<button id="btnChange" class="pwChangeModalButton">변경하기</button>
				<button id="btnCancel" class="pwChangeModalButton">취소하기</button>
			</div>
		</div>
	</div>
	<div id="modifyInfo" class="infoModal">
		<div id="modifyInfoContainer">
			<input type=hidden id=infoHiddenId value=${id}>
		<div id="infoInput">
			<h2>개인정보 변경</h2>
			<span>닉네임</span>
			<input type="text" id="changeNickname" class="infoChangeInput" value=${nickName}>
			<font size=2 id="chageNicknameText" class="infoChangeModalFont"></font>
			<span>이메일</span>
			<input type="text" id="changeMail" class="infoChangeInput" value=${mail} onkeyup="validEmail(this)" onkeydown="validEmail(this)">
			<font size=2 id="chageMailText" class="infoChangeModalFont"></font>
			<span>전화번호</span>
			<input
					type="tel"
					class="infoChangeInput form-control m-input"
					name="tel"
					id="telInput"
					required
					pattern="[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}"
					maxlength="13"
					placeholder="예) 010-9999-9999"
					value=${mobile} 
			/>
			<div class="divInfoChangeModalButton">	
				<button id="btnInfoChange" class="pwChangeModalButton">변경하기</button>
				<button id="btnInfoCancel" class="pwChangeModalButton">취소하기</button>
			</div>
		</div>
		</div>
	</div>
<%@ include file="./footer.jsp" %>
</body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="http://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script src="js/myPage.js"></script>
<script src="js/chkPw.js"></script>
</html>