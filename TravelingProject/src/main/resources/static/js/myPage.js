$(document)
.ready(function(){
	$.ajax({
		url:"/loadMyPageSchedule",
		type:"post",
		data:{},
		dataType:"json",
		success:function(data){
			let div1 = ''
			let div2 = ''
			let div3 = ''
			console.log(data)
			for(i=0; i<data.length; i++){
				let cityImg = '<div class="ScheduleImg"><div class="ImgContainer"><img src="/img/'+data[i]['cityImg']+'"></div>'
				let cityName = '<div class="NameContainer"><span class="CityName">'+data[i]['cityName']+'</span></div></div>'
				let scheduleDate = '<div class="ScheduleInfo"><div class="DateContainer">'+'<span class="textDate">여행 일자</span>'+'<span class="textUpdate">수정 일자</span>'
				let scheduleUpdateDate = '<span class="scheduleDate">'+data[i]['scheduleDays']+'</span><span class="scheduleUpdated">'+data[i]['scheduleUpdated']+'</span></div>'
				let scheduleButton = '<div class="ButtonContainer"><button id="ScheduleModify">일정수정</button><button id="ScheduleDelete">일정삭제</button></div></div>'
				div1 = cityImg+cityName
				div2 = scheduleDate + scheduleUpdateDate + scheduleButton
				div3 = '<div class="ScheduleContainer">'+div1+div2+'</div>'
				$('.MainSchedule').append(div3)
			}
				
		}
		
	})
})