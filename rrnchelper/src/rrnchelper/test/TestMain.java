package rrnchelper.test;

import rrnchelper.model.User;
import rrnchelper.util.AutoWorkUtility;
import rrnchelper.web.Link;
import rrnchelper.web.WebControl;

public class TestMain {
	public static void main(String[] args) {
		User user = new User();
		user
				.setFarmAddress("http://mapps.renren.com/rr_farm/farm/action/wap,indexAction.php?r=_efba0a9bf3d9&sid=7b2707218901e9e9a925c17194b782f94");

		WebControl webControl = user.getWebControl();
		webControl.setCharset("UTF-8");
		webControl
				.go("http://mapps.renren.com/rr_farm/farm/action/wap,friendTreeAction.php?fid=228842897&r=_090b727e3961&sid=7b2707218901e9e9a925c17194b782f94");
		user.setWebControl(webControl);
		AutoWorkUtility utility = new AutoWorkUtility(user);
		Link link = new Link(
				webControl,
				"��۵�ũ��",
				"http://mapps.renren.com/rr_farm/farm/action/wap,friendsFarmAction.php?fid=245686429&r=_8f179c5f0362&sid=7b2707218901e9e9a925c17194b782f94");
		utility.stealFriend(link);
	}
}