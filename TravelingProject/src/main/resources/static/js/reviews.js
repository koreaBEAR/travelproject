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
								let writeRev = '<div class= write><input type="button" value="리뷰쓰기""></div>'

								divLeft = '<div class="divLeft"><div class="leftContainer"><div class="placeImges">'+placeImg+'</div><div class="placeName">'+placeName+'</div></div></div>'
								divRight = '<div class="divRight"><div class="rightContainer"><div class="placeContent">'+placeContnet+placeTel+placeAddress+'</div><div class="placeIcon">'+likeIcon+placeLike+writeRev+'</div><div class="placeReviews">reviews</div></div></div>'
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
	loadReview(pageNum);
})
.on('click','#closeBtn',function(){
	$('.placeModal').css('display','none')
})

/*10페이지씩 제한 페이지네이션변경*/
function loadReview(pageNum){
    $.ajax({
        url: "/loadReview",
        data: {pageNum: pageNum},
        type: "post",
        dataType: "json",
        success : function ( data ){
            let i = 0;
            let startPage = parseInt(data[0]['startPage']);
            let endPage = parseInt(data[0]['endPage']);
            let totalPage = parseInt(data[0]['howmany']);
            var pageStr = "";
            $('.viewDivFooter').empty();

            // 첫페이지
            pageStr = '<span name=pageNum onclick="loadReview(1)">First</span>' + pageStr;

            if ( startPage > 10 ) {
                pageStr = pageStr + '<span name=pageNum onclick="loadReview(' + (startPage - 10) + ')"><<</span>';
            }

            for (i = startPage ; i <= endPage ; i++) {
                pageStr = pageStr + '<span name=pageNum onclick="loadReview(' + i + ')">' + i + '</span>';
            }

            if ( endPage < totalPage ) {
                pageStr = pageStr + '<span name=pageNum onclick="loadReview(' + (endPage + 1) + ')">>></span>';
            }

            // 마지막 페이지
            if ( totalPage > 10 && endPage < totalPage ) {
                pageStr = pageStr + '<span name=pageNum onclick="loadReview(' + totalPage + ')">Last</span>';
            }

            $('.viewDivFooter').append(pageStr);

            // 현재 선택된 페이지 버튼만 글씨 진하게
            $("span[name='pageNum']").removeClass("current");

            
            $("span[name='pageNum']").each(function() {
                if ($(this).text() === pageNum.toString()) {
                    $(this).addClass("current");
                }
            });

            $('.placeList').empty();
            for ( i = 1 ; i < data . length ; i ++ ) {
                let placeImg = '<img src="' + data[i]['placeImg'] + '" class="placeImg">';
                let hiddenId = '<input type="hidden" id="placeNum" name="placeNum" value="' + data[i]['placeId'] + '">';
                let nameStr = "<p><span class=boldText>" + data[i]['placeId'] + '/' + data[i]['placeName'] + "</span></p>";
                let div = '<div class="divA">' + hiddenId + '<div class="divImg">' + placeImg + '</div>' + nameStr + '</div>';
                $('.placeList').append(div);
            }
        }
    });
}


/*페이지네이션 기존코드*/
/*function loadReview(pageNum){
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
}*/