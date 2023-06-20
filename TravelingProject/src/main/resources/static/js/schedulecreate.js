$(document)
.ready(mapCreate)
.ready(before_date(3))
.ready(datePicker)
.ready(function() {	
	$('input:radio[name=select]').attr('checked',true);
	$('input:radio[name=leftSelect]').attr('checked',true);
	$('#leftRadioSelectPlace').attr('style','display: block')
	$('#leftRadioSelectLodging').attr('style','display: none')
})
.ready(function() {
  $('#searchInput').keypress(function(key) {
    if (key.keyCode == 13) {
      search();
      event.preventDefault();
    }
  })
})

.on('click', '#search', search) // 검색 이벤트
.on('click', '.page', pagination) //페이지네이션 이벤트
.on('click', '.page', placeListPageChange) //페이지에 맞는 리스트 구성을 위한 이벤트
.on('click', '#placeAdd', placeAppend) //업체 일정에 추가 이벤트
.on('click', '#scheduleDelete', scheduleDelete) //일정에 추가되어 있는 업체를 선택부분만 삭제하는 이벤트
.on('change', '.calender', dateCalculation) //여행의 시작날짜와 종료날짜의 기간계산을 위한 이벤트
.on('click', '.info', placeInfo) //업체정보를 띄우기 위한 이벤트
.on('click', '#scheduleCreate', scheduleDetailCreate) //일정생성 이벤트
.on('click', '#scheduleModalClose', scheduleModalClose) // 모달을 닫는 함수

// URL에 cityNum과 cityName을 변수에 저장하는 코드
tempAry = window.location.pathname.split('/');
cityNum = tempAry[2];
cityName = decodeURIComponent(tempAry[3]);

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
        placeListCount(cityNum,null);
        placeList(cityNum,null)
        $('#cityName').text(cityName);
      }
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
function placeList(city,pSeq) {
  let cP = $('#hidden_currentP').val()
  let pCategory = $("#rightRadioCurrentP").val();

  $.ajax ({
    url : "/placeList", 
    type : "post",
    data : {
          city : city,
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
	        <li id='placeCard${placeSeq}' class='placeCard' draggable="true" ondragstart= "drag(event)" value='${placeSeq}'>
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
					"<div>",
          "<li class='placeCard column'>",
          "<div class='placeImg'><img class='cartImg' src='",placeImg,"'></div>",
          "<div class='placeTitle'>",
          "<span id='placeName'><h7>",placeName,"</h7></span>",
          "<div class='iconFlex'>",
          "<div title='장소정보' id='placeInfo'><i id='",placeSeq,"'class='material-icons info'>info</i></div>",
          "<div title='장소추가' id='",placeSeq,"' onclick = 'selectPlaceAdd(this);'><i id='placeAdd' class='material-icons add'>add</i></div>",
          "</div></div></li></div>"
        );
        $('#divPlaceCard').append(html.join(""));
      }
    }
  })
}

//일정에 추가된 업체를 저장하는 배열입니다.
let placeSeqList = [];

// //일정에 업체를 추가하는 함수입니다.
function selectPlaceAdd(tagId) {
  let rightRadioCP = Number($('#rightRadioCurrentP').val());
  let pSeq = "";

  for ( let i = 0; i < placeSeqList.length; i++ ) {
    if ( tagId.id == placeSeqList[i] ) {
      return false;
    }
  }

  if ( rightRadioCP == 1 ) {
    for ( let i = 0; i < placeSeqList.length; i++ ) {
      if ( i != placeSeqList.length-1 ) {
        pSeq += placeSeqList[i] + ',';
      }
      else {
        pSeq += placeSeqList[i];
      }
    }
  }
  else if ( rightRadioCP == 2 ) {
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
  placeList(cityNum,pSeq);
  placeListCount(cityNum,pSeq)
  markerScheduleCreate(tagId.id)
  $('#searchInput').val("");
}

//좌측 리스트에 추가하는 업체를 그리는 함수입니다.
function placeAppend() {
  let rightRadioCP = Number($('#rightRadioCurrentP').val());

  if ( rightRadioCP == 1 ) {
    $('#lodgingSelect').click();
    leftRadio('lodgingSelect')
    selectPlace = $(this).parent().parent().parent().parent().index();
    $('#ulLodgingAddCart').append(placeCommonString[selectPlace]);
  }
  else if ( rightRadioCP == 2 ) {
    $('#placeSelect').click();
    leftRadio('placeSelect')
    selectPlace = $(this).parent().parent().parent().parent().parent().index();
    $('#ulPlaceAddCart').append(placeCommonString[selectPlace]);
  }
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
  placeList(cityNum,pSeq);
  placeListCount(cityNum,pSeq);
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

  if ( thisId == 'placeAllDelete') {
    for ( let i = 0; i < placeAddCartlen; i++ ) {
      placeDeleteSeq = $('#ulPlaceAddCart').children('li').val();
      for ( let j = 0; j < markers.length; j++ ) {
        if ( placeDeleteSeq == markers[j].Gb ) {
          markers[j].setMap(null);
          markers.splice(j,1);
          $('#ulPlaceAddCart ul:eq(0)').remove();
          placeSeqList.splice(j,1);
        }
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
    $('#ulPlaceAddCart').empty();
    placeList(cityNum,pSeq);
  }
  else if ( thisId == 'lodgingAllDelete' ) {
    for ( let i = 0; i < lodgingAddCartlen; i++ ) {
      lodgingDeleteSeq = $('#ulLodgingAddCart').children('li').val();
      for ( let j = 0; j < markers.length; j++ ) {
        if ( lodgingDeleteSeq == markers[j].Gb ) {
          markers[j].setMap(null);
          markers.splice(j,1);
          $('#ulLodgingAddCart ul:eq(0)').remove();
          placeSeqList.splice(j,1);
        }
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
    $('#ulLodgingAddCart').empty();
    placeList(cityNum,pSeq);
  }
}

//업체의 개수를 가져오는 함수입니다.
function placeListCount(city,pSeq){
  pCategory = $("#rightRadioCurrentP").val();
	$.ajax({	
		url: "/placeListCount",
		type: "post",
		data: {
			  city: city,
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
  placeList(cityNum,pSeq);
}

//검색 이벤트 함수 입니다.
function placeSearch() {
  searchText = $('#searchInput').val();
  let rightRadioCP = Number($('#rightRadioCurrentP').val());
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

  $.ajax({
    url: '/placeSearch',
    type: 'post',
    data: {
          search: searchText,
          bigCategory: bigCategory,
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

//업체정보를 modal로 생성하는 함수입니다.
function placeInfo() {
  let placeInfoId = $(this).attr('id');

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
        <div class="placeInfo_modal_header">
        <button type="button" class="close" onclick="placeInfoModalClose()">×</button>
        </div>
        <div class="placeInfo_modal_body">
          <img id="placeInfoImg" src="${placeImg}" alt="Place Info">
          <div>
            <div>
            <h2 class="placeInfo_title">${placeName}</h2>
            <p class="placeInfo_content">${placeContent}</p>
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
      $('#placeInfo_modal_content').append(placeInfo);
    }
  })
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

  //캘린더의 옵션을 setting하는 함수입니다.
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
  placeList(cityNum,pSeq);
  placeListCount(cityNum,pSeq);
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
  let city = cityNum;
	
  $.ajax({
    url: "/mapCreate",
    type: "post",
    data: {city: city},
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
  $('input:radio[name=rightScheduleModalRadio]').attr('checked',true);
  $('input:radio[name=leftScheduleModalRadio]').attr('checked',true);

  $('#placeAddCartCopy').empty();
  $('#ulPlaceAddCart').clone().appendTo('#placeAddCartCopy');
  $('#lodgingAddCartcopy').empty();
  $('#ulLodgingAddCart').clone().appendTo('#lodgingAddCartCopy');

  $('.addScheduleByDate').empty();
  $('#dayListButtonArea div:eq(0)').empty();
  for ( let i = 1; i < calculation; i++ ) {
    $('#dayListButtonArea').append(`
    <div class="divDayButton" onclick="openDayDetailPlan(${i})">
      <div id="dayButton${i}" class="dayButton">
      <h7>${i}DAY</h7>
    </div>
    `);
  }
	$('#dayButton1').trigger('click').css('background-color','red');
	$('.scheduleDate1').attr('style','display:block;');
	$('#placeAddCartCopy').attr('style','display: block')
	$('#lodgingAddCartCopy').attr('style','display: none')
	$('#ulPlaceAddCart').attr('style','height: 697px;')
}

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

function openDayDetailPlan(buttonNum) {
	let bNum = buttonNum;
	
	$('.dayButton').css('background-color','white');
	$(`#dayButton${bNum}`).css('background-color','red');
	$('#dayListButtonAreaCurrentP').val(bNum);
	$('.scheduleStyle').attr('style','display:none;')
	$(`.scheduleDate${bNum}`).attr('style','display:block;')
	$('.modalContainer').attr('style','display:none;')
	$(`#leftRadioSelectPlace${bNum}`).attr('style','display:block;')
	$('input:radio[name=leftScheduleModalRadio]').attr('checked',true);
}

let dragEl;
let dropEl;

let drag = function(ev){
	dragEl = ev.target;
}
let allowDrop = function(ev){
	ev.preventDefault();
}
let drop = function(ev){
	if(ev.target.tagName == 'ul' || ev.target.tagName == 'UL'){
		dropEl = ev.target;
		dropEl.append(dragEl);
	} else if(ev.target.tagName == 'li' || ev.target.tagName == 'LI'){
		dropEl = ev.target;
		let ul = ev.target.parentNode;
		ul.insertBefore(dragEl, dropEl);
	}
}

//일정상세페이지 modal을 종료하는 함수입니다.
function scheduleModalClose() {
	$('.scheduleModal').css('display','none');
	$('#ulPlaceAddCart').removeAttr('style');
}

// 일정 상세경로 클릭 시 길찾기
function roadFind() {
	let options =
	"top=15, left=80, width=1100, height=900, status=no, menubar=no, toolbar=no, resizable=no";
	window.open(
	"Https://map.kakao.com/?sName=천안시청&eName=휴먼교육센터",
	"길찾기",
	options
	);
}