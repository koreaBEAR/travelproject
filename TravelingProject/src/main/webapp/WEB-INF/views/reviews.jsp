<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>피드</title>
<link href="css/main.css" media="screen and (min-width:512px)" rel="stylesheet" >
<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/css/main.css">
</head>
<body>
<!-- 메인헤더 -->
<% if(session.getAttribute("id")==null){ %>
                    <div class="Mainhead">
                        <span id=title>
                            <a href="/main"><img class="title_img" src="/img/logo.jpg" ></a>
                        </span>
                        
                     <span id=head_line>
                        <div class="dong">
                            <a href="/" id="head_login">&nbsp;LOGIN</a>                          
                            <a href="/join" id="head_login">SIGNIN</a>                         
                        </div>
                       </span>
                 </div>
                 <% } else if(session.getAttribute("memberClass").equals("admin")) { %>
                 <div class="Mainhead">
                    <span id=title>
                        <a href="/main"><img src="/img/logo.jpg" class=title_img></a>
                    </span>
                     
                     <div class="dong">
                        <a id="head_mine">WELCOME,  ${id} &nbsp;</a>
                        <a href="/logout" id="head_logout">&nbsp;LOGOUT</a>
                        <a href="/manage_member" id="head_mypage">ADMINPAGE</a>
                    </div>
             	</div>
                <% } else { %>
                 <div class="Mainhead">
                    <span id=title>
                        <a href="/main"><img src="/img/logo.jpg" class=title_img></a>
                    </span>
                    <span id=head_line>
                     <div class="dong">
                        <a id="head_mine">WELCOME,  ${id}님! &nbsp;</a>
                       	<a href="/contact" id="head_mypage">CONTACT</a>
				        <a href="/tourlist" id="head_mypage">TOURLIST</a>
				        <a href="/mypage" id="head_mypage">MYPAGE</a>
				        <a href="/logout" id="head_mypage">&nbsp;LOGOUT</a>                            
                    <input type=hidden id=m_id name=m_id value=<%=session.getAttribute("id")%>>
                    </div>
                   </span>
             </div>
                <% } %>


    <div></br></br><h1>어디로 여행을 떠나시나요?</h1></div>

<!-- 정렬 카테고리 -->
    </br>
    <div class="sort">
        <select>
          <option value="best">인기순</option>
          <option value="ascorder">오름차순</option>
          <option value="descorder">내림차순</option>
        </select>
    </div>
<!-- 관광명소 div -->
    <div class=viewdiv>
        <div class="divtbl">
        </div>
    </div>
    <div class=viewDivFooter>
    <span name=pageNum>1</span>
    </div> 
    </body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="http://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script src="js/reviews.js"></script>
</html>