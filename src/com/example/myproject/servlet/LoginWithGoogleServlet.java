package com.example.myproject.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.utils.SystemProperty;

public class LoginWithGoogleServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4106832312162318634L;

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
			//remote
			String url = "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&access_type=offline&redirect_uri=http://loginwithgoogelid.appspot.com/infoservice&response_type=code&client_id=786517267222-745ihdgu1r2so8bukav088jqkcs7n6ll.apps.googleusercontent.com&prompt=consent";
			resp.sendRedirect(url);
		} else {
			String url = "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.profile+https://www.googleapis.com/auth/userinfo.email&access_type=offline&redirect_uri=http://localhost:8888/infoservice&response_type=code&client_id=786517267222-745ihdgu1r2so8bukav088jqkcs7n6ll.apps.googleusercontent.com&prompt=consent";
			resp.sendRedirect(url);
		}

	}

}
