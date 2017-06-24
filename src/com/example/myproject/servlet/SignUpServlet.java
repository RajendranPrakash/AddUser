package com.example.myproject.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.myproject.services.ContactService;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class SignUpServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		/*
		 * HttpSession ses = (HttpSession) req.getAttribute("sessionUserName");
		 * System.out.println(ses); System.out.println(session);
		 */
		if (session != null) {
			resp.sendRedirect("dashboard");
		} else {
			resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			req.getRequestDispatcher("/WEB-INF/signup.html").include(req, resp);
		}
		// req.getRequestDispatcher("/WEB-INF/signup.html").forward(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userName = req.getParameter("username");
		String password = req.getParameter("password");
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		if (userName.length() > 0 && password.length() > 0) {
			ContactService dbConnect = new ContactService();
			Entity user = dbConnect.signUpUser(userName, password);
			if (user == null) {
				req.getRequestDispatcher("/WEB-INF/signup.html").include(req, resp);
			} else {
				HttpSession session = req.getSession();
				session.setAttribute("sessionUserName", userName);
				resp.sendRedirect("/dashboard");
			}
		} else
			req.getRequestDispatcher("/WEB-INF/signup.html").include(req, resp);

	}

}