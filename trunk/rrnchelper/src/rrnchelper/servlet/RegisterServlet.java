package rrnchelper.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rrnchelper.model.Farm;
import rrnchelper.model.User;
import rrnchelper.model.UserDao;

public class RegisterServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	process(request, response);
    }
    
    public void process(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
	User I = new User();
	I.setUsername("not necessary");
	I.setFarmAddress("Your farm url");
	UserDao.saveOrUpdateUser(I);
    }

}
