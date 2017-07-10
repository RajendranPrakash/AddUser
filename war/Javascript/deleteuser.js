function deleteUserInfo(deleteEmailId){
	if(confirm("Are you sure you want to delete "+deleteEmailId))
	{
		var httpRequest;
		if (window.XMLHttpRequest) {
			httpRequest = new XMLHttpRequest();
		} else {
			httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
		}
		url = "/user/"+deleteEmailId;
		httpRequest.open("DELETE", url , true);
		httpRequest.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
		httpRequest.onreadystatechange = function() {
			if (httpRequest.readyState == 4 && httpRequest.status == 200) {
				var obj = JSON.parse(httpRequest.responseText);
				console.log(obj);
				if(obj.Operation === "Success")
				{
					alert("Successfully deleted");
					presentSetOfUser();
				}
				
			}
		}
		httpRequest.send();
	}
}

/*function updateUserdiv(editUser){
	alert(editUser);
	document.getElementById('editUserName').style.display = "inline";
	document.getElementById('updateUserName').value = "hello";
	document.getElementById('updateUserEmail').value = editUser;
	document.getElementById("updateUserEmail").readOnly = true;
}*/