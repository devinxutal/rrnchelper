package model;

public class User {
	private String username;
	private String password;
	private Farm myFarm;
	private WebControl webControl;

	public void gotoMyFarm() {
		webControl.doGet(myFarm.getFarmAddress());
	}

	public void checkEveryType() {
		for (Product product:myFarm.getProducts()) {
			if (webControl.doGetByName("¡¾" + product.getType() + "¡¿¡ï")) {
				reapAllProducts(product);
			}
			gotoMyFarm();
		}
	}

	public void reapAllProducts(Product product) {
		webControl.doGetByName(product.getReapAction());
	}

	public void feed() {

	}

	public void reapTheProduct() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public WebControl getWebControl() {
		return webControl;
	}

	public void setWebControl(WebControl webControl) {
		this.webControl = webControl;
	}

	public Farm getMyFarm() {
		return myFarm;
	}

	public void setMyFarm(Farm myFarm) {
		this.myFarm = myFarm;
	}
}
