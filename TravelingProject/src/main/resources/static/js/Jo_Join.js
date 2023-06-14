let str_gender = ''
$(document)
.ready(function(){
	let pw=$('#joinPw').val()
	let chk=$('#pwCheck').val()
	if(pw==''||chk==''){
		$('#chkPwMms').attr('color','red')
		$('#chkPwMms').html('비밀번호를 입력하세요')	
	}
})
.on('click','#btnback', function(){
	document.location="/"
})
.on('click','#btnsubmit', function(){
    str_gender=$('input[name=gender]:checked').val()
	$.ajax({url:"member_insert", type:'post',
		data:{id:$('#joinId').val(), pw:$('#joinPw').val(), name:$('#name').val(), nickName:$('#aka').val(),
		 birth:$('#birth').val(), email:$('#mail').val(), mobile:$('#telInput').val(), gender:str_gender},
		dataType:'text',
		beforeSend:function(){
			if($('#joinId').val()=='') {
				alert('아이디를 입력해주세요.');
				return false;
			} else if($('#joinPw').val()==''){
				alert('비밀번호를 입력해주세요.');
				return false;
			} else if($('#pwCheck').val()==''){
				alert('비밀번호 확인을 입력해주세요.');
				return false;
			} else if($('#name').val()==''){
				alert('이름을 입력해주세요.');
				return false;
			} else if($('#aka').val()==''){
				alert('닉네임을 입력해주세요.');
				return false;
			} else if($('#mail').val()==''){
				alert('이메일을 입력해주세요.')
				return false;
			} else if($('#telInput').val()==''){
				alert('휴대폰번호를 입력해주세요.');
				return false;
			} else if($('#birth').val()==''){
				alert('생년월일을 입력해주세요.');
				return false;
			} else if($('input[name="gender"]:checked').length == 0){
				alert('성별을 선택해주세요.');
				return false;
			}
		}, success:function(data){
			if(data=="ok") {
				document.location='/';
				$('#joinId, #joinPw, #pwCheck, #name, #aka, #mail, #telInput, #birth').val('');
			} else {
				alert("가입 실패 관리자에게 문의하세요. (관리자번호 : 010-7963-4246");
			}
	}})
})
.on('keyup','#joinId',function(){
		$.ajax({url:"id_check", type:'post', data:{id:$('#joinId').val()}, dataType:'text',
			beforeSend:function(){
				if($('#joinId').val()=='' || $('#joinId').val()==null){
					$('#chkId').html('아이디를 입력하세요.');
					$('#chkId').attr('color', 'red');
					$(this).focus();
					return false;
				}
			},success:function(data){
				if(data=="ok"){
					$('#chkId').html('사용불가능한 아이디입니다.');
					$('#chkId').attr('color', 'red');
				} else {
					$('#chkId').html('사용가능한 아이디입니다.');
					$('#chkId').attr('color', 'green');
				}
				
			}
		})
})
.on('keyup','#aka',function(){
		$.ajax({url:"nickName_check", type:'post', data:{aka:$('#aka').val()}, dataType:'text',
			beforeSend:function(){
				if($('#aka').val()=='' || $('#aka').val()==null){
					$('#chkNickName').html('닉네임을 입력하세요.');
					$('#chkNickName').attr('color', 'red');
					$(this).focus();
					return false;
				}
			},success:function(data){
				if(data=="ok"){
					$('#chkNickName').html('사용중인 닉네임입니다.');
					$('#chkNickName').attr('color', 'red');
				} else {
					$('#chkNickName').html('사용가능한 닉네임입니다.');
					$('#chkNickName').attr('color', 'green');
				}
				
			}
		})
})
.on('keyup','#pwCheck',function(){
	let pw=$('#joinPw').val()
	let chk=$('#pwCheck').val()
	checkpwlength(pw)
	if(pw==chk){
		$('#chkPwMms').attr('color','green')
		$('#chkPwMms').html('일치')	
	}else{
		$('#chkPwMms').attr('color','red')
		$('#chkPwMms').html('불일치')
	}
})
function characterCheckPW(obj){
var regExp = /[ \{\}\[\]\/?,;:|\)*~`^\-_+┼<>\#$%&\'\"\\\(\=]/gi; 
if( regExp.test(obj.value) ){
	$('#chkPwlength').attr('color','red')
	$('#chkPwlength').html("비밀번호에는 [!, @, .]을 제외한 특수문자 및 띄어쓰기를 입력하실수 없습니다.");
	obj.value = obj.value.substring( 0 , obj.value.length - 1 );
	}
}
function checkpwlength(pw){
	if(pw.length<8||pw.length>16){
		$('#chkPwlength').attr('color','red')
		$('#chkPwlength').html("비밀번호는 8 ~ 16 자리로 입력해주세요.")
		$('#joinPw').val('')
		$('#joinPw').trigger('focus')
		return false;
	}else{
		$('#chkPwlength').attr('color','green')
		$('#chkPwlength').html("사용가능한 비밀번호 입니다.")
		return false;
	}
}function validEmail(obj){
    if(validEmailCheck(obj)==false){
		$('#chkEmail').attr('color','red')
		$('#chkEmail').html("올바른 이메일 주소를 입력해주세요.")
        obj.focus();
        return false;
    }else{
		$('#chkEmail').attr('color','green')
		$('#chkEmail').html("사용가능한 이메일 주소입니다.")
		return false;
	}
}

function validEmailCheck(obj){
    var pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    return (obj.value.match(pattern)!=null)
}

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