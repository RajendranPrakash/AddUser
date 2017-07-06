var cursor = null;

function retriveUser(limit) {
	//alert("the value selected is "+limit);
	var httpRequest;
	if (window.XMLHttpRequest) {
		httpRequest = new XMLHttpRequest();
	} else {
		httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	var url;
	if(cursor == null)
		url = "/user?limit="+limit;
	else{
		url = "/user?limit="+limit+"&cursor="+cursor;
	}
	httpRequest.open("GET", url, true);
	httpRequest.onreadystatechange = function() {
		if (httpRequest.readyState == 4 && httpRequest.status == 200) {
			var obj = JSON.parse(httpRequest.responseText);
			var userInfoWithCursor = obj.data; 
			var userData = userInfoWithCursor.contacts;
			var numberOfUser = userData.length;
			var htmltable = document.getElementById("userinfotable");
			var addUser ="";
			for(var i = 0 ; i< numberOfUser; i++)
				addUser += "<tr><td>"+userData[i].name+"</td><td>"+userData[i].email+"</td><td>option</td></tr>";
			document.getElementById("userinfotable").innerHTML = addUser;
			if(userInfoWithCursor.hasOwnProperty('cursor')){
				cursor = userInfoWithCursor.cursor;
				
				//document.getElementById("cursor").value = cursor;
				document.getElementById('nextButton').style.display = "inline";
			}
			else{
				document.getElementById('nextButton').style.display = "none";
			}
		}
	}
	httpRequest.send();
}

function nextSetOfUser(){
	limit = document.getElementById("limitid").value;
	retriveUser(limit,cursor);
}

function allUser(limit) {
	limit = document.getElementById("limitid").value;
	cursor = null;
	retriveUser(limit,cursor);
}