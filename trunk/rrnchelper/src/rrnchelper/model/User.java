package rrnchelper.model;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import rrnchelper.web.WebControl;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class User {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
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
	@Persistent(mappedBy = "user")
	private List<Log> logs;
	@Persistent(mappedBy = "user")
	private List<Event> events;

	public User() {
		webControl = new WebControl("http://mapps.renren.com");
		autoWork = false;
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
		if (myFarm == null) {
			myFarm = new Farm(this.getFarmAddress());
		}
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

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getFarmAddress() {
		return farmAddress;
	}

	public void setFarmAddress(String farmAddress) {
		this.farmAddress = farmAddress;
	}

	public List<Log> getLogs() {
		if (logs == null) {
			logs = new LinkedList<Log>();
		}
		return logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	public List<Event> getEvents() {
		if (events == null) {
			events = new LinkedList<Event>();
		}
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

}
