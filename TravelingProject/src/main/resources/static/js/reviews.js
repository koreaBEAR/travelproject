$(document)
.ready(function(){
	loadReview(1)
})
.on('click','span[name=pageNum]',function(){
	var pageNum = parseInt($(this).text())
	$('span[name=page]').css({'background-color':'#fff','color':'#000'})
	$(this).css({'background-color':'#000','color':'#fff'})
	loadReview(pageNum);
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
			$('.divtbl').empty()
			for(i=1; i<data.length; i++){
				let placeImg = '<img src="'+data[i]['placeImg']+'"class="placeImg" alt="'+data[i]['placeId']+'">'
				let nameStr = "<p>"+data[i]['placeName']+"</p>"
				let addressStr = "<p>"+data[i]['placeAddress']+"</p>"
				let div = '<div class="divA"><div class="divImg">'+placeImg+'</div><div class="divB">'+nameStr+addressStr+'</div></div>'
				$('.divtbl').append(div)
			}
		}})
}