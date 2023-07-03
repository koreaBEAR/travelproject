<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TensorFlow.js Tutorial - boston housing </title>

<!-- Import TensorFlow.js -->
<script src="https://cdn.jsdelivr.net/npm/@tensorflow/tfjs@2.4.0/dist/tf.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@tensorflow/tfjs-vis"></script>

</head>
 
<body>
<script>
// 기존 사용자의 나이와 성별을 확인해고, 많이 추가한 업체를 확인해서
// 사용자의 맞게 업체를 추천해주는 기능구현
// 예약장소 1, 2, 3, 4, 5, 6
// 나이 성별 
	const 보스톤_원인 = [
		[50,0],
		[50,0],
		[50,0],
		[50,0],
		[50,0],
		[50,0],
		[50,0],
		[50,0],
		[50,0],
		[50,0],
	
		[50,1],
		[50,1],
		[50,1],
		[50,1],
		[50,1],
		[50,1],
		[50,1],
		[50,1],
		[50,1],
		[50,1],
	
		[40,0],
		[40,0],
		[40,0],
		[40,0],
		[40,0],
		[40,0],
		[40,0],
		[40,0],
		[40,0],
		[40,0],
	
		[40,1],
		[40,1],
		[40,1],
		[40,1],
		[40,1],
		[40,1],
		[40,1],
		[40,1],
		[40,1],
		[40,1],
	
		[30,0],
		[30,0],
		[30,0],
		[30,0],
		[30,0],
		[30,0],
		[30,0],
		[30,0],
		[30,0],
		[30,0],
	
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		
		[50,0],
		[50,0],
		[50,0],
		[50,0],
		[50,0],
		[50,0],
		[50,0],
		[50,0],
		[50,0],
		[50,0],
	
		[50,1],
		[50,1],
		[50,1],
		[50,1],
		[50,1],
		[50,1],
		[50,1],
		[50,1],
		[50,1],
		[50,1],
	
		[40,0],
		[40,0],
		[40,0],
		[40,0],
		[40,0],
		[40,0],
		[40,0],
		[40,0],
		[40,0],
		[40,0],
	
		[40,1],
		[40,1],
		[40,1],
		[40,1],
		[40,1],
		[40,1],
		[40,1],
		[40,1],
		[40,1],
		[40,1],
	
		[30,0],
		[30,0],
		[30,0],
		[30,0],
		[30,0],
		[30,0],
		[30,0],
		[30,0],
		[30,0],
		[30,0],
	
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		[30,1],
		[30,1]
	];

	const 보스톤_결과 = [
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		[ 1],
		
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		[ 2],
		
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		[ 3],
		
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		[ 4],
		
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		[ 5],
		
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		[ 6],
		[ 6]
	
	];
	const 질의데이터=[
	[50,0],
	[50,1],
	[40,0],
	[40,1],
	[30,0],
	[30,1]
	];
        

// 제시한 지역중에 높은순으로 추천순위가 된다.     

// 1. 과거의 데이터를 준비합니다. 
	var 원인 = tf.tensor(보스톤_원인);
	var 결과 = tf.tensor(보스톤_결과);
	var 질의 =tf.tensor(질의데이터);

// 2. 모델의 모양을 만듭니다. 
	var X = tf.input({ shape:2 });
	var H1 = tf.layers.dense({ units: 4, activation:'relu' }).apply(X);
	var H2 = tf.layers.dense({ units:4, activation:'relu' }).apply(H1);
	var Y = tf.layers.dense({ units: 1 }).apply(H2);
	var model = tf.model({ inputs: X, outputs: Y });
	var compileParam = { optimizer: tf.train.adam(), loss: tf.losses.meanSquaredError }
	model.compile(compileParam);
	tfvis.show.modelSummary({name:'요약', tab:'모델'}, model);

// 3. 데이터로 모델을 학습시킵니다. 
// var fitParam = {epochs:500}
	var _history = [];
	var fitParam = { 
		epochs: 1500, 
		callbacks:{
			onEpochEnd:
				function(epoch, logs){
					console.log('epoch', epoch, logs, 'RMSE=>', Math.sqrt(logs.loss));
					_history.push(logs);
					tfvis.show.history({name:'loss', tab:'역사'}, _history, ['loss']);
				}
		}
	} // loss 추가 예제
	model.fit(원인, 결과, fitParam).then(function (result) {

// 4. 모델을 이용합니다. 
// 4.1 기존의 데이터를 이용
		let 예측한결과 = model.predict(질의);
		예측한결과.print();
		let myArray= 예측한결과.dataSync();
		if ( myArray[0] < 1.5 ) {
			a = '식당';
		}
		if ( myArray[1] >= 1.5 && myArray[1] < 2.5 ) {
			b = '카페';
		}
		if ( myArray[2] >= 2.5 && myArray[2] < 3.5 ) {
			c = '체험';
		}
		if ( myArray[3] >= 3.5 && myArray[3] < 4.5 ) {
			d = '명소';
		}
		if ( myArray[4] >= 4.5 && myArray[4] < 5.5 ) {
			e = '호텔';
		}
		if ( myArray[5] >= 5.5 ) {
			f = '캠핑';
		}
		alert('50대 남성 : ' + a + '\n' + '50대 여성 : ' + b + '\n' + '40대 남성 : ' + c + '\n' + '40대 여성 : ' + d + '\n' + '30대 남성 : ' + e + '\n' + '30대 여성 : ' + f );
	})
</script>
</body> 
</html>