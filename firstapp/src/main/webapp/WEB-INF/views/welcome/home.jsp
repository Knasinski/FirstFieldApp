<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
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
<div style="text-align:center">
    <div >
      	<img width="450" alt="AMT Logo" src="https://zv29bqv028dj9ycg-zippykid.netdna-ssl.com/wp-content/uploads/2017/05/amt_logo_color_nav.png">
		<h1>Controllers</h1>
		<p>${meaningLess}</p>
		
    </div>
 </div>
  

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
</body>
</html>
