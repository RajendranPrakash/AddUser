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
public class LoginServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		if (email.length() > 0 && password.length() > 0) {
			ContactService dbConnect = new ContactService();
			Entity user = dbConnect.loginUser(email, password);
			resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			if (user != null) {
				HttpSession session = req.getSession();
				session.setAttribute("sessionEmail", email);
				resp.sendRedirect("/dashboard");
			} else {
				req.getRequestDispatcher("/WEB-INF/login.html").include(req, resp);
			}
		} else
			req.getRequestDispatcher("/WEB-INF/login.html").include(req, resp);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		if (session != null && session.getAttribute("sessionEmail") != null) {
			resp.sendRedirect("/dashboard");
		} else {
			req.getRequestDispatcher("/WEB-INF/login.html").include(req, resp);
		}

	}
}
