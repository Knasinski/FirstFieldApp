<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
body {font-family: "Courier New"; background-color: LightBlue;}

/* Style the tab */
.tab {
    overflow: hidden;
    border: 1px solid #ccc;
    background-color: #104B99;
}

/* Style the buttons inside the tab */
.tabcontent h3, .tabcontent span, .tabcontent tr, .tabcontent div { white-space: pre; font-family: "Courier New"; font-size: 20px; color: #104B99}

.tab button {
    background-color: inherit;
    float: left;
    border: 2px solid grey;
    outline: none;
    cursor: pointer;
    padding: 14px 16px;
    transition: 0.3s;
    font-size: 17px;
}

/* Change background color of buttons on hover */
.tab button:hover {
    background-color: #3175ed;
}

/* Create an active/current tablink class */
.tab button.active {
    background-color: #A8B538;
}

/* Style the tab content */
.tabcontent {
    display: none;
    padding: 6px 12px;
    border: 1px solid #ccc;
    border-top: none;
}
</style>











<title>Home</title>
<link rel="stylesheet"
href="${pageContext.request.contextPath}/resources/app/css/styles.css">

</head>
<div id="wrapper">


<!-- Comment out the date display.
<h1>Hello world!</h1>
<p>The time on the server is ${serverTime}.</p>
-->
<div id="clockData">clockData</div>

<div style="text-align:center">
    <div >
      	<img width="450" alt="AMT Logo" src="https://zv29bqv028dj9ycg-zippykid.netdna-ssl.com/wp-content/uploads/2017/05/amt_logo_color_nav.png">
      	<br>
    </div>
</div>
</div>
 
 <div class="tab">
  <button class="tablinks" onclick="openAtab(event, 'Controllers')">Controllers</button>
  <button class="tablinks" onclick="openAtab(event, 'Position Data')">Position Data</button>
  <button class="tablinks" onclick="openAtab(event, 'IO Data')">IO Data</button>
</div>

<!-- Tab content -->
<div id="Controllers" class="tabcontent">
  <!-- Add the list display of controller information -->
	<table>
		<thead>
			<tr>
				<th>controller id</th>
				<th>controller name</th>
				<th>controller type</th>
				<th>Pose (Robots only)</th>
				<!-- Add the display of controller_type -->
			</tr>
		</thead>
		
		<tbody>
			<c:forEach items="${controllers}" var="controller">
				<tr>
					<td>${controller.instanceId}</td>
					<td>${controller.name}</td>
					<td>${controller.controllerType}</td>
					<td>${controller.jointPose}</td>
					<!-- Add the display of controller_type -->
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div> 

<div id="Position Data" class="tabcontent">
	<h3>                        Joint Position                                                              Joint Odometer</h3>	
	<div>${Jhdrs}</div>
	<div id="r1jaData">r1jaData</div>
	<div id="r2jaData">r2jaData</div>
	<hr>
	
	<h3>                        Cartesian Position</h3>
	<div>${Chdrs}</div>
	<div id="r1CartData">r1CartData</div>
	<div id="r2CartData">r2CartData</div>

</div> 
<div id="IO Data" class="tabcontent">
	<h3>Name   Variable</h3>	
	
	<div id="r1VarData1">r1VarData1</div>
	<div id="r1VarData2">r1VarData2</div>
	<div id="r1VarData3">r1VarData3</div>
	<div id="r1VarData4">r1VarData4</div>
	<div id="r1VarData5">r1VarData5</div>
	<div id="r1VarData6">r1VarData6</div>
	<div id="r1VarData7">r1VarData7</div>
	<div id="r1VarData8">r1VarData8</div>
	<div id="r1VarData9">r1VarData9</div>
	
	<div id="r2VarData1">r2VarData1</div>
	<div id="r2VarData2">r2VarData2</div>
	<div id="r2VarData3">r2VarData3</div>
	<div id="r2VarData4">r2VarData4</div>
	<div id="r2VarData5">r2VarData5</div>
	<div id="r2VarData6">r2VarData6</div>
	<div id="r2VarData7">r2VarData7</div>
	<div id="r2VarData8">r2VarData8</div>
	<div id="r2VarData9">r2VarData9</div>
</div>




<script type="text/javascript">
function openAtab(evt, cityName) {
    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.className += " active";
} 


if(typeof(EventSource)!=="undefined") {
	var eSource = new EventSource("ClockServlet");
	//detect message receipt
	eSource.onmessage = function(event) {
		//write the received data to the page
		document.getElementById("clockData").innerHTML = event.data;
	};

	var eSource = new EventSource("R1StatVarServlet");
	//detect message receipt
	eSource.onmessage = function(event) {
		//Split the data received
		var sed = event.data.split("@", 9);
		
		document.getElementById("r1VarData1").innerHTML = sed[0];
		document.getElementById("r1VarData2").innerHTML = sed[1];
		document.getElementById("r1VarData3").innerHTML = sed[2];
		document.getElementById("r1VarData4").innerHTML = sed[3];
		document.getElementById("r1VarData5").innerHTML = sed[4];
		document.getElementById("r1VarData6").innerHTML = sed[5];
		document.getElementById("r1VarData7").innerHTML = sed[6];
		document.getElementById("r1VarData8").innerHTML = sed[7];
		document.getElementById("r1VarData9").innerHTML = sed[8];
	};

	var eSource = new EventSource("R2StatVarServlet");
	//detect message receipt
	eSource.onmessage = function(event) {
		//Split the data received
		var sed = event.data.split("@", 9);
		
		document.getElementById("r2VarData1").innerHTML = sed[0];
		document.getElementById("r2VarData2").innerHTML = sed[1];
		document.getElementById("r2VarData3").innerHTML = sed[2];
		document.getElementById("r2VarData4").innerHTML = sed[3];
		document.getElementById("r2VarData5").innerHTML = sed[4];
		document.getElementById("r2VarData6").innerHTML = sed[5];
		document.getElementById("r2VarData7").innerHTML = sed[6];
		document.getElementById("r2VarData8").innerHTML = sed[7];
		document.getElementById("r2VarData9").innerHTML = sed[8];
	};
	
	//create an object, passing it the name and location of the server side script
	var eSource = new EventSource("R1JointPoseServlet");
	//detect message receipt
	eSource.onmessage = function(event) {
		//write the received data to the page
		document.getElementById("r1jaData").innerHTML = event.data;
	};
	
	//create an object, passing it the name and location of the server side script
	var eSource = new EventSource("R2JointPoseServlet");
	//detect message receipt
	eSource.onmessage = function(event) {
		//write the received data to the page
		document.getElementById("r2jaData").innerHTML = event.data;
	};
	
	//create an object, passing it the name and location of the server side script
	var eSource = new EventSource("CartPosServlet");
	//detect message receipt
	eSource.onmessage = function(event) {
		var sed = event.data.split("@", 2);
		document.getElementById("r1CartData").innerHTML = sed[0];
		document.getElementById("r2CartData").innerHTML = sed[1];
	};
}
else {
	document.getElementById("clockData").innerHTML="Whoops! Your browser doesn't receive server-sent events.";
}
</script>
</body>
</html>
