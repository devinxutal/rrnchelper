package rrnchelper.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rrnchelper.model.User;
import rrnchelper.model.UserDao;

public class StopServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	process(request, response);
    }

    private void process(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	User I = UserDao.findAll().get(0);
	I.setAutoWork(false);
	UserDao.saveOrUpdateUser(I);
	response.sendRedirect("/");
    }
}
