var cursorStore = [];
var cursorIndex = -1;
function retrieveAllJokes(limit,category,cursor){

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
		url = "/user?limit=" + limit + "&cursor=" + cursor+"&category="+category;
	}
	
	
	httpRequest.open("get", url , true);
	httpRequest.onreadystatechange = function() {
		if (httpRequest.readyState == 4 && httpRequest.status == 200) {
			var idJokesCategory = JSON.parse(httpRequest.responseText);
			
			var arrayJokes = idJokesCategory.jokes;
			var allJokesInformation = arrayJokes.length;
			for(i=0 ; i< allJokesInformation; i++){
				var individualJoke = idJokesCategory.jokes[i];
			}
			
			
			var jokeadded = "<tr><td>"+jokes.id+"</td><td>"+jokes.joke;
			var jokecategories="</td>";
			if( jokes.category.length !=0){
				for(i=0;i<jokes.category.length;i++)
					jokecategories = jokecategories +" "+jokes.category;				
				jokeadded += jokecategories; 
			}
			document.getElementById("displayAddedJoke").innerHTML = jokeadded;
		}
	}
	httpRequest.send(); 
}

function allJokes(){
	var limit = document.getElementById("limit").value;
	var category = document.getElementById("category").value;
	retrivalAllJokes(limit, category, cursorStore[cursorIndex]);
}