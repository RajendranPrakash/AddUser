function updateUserName(){
	var httpRequest;
	if (window.XMLHttpRequest) {
		httpRequest = new XMLHttpRequest();
	} else {
		httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	var parameter = {"email":document.getElementById("updateUserEmail").value,"newName":document.getElementById("updateUserName").value};
	var parameterAsJson = JSON.stringify(parameter);
	httpRequest.open("PUT", "/user", true);
	httpRequest.onreadystatechange = function() {
		if (httpRequest.readyState == 4 && httpRequest.status == 200) {
			var obj = JSON.parse(httpRequest.responseText);
			console.log(obj);
			if(obj.Operation === "Success")
			{
				alert("Successfully updated");
				document.getElementById('editUserName').style.display = "none";
				document.getElementById("userinfotable").innerHTML = "";
				presentSetOfUser();
			}
			else{
				alert("Sorry No updation happened");
				document.getElementById('editUserName').style.display = "none";
			}
		}
	}
	httpRequest.send(parameterAsJson); 
}

function updateUserdiv(editName,editUserEmail){
	//alert(editUserEmail);
	document.getElementById('editUserName').style.display = "inline";
	document.getElementById('updateUserName').value = editName;
	document.getElementById('updateUserEmail').value = editUserEmail;
	document.getElementById("updateUserEmail").readOnly = true;
}