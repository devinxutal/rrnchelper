package rrnchelper.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rrnchelper.model.Farm;
import rrnchelper.model.User;
import rrnchelper.model.WebControl;

public class StartServlet extends HttpServlet {
	private final Logger log = Logger.getLogger(StartServlet.class.getName()); 

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		process(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try { 
        	Cache cache = CacheManager.getInstance().getCache("cache");
        	if (cache == null) {
        		cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap()); 
        		CacheManager.getInstance().registerCache("cache", cache);
        	}
        	User I = (User) cache.get("I");
    		if (I == null) {
    			I = new User();
    			I.setWebControl(new WebControl("http://mapps.renren.com"));
    			I.setMyFarm(new Farm());
    		}
    		I.setAutoWork(true);
			cache.put("I", I);
        } catch (CacheException e) { 
            log.warning(e.toString());
        }
		response.sendRedirect("/");
	}
}
