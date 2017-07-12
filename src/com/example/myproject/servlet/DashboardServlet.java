package com.example.myproject.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class DashboardServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		HttpSession session = req.getSession(false);
		if (session != null && session.getAttribute("sessionEmail") != null) {
			req.getRequestDispatcher("/WEB-INF/welcome.html").forward(req, resp);
			//System.out.println("welcome.html page");
		} else {
			resp.sendRedirect("/login");
		}
	}
}
