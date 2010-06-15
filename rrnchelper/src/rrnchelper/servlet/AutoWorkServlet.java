package rrnchelper.servlet;

import java.io.IOException;
import java.util.List;

import javax.jdo.Transaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rrnchelper.db.dao.UserDao;
import rrnchelper.model.User;
import rrnchelper.util.AutoWorkUtility;

public class AutoWorkServlet extends HttpServlet {
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
			System.out.println("开始处理用户"+user.getUsername());
			AutoWorkUtility utility = new AutoWorkUtility();
			utility.setUser(user);
			// utility.checkEvent();
			utility.gotoFarm();
			utility.checkEveryType();
			UserDao.saveOrUpdateUser(user);
		}else{
			System.out.println("没有找到用户"+user.getUsername());
		}
		UserDao.closePersistenceManager();
	}
}
