package rrnchelper.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rrnchelper.model.User;

public class AutoWorkServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
	
	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext application = request.getSession().getServletContext();
    	User I = (User)application.getAttribute("I");
    	if (I!=null && I.isAutoWork()) {
    		I.gotoMyFarm();
    		I.checkEveryType();
    	}
	}
}
