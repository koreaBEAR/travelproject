<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/gh/sunn-us/SUITE/fonts/static/woff2/SUITE.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/css/contact.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
<title>고객센터 문의게시판</title>
<script src="https://kit.fontawesome.com/3d35e74b72.js" crossorigin="anonymous"></script>
</head>
<style>

</style>
<body>
<!-- 헤더 -->
<%@ include file="./header.jsp" %>

	<div class="wrap" id="wrap">
		<h1>고객센터 문의게시판&nbsp;<i class="fa-solid fa-headset fa-sm" style="color: #000000;"></i></h1>
		<div class="page_info">
			<span id=count name=count><strong>총: </strong>${num} 건</span>
		</div>
		<section>
		<table id=tbllist class="table01">
			<tr>
				<th><input type="hidden" name="seq" id="seq" readonly>No.</th>
				<th>카테고리</th>
				<th>제목</th>
				<th>작성자</th>
				<th>작성일</th>
				<th>상태</th>
			</tr>
			<tbody id=bodylist>
			</tbody>
		</table>
		<div class="btn_right mt15">
			<button id=btnwrite>문의하기</button>
		</div>
			<div class="div_footer" align="center">
				<div class="pagination-container">
					<span class="pagination" name="page">1</span>
				</div>
			</div>
		</section>
		<div>
			<select name=type id="searchForm">
				<option value="검색기준">검색 기준</option>
				<option value="help_category">카테고리</option>
				<option value="member_nickName">닉네임</option>
				<option value="help_title">제목</option>
				<option value="member_nickName||help_title">전체검색</option>
			</select> <input type="text" id="keyword" name="keyword"> <input
				type="submit" id="btnsearch" value=검색>
		</div>

	</div>
	<!-- 다이어로그  -->
	<div class="row" id="typeDlg" style="display:none;">
	<p><h2>비밀번호 입력</h2></p>
	<p><input type="password" name=help_password id=help_password></p>
		<div class=diabtn>
			<button type="button" id=btnselect class="btn btn-primary">확인</button>
		</div>
	</div>
           
</body>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="http://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script>
let seg='';
$(document)

.ready(function(){	
	$('span[name=page]:eq(0)').trigger('click');
	
    $('h1').click(function() {
        location.reload();
    });
 })

.on('click','#btnwrite',function(){
	document.location="/contactwrite";
}) 


.on('click','span[name=page]',function(){
	var pageNum = parseInt($(this).text())
	loadData(pageNum);
})

.on('click','#tbllist tr', function(){
    seg = $(this).find('td:eq(0)').text();

    $.ajax({
        url: '/sortPost',
        type: 'post',
        dataType: 'text',
        data: {
            post_seq: seg
        },
        success: function(data) {
            if (data == 'open') {
                document.location = "/contactdetail/" + seg;
            } else if (data == 'secret') {
                console.log('dialog 한번 거치기');

                $('#typeDlg').dialog('open');  // 여기를 변경했습니다
            }
        }
    })
});


/* 엔터키 이벤트 발생 */
$('#help_password').keypress(function(event) {
	
    if (event.which === 13) {
        event.preventDefault(); 

        $('#btnselect').click();
    }
});

// 다이얼로그 비밀번호창
$( "#typeDlg" ).dialog({
    autoOpen: false,
    position: { my: "center", at: "center", of: window },
    closeText: "X",// X 버튼 추가
    open: function() {
        $('#btnselect').on('click', function() {
            $.ajax({
                url: '/pwdselect/' + seg,
                type: 'post',
                dataType: 'text',  //change here
                data: { help_password: $('#help_password').val() },
                success: function(data) {
                    if (data == 'contactdetail') {
                        document.location = "/contactdetail/" + seg; //page redirection
                    } else if (data == 'invalid password') {
                        alert("비밀번호가 일치하지 않습니다.");
                        $('#help_password').val('');
                        return false;
                    }
                }
            });
        });
    }
});

$('#btnsearch').click(function() {
	SearchForm();
});

function loadData(pageNum){
	$.ajax({url:'/getList',type:'post',dataType:'json', data:{pageNum:pageNum},
		success:function(data) {
			
			let i=0
			let endPage = parseInt(data[0]['howmany'])
			console.log(data[0]['howmany'])
			var pageStr=''
			$('.div_footer').empty()
			for(i=1; i<=endPage; i++){
				pageStr=pageStr+ '<div class="pagination-container"><span class="pagination" name="page">'+i+'</span></div>';
				console.log(pageStr)
			}
			$('.div_footer').append(pageStr)		

			$('#bodylist').empty();
			
			for(i=1; i<data.length; i++) {
				str='<tr>';
				str+='<td>'+data[i]['help_seq']+'</td>';
				str+='<td>'+data[i]['help_category']+'</td>';
				str+='<td>'+data[i]['help_title']+'</td>';
				str+='<td>'+data[i]['member_nickname']+'</td>';
				str+='<td>'+data[i]['help_created']+'</td>';
				str+='<td>'+data[i]['help_complete']+'</td></tr>';

				$('#bodylist').append(str);
			}
		}})
}


function SearchForm(pageNum) {
    var type = $('#searchForm').val();
    var keyword = $('#keyword').val();
    
  	 // 카테고리가 선택되지 않았을 경우 리턴펄스
    if ($('#searchForm option:selected').val() == "검색기준") {
        alert('카테고리를 선택해주세요.');
        return false;
    }
  	 
    $.ajax({
        url: '/search',
        type: 'post',
        dataType: 'json',
        data: {
            type: type,
            keyword: keyword,
            pageNum:pageNum
        },
        success: function(response) {
            let data = response.results;
            let howmanyPages = data[0] && data[0].howmany ? parseInt(data[0].howmany) : 0;

            console.log(howmanyPages);

            let pageStr = '';
            $('.div_footer').empty();
            for (let i = 1; i <= howmanyPages; i++) {
            	pageStr=pageStr+ '<div class="pagination-container"><span class="pagination" name="page">'+i+'</span></div>';
            }
            $('.div_footer').append(pageStr);

            $('#bodylist').empty();
            $('#count').empty();

            for (let i = 1; i < data.length; i++) {
                let str = '<tr>';
                str += '<td>' + data[i]['help_seq'] + '</td>';
                str += '<td>' + data[i]['help_category'] + '</td>';
                str += '<td>' + data[i]['help_title'] + '</td>';
                str += '<td>' + data[i]['member_nickname'] + '</td>';
                str += '<td>' + data[i]['help_created'] + '</td>';
                str += '<td>' + data[i]['help_complete'] + '</td></tr>';

                $('#bodylist').append(str);
            }
            $('#count').append("<strong>총: </strong>" + response.count + " 건");
        }

    });
}


</script>
</html>