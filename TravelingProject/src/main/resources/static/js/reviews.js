let currentPage = 1
let likeNum = 0
let placeNum = ''
$(document)
.ready(function(){
	$('.placeModal').css('display','none')
	loadReview(1)
	$('#placeList').on('click','.divA',function(){
		placeNum = $(this).find('#placeNum').val()
		console.log("placeNum: "+placeNum)
		$.ajax({
			url:"/loadReviewOne",
			type:"post",
			data:{placeNum:placeNum},
			dataType:"text",
			success:function(data){
				console.log(data)
				if(data=="zero"){
					loadPlaceInfo()
				}else if(data=="overOne"){
					loadReviewInfo()
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
	var hiddenValue = placeNum;
	console.log(hiddenValue);
})
.on('click', '.checkbox-icon', function() {
	let placeNum=$('#hiddenPlaceNum').val()
  if ($('.checkbox input[type="checkbox"]:checked+label').length == 0) {
    upLike(placeNum);
  } else {
    downLike(placeNum);
  }
})
.on('click','#popularity',function(){
	loadReviewPopular(1)
})
.on('click','#asc',function(){
	loadReviewASC(1)
})
.on('click','#desc',function(){
	loadReviewDESC(1)
})
.on('click','#revBtn',function(){
	let placeNum = $('#hiddenPlaceNum').val();
	$('#btnUpload').val(placeNum);
    $('#reviewPlace').text($('.placeName').text());
    
    $('#reviewWriteDig').dialog({
        title:'',
        closeText: "X",
        modal:true,
        width:'500px'
    })
})
.on('click','#btnUpload',function(){
	let memberNickName = $('#uesrNickname').text();
	let placeNum = $(this).val()
		$.ajax({
		url : '/revContentInsert',
		type : 'post',
		dataType : 'text',
		data : { revContent:$('#revContent').val(), placeNum : placeNum, memberNickName:memberNickName},
        beforeSend: function() {
        	 if ($('#revContent').val() == '') {
        	        alert('리뷰를 작성해주세요');
        	        return false; 
        	    }},
		success : function(data) {
			console.log('작성완료')
			$('#btnCancel').trigger('click');
		}
	})
})

.on('click', '#btnCancel', function() {
	$('#revContent').val('')
    $('#reviewWriteDig').dialog('close');
    loadReviewContent()
})
// Dropdown menu for 'Search by region' button
$('.dropdown .btn-secondary').eq(0).click(function() {
  var dropdownMenu = $(this).next('.city');
  dropdownMenu.slideToggle(300);
  var buttonOffset = $(this).offset();
  var buttonWidth = $(this).outerWidth();
  var dropdownMenuWidth = dropdownMenu.outerWidth();
  dropdownMenu.css({
    'left': buttonOffset.left + buttonWidth+20 - dropdownMenuWidth
  });
});

// Dropdown menu for 'Sort lookup' button
$('.dropdown .btn-secondary').eq(1).click(function() {
  var dropdownMenu = $(this).next('.city');
  dropdownMenu.slideToggle(300);
  var buttonOffset = $(this).offset();
  var buttonWidth = $(this).outerWidth();
  var dropdownMenuWidth = dropdownMenu.outerWidth();
  dropdownMenu.css({
    'left': buttonOffset.left + buttonWidth+60 - dropdownMenuWidth
  });
});

// Set the dropdown menu to close when a non-click area is clicked
$(window).click(function(event) {
  if (!$(event.target).closest('.dropdown').length) {
    $('.dropdown .city').slideUp(300);
  }
});
// Add a click event listener to the <li> elements
$('.dropdown-item').click(function() {
  // Get the ID of the clicked <li> element
  var id = $(this).attr('id');
  loadReviewCity(1,id)
});
$('.tag').click(function(){
	var n1 = parseInt($(this).attr('id'))
	var n2 = n1 + 1
	console.log('<'+n1+','+n2+'>')
	loadReviewCategory(1,n1,n2);
})

function loadLike(){
	let placeNum=$('#hiddenPlaceNum').val()
	$.ajax({
		url:"/loadLike",
		data:{placeNum:placeNum},
		type:"post",
		dataType:"text",
		success:function(data){
			$('.placeLike').text(data)
		}
	})
}
function upLike(placeNum){
	$.ajax({
		url:"/upLike",
		data:{placeNum:placeNum},
		type:"post",
		dataType:"text",
		success:function(data){
			loadLike()
		}
	})
}
function downLike(placeNum){
	$.ajax({
		url:"/downLike",
		data:{placeNum:placeNum},
		type:"post",
		dataType:"text",
		success:function(data){
			loadLike()
		}
	})
}
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
//인기순
function loadReviewPopular(pageNum){
    $.ajax({
        url: "/loadReviewPopular",
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
function loadReviewASC(pageNum){
    $.ajax({
        url: "/loadReviewASC",
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
function loadReviewDESC(pageNum){
    $.ajax({
        url: "/loadReviewDESC",
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
function loadReviewCity(pageNum, cityNum){
    $.ajax({
        url: "/loadReviewCity",
        data: {pageNum: pageNum,cityNum:cityNum },
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
function loadReviewCategory(pageNum,n1,n2){
    $.ajax({
        url: "/loadReviewCategory",
        data: {pageNum: pageNum,n1:n1,n2:n2 },
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
function loadPlaceInfo(){
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
			let placeImg = '<img src="'+ data[i]['placeImg']+'"class="reivewImg"><input type="hidden" id="hiddenPlaceNum" value="'+placeNum+'">'
			let placeName = '<p><span class="placeName">'+data[i]['placeName']+'</span></p>'
			let placeContnet = '<p><span class="placeContent">'+data[i]['placeContent']+'</span></p>'
			let placeTel = '<p><span class="placeTel">'+data[i]['placeTel']+'</span></p>'
			let placeAddress = '<p><span class="placeAddress">'+data[i]['placeAddress']+'</span></p>'
			let placeLike = '<p><span class="placeLike">'+data[i]['placeLike']+'</span></p>'
			let likeIcon = `<div class="checkbox"><input type="checkbox" id="myCheckbox"><label for="myCheckbox" class="checkbox-icon"></label></div>`;
			let writeRev = '<div class= write><input type="button" value="리뷰쓰기" id="revBtn"></div>'

			divLeft = '<div class="divLeft"><div class="leftContainer"><div class="placeImges">'+placeImg+'</div><div class="placeName">'+placeName+'</div></div></div>'
			divRight = '<div class="divRight"><div class="rightContainer"><div class="placeContent">'+placeContnet+placeTel+placeAddress+'</div><div class="placeIcon">'+likeIcon+placeLike+writeRev+'</div><div class="placeReviews">reviews</div></div></div>'
		}
			console.log("divLeft: "+divLeft)
			console.log("divRight: "+divRight)
			$('.divMain').append(divLeft,divRight)
			loadLike()
	}
})	
}
function loadReviewInfo(){
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
			let placeImg = '<img src="'+ data[i]['placeImg']+'"class="reivewImg"><input type="hidden" id="hiddenPlaceNum" value="'+placeNum+'">'
			let placeName = '<p><span class="placeName">'+data[i]['placeName']+'</span></p>'
			let placeContnet = '<p><span class="placeContent">'+data[i]['placeContent']+'</span></p>'
			let placeTel = '<p><span class="placeTel">'+data[i]['placeTel']+'</span></p>'
			let placeAddress = '<p><span class="placeAddress">'+data[i]['placeAddress']+'</span></p>'
			let placeLike = '<p><span class="placeLike">'+data[i]['placeLike']+'</span></p>'
			let likeIcon = `<div class="checkbox"><input type="checkbox" id="myCheckbox"><label for="myCheckbox" class="checkbox-icon"></label></div>`;
			let writeRev = '<div class= write><input type="button" value="리뷰쓰기" id="revBtn"></div>'
			divLeft = '<div class="divLeft"><div class="leftContainer"><div class="placeImges">'+placeImg+'</div><div class="placeName">'+placeName+'</div></div></div>'
			divRight = '<div class="divRight"><div class="rightContainer"><div class="placeContent">'+placeContnet+placeTel+placeAddress+'</div><div class="placeIcon">'+likeIcon+placeLike+writeRev+'</div><div class="placeReviews"></div></div></div>'
		}
		console.log("divLeft: "+divLeft)
		console.log("divRight: "+divRight)
		$('.divMain').append(divLeft,divRight)
			loadReviewContent()
	}
})
}
function loadReviewContent(){
$.ajax({
	url:"/loadReviewContent",
	type:"post",
	data:{placeNum:placeNum, page:currentPage},
	dataType:"json",
	success:function(data){
		$('.placeReviews').empty()
		for(i=0; i<data.length; i++){
			let placeReview =''
			let reviewNickName = '<p><span class="memberNickName">'+data[i]['reviewNickName']+'</span></p>'
			let reviewContent = '<p><span class="reviewContent">'+data[i]['reviewContent']+'</span></p>'
			let reviewDate = '<p><span class="reviewDate">'+data[i]['reviewDate']+'</span></p>'
			placeReview = '<div class="reviewblock">'+reviewNickName+reviewContent+reviewDate+'</div>'
			console.log("placeReview: "+placeReview)
			$('.placeReviews').append(placeReview)
		}
		loadLike()
	}
})	
}