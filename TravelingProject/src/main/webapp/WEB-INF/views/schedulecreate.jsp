<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/css/schedulecreate.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
<link rel="stylesheet" type="text/css" href="/css/header.css">
<title>일정상세페이지</title>
</head>
<body>
	<%@ include file="./header.jsp"%>
	<!-- 지도를 표시할 div 입니다 -->
	<div id="map"></div>
	<div id="divScheduleCreate">
		<button id="scheduleCreate">
			<h7>일정생성</h7>
		</button>
	</div>
	<div id="rightSideBar">
		<div id="divSearch">
			<input type="text" placeholder="업체명을 입력하세요." id="searchInput">
			<button id="search" class="material-icons">search</button>
		</div>
		<div class="rightSelect rightRadioCss">
			<input type="radio" id="lodging" name="select" onclick="rightRadio(this)"> <label for="lodging" style="width: 100%;"> <span>숙박</span>
			</label> <input type="radio" id="place" name="select" onclick="rightRadio(this)"> <label for="place" style="width: 100%; height: 20px;"> <span>명소</span>
			</label> <input type="hidden" id="rightRadioCurrentP" value="2">
		</div>
		<div id="sectionTitle">
			<span>추천장소</span>
		</div>
		<div id="divPlaceCard">
		</div>
		<div id="normal_menu_pagination" class="normal_menu_pagination">
			<ul>
				<li class="page">처음</li>
				<li class="page">이전</li>
			</ul>
			<ul id="ul_pageNumber">
			</ul>
			<ul>
				<li class="page">다음</li>
				<li class="page">마지막</li>
			</ul>
		</div>
		<input type="hidden" id="hidden_currentP" value="1"> <input type="hidden" id="placeListCount" value="1">
	</div>
	<div id="leftSideBar">
		<div id="leftTop">
			<div id="cityName">서울</div>
			<div id="day">3&nbsp;DAY</div>
			<div id="divCalender">
				<input type="text" class="calender" id="startDate"> ~ <input type="text" class="calender" id="endDate">
			</div>
		</div>
		<div id="selectList">선택목록</div>
		<div class="leftSelect leftRaidoCss">
			<input type="radio" id="lodgingSelect" name="leftSelect" onclick="leftRadio(this)"> <label for="lodgingSelect" style="width: 100%;"> <span>숙박</span>
			</label> <input type="radio" id="placeSelect" name="leftSelect" onclick="leftRadio(this)"> <label for="placeSelect" style="width: 100%;"> <span>명소</span>
			</label> <input type="hidden" id="leftRadioCurrentP" value="2">
		</div>
		<div id="leftRadioSelectPlace" style="display: none;">
			<div>
				<button id="placeAllDelete" onclick="allDelete(this)">명소전체삭제</button>
			</div>
			<div id="divPlaceAddCart" class="container"></div>
		</div>

		<div id="leftRadioSelectLodging" style="display: block;">
			<div>
				<button id="lodgingAllDelete" onclick="allDelete(this)">숙박전체삭제</button>
			</div>
			<div id="divLodgingAddCart" class="container"></div>
		</div>
	</div>
	<div id="divPlaceInfo" class="modal">
		<div id="placeInfo_modal_content"></div>
	</div>
	<div id="divModalScheduleCreate" class="scheduleModal">
		<div id="scheduleCreateModalContent">
			<div id="divScheduleModalClose">
				<button type="button" id="scheduleModalClose" class="close">×</button>
			</div>
			<div id="scheduleMap" class="map"></div>
			<div id="dayListButtonArea">
				<input type="hidden" id="dayListButtonAreaCurrentP" value="1">
			</div>
			<div id="divScheduleModalRightSidebar">
				<div class="rightScheduleModalRadio rightScheduleModalRadioCss">
					<input type="radio" id="rightScheduleModalRadioLodging" name="rightScheduleModalRadio" onclick="rightScheduleModalRadio(this)">
					<label for="rightScheduleModalRadioLodging" style="width: 100%;"><span>숙박</span></label>
					<input type="radio" id="rightScheduleModalRadioPlace" name="rightScheduleModalRadio" onclick="rightScheduleModalRadio(this)">
					<label for="rightScheduleModalRadioPlace" style="width: 100%; height: 20px;"><span>명소</span></label>
					<input type="hidden" id="rightScheduleModalRadioCurrentP" value="2">
				</div>
				<div id="placeAddCartCopy"></div>
				<div id="lodgingAddCartCopy"></div>
			</div>
			<div id="divScheduleModalLeftSidebar">
				<div class="leftScheduleModalRadio leftScheduleModalRadioCss">
					<input type="radio" id="leftScheduleModalRadioLodging" name="leftScheduleModalRadio" onclick="leftScheduleModalRadio(this)">
					<label for="leftScheduleModalRadioLodging" style="width: 100%;"> <span>숙박</span></label>
					<input type="radio" id="leftScheduleModalRadioPlace" name="leftScheduleModalRadio" onclick="leftScheduleModalRadio(this)">
					<label for="leftScheduleModalRadioPlace" style="width: 100%;"><span>명소</span></label>
					<input type="hidden" id="leftScheduleModalRadioCurrentP" value="2">
				</div>
				<div class="scheduleDate1 scheduleStyle" style="display: none;">
					<div id="leftRadioSelectPlace1" class="leftRadioSelectPlaceCss container" style="display: block;">
					</div>
					<div id="leftRadioSelectLodging1" class="leftRadioSelectPlaceCss container" style="display: none;">
					</div>
				</div>
				<div class="scheduleDate2 scheduleStyle" style="display: none;">
					<div id="leftRadioSelectPlace2" class="leftRadioSelectPlaceCss container" style="display: none;">
					</div>
					<div id="leftRadioSelectLodging2" class="leftRadioSelectPlaceCss container" style="display: none;">
					</div>
				</div>
			</div>
		</div>
	</div>
		
    <!--
        <div id="typeDlg" style="display: none;">
            <a href="https://map.kakao.com/?sName=천안시청&eName=휴먼교육센터">링크</a>
        </div>
     -->
</body>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Sortable/1.14.0/Sortable.min.js" integrity="sha512-zYXldzJsDrNKV+odAwFYiDXV2Cy37cwizT+NkuiPGsa9X1dOz04eHvUWVuxaJ299GvcJT31ug2zO4itXBjFx4w==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Sortable/1.14.0/Sortable.min.js"></script>
<script src="https://code.jquery.com/jquery-latest.js"></script>
<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=83a5e10151ba0f12ecdb1a465d31b58e&libraries=services"></script>
<script src="/js/schedulecreate.js"></script>
</html>

