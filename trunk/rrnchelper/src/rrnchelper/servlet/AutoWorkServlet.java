package rrnchelper.servlet;

import java.io.IOException;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rrnchelper.model.User;

public class AutoWorkServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cache cache = CacheManager.getInstance().getCache("cache");
		if (cache==null) System.out.println("I can't find the cache!");
		if (cache != null) {
			User I = (User) cache.get("I");
			if (I != null && I.isAutoWork()) {
				I.gotoMyFarm();
				I.checkEveryType();
			}
		}
	}
}
