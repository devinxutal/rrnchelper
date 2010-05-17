package rrnchelper.model;

import java.io.Serializable;

public class Product implements Serializable {
	private String reapAction;
	private String type;
	public static final String 农田 = "农田";
	public static final String 果树 = "果树";
	public static final String 畜牧 = "畜牧";
	public static final String 机械 = "机械";

	public Product(String type) {
		this.type = type;
		if (农田.equals(type)) reapAction = "收获全部农产品";
		if (果树.equals(type)) reapAction = "收获全部果树产品";
		if (畜牧.equals(type)) reapAction = "收获全部畜牧产品";
		if (机械.equals(type)) reapAction = "收获全部机械产品";
	}
	
	public String getType() {
		return type;
	}

	public String getReapAction() {
		return reapAction;
	}
}
