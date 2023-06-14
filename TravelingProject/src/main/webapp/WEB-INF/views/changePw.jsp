<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<input type=hidden id=hidden value=${id}>
	<input type="text" id="nowPw">
	<font size=2 id=nowPwText></font><br>
	<input type="text" id="changePw" onkeyup="characterCheckPW(this)"onkeydown="characterCheckPW(this)">
	<font size=2 id=changePwText></font><br>
	<input type="text" id="chkChangePw">
	<font size=2 id=chkChangePwText></font><br>
	
	<button id="btnChange">변경하기</button>
	<button id="btnCancel">취소하기</button>
</body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="http://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script src="js/chkPw.js"></script>
</html>