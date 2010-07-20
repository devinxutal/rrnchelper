package rrnchelper.util;

import rrnchelper.model.Log;
import rrnchelper.model.User;

public class LoggingUtility {
	public static void logging(User user, LogType type, String content) {
		Log log = new Log(type.toString(), content);
		//user.getLogs().add(log);
		System.out.println("["+log.getTime() + "] "+log.getContent());
	}
}
