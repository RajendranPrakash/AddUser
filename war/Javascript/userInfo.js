window.onload = userInformation;
function userInformation() {
	var httpRequest;
	if (window.XMLHttpRequest) {
		httpRequest = new XMLHttpRequest();
	} else {
		httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	httpRequest.open("GET", "/api/v1/me", true);
	httpRequest.onreadystatechange = function() {
		if (httpRequest.readyState == 4 && httpRequest.status == 200) {
			var obj = JSON.parse(httpRequest.responseText);
			//alert(obj);
			document.getElementById("name").innerHTML = obj.name;
		}
	}
	httpRequest.send(); 
}