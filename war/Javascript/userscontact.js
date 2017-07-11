var cursorindex = -1;
var userCursorInfo = [];
window.onload = onloadRetrival;

function onloadRetrival() {
	userInformation();
	retriveUser(document.getElementById("limitid").value);
}

function retriveUser(limit, cursor) {
	var httpRequest;
	if (window.XMLHttpRequest) {
		httpRequest = new XMLHttpRequest();
	} else {
		httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	var url;
	if (cursorindex == -1) {
		url = "/user?limit=" + limit;
	} else {
		url = "/user?limit=" + limit + "&cursor=" + cursor;
	}
	httpRequest.open("GET", url, true);
	httpRequest.onreadystatechange = function() {
		if (httpRequest.readyState == 4 && httpRequest.status == 200) {
			var obj = JSON.parse(httpRequest.responseText);
			var userInfoWithCursor = obj.data;
			var userData = userInfoWithCursor.contacts;
			var numberOfUser = userData.length;
			var htmltable = document.getElementById("userinfotable");
			var addUser = "";
			if (numberOfUser == 0) {
				addUser += "<tr>No Users found</tr>";
				cursorindex = cursorindex + 1;
			} else
				for (var i = 0; i < numberOfUser; i++) {
					addUser += "<tr><td>"
							+ userData[i].name
							+ "</td><td>"
							+ userData[i].email
							+ "</td><td><input type='button' value='Delete' onclick=\"deleteUserInfo('"
							+ userData[i].email
							+ "')\"> <input type='button' value='Edit' onclick=\"updateUserdiv('"
							+ userData[i].name + "','" + userData[i].email
							+ "')\"></td></tr>";
				}
			if (userInfoWithCursor.hasOwnProperty('cursor')) {
				cursorindex = cursorindex + 1;
				userCursorInfo[cursorindex] = userInfoWithCursor.cursor;
				if (cursorindex == 0) {
					if ((numberOfUser + 1) < limit) {
						// alert("json has cursor"++"cursorindex "+cursorindex);
						document.getElementById('nextButton').style.display = "none";
						// document.getElementById('backButton').style.display =
						// "none";
					} else {
						document.getElementById('nextButton').style.display = "inline";
						// document.getElementById('backButton').style.display =
						// "none";
					}
					document.getElementById('backButton').style.display = "none";
				} else {
					if ((numberOfUser + 1) < limit) {
						document.getElementById('nextButton').style.display = "none";
						// document.getElementById('backButton').style.display =
						// "inline";
					} else {
						// alert("json has cursor-- cursorindex "+cursorindex+"
						// numberof data retrived "+(numberOfUser+1));
						document.getElementById('nextButton').style.display = "inline";
						// document.getElementById('backButton').style.display =
						// "inline";
					}
					document.getElementById('backButton').style.display = "inline";
				}
			} else {
				document.getElementById('nextButton').style.display = "none";
				if (cursorindex == 0) {
					// document.getElementById('nextButton').style.display =
					// "none";
					document.getElementById('backButton').style.display = "none";
				} else {
					// document.getElementById('nextButton').style.display =
					// "none";
					document.getElementById('backButton').style.display = "inline";
				}
			}
			document.getElementById("userinfotable").innerHTML = addUser;

		}
	}
	httpRequest.send();
}

function nextSetOfUser() {
	limit = document.getElementById("limitid").value;
	retriveUser(limit, userCursorInfo[cursorindex]);
}

function presentSetOfUser() {
	limit = document.getElementById("limitid").value;
	cursorindex = cursorindex - 1;
	retriveUser(limit, userCursorInfo[cursorindex]);
}

function allUser(limit) {
	userCursorInfo = [];
	cursorindex = -1;
	retriveUser(limit);
}

function previousSetOfUser() {
	// alert("previous button");
	cursorindex = cursorindex - 2;
	retriveUser(limit, userCursorInfo[cursorindex]);
	/*
	 * if(cursorindex == -1){ retriveUser(limit); } else{
	 * retriveUser(limit,userCursorInfo[cursorindex]); }
	 */
}