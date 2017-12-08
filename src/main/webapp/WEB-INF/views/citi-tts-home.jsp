<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>External Window - CIti TTS Home</title>

<!-- <link type="text/css" id="mainTheme" rel="stylesheet"
	href="/WEB-INF/views/Sly.css"> -->

<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.10.1.min.js">
	
</script>

<script type="text/javascript" >

	function callGeneratePIN() {
		var val = Math.floor(100 + Math.random() * 999);
		//console.log(val);
		
		//document.getElementById("textBox").value = val;
		/* $.ajax({
			url : '/TTS/generatePin',
			method : 'GET',
			success : function() {
				document.getElementById("textbox").value = '${mpin}';
			}
		}); */
		postPIN(val);
		return val;
	}

	
	function postPIN(val){
	    $.ajax({
	            type : "POST",
	            contentType : 'application/text; charset=utf-8',
	            dataType : 'text',
	            url : "/TTS/postPin",
	            data : {"pin": val},
	            success : function(result) {
	                console.log("SUCCESS: ", data);
	            },
	            error: function(e){
	                console.log("ERROR: ", e);
	            },
	            done : function(e) {
	                console.log("DONE");
	            }
	    });
	}
	
	function doAjaxGet() {
		$.ajax({
			url : '/TTS/generatePin1',
			method : 'GET',
			success : function(data) {
				document.getElementById("textBox").innerHTML = 122;
			}
		});
	}
</script>

<script>
function loadTextBox() {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      document.getElementById("textBox").innerHTML =
      [[${mpin}]];
      //this.responseText;
    }
  };
  xhttp.open("GET", "/TTS/generatePin", true);
  xhttp.send();
}
</script>

</head>

<body>

	<h1>
		Welcome to Citi TTS Home
		<h1>

			<br> 123 123 123
			<c:out value="${twilioNo}" />

			<br> <input type="text" id="textBox" /> <input type="button"
				id="button1" value="Generate PIN"
				onclick="document.getElementById('textBox').value=callGeneratePIN()" /> 
			
			<input
				type="button" id="button2" value="Generate PIN11"
				onclick="loadTextBox()" />
</body>

</html>