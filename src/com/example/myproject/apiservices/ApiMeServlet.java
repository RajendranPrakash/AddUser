package com.example.myproject.apiservices;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.myproject.pojo.Contact;
import com.example.myproject.services.ContactService;
import com.example.myproject.services.Mapper;
import com.google.appengine.api.datastore.Entity;

public class ApiMeServlet extends HttpServlet{

	private static final long serialVersionUID = -6065258711129517376L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = req.getSession(false);
		if(session != null && session.getAttribute("sessionEmail") != null){
			String emailId = (String) session.getAttribute("sessionEmail");
			//System.out.println("email id in API services is " + emailId);
			
			ContactService apiRequest = new ContactService();
			//Entity userInfo = apiRequest.fetchUserInformation(emailId);
			Entity userInfo = apiRequest.checkUser(emailId);
			if(userInfo != null){
				Contact userInformation = new Contact();
				userInformation.setEmail((String) userInfo.getProperty("email"));
				userInformation.setName((String)userInfo.getProperty("name"));
				userInformation.setUserName((String)userInfo.getProperty("userName"));
				Mapper mapper = new Mapper();
				String jsonUserInfo = mapper.objectToJson(userInformation);
				resp.setContentType("application/json");
				resp.getWriter().println(jsonUserInfo);	
			}
			else{
				System.out.println("login first");
			}
		}
		else{
			System.out.println("login first, Authentication required");
		}
	}

}
