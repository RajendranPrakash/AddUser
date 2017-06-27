package com.example.myproject.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import com.example.myproject.pojo.UsersInformation;
import com.example.myproject.services.ContactService;
import com.example.myproject.services.Mapper;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.labs.repackaged.org.json.JSONException;

@SuppressWarnings("serial")
public class UsersServlet extends HttpServlet {

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int limit = Integer.parseInt(req.getParameter("limit"));
		String startCursor = req.getParameter("cursor");

		ContactService contactService = new ContactService();
		Object queryToResults = contactService.fetchUserInformationWithLimit(limit, startCursor);

		ObjectMapper objectmapper = new ObjectMapper();
		ObjectNode jsonOutput = objectmapper.createObjectNode();
		// System.out.println("the result set is "+queryResults);
		if (queryToResults instanceof QueryResultList) {
			QueryResultList<Entity> queryResults = (QueryResultList<Entity>) queryToResults;
			((ObjectNode) jsonOutput).put("ok", true);
			((ObjectNode) jsonOutput).put("msg", "");
			ObjectNode jsonData = objectmapper.createObjectNode();
			ArrayNode entityProperity = objectmapper.createArrayNode();
			for (Entity entity : queryResults) {
				UsersInformation userInformation = new UsersInformation();
				userInformation.setEmail((String) entity.getProperty("email"));
				userInformation.setName((String) entity.getProperty("name"));
				userInformation.setPassword((String) entity.getProperty("password"));
				userInformation.setUserName((String) entity.getProperty("userName"));
				
				Mapper mapper = new Mapper();
				JsonNode jsonObj = new ObjectMapper().readTree(mapper.pojoStringToJson(userInformation).toString());
				((ArrayNode) entityProperity).add(jsonObj);

			}
			String cursorString = queryResults.getCursor().toWebSafeString();
			//System.out.println("the cursor is " + cursorString);
			((ObjectNode) jsonData).put("contacts", entityProperity);
			((ObjectNode) jsonData).put("cursor", cursorString);

			((ObjectNode) jsonOutput).put("data", jsonData);

		} else {
			IllegalArgumentException stringAsException = (IllegalArgumentException) queryToResults;
			((ObjectNode) jsonOutput).put("ok", false);
			((ObjectNode) jsonOutput).put("msg", ""+stringAsException);
		}

		resp.setContentType("application/json");
		resp.getWriter().println(jsonOutput);

	}
}
