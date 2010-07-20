package rrnchelper.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rrnchelper.db.dao.UserDao;
import rrnchelper.model.User;
import rrnchelper.util.AutoWorkUtility;

public class AutoFeedServlet extends HttpServlet {
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
		if (user != null && user.isAutoWork()) {
			System.out.println("user "+user.getUsername()+" start feeding");
			AutoWorkUtility utility = new AutoWorkUtility();
			utility.setUser(user);
			// utility.checkEvent();
			utility.gotoFarm();
			if(user.getUsername().equals("suning")){
				utility.feedDevin();
				utility.feedChenyin();
			}else if(user.getUsername().equals("rrnchelper")){
				utility.feedDevin();
				utility.feedComicLee();
				utility.feedSuning();
			}else if(user.getUsername().equals("yinfei")){
				utility.feedDevin();
			}
			UserDao.saveOrUpdateUser(user);
		}else{
			System.out.println("cannot find user "+user.getUsername()+" user is not in autowork mode");
		}
	}
}
