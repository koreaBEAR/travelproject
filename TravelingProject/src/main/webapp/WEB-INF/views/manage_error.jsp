<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ page session="true" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.3.0/jquery.form.min.js"></script>
<link href="https://cdn.jsdelivr.net/gh/sunn-us/SUITE/fonts/static/woff2/SUITE.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/static/css/header.css">
</head>
<style>
  body {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  img {
    display: block;
    margin: 50px auto;
  }

  h2 {
    margin: 0;
    padding: 10px;
    margin-top: -120px;
    margin-bottom: 10px;
    text-align: center;
  }

  a {
    text-decoration: none;
    color: #000;
  }

  a img {
    width: 50px; 
    height: 50px;
    display: inline-block;
    vertical-align: middle;
    margin-right: 10px;
  }
</style>

<body>
  <%@ include file="./header.jsp" %>
  <img src="/static/error/error.jpg">
  <h2>
    <a href="/main">
      <img src="/static/error/arrow.gif">
      <span style="display: inline-block; vertical-align: middle;">Go to main</span>
    </a>
  </h2>
</body>

</html>