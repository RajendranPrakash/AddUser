function addNewUser(){
	var httpRequest;
	if (window.XMLHttpRequest) {
		httpRequest = new XMLHttpRequest();
	} else {
		httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	httpRequest.open("GET", "/adduser", true);
	httpRequest.onreadystatechange = function() {
		if (httpRequest.readyState == 4 && httpRequest.status == 200) {
			var obj = JSON.parse(httpRequest.responseText);
			alert("User added successfully "+obj.name);
		}
	}
	httpRequest.send(); 
}