package debug;

import model.Farm;
import model.User;
import model.WebControl;

public class Console {

	public static void main(String[] args) {
		User I = new User();
		I.setWebControl(new WebControl("http://mapps.renren.com"));
		I.setMyFarm(new Farm());
		I.gotoMyFarm();
		I.checkEveryType();
	}

}
