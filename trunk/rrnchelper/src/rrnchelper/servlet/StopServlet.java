package rrnchelper.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rrnchelper.model.User;

public class StopServlet extends HttpServlet {
	private final Logger log = Logger.getLogger(StopServlet.class.getName());

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cache cache = CacheManager.getInstance().getCache("cache");
		if (cache != null) {
			User I = (User) cache.get("I");
			if (I != null) {
				I.setAutoWork(false);
			}
		}
		response.sendRedirect("/");
	}
}
