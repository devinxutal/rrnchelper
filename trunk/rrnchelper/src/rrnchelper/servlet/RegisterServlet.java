package rrnchelper.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rrnchelper.db.dao.UserDao;
import rrnchelper.model.Farm;
import rrnchelper.model.User;

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
	I.setUsername("yourusername");
	I.setFarmAddress("http://mapps.renren.com/rr_farm/farm/action/wap,indexAction.php?sid=7b2707218901e9e9a925c17194b782f94");
	UserDao.saveOrUpdateUser(I);
    }

}
