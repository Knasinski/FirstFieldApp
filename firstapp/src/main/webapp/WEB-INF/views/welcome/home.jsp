<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
body {font-family: Arial;}

/* Style the tab */
.tab {
    overflow: hidden;
    border: 1px solid #ccc;
    background-color: #104B99;
}

/* Style the buttons inside the tab */
.tab button {
    background-color: inherit;
    float: left;
    border: none;
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
<body>
<div id="wrapper">


<!-- Comment out the date display.
<h1>Hello world!</h1>
<p>The time on the server is ${serverTime}.</p>
-->
<div id="serverData">Here is where the server sent data will appear</div>

<div style="text-align:center">
    <div >
      	<img width="450" alt="AMT Logo" src="https://zv29bqv028dj9ycg-zippykid.netdna-ssl.com/wp-content/uploads/2017/05/amt_logo_color_nav.png">
      	<br>
    </div>
</div>
</div>
 
 <div class="tab">
  <button class="tablinks" onclick="openAtab(event, 'Controllers')">Controllers</button>
  <button class="tablinks" onclick="openAtab(event, 'RobotJoints')">Robot Joints</button>
  <button class="tablinks" onclick="openAtab(event, 'IOs')">IOs</button>
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

<div id="RobotJoints" class="tabcontent">
  <h3>RobotJoints</h3>
  <p>List of RobotJoints</p>
</div> 
<div id="IOs" class="tabcontent">
  <h3>RobotJoints</h3>
  <p>List of IOs</p>
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
	//create an object, passing it the name and location of the server side script
	var eSource = new EventSource("FieldServlet");
	//detect message receipt
	eSource.onmessage = function(event) {
		//write the received data to the page
		document.getElementById("serverData").innerHTML = event.data;
	};
}
else {
	document.getElementById("serverData").innerHTML="Whoops! Your browser doesn't receive server-sent events.";
}
</script>
</body>
</html>
