$(document)
.on('keyup','#nowPw',function(){
	$.ajax({url:"nowPwCheck", type:'post', data:{nowPw:$('#nowPw').val(), id:$('#hidden').val()}, dataType:'text',
		beforeSend:function(){
			if($('#nowPw').val()=='' || $('#nowPw').val()==null){
				$('#nowPwText').html('현재 비밀번호를 입력하세요.');
				$('#nowPwText').attr('color', 'red');
				$(this).focus();
				return false;
			}
		},success:function(data){
			if(data=="ok"){
				$('#nowPwText').html('현재 비밀번호 확인.');
				$('#nowPwText').attr('color', 'green');
			} else {
				$('#nowPwText').html('비밀번호가 다릅니다.');
				$('#nowPwText').attr('color', 'red');
			}
			
		}
	})
})
.on('click','#btnChange',function(){
	$.ajax({
		url:"changeMemberPw",
		type:"post",
		data:{id:$('#hidden').val(),newPw:$('#changePw').val(),chkNewPw:$('#chkChangePw').val()},
		dataType:'text',
		success:function(data){
			if(data=="ok"){
				alert('비밀번호가 변경되었습니다.')
					$('#nowPw').val('')
					$('#changePw').val('')
					$('#chkChangePw').val('')
					document.location="/main"
			}else{
				alert('비밀번호가 변경오류.')
				$('#nowPw').val('')
				$('#changePw').val('')
				$('#chkChangePw').val('')
				return false;
			}
		}
	})
})
.on('click','#btnCancel',function(){
	$('#nowPw').val('')
	$('#changePw').val('')
	$('#chkChangePw').val('')
	document.location="/main"
})
.on('keyup','#chkChangePw',function(){
	let pw=$('#changePw').val()
	let chk=$('#chkChangePw').val()
	console.log(pw)
	console.log(chk)
	if(pw==chk){
		$('#chkChangePwText').attr('color','green')
		$('#chkChangePwText').html('일치')	
	}else{
		$('#chkChangePwText').attr('color','red')
		$('#chkChangePwText').html('불일치')
	}
})
.on('focus','#chkChangePw',function(){
	let pw=$('#changePw').val()
	checkpwlength(pw)
})
function characterCheckPW(obj){
var regExp = /[ \{\}\[\]\/?,;:|\)*~`^\-_+┼<>\#$%&\'\"\\\(\=]/gi; 
if( regExp.test(obj.value) ){
	$('#changePwText').attr('color','red')
	$('#changePwText').html("비밀번호에는 [!, @, .]을 제외한 특수문자 및 띄어쓰기를 입력하실수 없습니다.");
	obj.value = obj.value.substring( 0 , obj.value.length - 1 );
	}
}
function checkpwlength(pw){
	if(pw.length<8||pw.length>16){
		$('#changePwText').attr('color','red')
		$('#changePwText').html("비밀번호는 8 ~ 16 자리로 입력해주세요.")
		$('#changePw').val('')
		$('#changePw').trigger('focus')
		return false;
	}else{
		$('#changePwText').attr('color','green')
		$('#changePwText').html("사용가능한 비밀번호 입니다.")
		return false;
	}
}