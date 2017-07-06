package com.example.myproject.apiservices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.myproject.pojo.Contact;
import com.example.myproject.services.ContactService;
import com.example.myproject.services.Mapper;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.QueryResultList;


public class UsersServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5386139457678750764L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map<String, Object> jsonResult = new LinkedHashMap<String, Object>();
		String startCursor = req.getParameter("cursor");
		String limitString = req.getParameter("limit");
		int limit = 10;
		if (limitString != null && limitString.equals("") != true) {
			limit = Integer.parseInt(limitString);
			if (limit < 0) {
				limit = 10;
			} else if (limit > 100) {
				limit = 100;
			}
		}

		ContactService contactService = new ContactService();
		try {
			QueryResultList<Entity> queryResults = contactService.fetchUserInformationWithLimit(limit, startCursor);

			jsonResult.put("ok", true);
			jsonResult.put("msg", "");

			Map<String, Object> jsonDataAndCursor = new LinkedHashMap<String, Object>();

			ArrayList<Contact> entities = new ArrayList<Contact>();
			for (Entity entity : queryResults) {
				Contact userInformation = new Contact();
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
		//System.out.println("hello in user api");
		resp.setContentType("application/json");
		resp.getWriter().println(new Mapper().objectToJson(jsonResult));

	}
}
