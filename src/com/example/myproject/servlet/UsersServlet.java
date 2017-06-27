package com.example.myproject.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.myproject.services.ContactService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.QueryResultList;

@SuppressWarnings("serial")
public class UsersServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int limit = Integer.parseInt(req.getParameter("limit"));
		String startCursor = req.getParameter("cursor");

		ContactService contactService = new ContactService();
		QueryResultList<Entity> queryResults = contactService.fetchUserInformationWithLimit(limit, startCursor);
		
		
		if (queryResults != null) {
			for (Entity entity : queryResults) {
				resp.getWriter().println(entity.getProperty("UserName") + "\t" + entity.getProperty("Name") + "\t"
						+ entity.getProperty("Email") + "<br>");
			}

			String cursorString = queryResults.getCursor().toWebSafeString();
			resp.getWriter().println("<a href='/user?limit=" + limit + "&cursor=" + cursorString + "'>Next</a>");
		} else
			resp.getWriter().println("sorry don't miss use it");
		resp.getWriter().println("</pre></html>");
		

	}
}
