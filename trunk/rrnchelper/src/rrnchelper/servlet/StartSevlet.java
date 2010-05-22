package rrnchelper.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rrnchelper.db.dao.UserDao;
import rrnchelper.model.Log;
import rrnchelper.model.User;
import rrnchelper.util.LogType;
import rrnchelper.util.LoggingUtility;

public class StartSevlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		process(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = UserDao.findAll().get(0);
		user.setAutoWork(true);
		LoggingUtility.logging(user, LogType.System, "自动收菜程序已经启动");
		UserDao.saveOrUpdateUser(user);
		response.sendRedirect("/");
	}
}
