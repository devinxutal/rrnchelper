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
		List<User> users = UserDao.findByFilter("autoWork = true");
		AutoWorkUtility utility = new AutoWorkUtility();
		for (User user : users) {
			utility.setUser(user);
			//utility.checkEvent();
			utility.gotoFarm();
			utility.checkEveryType();
			UserDao.saveOrUpdateUser(user);
		}
		UserDao.closePersistenceManager();
	}
}
