$(document)
.ready(function(){
	var images=[
		"/img/63빌딩.jpg",
		"/img/강릉메인.jpg",
		"/img/경주메인.jpg",
		"/img/부산메인.jpg",
		"/img/여수메인.jpg",
		"/img/제주메인.jpg",
		"/img/포항메인.jpg"
	]
	function getRandomImage(){
		var randomIndex = Math.floor(Math.random() * images.length)
		return images[randomIndex]
	}
	function changeImage(){
		var randomImage = getRandomImage()
		$('.slideShow img').attr('src',randomImage)
	}
	changeImage()
	
	$(window).on('beforeunload',function(){
		changeImage()
	})
	 $('input').keypress(function(event) {
    if (event.keyCode === 13) {
      event.preventDefault(); // 폼 전송 방지
      $('.loginButton').click(); // 버튼 클릭
    }
  })
})
.on('click','#joinLink',function(){
	document.location="/join"
})
.on('click','.loginButton',function(){
	let id=$('#loginID').val()
	let pw=$('#loginPW').val()
	
	$.ajax({url:"member_check",type:"post",data:{id:id,pw:pw},dataType:"text",
			beforeSend:function(){
				let id=$('#loginID').val()
				let pw=$('#loginPW').val()
				if(id==''){
					alert('아이디를 입력해주세요.')
				}else if(pw==''){
					alert('비밀번호를 입력해주세요.')
				}
			},success:function(data){
				if(data=="ok"){
					document.location="/main"
				}else{
					alert('로그인 실패')
				}
			}
	})
})
.on('click','#findId',function(){
	 $('#IdDlg').dialog({
		 title:'아이디 찾기',
	     modal:true,
	     width:500,
	     height:380,
	     resizeable : false,
	     show : 'fadeIn',
	     hide : 'fadeOut'	     
	 })
})
.on('click','#findPw',function(){
	 $('#PwDlg').dialog({
		 title:'비밀번호 찾기',
	     modal:true,
	     width:500,
	     height:420,
	     resizeable : false,
	     show : 'fadeIn',
	     hide : 'fadeOut'
	 })
})
.on('click','#btnfind',function(){
	let id=$('#findIdName').val()
	let mail=$('#findEm').val()
    
    $.ajax({
		url:"/findId",
		type:"post",
		data:{id:id,mail:mail},
		dataType:"text",
		beforeSend:function(){
			if(id==''){
				alert('이름을 입력하세요.')
				return false;
			}else if(mail==''){
				alert('이메일을 입력하세요.')
				return false;
			}    
		},success:function(data){
			if(data!=""){
				alert('당신의 아이디는'+data+'입니다.')
				$('IdDlg').dialog("close")
			}
			else{
				alert('가입되어있지 않은 정보입니다.')
			}
		}
		
	})
})
.on('click','#btnFindPw',function(){
	 let id = $('#pwdid').val()
     let email = $('#pwdem').val()
     let name = $('#pwdnm').val()
     let randomPw = generateRandomString(8)
     $.ajax({
		url:"/findPw",
		type:"post",
		data:{id:id,email:email,name:name,randomPw:randomPw},
		dataType:"text",
		beforeSend:function(){
			if(id==''){
				alert('아이디를 입력하세요.')
				return false;
			}else if(email==''){
				alert('이메일을 입력하세요.')
				return false;
			}else if(name==''){
				alert('이름을 입력하세요.')
				return false;
			}
		},success:function(data){
			if(data!=''){
			// 이메일 전송 요청
		        $.ajax({
		            url: '/sendEmail',
		            type: 'post',
		            data:{
		                recipient: email,
		                subject: '비밀번호 찾기',
		                content: '회원님의 임시비밀번호는 ' + data + '입니다.'
		            },
		            success: function(response) {
		                alert(response);
		                $('#pwdid').val('')
		                $('#pwdem').val('')
		                $('#pwdnm').val('')
		            },
		            error: function(xhr, status, error) {
		                alert('이메일 전송에 실패했습니다.');
		            }
		        })	
			}else{
				alert('가입되어있지 않은 정보입니다.')
			}
		}
		
	})
        
})
.on('click','.testButton',function(){
	var email='gusqls21555@gmail.com'
	var name='test'
	$.ajax({
        url: '/sendEmail',
        type: 'post',
        data:{
            recipient: email,
            subject: '비밀번호 찾기',
            content: '안녕하세요, ' + name + '님의 비밀번호 찾기 이메일입니다.'
        },
        success: function(response) {
            alert(response);
        },
        error: function(xhr, status, error) {
            alert('이메일 전송에 실패했습니다.');
        }
    })	
})
.on('click','#btnShow',function(){
	 $(".form").fadeIn();
    $(".formTitle").fadeOut();
})

function togglePasswordVisibility() {
    var passwordInput = document.getElementById("loginPW");
    var eyeIcon = document.getElementById("eyeOpen");

    if (passwordInput.type === "password") {
      passwordInput.type = "text";
      eyeIcon.src = "/img/eyeClose.png";
    } else {
      passwordInput.type = "password";
      eyeIcon.src = "/img/eyeOpen.png";
    }
}
function generateRandomString(length) {
var characters = '0123456789abcdefghijklmnopqrstuvwxyz';
var randomString = '';

for (var i = 0; i < length; i++) {
	var randomNumber = Math.floor(Math.random() * characters.length);
	randomString += characters.substring(randomNumber, randomNumber + 1);
}

return randomString;
}