<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Place_insert</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=83a5e10151ba0f12ecdb1a465d31b58e&libraries=services"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.3.0/jquery.form.min.js"></script>
<link href="https://cdn.jsdelivr.net/gh/sunn-us/SUITE/fonts/static/woff2/SUITE.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/static/css/header.css">
</head>
<style>
.section {
	margin-top: 150px;
	padding: 20px;	
}

table {
	width: 90%;
	border-collapse: collapse;
}

table td {
	padding: 8px;
	vertical-align: middle;
}

select[name=city], select[name=place] {
	padding: 8px;
	font-size: 16px;
	border-radius: 4px;
	border: 1px solid #ccc;
	margin-right: 10px;
}

input[type=text], textarea {
	width: 80%;
	padding: 8px;
	font-size: 16px;
	border-radius: 4px;
	border: 1px solid #ccc;
}

input[type=button], input[type=submit] {
	padding: 8px 16px;
	font-size: 16px;
	border-radius: 4px;
	background-color: #000;
	color: white;
	border: none;
	cursor: pointer;
}

input[type=button]:hover, input[type=submit]:hover {
	background-color: #213555;
}

#postcode {
	margin-right: 5px;
}

#imageContainer {
	vertical-align: middle;
}

#btnUpload {
	background-color: #000;
	color: #ffffff;
	border: none;
	text-decoration: none;
	padding: 6px 16px;
	font-size: 15px;
	font-weight: bold;
	border-radius: 4px;
	cursor: pointer;
}

#btnCancel {
	background-color: #000;
	color: #ffffff;
	border: none;
	text-decoration: none;
	padding: 6px 16px;
	font-size: 15px;
	font-weight: bold;
	border-radius: 4px;
	cursor: pointer;
}

.uploaded-image {
	width: 100px; /* Adjust the width as desired */
	height: auto;
	display: inline-block;
	margin-right: 10px; /* Adjust the margin as desired */
}

.image-wrapper {
	display: inline-block;
	position: relative;
	margin-right: 10px;
	margin-bottom: 10px;
}
</style>

<body>
<%@ include file="./header.jsp" %>
<div class="section">
<table>
	<tr>
		<td>카테고리</td>
		<td><select name=city id=city onchange="categoryChange()">
			<option>지역</option>
			<option value=02>서울</option>
			<option value=032>인천</option>
			<option value=051>부산</option>
			<option value=042>대전</option>
			<option value=031>가평</option>
			<option value=033>강릉</option>
			<option value=043>제천</option>
			<option value=063>전주</option>
			<option value=061>여수</option>
			<option value=054>경주</option>
			<option value=064>제주</option>
			<option value=055>거제/통영</option>
		</select>
		<select name=place id=place>
			<option>분류</option>
		</select></td>
	</tr>
	<tr>		
		<td>업체명</td>
		<td><input type=text name=name id=name required></td>		
	</tr>
	<tr>		
		<td>주소</td>
		<td><input type="text" name="postcode" id="postcode" placeholder="우편번호" autocomplete="off">
      		<input type="button" onclick="sample6_execDaumPostcode()" value="우편번호 찾기"><br>
      		<input type="text" name="address" id="address" placeholder="주소" autocomplete="off"><br>
      		<input type="text" name="detailAddress" id="detailAddress" placeholder="상세주소" autocomplete="off">
      		<input type="button" onclick="locationserch()" value="위치">
      		<input type=text id=lat name=lat><input type=text id=lng name=lng><br></td>	
	</tr>
	<tr>		
		<td>전화</td>
		<td><input type=text name=tel id=tel required></td>		
	</tr>
	<tr>		
		<td>영업시간</td>
		<td><input type=text name=open id=open required></td>		
	</tr>
	<tr>		
		<td>설명</td>
		<td><textarea name=content id=content required></textarea></td>		
	</tr>
	<tr>
      <td>이미지</td>
      <td>
        <form id="uploadForm" method="post" action="/upload_image" enctype="multipart/form-data">
          <input type="file" name="images" id="fileInput" multiple="multiple" accept=".png, .jpg, .jpeg">
        </form>
      </td>
    </tr>
    <tr>
      <td>이미지보기</td>
      <td id="imageContainer"></td>
      <td><input type="hidden" name="imageNames" id="imageNames"></td>     
    </tr>
	<tr>
		<td colspan=2 align=center><input type=submit value=등록 id=btnUpload>&nbsp&nbsp&nbsp  
		<input type=button value=취소 id=btnCancel></td>
	</tr>	
</table>
</div>

</body>

<script>
let fileNames = [];
let like = 0;
$(document)
.on('click', '#btnUpload', function() {
	let imageNames = fileNames;
    
    $.ajax({
        url: '/place_upload',
        type: 'post',
        dataType: 'text',
        data: {
            city: $('#city').val(),
            place: $('#place').val(),
            name: $('#name').val(),
            postcode: $('#postcode').val(),
            address: $('#address').val(),
            detailAddress: $('#detailAddress').val(),
            lat: $('#lat').val(),
            lng: $('#lng').val(),
            tel: $('#tel').val(),
            open: $('#open').val(),
            content: $('#content').val(),
            imageNames: fileNames,
            like : like
        },
        beforeSend: function() {
        	 if ($('#city').val() === '지역') {
        	        alert('지역과 분류를 선택해주세요.');
        	        return false; 
        	    }
            if ($('input[name=name]').val() == '') {
                alert('업체명을 작성해주세요.');
                return false;
            }
        },
        success: function(data) {
        	
        },
        complete: function() {
            document.location = "/manage_place";
        }
    });
})

.on('click', '#btnCancel', function() {
	document.location = "/manage_place";
		return false;
})


function categoryChange() {
   var place = $("#place");
   place.empty();

   place.append($('<option></option>').val('1').text('식당'));
   place.append($('<option></option>').val('2').text('카페'));
   place.append($('<option></option>').val('3').text('체험'));
   place.append($('<option></option>').val('4').text('명소'));
   place.append($('<option></option>').val('5').text('호텔'));
   place.append($('<option></option>').val('6').text('캠핑'));
 }

function sample6_execDaumPostcode() {
	  new daum.Postcode({
	    oncomplete: function(data) {
	      // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
	      // 각 주소의 노출 규칙에 따라 주소를 조합한다.
	      // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
	      var addr = ''; // 주소 변수

	      // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
	      if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
	        addr = data.roadAddress;
	      } else { // 사용자가 지번 주소를 선택했을 경우(J)
	        addr = data.jibunAddress;
	      }

	      // 우편번호와 주소 정보를 해당 필드에 넣는다.
	      document.getElementById('postcode').value = data.zonecode;
	      document.getElementById('address').value = addr;
	      // 커서를 상세주소 필드로 이동한다.
	      document.getElementById('detailAddress').focus();
	    }
	  }).open();
	}


$(document).on('change', '#fileInput', function() {
    var formData = new FormData($('#uploadForm')[0]);
    $.ajax({
        url: '/upload_image',
        type: 'POST',
        data: formData,
        dataType: 'text',
        processData: false,
        contentType: false,
        success: function(response) {
            fileNames = [];
            for (var pair of formData.entries()) {
                fileNames.push(pair[1].name);
            }

            showImages(fileNames);
            console.log(fileNames);
        },
        error: function(xhr, status, error) {
            alert('failed');
        }
    });
});

$(document).on('click', '.delete-image', function() {
    var fileName = $(this).data('filename');
    var imageIndex = fileNames.indexOf(fileName);
    if (imageIndex > -1) {
        fileNames.splice(imageIndex, 1);
    }
    $(this).parent().remove();
    $('#imageNames').val(fileNames.join(','));
});

function showImages(fileNames) {
    var imageContainer = $('#imageContainer');
    imageContainer.empty();

    for (var i = 0; i < fileNames.length; i++) {
        var fileName = fileNames[i];
        var imageUrl = '/img//place/' + encodeURIComponent(fileName);

        var imageElement = $('<div>').addClass('image-wrapper');
        var img = $('<img>').attr('src', imageUrl).addClass('uploaded-image');
        var deleteButton = $('<button>').text('X').addClass('delete-image').data('filename', fileName);

        imageElement.append(img);
        imageElement.append(deleteButton);
        imageContainer.append(imageElement);
    }

    $('#imageNames').val(fileNames.join(','));
}

function locationserch() {
	  let location = $("#address").val(); // 주소를 가져오는 input위치
	  // 주소-좌표 변환 객체를 생성합니다
	  console.log(location);
	  let geocoder = new kakao.maps.services.Geocoder();

	  // 주소로 좌표를 검색합니다
	  geocoder.addressSearch(location, function (result, status) {
	    // 정상적으로 검색이 완료됐으면
	    if (status === kakao.maps.services.Status.OK) {
	      document.getElementById('lat').value=result[0].y;
	      document.getElementById('lng').value=result[0].x;
	    }
	  });
	}

</script>
</html>