package com.example.myproject.apiservices;

import java.io.IOException;
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
		System.out.println("update method in user servlet");
		/*HttpSession session = req.getSession(false);
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		if (session != null && session.getAttribute("sessionEmail") != null) {
			Mapper mapper = new Mapper();
			Map<String, Object> bodyMessage = mapper.stringToMap(getBody(req));
			String emailId = (String)bodyMessage.get("email");
			String name = (String)bodyMessage.get("newName");
			ContactService userInfoDelete = new ContactService();
			Entity updatedUserInfo = userInfoDelete.updateUserName(emailId,name);
			if(updatedUserInfo == null)
				System.out.println("No User Found");
			else
				System.out.println("User Name changed");
			resp.sendRedirect("/dashboard");
		} else {
			req.getRequestDispatcher("/WEB-INF/login.html").include(req, resp);
		}*/
	}
	

	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		HttpSession session = req.getSession(false);
		Mapper mapper = new Mapper();
		resp.setContentType("application/Json");
		Map<String,String> responseMessage = new LinkedHashMap<String,String>();
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		if (session != null && session.getAttribute("sessionEmail") != null) {
			String emailId = req.getPathInfo().substring(1);
			System.out.println("delete method called "+emailId);
			ContactService userInfoDelete = new ContactService();
			if(userInfoDelete.deleteUser(emailId).equals("Success"))
			{
				responseMessage.put("Operation","Success");				
			}
			else{
				responseMessage.put("Operation","Failure");
				responseMessage.put("Message", "Emailid doesn't exist");
			}
			//resp.getWriter().println(mapper.objectToJson(responseMessage));
			
		} else {
			//req.getRequestDispatcher("/WEB-INF/login.html").include(req, resp);
			responseMessage.put("Operation", "Failure");
			responseMessage.put("Message", "Authentication required");
			//resp.getWriter().println(mapper.objectToJson(responseMessage));
		}
		resp.getWriter().println(mapper.objectToJson(responseMessage));
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
