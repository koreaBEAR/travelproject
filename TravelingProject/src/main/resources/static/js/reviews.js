var placeNum = ''
$(document)
.ready(function(){
	$('.placeModal').css('display','none')
	loadReview(1)
})
.on('click','span[name=pageNum]',function(){
	var pageNum = parseInt($(this).text())
	$('span[name=page]').css({'background-color':'#fff','color':'#000'})
	$(this).css({'background-color':'#000','color':'#fff'})
	loadReview(pageNum);
})
.on('click','.divA',function(){
	placeNum = $('#aceNum').val()
	$.ajax({
		url:"/loadDetail",
		type:"post",
		data:{placeNum:placeNum},
		dataType:"json",
		success:function(data){
			$('.divMain').empty()
			for(i=1; i<data.length; i++){
				let placeImg = '<img src="'+ data[i]['placeImg']+'"class="reivewImg">'
				let placeName = '<p><span class="placeName">'+data[i]['placeName']+'</span></p>'
				$('.placeImges').append(placeImg)
				$('.placeName').append(placeName)
			}
		}
	})
	$('.placeModal').css('display','block')
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
				let nameStr = "<p><span class=boldText>"+data[i]['placeName']+"</span></p>"
				let div = '<div class="divA">'+hiddenId+'<div class="divImg">'+placeImg+'</div>'+nameStr+'</div>'
				$('.placeList').append(div)
			}
		}})
}