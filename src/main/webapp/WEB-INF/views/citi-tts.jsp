<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TTS Home</title>

<c:url value="/resources/style/style.css" var="mainCssUrl" />
<c:url value="/resources/error.jsp" var="errorJspUrl" />
<c:url value="/resources/images/" var="imagesUrl" />

<link href="${mainCssUrl}"	rel="stylesheet" />

<script type="text/javascript" src="http://code.jquery.com/jquery-1.8.2.min.js"></script>
<link rel="stylesheet" href="http://www.marghoobsuleman.com/mywork/jcomponents/image-dropdown/samples/css/msdropdown/dd.css"/>
<script src="http://www.marghoobsuleman.com/mywork/jcomponents/image-dropdown/samples/js/msdropdown/jquery.dd.min.js"></script>
<script src="http://www.marghoobsuleman.com/mywork/jcomponents/image-dropdown/samples/css/msdropdown/flags.css"></script>
<link href='https://fonts.googleapis.com/css?family=Muli' rel='stylesheet'>

<script type="text/javascript">
console.log('DEBUG ISIN OUT : '+ '${isin}');

		$(document).ready(function() {
		$("#lenguage").msDropdown();
	})

	function callController(divId, lang) {
		$.ajax({
			type : 'GET',
			url : "privetts/generateAndSavePin?lang=" + lang,
			dataType : 'text',
			success : function(data, status) {
				console.log('I reached here inspite of exception '+status);
				if(data == 'Error'){
					console.log('Return from generateAndSavePin is null '+status);
					window.location = '${errorJspUrl}';
				}else{
					console.log('Return from generateAndSavePin is not null '+status);
					console.log(data);
					divId.textContent = data;
				}
			},
			error : function(status, errorThrown) {
				console.log(status);
				console.log(errorThrown);
			}
		});
	}

	function callController2(divId, lang){
		console.log('I am inside new AJAX function..');
		 var xhttp = new XMLHttpRequest();
		  xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		    	divId.value = this.responseText;
		    } else {
		    	window.location.href = "/WEB-INF/views/error.jsp";
		    }
		  };
		  xhttp.open("GET", "privetts/generateAndSavePin?lang=" + lang, true);
		  xhttp.send();
	}

	function downloadKFS(){
		var key = '${key}';
		if(key == null){
			key = '${isin}';
		}
		console.log('DEBUG KEY for Download KFS: '+ key);
		location.href='privetts/downloadKFS';
	}

	function languageChange(){
        var table = document.getElementById("buttonTable");
        var rows = table.getElementsByTagName("tr").length;
        if(rows > 0){
        	table.deleteRow(0);
        }

		var selectedindex = document.getElementById("lenguage").selectedIndex;
		if(selectedindex != 0){
			var element = document.createElement("input");
	        element.setAttribute("type", "button");
	        element.setAttribute("id", "listen");
	        element.setAttribute("value", "Listen to your investment funds risks");
	        element.setAttribute("onclick", "callControllerWithLanguage()");

	        var row = table.insertRow(0);
	        var cell1 = row.insertCell(0);
	        cell1.appendChild(element);
		}
	}

	function callControllerWithLanguage(){
		console.log('DEBUG ISIN: '+ '${isin}');
		divId = document.getElementById("pinText1");
		//Disable other tabs

		var dropDown = document.getElementById("lenguage");
		var language = dropDown.options[dropDown.selectedIndex].value;

		callController(divId, language);
		document.getElementById('content1').style.visibility = 'visible';
		document.getElementById('mainselection').className += 'disabledSelection';

		var newPin = document.createElement("input");
		newPin.setAttribute("type", "button");
		newPin.setAttribute("id", "newPin");
		newPin.setAttribute("value", "Generate a new Pin");
		newPin.setAttribute("onclick", "generateNewPin()");

        var done = document.createElement("input");
        done.setAttribute("type", "button");
        done.setAttribute("id", "done");
        done.setAttribute("value", "I'm done");
        done.setAttribute("onclick", "processDone()");

        var table = document.getElementById("buttonTable");
        table.deleteRow(0);
        var row = table.insertRow(0);

        var cell1 = row.insertCell(0);
        cell1.appendChild(done);
        cell1.style.width = '50%';

        var cell2 = row.insertCell(1);
        cell2.appendChild(newPin);
        cell2.style.width = '50%';
	}

	function generateNewPin(){
		location.reload();
		var dropDown = document.getElementById("lenguage");
		dropDown.selectedIndex = 0;
	}

	function processDone(){
		generateNewPin();
	}
</script>
</head>
<body>
	<div class="pageDiv">
		<div class="mainDiv">
			<h1>Learn about your investment fund risks.</h1>
			<p class="header">Understanding your investment will help you make the right decisions. <br> Find out more by listening to your investment fund risks.</p>
			<div id="mainselection">
				<p class="languageHeader"><b>Your selected language:</b></p>
				<div id="selectContainer">
					<select id="lenguage" name="languages" onchange="languageChange()">
						<option selected  disabled="disabled" class="firstOption">Select your Language</option>
						<option value = "zh-HK" data-image="${imagesUrl}/HK-icon.png" data-title="CANTONESE">CANTONESE</option>
						<option value = "en" data-image="${imagesUrl}/EN-icon.png" data-title="ENGLISH">ENGLISH</option>
						<option value = "zh-CN" data-image="${imagesUrl}/CH-icon.png" data-title="MANDARIN">MANDARIN</option>
					</select>
				</div>
			</div>
			<div id="content1">
				<div id="dialInText">
					<p><b>Dial-In</b><br></p>
					<p class="uniquePin">Dial-in the number below to listen to the key risks associated with your investment.<br><h1 class="numberPin"><b>${twilioNo}</b></h1></p>
				</div>
				<div id="uniquePin">
					<p><b>Unique pin:</b><br></p>
					<p class="uniquePin">Key in the unique PIN upon prompt.</p>
				</div>
				<table id=uniquePinTable>
					<tr>
						<td>
							<h1 class="numberPin">
								<span id="pinText1"></span>
							</h1>
						</td>
					</tr>
					<tr>
						<td>
							<button type="button" onclick="downloadKFS()">
								<img src="${imagesUrl}/download.png" alt="Place Holder" />
								<span><b>Download the report</b></span>
							</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<br>
		<br>		
		<table id="buttonTable">
		</table>
	</div>
</body>
</html>