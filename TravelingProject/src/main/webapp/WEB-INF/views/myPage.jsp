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
					<a href="*" id="modify">개인정보 수정</a>
					<a href="*" id="change">비밀번호 변경</a>
				</div>
			</div>
			<div class="MainSchedule">
			</div>
		</div>
	</div>
<%@ include file="./footer.jsp" %>
</body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="http://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script src="js/myPage.js"></script>
</html>