package rrnchelper.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rrnchelper.model.Farm;
import rrnchelper.model.User;
import rrnchelper.model.WebControl;

public class StartSevlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		process(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext application = request.getSession().getServletContext();
		User I = (User) application.getAttribute("I");
		if (I == null) {
			I = new User();
			I.setWebControl(new WebControl("http://mapps.renren.com"));
			I.setMyFarm(new Farm());
			application.setAttribute("I", I);
		}
		I.setAutoWork(true);
		response.sendRedirect("/");
	}
}
