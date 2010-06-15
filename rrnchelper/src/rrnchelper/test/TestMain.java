package rrnchelper.test;

import rrnchelper.model.User;
import rrnchelper.util.AutoWorkUtility;
import rrnchelper.web.Link;
import rrnchelper.web.WebControl;

public class TestMain {
	public static void main(String[] args) {
		User user = new User();
		user
				.setFarmAddress("http://mapps.renren.com/rr_farm/farm/action/wap,indexAction.php?r=_c4193c07c59a&sid=bf3ae9180a92a43ea2c86cc684b443c47");

		WebControl webControl = user.getWebControl();
		user.setSid("bf3ae9180a92a43ea2c86cc684b443c47");
		webControl.setCharset("UTF-8");
		user.setWebControl(webControl);
		AutoWorkUtility utility = new AutoWorkUtility(user);
		utility.feedComicLee();
	}
}
