package com.example.myproject.apiservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

	public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Map<String, String> responseMessage = new LinkedHashMap<String, String>();
		Mapper mapper = new Mapper();
		resp.setContentType("application/Json");
		System.out.println("update method in user servlet");
		HttpSession session = req.getSession(false);
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

		if (session != null && session.getAttribute("sessionEmail") != null) {
			Map<String, Object> bodyMessage = mapper.stringToMap(getBody(req));
			String emailId = (String) bodyMessage.get("email");
			String name = (String) bodyMessage.get("newName");
			ContactService userInfoDelete = new ContactService();
			if (userInfoDelete.updateUserName(emailId, name).equals("Success")) {
				System.out.println("User Found name renamed");
				responseMessage.put("Operation", "Success");
			} else {
				System.out.println("No User found");
				responseMessage.put("Operation", "Failure");
				responseMessage.put("Message", "Emailid doesn't exist");
			}
			// resp.sendRedirect("/dashboard");
		} else {
			responseMessage.put("Operation", "Failure");
			responseMessage.put("Message", "Authentication required");
			// req.getRequestDispatcher("/WEB-INF/login.html").include(req,
			// resp);
		}

		resp.getWriter().println(mapper.objectToJson(responseMessage));
	}

	public String getBody(HttpServletRequest request) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		return response.toString();
	}

	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Map<String, String> responseMessage = new LinkedHashMap<String, String>();
		Mapper mapper = new Mapper();
		resp.setContentType("application/Json");

		HttpSession session = req.getSession(false);
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		if (session != null && session.getAttribute("sessionEmail") != null) {
			Map<String, Object> bodyMessage = mapper.stringToMap(getBody(req));
			String emailId = (String) bodyMessage.get("email");
			// System.out.println("delete method called to call datastore
			// services for "+emailId);
			ContactService userInfoDelete = new ContactService();
			if (userInfoDelete.deleteUser(emailId).equals("Success")) {
				responseMessage.put("Operation", "Success");
			} else {
				responseMessage.put("Operation", "Failure");
				responseMessage.put("Message", "Emailid doesn't exist");
			}
			// responseMessage.put("Operation",userInfoDelete.deleteUser(emailId));
			// resp.getWriter().println(mapper.objectToJson(responseMessage));

		} else {
			// req.getRequestDispatcher("/WEB-INF/login.html").include(req,
			// resp);
			responseMessage.put("Operation", "Failure");
			responseMessage.put("Message", "Authentication required");
			// resp.getWriter().println(mapper.objectToJson(responseMessage));
		}
		resp.getWriter().println(mapper.objectToJson(responseMessage));
		/*
		 * HttpSession session = req.getSession(false); Mapper mapper = new
		 * Mapper(); resp.setContentType("application/Json"); Map<String,String>
		 * responseMessage = new LinkedHashMap<String,String>();
		 * resp.setHeader("Cache-Control",
		 * "no-cache, no-store, must-revalidate"); if (session != null &&
		 * session.getAttribute("sessionEmail") != null) {
		 * //System.out.println(req.getRequestURL()); String emailId =
		 * req.getPathInfo().substring(1);
		 * System.out.println("delete method called with the url "+req.
		 * getRequestURL());
		 * //System.out.println("delete method called with the path info "+req.
		 * getPathInfo());
		 * System.out.println("the email id received to delete is "+emailId);
		 * //System.out.println("delete method called "+emailId); ContactService
		 * userInfoDelete = new ContactService();
		 * if(userInfoDelete.deleteUser(emailId).equals("Success")) {
		 * responseMessage.put("Operation","Success"); } else{
		 * responseMessage.put("Operation","Failure");
		 * responseMessage.put("Message", "Emailid doesn't exist"); }
		 * //resp.getWriter().println(mapper.objectToJson(responseMessage));
		 * 
		 * } else {
		 * //req.getRequestDispatcher("/WEB-INF/login.html").include(req, resp);
		 * responseMessage.put("Operation", "Failure");
		 * responseMessage.put("Message", "Authentication required");
		 * //resp.getWriter().println(mapper.objectToJson(responseMessage)); }
		 * resp.getWriter().println(mapper.objectToJson(responseMessage));
		 */
	}

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
		// System.out.println("hello in user api");
		resp.setContentType("application/json");
		resp.getWriter().println(new Mapper().objectToJson(jsonResult));

	}
}
