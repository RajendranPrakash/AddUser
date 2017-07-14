package com.example.myproject.apiservices;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.myproject.pojo.Jokes;
import com.example.myproject.services.HttpConnectionToURL;
import com.example.myproject.services.Mapper;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

public class ApiServiceForJokeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static {
		ObjectifyService.register(Jokes.class);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String cursorStr = req.getParameter("cursor");
		String limitString = req.getParameter("limit");
		int limit = 10;
		if (limitString != null && limitString.equals("") != true) {
			limit = Integer.parseInt(limitString);
			if (limit < 0) {
				limit = 10;
			}
		}

		Query<Jokes> query = ObjectifyService.ofy().load().type(Jokes.class).limit(limit);
		if (cursorStr != null)
			query = query.startAt(Cursor.fromWebSafeString(cursorStr));

		Map<String, Object> jokesAndCursorJson = new LinkedHashMap<String, Object>();
		ArrayList<Object> jokesAsArrayList = new ArrayList<Object>();
		boolean continu = false;
		QueryResultIterator<Jokes> iterator = query.iterator();
		while (iterator.hasNext()) {
			Jokes joke = iterator.next();
			System.out.println("hte id of car " + joke.getId());
			jokesAsArrayList.add(joke);
			continu = true;
		}
		jokesAndCursorJson.put("Jokes", jokesAsArrayList);

		String cursorString = null;
		if (continu) {
			cursorString = iterator.getCursor().toWebSafeString();
			System.out.println("The cursor " + cursorString);
		}
		if (jokesAsArrayList.size() != 0)
			jokesAndCursorJson.put("cursor", cursorString);

		System.out.println(" data is going to return as json ");
		resp.setContentType("application/json");
		resp.getWriter().println(new Mapper().objectToJson(jokesAndCursorJson));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		URL url = new URL("http://api.icndb.com/jokes/random/");
		HttpConnectionToURL httpConnectionToURL = new HttpConnectionToURL();
		String apiJokeAsJson = httpConnectionToURL.fetchUserInformation(url);

		Mapper mapper = new Mapper();
		Map<String, Object> jokeTypeValue = mapper.stringToMap(apiJokeAsJson);

		System.out.println("the joke type and value " + jokeTypeValue);

		Map<String, Object> valueAsJson = (Map<String, Object>) jokeTypeValue.get("value");

		Object idOfJoke = valueAsJson.get("id");

		Jokes jokeAvailable = ObjectifyService.ofy().load().type(Jokes.class).id((int) idOfJoke).now();
		if (jokeAvailable == null) {

			// Assignment to POJO
			Jokes jokesAlongCategory = new Jokes();
			jokesAlongCategory.setId((int) valueAsJson.get("id"));
			jokesAlongCategory.setJoke((String) valueAsJson.get("joke"));
			ArrayList<String> categoryList = (ArrayList<String>) valueAsJson.get("categories");
			jokesAlongCategory.setCategory(categoryList);

			ObjectifyService.ofy().save().entities(jokesAlongCategory).now();

			System.out.println(" data stored into the datastore ");
			resp.setContentType("application/json");
			resp.getWriter().println(mapper.objectToJson(jokesAlongCategory));
		}

	}

}
