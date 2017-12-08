<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Citi TTS Home</title>

<link href="/TTS/resources/style/style.css"	rel="stylesheet" />

<script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.min.js"></script>

<script type="text/javascript">
console.log('DEBUG ISIN OUT : '+ '${isin}');

	jQuery(document).ready(function($) {
		$("#tab1").click(function(event) {
			console.log('DEBUG ISIN: '+ '${isin}');
			var isinStr = '${isin}';
			divId = document.getElementById("pinText1");
			//Disable other tabs
			document.getElementById('tab2').disabled = true;
			document.getElementById('tab3').disabled = true;
			
			callController(divId, "ENG");
			document.getElementById('tab1').disabled = true;
			//callController(divId, isinStr, "ENG");
		});
		$("#tab2").click(function(event) {
			console.log('DEBUG ISIN: '+ '${isin}');
			var isinStr = '${isin}';
			
			//Disable other tabs
			document.getElementById('tab1').disabled = true;
			document.getElementById('tab3').disabled = true;
			
			divId = document.getElementById("pinText2");
			callController(divId, "CAN");
			document.getElementById('tab2').disabled = true;
			//callController(divId, isinStr, "CAN");
		});
		$("#tab3").click(function(event) {
			console.log('DEBUG ISIN: '+ '${isin}');
			var isinStr = '${isin}';
			
			//Disable other tabs
			document.getElementById('tab1').disabled = true;
			document.getElementById('tab2').disabled = true;
			
			divId = document.getElementById("pinText3");
			callController(divId, "MAN");
			document.getElementById('tab3').disabled = true;
			//callController(divId, isinStr, "MAN");
		});
	});

	function callController(divId, lang) {
		//"/TTS/generateAndSavePin?isin=" +isinStr+ "&lang=" + lang
		$.ajax({
			type : 'GET',
			url : "/TTS/generateAndSavePin?lang=" + lang,
			dataType : 'text',
			success : function(data, status) {
				console.log('I reached here inspite of exception')
				console.log(data);
				divId.value = data;					
				//document.getElementById('dialInText').style.visibility='visible';
				//callBackFunction(isSuccess, divId, data);
			},
			error : function(status, errorThrown) {
				console.log(status);
				console.log(errorThrown);
			}
		});
	}
	
	function callBackFunction(isSuccess, divId){
		if(isSuccess){
			divId.value = data;
		}
	}
</script>
</head>
<body>
	<main>
		<br>
		<div id="dialInText">
			<p><b>Dail-In</b></p>
			<p>Please Call: <b>${twilioNo}</b></p>		
		</div>
		<br>
		<p><b>Language Selection</b></p>	
		<input id="tab1" type="radio" name="tabs"> <label for="tab1">ENGLISH</label>
		<input id="tab2" type="radio" name="tabs"> <label for="tab2">CANTONESE</label>
		<input id="tab3" type="radio" name="tabs"> <label for="tab3">MANDARIN</label>
	
		<section id="content1">
			PIN: <input type="text" id="pinText1" name="PIN"> 
		</section> 
		<section id="content2">
			PIN: <input type="text" id="pinText2" name="PIN"> 
		</section> 
		<section id="content3"> 
			PIN: <input type="text" id="pinText3" name="PIN">
		</section>
	</main>
</body>
</html>