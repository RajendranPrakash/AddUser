package com.example.myproject.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.myproject.pojo.UsersInformation;
import com.example.myproject.services.ContactService;
import com.example.myproject.services.Mapper;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.QueryResultList;

@SuppressWarnings("serial")
public class UsersServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map<String, Object> jsonResult = new LinkedHashMap<String, Object>();
		try {
			int limit = Integer.parseInt(req.getParameter("limit"));
			String startCursor = req.getParameter("cursor");

			ContactService contactService = new ContactService();
			QueryResultList<Entity> queryResults = contactService.fetchUserInformationWithLimit(limit, startCursor);

			jsonResult.put("ok", true);
			jsonResult.put("msg", "");

			Map<String, Object> jsonDataAndCursor = new LinkedHashMap<String, Object>();

			ArrayList<UsersInformation> entities = new ArrayList<UsersInformation>();
			for (Entity entity : queryResults) {
				UsersInformation userInformation = new UsersInformation();
				userInformation.setEmail((String) entity.getProperty("email"));
				userInformation.setName((String) entity.getProperty("name"));
				// userInformation.setPassword((String)
				// entity.getProperty("password"));
				userInformation.setUserName((String) entity.getProperty("userName"));

				entities.add(userInformation);

			}
			String cursorString = queryResults.getCursor().toWebSafeString();

			jsonDataAndCursor.put("contacts", entities);
			if (entities.size() != 0)
				jsonDataAndCursor.put("cursor", cursorString);

			jsonResult.put("data", jsonDataAndCursor);
		} catch (Exception exceptionObject) {

			jsonResult.put("ok", false);
			jsonResult.put("msg", exceptionObject);
		}

		resp.setContentType("application/json");
		resp.getWriter().println(new Mapper().objectToJson(jsonResult));

	}
}
