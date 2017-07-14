function addJokes(){
	//alert("welcome");
	var httpRequest;
	if (window.XMLHttpRequest) {
		httpRequest = new XMLHttpRequest();
	} else {
		httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
	}
	httpRequest.open("Post", "/api/v1/joke", true);
	httpRequest.onreadystatechange = function() {
		if (httpRequest.readyState == 4 && httpRequest.status == 200) {
			var jokes = JSON.parse(httpRequest.responseText);
			var jokeadded = "Id - "+jokes.id+"<br>Joke -  "+jokes.joke;
			var jokecategories="<br>Categories -";
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