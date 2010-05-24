package rrnchelper.db.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import rrnchelper.db.PMF;
import rrnchelper.model.User;

public class UserDao {
	private static PersistenceManager pm;
	private static Object lockObject = new Object();

	public static PersistenceManager getPersistenceManager() {
		if (pm == null || pm.isClosed()) {
			synchronized (lockObject) {
				if (pm == null || pm.isClosed()) {
					pm = PMF.get().getPersistenceManager();
				}
			}
		}
		return pm;
	}

	public static boolean saveOrUpdateUser(User user) {
		try {
			getPersistenceManager().makePersistent(user);
			return true;
		} finally {
		}
	}

	public static boolean deleteUser(User user) {
		try {
			getPersistenceManager().deletePersistent(user);
			return true;
		} finally {
		}
	}

	public static User findUser(Long id) {
		try {
			return (User) getPersistenceManager().getObjectById(id);
		} finally {
		}
	}

	public static List<User> findByFilter(String filter) {
		if (filter.indexOf("=") > -1
				&& filter.indexOf("=") != filter.indexOf("==")) { // 对=做一些处理
			filter = filter.replace("=", "==");
		}
		filter = filter.replace(",", " && ");
		try {
			Query query = getPersistenceManager().newQuery(User.class, filter);
			List<User> users = (List<User>) query.execute();
			users.size(); // 这个步骤用于解决延迟加载问题
			return users;
		} finally {
		}
	}

	public static List<User> findAll() {
		try {
			Query query = getPersistenceManager().newQuery("select from " + User.class.getName());
			List<User> users = (List<User>) query.execute();
			users.size();
			return users;
		} finally {
		}
	}

	public static void closePersistenceManager() {
		pm.close();
	}
}
