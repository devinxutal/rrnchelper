package rrnchelper.util;

public class StringUtility {
	public static String makeStaredLinkName(String content) {
		return "¡¾" + content + "¡¿¡ï";
	}

	public static String makeUnstaredLinkName(String content) {
		return "¡¾" + content + "¡¿";
	}
}
