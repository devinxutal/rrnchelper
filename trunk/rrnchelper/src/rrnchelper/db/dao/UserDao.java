package rrnchelper.db.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import rrnchelper.db.PMF;
import rrnchelper.model.User;

public class UserDao {

	public static boolean saveOrUpdateUser(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(user);
			return true;
		} finally {
			pm.close();
		}
	}

	public static boolean deleteUser(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.deletePersistent(user);
			return true;
		} finally {
			pm.close();
		}
	}

	public static User findUser(Long id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			return (User) pm.getObjectById(id);
		} finally {
			pm.close();
		}
	}

	public static List<User> findByFilter(String filter) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		if (filter.indexOf("=") > -1
				&& filter.indexOf("=") != filter.indexOf("==")) { // 对=做一些处理
			filter = filter.replace("=", "==");
		}
		filter = filter.replace(",", " && ");
		try {
			Query query = pm.newQuery(User.class, filter);
			List<User> users = (List<User>) query.execute();
			users.size(); // 这个步骤用于解决延迟加载问题
			return users;
		} finally {
			pm.close();
		}
	}

	public static List<User> findAll() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery("select from " + User.class.getName());
			List<User> users = (List<User>) query.execute();
			users.size();
			return users;
		} finally {
			pm.close();
		}
	}
}
