let currentPage = 1
$(document)
.ready(function(){
	$('.placeModal').css('display','none')
	loadReview(1)
	$('#placeList').on('click','.divA',function(){
		let placeNum = $(this).find('#placeNum').val()
		console.log("placeNum: "+placeNum)
		$.ajax({
			url:"/loadReviewOne",
			type:"post",
			data:{placeNum:placeNum},
			dataType:"text",
			success:function(data){
				console.log(data)
				if(data=="zero"){
					$.ajax({
						url:"/loadPlaceInfo",
						type:"post",
						data:{placeNum:placeNum},
						dataType:"json",
						success:function(data){
							let divLeft = ''
							let divRight = ''
							$('.divMain').empty()
							for(i=0; i<data.length; i++){
								let placeImg = '<img src="'+ data[i]['placeImg']+'"class="reivewImg">'
								let placeName = '<p><span class="placeName">'+data[i]['placeName']+'</span></p>'
								let placeContnet = '<p><span class="placeContent">'+data[i]['placeContent']+'</span></p>'
								let placeTel = '<p><span class="placeTel">'+data[i]['placeTel']+'</span></p>'
								let placeAddress = '<p><span class="placeAddress">'+data[i]['placeAddress']+'</span></p>'
								let placeLike = '<p><span class="placeLike">'+data[i]['placeLike']+'</span></p>'
								let likeIcon = `<div class="checkbox"><input type="checkbox" id="myCheckbox"><label for="myCheckbox" class="checkbox-icon"></label></div>`;

								divLeft = '<div class="divLeft"><div class="leftContainer"><div class="placeImges">'+placeImg+'</div><div class="placeName">'+placeName+'</div></div></div>'
								divRight = '<div class="divRight"><div class="rightContainer"><div class="placeContent">'+placeContnet+placeTel+placeAddress+'</div><div class="placeIcon">'+placeLike+likeIcon+'</div><div class="placeReviews">reviews</div></div></div>'
							}
								console.log("divLeft: "+divLeft)
								console.log("divRight: "+divRight)
								$('.divMain').append(divLeft,divRight)
						}
					})
				}else if(data=="overOne"){
					$.ajax({
						url:"/loadReviewInfo",
						type:"post",
						data:{placeNum:placeNum},
						dataType:"json",
						success:function(data){
							let divLeft = ''
							let divRight = ''
							$('.divMain').empty()
							for(i=0; i<data.length; i++){
								let placeImg = '<img src="'+ data[i]['placeImg']+'"class="reivewImg">'
								let placeName = '<p><span class="placeName">'+data[i]['placeName']+'</span></p>'
								let placeContnet = '<p><span class="placeContent">'+data[i]['placeContent']+'</span></p>'
								let placeTel = '<p><span class="placeTel">'+data[i]['placeTel']+'</span></p>'
								let placeAddress = '<p><span class="placeAddress">'+data[i]['placeAddress']+'</span></p>'
								let placeLike = '<p><span class="placeLike">'+data[i]['placeLike']+'</span></p>'
								divLeft = '<div class="divLeft"><div class="leftContainer"><div class="placeImges">'+placeImg+'</div><div class="placeName">'+placeName+'</div></div></div>'
								divRight = '<div class="divRight"><div class="rightContainer"><div class="placeContent">'+placeContnet+placeTel+placeAddress+'</div><div class="placeIcon">'+placeLike+'</div><div class="placeReviews"></div></div></div>'
							}
							console.log("divLeft: "+divLeft)
							console.log("divRight: "+divRight)
							$('.divMain').append(divLeft,divRight)
							$('.placeReviews').empty()
								$.ajax({
									url:"/loadReviewContent",
									type:"post",
									data:{placeNum:placeNum, page:currentPage},
									dataType:"json",
									success:function(data){
										for(i=0; i<data.length; i++){
											let placeReview =''
											let reviewNickName = '<p><span class="memberNickName">'+data[i]['reviewNickName']+'</span></p>'
											let reviewContent = '<p><span class="reviewContent">'+data[i]['reviewContent']+'</span></p>'
											let reviewDate = '<p><span class="reviewDate">'+data[i]['reviewDate']+'</span></p>'
											placeReview = '<div class="reviewblock">'+reviewNickName+reviewContent+reviewDate+'</div>'
											console.log("placeReview: "+placeReview)
											$('.placeReviews').append(placeReview)
										}
									}
								})
								
						}
					})
				}
			}
		})
		$('.placeModal').css('display','block')
	})
})	
.on('click','span[name=pageNum]',function(){
	var pageNum = parseInt($(this).text())
	$('span[name=page]').css({'background-color':'#fff','color':'#000'})
	$(this).css({'background-color':'#000','color':'#fff'})
	loadReview(pageNum);
})
.on('click','#closeBtn',function(){
	$('.placeModal').css('display','none')
})

function loadReview(pageNum){
	$.ajax({
		url:"/loadReview",
		data:{pageNum:pageNum},
		type:"post",
		dataType:"json",
		success:function(data){
			let i = 0
			let endPage = parseInt(data[0]['howmany'])
			var pageStr = ''
			$('.viewDivFooter').empty()
			for(i=1; i<=endPage; i++){
				pageStr = pageStr + '&nbsp;<span name=pageNum>'+i+'</span>&nbsp;'
			}
			$('.viewDivFooter').append(pageStr)
			$('.placeList').empty()
			for(i=1; i<data.length; i++){
				let placeImg = '<img src="'+data[i]['placeImg']+'" class="placeImg">'
				let hiddenId = '<input type="hidden" id="placeNum" name="placeNum" value="'+data[i]['placeId']+'">'
				let nameStr = "<p><span class=boldText>"+data[i]['placeId']+'/'+data[i]['placeName']+"</span></p>"
				let div = '<div class="divA">'+hiddenId+'<div class="divImg">'+placeImg+'</div>'+nameStr+'</div>'
				$('.placeList').append(div)
			}
		}})
}