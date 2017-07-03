package com.example.myproject.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.myproject.services.ContactService;
import com.example.myproject.services.HttpConnectionToURL;
import com.example.myproject.services.Mapper;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class InfoServiceServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// System.out.println("get");
		String code = req.getParameter("code");
		// System.out.println(code);
		URL url = new URL("https://www.googleapis.com/oauth2/v4/token");
		HttpURLConnection httpURLConnection = null;
		try {
			String urlParameters = "code=" + code
					+ "&client_id=786517267222-745ihdgu1r2so8bukav088jqkcs7n6ll.apps.googleusercontent.com&client_secret=-NsbkEDXqGqBUkRSdUDuqQcY&redirect_uri=http://localhost:8888/infoservice&grant_type=authorization_code";
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpURLConnection.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			Map<String, Object> jsonInformation = new HashMap<String, Object>();
			Mapper mapper = new Mapper();
			jsonInformation = mapper.stringToMap(response.toString());

			String urlForApiInformation = "https://www.googleapis.com/oauth2/v1/userinfo?access_token="
					+ jsonInformation.get("access_token");
			url = new URL(urlForApiInformation);
			HttpConnectionToURL httpConnection = new HttpConnectionToURL();
			String JsonUserInfo = httpConnection.fetchUserInformation(url);

			jsonInformation = mapper.stringToMap(JsonUserInfo);

			ContactService contactService = new ContactService();
			Entity createdUser = contactService.addGoogleUser((String) jsonInformation.get("email"),
					(String) jsonInformation.get("name"));

			HttpSession session = req.getSession();
			session.setAttribute("sessionEmail", createdUser.getProperty("email"));
			resp.sendRedirect("/login");

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}
