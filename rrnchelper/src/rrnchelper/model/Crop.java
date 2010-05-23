package rrnchelper.model;

import java.util.Date;

public class Crop {
	private String type;
	private String name;
	private String Status;
	private int quantity;
	private boolean reapable;
	private String reapUrl;
	private Date ripeTime;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Date getRipeTime() {
		return ripeTime;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean isReapable() {
		return reapable;
	}

	public void setReapable(boolean ripe) {
		this.reapable = ripe;
	}

	public String getReapUrl() {
		return reapUrl;
	}

	public void setReapUrl(String reapUrl) {
		this.reapUrl = reapUrl;
	}

	public Date isRipeTime() {
		return ripeTime;
	}

	public void setRipeTime(Date ripeTime) {
		this.ripeTime = ripeTime;
	}

}
