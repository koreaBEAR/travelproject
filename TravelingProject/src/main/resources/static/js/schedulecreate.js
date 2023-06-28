// URL에 cityNum과 cityName을 변수에 저장하는 코드
tempAry = window.location.pathname.split('/');
cityNum = tempAry[2];
cityName = decodeURIComponent(tempAry[3]);
siteName = tempAry[1];
scheduleSeq = tempAry[4];


$(document)
.ready(mapCreate)
.ready(function() {
	if ( siteName == 'schedulecreate') {
		before_date(3)
	}
	else {
		return false;
	}
})
	
.ready(datePicker)
.ready(function() {	
	$('#place').trigger('click');
	$('#placeSelect').trigger('click');
	$('#leftRadioSelectPlace').attr('style','display: block')
	$('#leftRadioSelectLodging').attr('style','display: none')
})
.ready(function() {
  $('#searchInput').keypress(function(key) {
    if (key.keyCode == 13) {
      placeSearch();
      event.preventDefault();
    }
  })
})
.ready(function() {
	if ( siteName == 'scheduleupdate') {
		scheduleupdateimport();
	}
})

.on('click', '#search', placeSearch) // 검색 이벤트
.on('click', '.page', pagination) //페이지네이션 이벤트
.on('click', '.page', placeListPageChange) //페이지에 맞는 리스트 구성을 위한 이벤트
.on('click', '#scheduleDelete', scheduleDelete) //일정에 추가되어 있는 업체를 선택부분만 삭제하는 이벤트
.on('change', '.calender', dateCalculation) //여행의 시작날짜와 종료날짜의 기간계산을 위한 이벤트
.on('click', '.info', placeInfo) //업체정보를 띄우기 위한 이벤트
.on('click', '#scheduleCreate', scheduleDetailCreate) //일정생성 이벤트
.on('click', '#scheduleModalClose', scheduleModalClose) // 모달을 닫는 이벤트
.on('click', '#modalSaveButton', modalSaveButton) // 일정상세페이지에서 일정저장 버튼 클릭 시 이벤트


//초기 위도,경도를 통한 선택지역 맵 불러오기
function mapCreate() {
  $.ajax({
    url: "/mapCreate",
    type: "post",
    data: {city: cityNum},
    dataType: "json",
    success:function(data) {
      for ( let i = 0; i < data.length; i++ ) {
        cityLat = data[i].lat
        cityLng = data[i].lng
        let mapContainer = document.getElementById("map"), // 지도를 표시할 div
        mapOption = {
          center: new kakao.maps.LatLng(cityLat, cityLng), // 지도의 중심좌표
          level: 9, // 지도의 확대 레벨
        }
        // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
        map = new kakao.maps.Map(mapContainer, mapOption)
			}
  	  placeListCount(cityNum,null);
      $('#cityName').text(cityName);
    }
  });
}

let markers = [];
//일정에 추가된 업체를 마커로 보여주는 함수입니다.
function markerScheduleCreate(tagId) {
  $.ajax({
    url : "/markerScheduleCreate",
    type : "post",
    data : {
            pSeq : tagId
    },
    dataType : "json",
    success:function(data) {
      let placeSeq = data[data.length-1].seq
      let placeLat = data[data.length-1].lat
      let placeLng = data[data.length-1].lng

      // 마커 이미지의 이미지 주소입니다.
      let imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
      // 마커 이미지의 이미지 크기 입니다
      let imageSize = new kakao.maps.Size(24, 35);
      // 마커 이미지를 생성합니다
      let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
      // 마커를 생성합니다.
      let marker = new kakao.maps.Marker();
      // 마커 이름 저장
      marker.setTitle(placeSeq);
      // 마커 위치 불러오기
      latLng = new kakao.maps.LatLng(placeLat, placeLng);
      // 마커 위치 저장
      marker.setPosition(latLng);
      // 지도 위치 저장
      marker.setMap(map);
      // 마커 이미지
      marker.setImage(markerImage);
      // 생성된 마커를 배열에 추가합니다
      markers.push(marker);
    }
  })
}

//업체 그리는 코드를 저장하는 배열입니다.
let placeCommonString = [];

//우측 리스트에 업체를 불러오고 placeCommonString 배열에 업체 그리는 코드를 저장하는 함수입니다.
function placeList(pSeq) {
  let cP = $('#hidden_currentP').val()
  let pCategory = $("#rightRadioCurrentP").val();

  $.ajax ({
    url : "/placeList", 
    type : "post",
    data : {
          city : cityNum,
          currentP: cP,
          pSeq: pSeq,
          pCategory : pCategory
    },
    dataType : "json",
    success:function(data) {
      $('#divPlaceCard').empty();

      for ( let i = 0; i < data.length; i++ ) {
        let placeSeq = data[i].seq;
        let placeName = data[i].name;
        let placeImg = data[i].img;

        let commonString = `
	        <li id='placeCard${placeSeq}' class='placeCard' draggable="true" ondragstart= "drag(event,${placeSeq})" value='${placeSeq}'>
	          <div class='placeImg'>
	            <img class='cartImg' src='${placeImg}'>
	          </div>
	          <div class='placeTitle'>
	            <span id='placeName'><h7>${placeName}</h7></span>
	            <div class='iconFlex' id='${placeSeq}' onclick = 'selectPlaceDelete(this);'>
	              <i id='scheduleDelete'title="목록에서 삭제" class="material-icons">clear</i>
	            </div>
	          </div>
	        </li>`;
        placeCommonString[i] = (commonString);
        html = [];
        html.push(
          "<li class='placeCard column'>",
          "<div class='placeImg'><img class='cartImg' src='",placeImg,"'></div>",
          "<div class='placeTitle'>",
          "<span id='placeName'><h7>",placeName,"</h7></span>",
          "<div class='iconFlex'>",
          "<i title='장소정보' id='",placeSeq,"'class='material-icons info' onclick = 'placeInfo();'>info</i>",
          "<i title='장소추가' id='",placeSeq,"' class='material-icons add' onclick = 'selectPlaceAdd(this,",i,");'>add</i>",
          "</div></div></li>"
        );
        $('#divPlaceCard').append(html.join(""));
      }
    }
  })
}

//일정에 추가된 업체를 저장하는 배열입니다.
let placeSeqList = [];

// //일정에 업체를 추가하는 함수입니다.
function selectPlaceAdd(tagId,ArrayNum) {
  let rightRadioCP = Number($('#rightRadioCurrentP').val());
  let calculation = $('#day').text().split('');
	calculation = calculation[0];
	let placeLiLen = $('#ulPlaceAddCart').children().length;
	let lodgingLiLen = $('#ulLodgingAddCart').children().length;
  let pSeq = "";
	
	if ( rightRadioCP == 1 ) {
		if ( lodgingLiLen == (calculation-1) ) {
			alert('최대개수를 초과하였습니다.');
			return false;
		}
		else {
		  for ( let i = 0; i < placeSeqList.length; i++ ) {
		    if ( tagId.id == placeSeqList[i] ) {
		      return false;
		    }
		  }
		
	    $('#lodgingSelect').click();
			leftRadio('lodgingSelect')
			$('#ulLodgingAddCart').append(placeCommonString[ArrayNum]);
	    for ( let i = 0; i < placeSeqList.length; i++ ) {
	      if ( i != placeSeqList.length-1 ) {
	        pSeq += placeSeqList[i] + ',';
	      }
	      else {
	        pSeq += placeSeqList[i];
	      }
	    }
		}
	}
	else if ( rightRadioCP == 2 ) {
		if ( placeLiLen == (calculation*6) ) {
			alert('최대개수를 초과하였습니다.');
			return false;
		}
		$('#placeSelect').click();
		leftRadio('placeSelect')
		$('#ulPlaceAddCart').append(placeCommonString[ArrayNum]);
    placeSeqList[placeSeqList.length] = (tagId.id);
    for ( let i = 0; i < placeSeqList.length; i++ ) {
      if ( i != placeSeqList.length-1 ) {
        pSeq += placeSeqList[i] + ',';
      }
      else {
        pSeq += placeSeqList[i];
      }
    }
	}
  placeList(pSeq);
  placeListCount(pSeq)
  markerScheduleCreate(tagId.id)
  $('#searchInput').val("");
}

//일정에 추가되어있는 업체를 취소하고 우측 리스트에 다시 나타나게하는 함수입니다.
function selectPlaceDelete(tagId) {
  for ( let i = 0; i < placeSeqList.length; i++ ) {
    if ( placeSeqList[i] == tagId.id ) {
      placeSeqList.splice(i,1);
    }
  }
  let pSeq = "";
  for ( let i = 0; i < placeSeqList.length; i++ ) {
    if ( i != placeSeqList.length-1 ) {
      pSeq += placeSeqList[i] + ',';
    }
    else {
      pSeq += placeSeqList[i];
    }
  }
  for (let i = 0; i < markers.length; i++) {
    if (markers[i].Gb == tagId.id) {
      markers[i].setMap(null)
      markers.splice(i,1);
    }
  }
  placeList(pSeq);
  placeListCount(pSeq);
}

//일정에 추가되어있는 업체를 원하는 업체만 지우는 함수입니다.
function scheduleDelete() {
  $(this).parent().parent().parent().remove();
}

//일정에 추가되어있는 업체를 전부 취소하고 지우는 함수입니다.
function allDelete(tagId) {
  let placeAddCartlen = $('#ulPlaceAddCart').children().length;
  let lodgingAddCartlen = $('#ulLodgingAddCart').children().length;
  let thisId = tagId.id;
  let pSeq = '';
	
	if ( thisId == 'placeAllDelete') {
    for ( let i = 0; i < placeAddCartlen; i++ ) {
      placeDeleteSeq = $('#ulPlaceAddCart').children('li').val();
      for ( let j = 0; j < markers.length; j++ ) {
        if ( placeDeleteSeq == markers[j].Gb ) {
          markers[j].setMap(null);
          markers.splice(j,1);
          $('#ulPlaceAddCart li:eq(0)').remove();
          placeSeqList.splice(j,1);
        }
      }
    }
    for ( let i = 0; i < placeSeqList.length; i++ ) {
      if ( i != placeSeqList.length-1 ) {
        pSeq += placeSeqList[i] + ',';
      }
      else {
        pSeq += placeSeqList[i];
      }
    }
  }
  if ( thisId == 'lodgingAllDelete'){
    for ( let i = 0; i < lodgingAddCartlen; i++ ) {
      lodgingDeleteSeq = $('#ulLodgingAddCart').children('li').val();
      for ( let j = 0; j < markers.length; j++ ) {
        if ( lodgingDeleteSeq == markers[j].Gb ) {
          markers[j].setMap(null);
          markers.splice(j,1);
          $('#ulLodgingAddCart li:eq(0)').remove();
          placeSeqList.splice(j,1);
        }
      }
    }
    for ( let i = 0; i < placeSeqList.length; i++ ) {
      if ( i != placeSeqList.length-1 ) {
        pSeq += placeSeqList[i] + ',';
      }
      else {
        pSeq += placeSeqList[i];
      }
    }
  }
  placeList(pSeq);
}

//업체의 개수를 가져오는 함수입니다.
function placeListCount(pSeq){
  let pCategory = $("#rightRadioCurrentP").val();
	$.ajax({	
		url: "/placeListCount",
		type: "post",
		data: {
			  city: cityNum,
        pSeq: pSeq,
        pCategory: pCategory
		},
		dataType: "json",
		success:function(count) {
			$('#placeListCount').val(count);
			pagination();
		}
	})
}

//페이지네이션 버튼을 누르는 상황마다 이벤트가 발생하는 함수입니다.
function pagination(){
	$("#ul_pageNumber").empty();
	dataLength = Number($("#placeListCount").val());
	thisText = $(this).text();
	last = Math.ceil(dataLength / 8);
	
	cP = Number($("#hidden_currentP").val());
	
	if (thisText == "처음") {
		cP = 1;
		$("#hidden_currentP").val(cP);
	}
	else if (thisText == "이전" && cP - 1 > 0) {
		cP -= 1;
		$("#hidden_currentP").val(cP);
	}
	else if (thisText == "다음" && cP + 1 < last + 1) {
		cP += 1;
		$("#hidden_currentP").val(cP);
	}
	else if (thisText == "마지막") {
		cP = last;
		$("#hidden_currentP").val(cP);
	}
	else if ($.isNumeric(thisText)) {
		cP = Number(thisText);
		$("#hidden_currentP").val(cP);
	}
		
	startIndex = Math.floor(cP / 5) * 5 + 1;
	if (cP % 5 == 0) startIndex = (Math.floor(cP / 5) - 1) * 5 + 1;
	
	if (cP <= 5) {
		if (last > 5) {
			for (i = 1; i <= 5; i++)
			$("#ul_pageNumber").append(`<li id='np${i}' class='page pnum'>${i}</li>`);
		}
		else {
			for (i = 1; i <= last; i++)
			$("#ul_pageNumber").append(`<li id='np${i}' class='page pnum'>${i}</li>`);
		}
	}
	else {
		if (last > startIndex + 4) {
			for (i = startIndex; i <= startIndex + 4; i++) {
				$("#ul_pageNumber").append(`<li id='np${i}' class='page pnum'>${i}</li>`);
			}
		}
		else {
			for (i = startIndex; i <= last; i++) {
				$("#ul_pageNumber").append(`<li id='np${i}' class='page pnum'>${i}</li>`);
			}
		}
	}
	$(`#np${cP}`).css("color","red")
}

//페이지 이동 시 cityNum과 일정에 추가된 업체의 seqNum을 PlaceList에 보내는 함수입니다.
function placeListPageChange() {
  let pSeq = "";
  for ( let i = 0; i < placeSeqList.length; i++ ) {
    if ( i != placeSeqList.length-1 ) {
      pSeq += placeSeqList[i] + ',';
    }
    else {
      pSeq += placeSeqList[i];
    }
  }
  placeList(pSeq);
}

//검색 이벤트 함수 입니다.
function placeSearch() {
  let searchText = $('#searchInput').val();
  let rightRadioCP = Number($('#rightRadioCurrentP').val());
  let cP = $('#hidden_currentP').val()
  let bigCategory = '';
  let pSeq = '';

  if ( rightRadioCP == 1 ) {
    bigCategory = '5,6'
    $('#lodgingSelect').click();
  }
  else if ( rightRadioCP == 2 ) {
    bigCategory = '1,2,3,4'
    $('#placeSelect').click();
  }

  for ( let i = 0; i < placeSeqList.length; i++ ) {
    if ( i != placeSeqList.length-1 ) {
      pSeq += placeSeqList[i] + ',';
    }
    else {
      pSeq += placeSeqList[i];
    }
  }

	if ( searchText == null || searchText == '' ) {
		placeList(pSeq);
	}
	else {
	  $.ajax({
	    url: '/placeSearch',
	    type: 'post',
	    data: {
	          search: searchText,
	          city: cityNum,
	          bigCategory: bigCategory,
	          currentP: cP,
	          pSeq: pSeq
	    },
	    dataType: 'json',
	    success:function(data) {
	      $('#divPlaceCard').empty();
	      for ( let i = 0; i < data.length; i++ ) {
	        let placeSeq = data[i].seq;
	        let placeName = data[i].name;
	        let placeImg = data[i].img;
	
	        let commonString = `
	        <div id='divPlaceCard${placeSeq}'draggable="true" ondragstart= "drag(event)" value='${placeSeq}'>
		        <li id='placeCard${placeSeq}' class='placeCard' value='${placeSeq}'>
		          <div class='placeImg'>
		            <img class='cartImg' src='${placeImg}'>
		          </div>
		          <div class='placeTitle'>
		            <span id='placeName'><h7>${placeName}</h7></span>
		            <div class='iconFlex' id='${placeSeq}' onclick = 'selectPlaceDelete(this);'>
		              <i id='scheduleDelete'title="목록에서 삭제" class="material-icons">clear</i>
		            </div>
		          </div>
		        </li>
		       </div>`
	        placeCommonString[i] = (commonString);
	        html = [];
	        html.push(
	          "<li class='placeCard'>",
	          "<div class='placeImg'><img class='cartImg' src='",placeImg,"'></div>",
	          "<div class='placeTitle'>",
	          "<span id='placeName'><h7>",placeName,"</h7></span>",
	          "<div class='iconFlex'>",
	          "<div title='장소정보' id='placeInfo'><i id='",placeSeq,"'class='material-icons info'>info</i></div>",
	          "<div title='장소추가' id='",placeSeq,"' onclick = 'selectPlaceAdd(this);'><i id='placeAdd' class='material-icons add'>add</i></div>",
	          "</div></div></li>"
	        );
	        $('#divPlaceCard').append(html.join(""));
	      }
	      $('#placeListCount').val(data.length);
				pagination();
	    }
	  })
	}
}

//업체정보를 modal로 생성하는 함수입니다.
function placeInfo() {
  let placeInfoId = $(this).attr('id');
  let infoLen = $('#placeInfoModalContent').children().length;
	
	if ( infoLen == 0 ) {
	  $.ajax({
	    url: "/placeInfo",
	    type: "post",
	    data: { 
	          placeInfoId: placeInfoId
	    },
	    dataType: "json",
	    success:function(data) {
	      $('.modal').css('display','block');
	      placeName = data[data.length-1].name;
	      placeAddress = data[data.length-1].address;
	      placeTel = data[data.length-1].tel;
	      placeContent = data[data.length-1].content;
	      placeImg = data[data.length-1].img;
	      placeOpen = data[data.length-1].open;
	
	      placeOpen = placeOpen.split('/');
	
	      let placeInfo = `
	        <div class="placeInfoModalHeader">
	        <button type="button" class="close" onclick="placeInfoModalClose()">×</button>
	        </div>
	        <div class="placeInfoModalBody">
	          <img id="placeInfoImg" src="${placeImg}" alt="Place Info">
	          <div>
	            <div>
	            <h3 class="placeInfoTitle">${placeName}</h3>
	            <p class="placeInfoContent">${placeContent}</p>
	            <h4 class="modalTitle"><영업시간></h4>
	            <p class="modalOpen">${placeOpen[0]}</p>
	            <p class="modalOpen">${placeOpen[1]}</p>
	            <p class="modalOpen">${placeOpen[2]}</p>
	            <p class="modalOpen">${placeOpen[3]}</p>
	            <p class="modalOpen">${placeOpen[4]}</p>
	            <p class="modalOpen">${placeOpen[5]}</p>
	            <p class="modalOpen">${placeOpen[6]}</p>
	            <h4 class="modalTitle"><주소></h4>
	            <p class="modalAddress">${placeAddress}</p>
	            <h4 class="modalTitle"><전화번호></h4>
	            <p class="modalTel">${placeTel}</p>
	          </div>
	        </div>
	      `
	      $('#placeInfoModalContent').append(placeInfo);
	    }
	  })
  }
  else {
	  return false;
  }
}

//업체정보 modal을 종료하는 함수입니다.
function placeInfoModalClose() {
  $('.modal').css('display','none')
  $('#placeInfo_modal_content').empty();
}

//캘린더에 초기 날짜를 setting하는 함수입니다. (초기setting : 현재날짜 ~ 3일 후 날짜)
function before_date(day){
	let date = new Date();
		let end = new Date(Date.parse(date) + day * 1000 * 60 * 60 * 24);
		let today = new Date(Date.parse(date) - 0 * 1000 * 60 * 60 * 24);
		
		let s_yyyy = today.getFullYear();
		let s_mm = today.getMonth()+1;
		let s_dd = today.getDate();
		
		if(s_mm<10){
			s_mm = "0" + s_mm;
		} if(s_dd<10){
      s_dd = "0" + s_dd;
		}
		
		let e_yyyy = end.getFullYear();
		let e_mm = end.getMonth()+1;
		let e_dd = end.getDate()-1;
		
		if(e_mm<10){
			e_mm = "0" + e_mm;
		} if(e_dd<10){
      e_dd = "0" + e_dd;
		}
		
		let startDate = s_yyyy + "-" + s_mm + "-" + s_dd;
		let endDate = (e_yyyy + "-" + e_mm + "-" + e_dd);
		
		$('#startDate').val(startDate);
		$('#endDate').val(endDate);
  }

// 캘린더의 옵션을 setting하는 함수입니다.
function datePicker() {
  $('#startDate').datepicker({
		changeMonth: true,
		changeYear: true,
		dateFormat: "yy-mm-dd",
		currentText: "오늘",
		closeText: "닫기",
    minDate: 0,
		dayNames: ['월요일','화요일','수요일','목요일','금요일','토요일','일요일'],
		dayNamesMin: ['월','화','수','목','금','토','일'],
		monthNamesShort:['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
  });
  $('#endDate').datepicker({
		changeMonth: true,
		changeYear: true,
		dateFormat: "yy-mm-dd",
		currentText: "오늘",
		closeText: "닫기",
    minDate: 0,
		dayNames: ['월요일','화요일','수요일','목요일','금요일','토요일','일요일'],
		dayNamesMin: ['월','화','수','목','금','토','일'],
		monthNamesShort:['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
  });
}

//여행 시작날짜와 종료날짜의 기간계간 함수입니다.
function dateCalculation() {
  let startDate = $('#startDate').val();
  let endDate = $('#endDate').val();

  startDate = startDate.split('-');
  endDate = endDate.split('-');
  
  startDate = new Date(startDate[0], Number(startDate[1])-1, startDate[2]);
  endDate = new Date(endDate[0], Number(endDate[1])-1, endDate[2]);
  let calculation = Number(endDate) - Number(startDate);
  let cDay = 24 * 60 * 60 * 1000;
  
  dateDifference = 0;
  
  dateDifference = parseInt(calculation/cDay) + 1;
  
  changeDayCount = `${dateDifference} DAY`;
  $('#day').text(changeDayCount);
}

//우측 사이드바에서 숙박&명소 클릭 시 이벤트발생 함수입니다.
function rightRadio(tagId) {
  let thisId = tagId.id
  let rightRadioCp = Number($("#rightRadioCurrentP").val());
  
  if (thisId == 'lodging') {
    rightRadioCp = 1;
    $("#rightRadioCurrentP").val(rightRadioCp);
  }
  else if (thisId == 'place') {
    rightRadioCp = 2;
    $("#rightRadioCurrentP").val(rightRadioCp);
  }
  let pSeq = '';
  for ( let i = 0; i < placeSeqList.length; i++ ) {
    if ( i != placeSeqList.length-1 ) {
      pSeq += placeSeqList[i] + ',';
    }
    else {
      pSeq += placeSeqList[i];
    }
  }
  placeList(pSeq);
  placeListCount(pSeq);
}

//좌측 사이드바에 숙박&명소 클릭 시 이벤트발생 함수입니다.
function leftRadio(tagId) {
  let thisId = tagId.id;
  let leftRadioCP = Number($("#leftRadioCurrentP").val());
  
  if ( thisId == 'lodgingSelect') {
    leftRadioCP = 1;
    $("#leftRadioCurrentP").val(leftRadioCP);
    $('#leftRadioSelectLodging').attr('style','display: block')
    $('#leftRadioSelectPlace').attr('style','display: none')
    
  }
  else if ( thisId == 'placeSelect') {
    leftRadioCP = 2;
    $("#leftRadioCurrentP").val(leftRadioCP);
    $('#leftRadioSelectPlace').attr('style','display: block')
    $('#leftRadioSelectLodging').attr('style','display: none')
  }
}

//일정상세페이지 modal을 생성하는 함수입니다.
function scheduleDetailCreate() {
  $('.scheduleModal').css('display','block');
  let calculation = $('#day').text().split('');
  calculation = calculation[0];
	
  $.ajax({
    url: "/mapCreate",
    type: "post",
    data: {city: cityNum},
    dataType: "json",
    success:function(data) {
        cityLat = data[data.length-1].lat
        cityLng = data[data.length-1].lng
        
        let scheduleMapContainer = document.getElementById("scheduleMap"), // 지도를 표시할 div
        scheduleMapOption = {
          center: new kakao.maps.LatLng(cityLat, cityLng), // 지도의 중심좌표
          level: 9, // 지도의 확대 레벨
        }
        // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
        scheduleMap = new kakao.maps.Map(scheduleMapContainer, scheduleMapOption)
    }
  });
  $('#rightScheduleModalRadioPlace').trigger('click');
  $('#leftScheduleModalRadioPlace').trigger('click');

  $('#placeAddCartCopy').empty();
  $('#ulPlaceAddCart').clone().appendTo('#placeAddCartCopy');
  $('#lodgingAddCartCopy').empty();
  $('#ulLodgingAddCart').clone().appendTo('#lodgingAddCartCopy');

  for ( let i = 1; i < calculation; i++ ) {
    $('#dayListButtonArea').append(`
    <div class="divDayButton" onclick="openDayDetailPlan(${i})">
      <div id="dayButton${i}" class="dayButton">
      <h7>${i}DAY</h7>
    </div>
    `);
    $('#divScheduleModalLeftSidebar').append(`
			<div class="scheduleDate${i} scheduleStyle" style="display: none;">
				<div id="leftRadioSelectPlace${i}" class="modalContainer" style="display: none;">
					<ul id="ulPlaceContainer${i}" ondrop="drop(event)" class="modalLeftRadioSelectCss" ondragover="allowDrop(event);"></ul>
				</div>
				<div id="leftRadioSelectLodging${i}" class="modalContainer" style="display: none;">
					<ul id="ulLodgingContainer${i}"ondrop="drop(event)" class="modalLeftRadioSelectCss" ondragover="allowDrop(event);"></ul>
				</div>
			</div>
    `)
  }
  $('.leftRadioSelectPlaceCss').empty();
	$('#dayButton1').trigger('click').css('background-color','red');
	$('.scheduleDate1').attr('style','display:block;');
  $('#leftRadioSelectPlace1').attr('style','display:block')
	$('#placeAddCartCopy').attr('style','display: block')
	$('#lodgingAddCartCopy').attr('style','display: none')
	$('.ulPlaceAddCartCss').attr('style','height: 697px;')
	$('.ulLodgingAddCartCss').attr('style','height: 697px;')
}

//일정상세페이지 modal을 종료하는 함수입니다.
function scheduleModalClose() {
	let calculation = $('#day').text().split('');
  calculation = calculation[0];
  
	for ( let i = 1; i < calculation; i++ ) {
		let placeAddCartlen = $(`#ulPlaceContainer${i}`).children().length;
		for ( let j = 0; j < placeAddCartlen; j++ ) {
	    placeDeleteSeq = $(`#ulPlaceContainer${i}`).children('li').val();
			for ( let k = 0; k < modalMarkers.length; k++ ) {
			  if ( placeDeleteSeq == modalMarkers[k].Gb ) {
			    modalMarkers[k].setMap(null);
			    modalMarkers.splice(k,1);
			  }
			}
		}
	}
	for ( let i = 1; i < calculation; i++ ) {
	  let lodgingAddCartlen = $(`#ulLodgingContainer${i}`).children().length;
		for ( let j = 0; j < lodgingAddCartlen; j++ ) {
	    placeDeleteSeq = $(`#ulPlaceContainer${i}`).children('li').val();
			for ( let k = 0; k < modalMarkers.length; k++ ) {
			  if ( placeDeleteSeq == modalMarkers[k].Gb ) {
			    modalMarkers[k].setMap(null);
			    modalMarkers.splice(k,1);
			  }
			}
		}
	}
	$('.scheduleModal').css('display','none');
	$('.ulPlaceAddCart').removeAttr('style');
	$('#ulPlaceAddCart').removeAttr('style');
	$('.ulLodgingAddCartCss').removeAttr('style');
	$('#ulLodgingAddCart').removeAttr('style');
	$('.divDayButton').remove();
	$('.scheduleStyle').remove();
}

// 일정상세페이지에서 우측 사이드바의 숙박&명소 버튼을 클릭 시 이벤트 함수입니다.
function rightScheduleModalRadio(tagId) {
	let thisId = tagId.id;
	let rightScheduleModalRadioCP = Number($('#rightScheduleModalRadioCurrentP').val());
	
	if ( thisId == 'rightScheduleModalRadioLodging') {
    rightScheduleModalRadioCP = 1;
    $("#rightScheduleModalRadioCurrentP").val(rightScheduleModalRadioCP);
    $('#lodgingAddCartCopy').attr('style','display: block')
    $('#placeAddCartCopy').attr('style','display: none')
    
  }
  else if ( thisId == 'rightScheduleModalRadioPlace') {
    rightScheduleModalRadioCP = 2;
    $("#rightScheduleModalRadioCurrentP").val(rightScheduleModalRadioCP);
    $('#placeAddCartCopy').attr('style','display: block')
    $('#lodgingAddCartCopy').attr('style','display: none')
  }
}

// 일정상세페이지에서 좌측 사이드바의 숙박&명소 버튼을 클릭 시 이벤트 함수입니다.
function leftScheduleModalRadio(tagId){
	let thisId = tagId.id;
	let leftScheduleModalRadioCP = Number($('#leftScheduleModalRadioCurrentP').val());
	let dayListButtonAreaCP = Number($('#dayListButtonAreaCurrentP').val());
	
	if ( thisId == 'leftScheduleModalRadioLodging') {
    leftScheduleModalRadioCP = 1;
    $("#leftScheduleModalRadioCurrentP").val(leftScheduleModalRadioCP);
    $(`#leftRadioSelectLodging${dayListButtonAreaCP}`).attr('style','display: block;')
    $(`#leftRadioSelectPlace${dayListButtonAreaCP}`).attr('style','display: none;')
    
  }
  else if ( thisId == 'leftScheduleModalRadioPlace') {
    leftScheduleModalRadioCP = 2;
    $("#leftScheduleModalRadioCurrentP").val(leftScheduleModalRadioCP);
    $(`#leftRadioSelectPlace${dayListButtonAreaCP}`).attr('style','display: block;')
    $(`#leftRadioSelectLodging${dayListButtonAreaCP}`).attr('style','display: none;')
  }
}

// 일정상세페이지에서 일별 버튼 클릭 시 이벤트 함수입니다.
function openDayDetailPlan(buttonNum) {
	let bNum = buttonNum;
	let leftScheduleModalRadioCP = Number($('#leftScheduleModalRadioCurrentP').val());
	
	$('.dayButton').css('background-color','white');
	$(`#dayButton${bNum}`).css('background-color','red');
	$('#dayListButtonAreaCurrentP').val(bNum);
	
	if ( leftScheduleModalRadioCP == 1) {
		$('.scheduleStyle').attr('style','display:none;')
		$(`.scheduleDate${bNum}`).attr('style','display:block;')
		$('.modalContainer').attr('style','display:none;')
		$(`#leftRadioSelectLodging${bNum}`).attr('style','display:block;')
	}
	else if (leftScheduleModalRadioCP == 2) {
		$('.scheduleStyle').attr('style','display:none;')
		$(`.scheduleDate${bNum}`).attr('style','display:block;')
		$('.modalContainer').attr('style','display:none;')
		$(`#leftRadioSelectPlace${bNum}`).attr('style','display:block;')
	}
}

// 드래그앤드롭 이벤트에 사용되는 전역변수
let dragEl;
let dropEl;
let modalMarkers = [];

// 드래그앤드롭 이벤트 함수들입니다.
let drag = function(ev,pSeq){
	dragEl = ev.target;
	markerSeq = pSeq;
	
}
let allowDrop = function(ev){
	ev.preventDefault();
	
}
let drop = function(ev){
	let rightScheduleModalRadioCP = $('#rightScheduleModalRadioCurrentP').val();
	let leftScheduleModalRadioCP = $('#leftScheduleModalRadioCurrentP').val();
	if(ev.target.tagName == 'ul' || ev.target.tagName == 'UL'){
		dropEl = ev.target;
		if ( dragEl.tagName == 'img' || dragEl.tagName == 'IMG') {
			return false;
		}
		else if ( rightScheduleModalRadioCP != leftScheduleModalRadioCP) {
			return false;
		}
		else {
			dropEl.append(dragEl);
			if ( ev.target.id == 'ulPlaceAddCart' || ev.target.id == 'ulLodgingAddCart') {
			  for (let i = 0; i < modalMarkers.length; i++) {
			    if (modalMarkers[i].Gb == markerSeq) {
			      modalMarkers[i].setMap(null)
			      modalMarkers.splice(i,1);
			    }
			  }
			}
			else {
				$.ajax({
			    url : "/markerScheduleCreate",
			    type : "post",
			    data : {
			            pSeq : markerSeq
			    },
			    dataType : "json",
			    success:function(data) {
			      let placeSeq = data[data.length-1].seq
			      let placeLat = data[data.length-1].lat
			      let placeLng = data[data.length-1].lng
			
			      // 마커 이미지의 이미지 주소입니다.
			      let imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
			      // 마커 이미지의 이미지 크기 입니다
			      let imageSize = new kakao.maps.Size(24, 35);
			      // 마커 이미지를 생성합니다
			      let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
			      // 마커를 생성합니다.
			      let marker = new kakao.maps.Marker();
			      // 마커 이름 저장
			      marker.setTitle(placeSeq);
			      // 마커 위치 불러오기
			      latLng = new kakao.maps.LatLng(placeLat, placeLng);
			      // 마커 위치 저장
			      marker.setPosition(latLng);
			      // 지도 위치 저장
			      marker.setMap(scheduleMap);
			      // 마커 이미지
			      marker.setImage(markerImage);
			      // 생성된 마커를 배열에 추가합니다
			      modalMarkers.push(marker);
			    }
		  	})			
			}
		}
	}
	else if(ev.target.tagName == 'li' || ev.target.tagName == 'LI'){
		dropEl = ev.target;
		let ul = ev.target.parentNode;
		ul.insertBefore(dragEl, dropEl);
	}
}

// 일정상세페이지에서 일정저장 버튼을 클릭 시 이벤트함수입니다.
function modalSaveButton() {
	let calculation = $('#day').text().split('');
	calculation = calculation[0];
	let scheduleData = '';
	let startDate = $('#startDate').val();
	let endDate = $('#endDate').val();
	let scheduleDate = startDate+'~'+endDate;
	let siteNum;
	
	for ( let i = 1; i < calculation; i++ ) {
		let placeLiLen = $(`#ulPlaceContainer${i}`).children().length;
		let lodgingLiLen = $(`#ulLodgingContainer${i}`).children().length;
			scheduleData += `day${i}/`;
		for ( let j = 0; j < placeLiLen; j++ ) {
			let placeSeq = $(`#ulPlaceContainer${i} li:eq(${j})`).val();
			scheduleData += placeSeq+',';
		}
		for ( let k = 0; k < lodgingLiLen; k++ ) {
			let lodgingSeq = $(`#ulLodgingContainer${i} li:eq(${k})`).val();
			if ( i == calculation-1 && k == lodgingLiLen-1 ) {
				scheduleData += lodgingSeq;				
			}
			else {
				scheduleData += lodgingSeq+'-';
			}
		}
	}
	
	if ( siteName == 'schedulecreate') {
		siteNum = 1;
	}
	
	else if ( siteName == 'scheduleupdate') {
		siteNum = 2;
	}

	$.ajax({
		url: "/modalSaveButton",
		type: "post",
		data: {
			city: cityNum,
			sData: scheduleData,
			sDays: scheduleDate,
			siteNum: siteNum,
			scheduleSeq: scheduleSeq
		},
		dataType: "text",
		success:function(check) {
			if ( check == 'true') {
				scheduleModalClose();
				$('#placeAllDelete').click();
				$('#lodgingAllDelete').click();
				
			}
		}
	})
}

scheduleUpdateCommonString = [];

function scheduleupdateimport() {
	let pSeq = '';
	$.ajax({
		url: "/scheduleupdateimport",
		type: "post",
		data: {
			scheduleSeq : scheduleSeq
		},
		dataType: "json",
		success:function(data) {
			console.log(data);
			let scheduleDays = data[data.length-1].days;
			scheduleDays = scheduleDays.split('~');
			let startDate = scheduleDays[0];
			let endDate = scheduleDays[1];
			$('#startDate').val(startDate);
			$('#endDate').val(endDate);
			for ( let i = 0; i < (data.length-1); i++ ) {
				placeSeq = data[i].seq;
				placeCategory = Number(data[i].category);
				placeName = data[i].name;
				placeImg = data[i].img;
				
				let commonString = `
				        <li id='placeCard${placeSeq}' class='placeCard' draggable="true" ondragstart= "drag(event,${placeSeq})" value='${placeSeq}'>
				          <div class='placeImg'>
				            <img class='cartImg' src='${placeImg}'>
				          </div>
				          <div class='placeTitle'>
				            <span id='placeName'><h7>${placeName}</h7></span>
				            <div class='iconFlex' id='${placeSeq}' onclick = 'selectPlaceDelete(this);'>
				              <i id='scheduleDelete'title="목록에서 삭제" class="material-icons">clear</i>
				            </div>
				          </div>
				        </li>`;
				scheduleUpdateCommonString[i] = (commonString);
				if ( placeCategory == 5 || placeCategory == 6 ) {
					$('#ulLodgingAddCart').append(scheduleUpdateCommonString[i]);					
				}
				else {
					$('#ulPlaceAddCart').append(scheduleUpdateCommonString[i]);
				}
				placeSeqList[placeSeqList.length] = (placeSeq);
				if ( i == data.length-2 ) {
					pSeq += placeSeqList[i];
				}
				else {
					pSeq += placeSeqList[i] + ',';
				}
				markerScheduleCreate(placeSeq);
			}
			placeList(pSeq);
		}
	})
	
	
}