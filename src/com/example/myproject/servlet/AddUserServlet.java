package com.example.myproject.servlet;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.myproject.pojo.UsersInformation;
import com.example.myproject.services.ContactService;
import com.example.myproject.services.HttpConnectionToURL;
import com.example.myproject.services.Mapper;

@SuppressWarnings("serial")
public class AddUserServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// retriving user information from the url
		// ("http://uinames.com/api/?ext")
		URL url = new URL("http://uinames.com/api/?ext");
		HttpConnectionToURL httpConnectionToURL = new HttpConnectionToURL();
		String userInformationFromJson = httpConnectionToURL.fetchUserInformation(url);

		// converting String to POJO
		Mapper mapper = new Mapper();
		Map<String, Object> mapData = mapper.stringToMap(userInformationFromJson);

		// Assignment to POJO
		UsersInformation userInformation = new UsersInformation();
		userInformation.setName((String) mapData.get("name") + " " + (String) mapData.get("surname"));
		userInformation.setUserName((String) mapData.get("email"));
		userInformation.setEmail((String) mapData.get("email"));
		userInformation.setPassword((String) mapData.get("password"));

		// adding userinformation into the database if not exist
		ContactService addUserInfo = new ContactService();
		addUserInfo.addUser(userInformation);

		// resonse Pojo as json
		resp.setContentType("application/json");
		resp.getWriter().println(mapper.objectToJson(userInformation));

	}
}
