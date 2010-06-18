package rrnchelper.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rrnchelper.db.dao.UserDao;
import rrnchelper.model.User;
import rrnchelper.util.LogType;
import rrnchelper.util.LoggingUtility;


public class ViewLogServlet extends HttpServlet {
	

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
		User user = UserDao.getUserByUsername(request.getParameter("username"));
		if (user != null) {
			user.setAutoWork(false);
			LoggingUtility.logging(user, LogType.System, user.getUsername()+":自动收菜程序已经停止");
			UserDao.saveOrUpdateUser(user);
			response.sendRedirect("/");
		}
		UserDao.closePersistenceManager();
	}
}
