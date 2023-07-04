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
			for(i=0; i<data.length; i++){
				let cityImg = '<div class="ScheduleImg"><div class="ImgContainer"><img src="/img/'+data[i]['cityImg']+'"></div>'
				let cityName = '<div class="NameContainer"><span class="CityName">'+data[i]['cityName']+'</span></div></div>'
				let scheduleDate = '<div class="ScheduleInfo"><div class="DateContainer">'+'<span class="textDate">여행 일자</span>'+'<span class="textUpdate">수정 일자</span>'
				let scheduleUpdateDate = '<span class="scheduleDate">'+data[i]['scheduleDays']+'</span><span class="scheduleUpdated">'+data[i]['scheduleUpdated']+'</span></div>'
				let scheduleButton = `<div class="ButtonContainer"><button id="ScheduleModify" onclick="scheduleModify(${data[i]['citySeq']},'${data[i]['cityName']}',${data[i]['schedule_seq']})">일정수정</button><button id="ScheduleDelete" onclick ="scheduleDelete(${data[i]['schedule_seq']})">일정삭제</button></div></div>`
				div1 = cityImg+cityName
				div2 = scheduleDate + scheduleUpdateDate + scheduleButton
				div3 = '<div class="ScheduleContainer">'+div1+div2+'</div>'
				$('.MainSchedule').append(div3)
			}
		}
	})
})

.on('click','#ScheduleDelete', scheduleTagDelete)
.on('click', '#change', pwChangeModal)
.on('click','#modify',modifyInfo)
.on('click','#btnInfoChange',function(){
	let id = $('#infoHiddenId').val()
	let nickname = $('#chageNicknameText').html()
	let eMail = $('#chageMailText').html()
	if(nickname == '사용가능한 닉네임입니다.' || eMail == "사용가능한 이메일 주소입니다."){
		updateInfo(id)
	}else{
		alert('다시 입력해주세요.')
		return false;
	}
})
.on('click','#btnInfoCancel',function(){
	document.location="/myPage"
})
.on('keyup','#changeNickname',function(){
		$.ajax({url:"/nickName_check", type:'post', data:{aka:$('#changeNickname').val()}, dataType:'text',
			beforeSend:function(){
				if($('#changeNickname').val()=='' || $('#changeNickname').val()==null){
					$('#chageNicknameText').html('닉네임을 입력하세요.');
					$('#chageNicknameText').attr('color', 'red');
					$(this).focus();
					return false;
				}
			},success:function(data){
				if(data=="ok"){
					$('#chageNicknameText').html('사용중인 닉네임입니다.');
					$('#chageNicknameText').attr('color', 'red');
				} else {
					$('#chageNicknameText').html('사용가능한 닉네임입니다.');
					$('#chageNicknameText').attr('color', 'green');
				}
				
			}
		})
})
function scheduleModify(citySeq,cityName,scheduleSeq) {
	document.location = "/scheduleupdate/" + citySeq + "/" + cityName + "/" + scheduleSeq;
}

function scheduleDelete(scheduleSeq) {
	$.ajax({
		url: "/scheduledelete",
		type: "post",
		data: {
			scheduleSeq: scheduleSeq
		},
		dataType: "text",
		success:function(check) {
			if ( check == 'true') {
				alert('삭제가 완료되었습니다.');
				
			}
		}
	})
}

function scheduleTagDelete() {
	$(this).parent().parent().parent().remove();
	
}

function pwChangeModal() {
	$('.modal').css('display','block');
}
function modifyInfo(){
	$('.infoModal').css('display','block')
}
function updateInfo(id){
	let mail = $('#changeMail').val()
	let nickname = $('#changeNickname').val()
	let tel = $('#telInput').val()
	$.ajax({
		url:"/memberInfoModify",
		type:"post",
		data:{id:id,mail:mail,nickname:nickname,tel:tel},
		dataType:"text",
		before:function(){
			if(mail==''){
				alert('이메일 주소를 입력하세요')
			}
			if(nickname==''){
				alert('닉네임을 입력하세요')
			}
			if(tel==''){
				alert('전화번호를 입력하세요')
			}
		},
		success:function(data){
			$('#changeMail').val('')
			$('#changeNickname').val('')
			$('#telInput').val('')
			$('#btnInfoCancel').trigger('click')
		}
	})
}
//전화번호
$('#telInput').keyup(function(event){
	event=event || window.event
	var _val = this.value.trim()
	this.value = autoHypenTel(_val)
})
function autoHypenTel(str){
	str = str.replace(/[^0-9]/g, '')
	var tmp = ''
	
	if (str.substring(0,2) == 02){
		if (str.length < 3){
			return str
		} else if (str.length < 6){
			tmp += str.substring(0,2)
			tmp += '-'
			tmp += str.substring(2)
			return tmp
		} else if (str.length < 10){
			tmp += str.substring(0,2)
			tmp += '-'
			tmp += str.substring(2,3)
			tmp += '-'
			tmp += str.substring(5)
			return tmp
		} else {
			tmp += str.substr(0, 2)
			tmp += '-'
			tmp += str.substr(2, 4)
			tmp += '-'
			tmp += str.substr(6, 4)
			return tmp
		}
	} else{
		if (str.length < 4) {
			return str
    	} else if (str.length < 7) {
			tmp += str.substr(0, 3)
			tmp += '-'
			tmp += str.substr(3)
			return tmp
	    } else if (str.length < 11) {
			tmp += str.substr(0, 3)
			tmp += '-'
			tmp += str.substr(3, 3)
			tmp += '-'
			tmp += str.substr(6)
			return tmp
	    } else {
			tmp += str.substr(0, 3)
			tmp += '-'
			tmp += str.substr(3, 4)
			tmp += '-'
			tmp += str.substr(7)
			return tmp
	    }
	}
	return str
}
function validEmail(obj){
    if(validEmailCheck(obj)==false){
		$('#chageMailText').attr('color','red')
		$('#chageMailText').html("올바른 이메일 주소를 입력해주세요.")
        obj.focus();
        return false;
    }else{
		$('#chageMailText').attr('color','green')
		$('#chageMailText').html("사용가능한 이메일 주소입니다.")
		return false;
	}
}
function validEmailCheck(obj){
    var pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    return (obj.value.match(pattern)!=null)
}