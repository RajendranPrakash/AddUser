function deleteUserInfo(deleteEmailId){
	if(confirm("Are you sure you want to delete "+deleteEmailId))
	{
		var httpRequest;
		if (window.XMLHttpRequest) {
			httpRequest = new XMLHttpRequest();
		} else {
			httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
		}
		var parameter = {"email":deleteEmailId};
		var parameterAsJson = JSON.stringify(parameter);
		httpRequest.open("DELETE", "/user" , true);
		httpRequest.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
		httpRequest.onreadystatechange = function() {
			if (httpRequest.readyState == 4 && httpRequest.status == 200) {
				var obj = JSON.parse(httpRequest.responseText);
				console.log(obj);
				if(obj.Operation === "Success")
				{
					alert("Successfully deleted");
					document.getElementById("userinfotable").innerHTML = "";
					presentSetOfUser();
				}
			}
		}
		httpRequest.send(parameterAsJson);
	}
}