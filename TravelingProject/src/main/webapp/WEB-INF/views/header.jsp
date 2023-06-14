<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="/css/header.css">
<title>Header</title>
</head>
<body>
<!-- 메인헤더 -->
<% if(session.getAttribute("id")==null){ %>
<div class="Mainhead">
    <span id="title">
        <a href="/main"><img class="title_img" src="/img/logo.jpg" ></a>
    </span>
    <span id="head_line">
        <div class="dong">
            <a href="/" id="head_mypage">LOGIN</a>
            <a href="/join" id="head_mypage">SIGNIN</a>
        </div>
    </span>
</div>
<% } else if(session.getAttribute("memberClass").equals("admin")) { %>
<div class="Mainhead">
    <span id="title">
        <a href="/main"><img src="/img/logo.jpg" class="title_img"></a>
    </span>
    <div class="dong">
        <a id="head_mine">WELCOME, ${id}</a>
        <a href="/logout" id="head_mypage">LOGOUT</a>
        <a href="/manage_member" id="head_mypage">ADMINPAGE</a>
    </div>
</div>
<% } else { %>
<div class="Mainhead">
    <span id="title">
        <a href="/main"><img src="/img/logo.jpg" class="title_img"></a>
    </span>
    <span id="head_line">
        <div class="dong">
            <a id="head_mine">WELCOME, ${id}님!</a>
            <a href="/contact" id="head_mypage">CONTACT</a>
            <a href="/tourlist" id="head_mypage">TOURLIST</a>
            <a href="/mypage" id="head_mypage">MYPAGE</a>
            <a href="/logout" id="head_mypage">LOGOUT</a>
            <input type="hidden" id="m_id" name="m_id" value="<%=session.getAttribute("id")%>">
        </div>
    </span>
</div>
<% } %>

</body>
</html>