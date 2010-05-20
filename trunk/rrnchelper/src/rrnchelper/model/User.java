package rrnchelper.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class User {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
    @Persistent
    private String username;
    @Persistent
    private String password;
    @NotPersistent
    private Farm myFarm;
    @NotPersistent
    private WebControl webControl;
    @Persistent
    public String farmAddress;
    @Persistent
    public boolean autoWork;

    public User() {
	webControl = new WebControl("http://mapps.renren.com");
	autoWork = false;
    }

    public void gotoMyFarm() {
        myFarm = new Farm(farmAddress);
	webControl.doGet(myFarm.getFarmAddress());
    }

    public void checkEveryType() {
	for (Product product : myFarm.getProducts()) {
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

    public boolean isAutoWork() {
	return autoWork;
    }

    public void setAutoWork(boolean autoWork) {
	this.autoWork = autoWork;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFarmAddress() {
        return farmAddress;
    }

    public void setFarmAddress(String farmAddress) {
        this.farmAddress = farmAddress;
    }
}
