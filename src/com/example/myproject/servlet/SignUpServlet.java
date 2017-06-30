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
		System.out.println("signupget");
		if (session != null  && session.getAttribute("sessionEmail") != null) {
			resp.sendRedirect("dashboard");
		} else {
			resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			req.getRequestDispatcher("/WEB-INF/signup.html").include(req, resp);
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

		System.out.println("signuppost");
		if (name.length() > 0 && password.length() > 0 && email.length() > 5) {
			ContactService dbConnect = new ContactService();
			Entity user = dbConnect.signUpUser(name, password, email);
			if (user == null) {
				req.getRequestDispatcher("/WEB-INF/signup.html").include(req, resp);
			} else {
				HttpSession session = req.getSession();
				session.setAttribute("sessionEmail", email);
				resp.sendRedirect("/dashboard");
			}
		} else
			req.getRequestDispatcher("/WEB-INF/signup.html").include(req, resp);

	}

}