] <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<style>
body {
    margin: 0;
    padding: 0;
}
.Main {
    display: flex;
    align-items: center;
    justify-content: center;
    height:auto;
    margin-top:1%;
}
.MainContainer {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
.ImgContainer > img {
    width:80px;
    height:80px;
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
	width:100%;
	display: flex;
	justify-content: space-between;
	border:1px solid black;
}

.ScheduleImg {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.ScheduleInfo {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>
<body>
    <%@ include file="./header.jsp" %>
    <div class="Main">
        <div class="MainContainer">
            <div class="MainContent">
                <div class="MemberInfo">
                    <span class="MemberId">멤버ID</span>
                </div>
                <div class="Modify">
                    <a href="*" id="modify">개인정보 수정</a>
                    <a href="*" id="change">비밀번호 변경</a>
                </div>
            </div>
            <div class="ScheduleContainer">
                <div class="ScheduleImg">
                    <div class="ImgContainer">
                        <img src="/img/test.png">
                    </div>
                    <div class="NameContainer">
                        <span class="CityName"> CityName</span>
                    </div>
                </div>
                <div class="ScheduleInfo">
                    <div class="DateContainer">
                        <P><span class="StartDate">YYYY/MM/DD</span>~<span class="EndDate">YYYY/MM/DD</span></P>
                    </div>
                    <div class="BottonContainer">
                        <button id="ScheduleModify">일정 수정</button>
                        <button id="ScheduleDelete">일정 삭제</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="./footer.jsp" %>
</body>
</html>