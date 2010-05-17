package rrnchelper.model;

import java.io.Serializable;

public class Product implements Serializable {
	private String reapAction;
	private String type;
	public static final String ũ�� = "ũ��";
	public static final String ���� = "����";
	public static final String ���� = "����";
	public static final String ��е = "��е";

	public Product(String type) {
		this.type = type;
		if (ũ��.equals(type)) reapAction = "�ջ�ȫ��ũ��Ʒ";
		if (����.equals(type)) reapAction = "�ջ�ȫ��������Ʒ";
		if (����.equals(type)) reapAction = "�ջ�ȫ��������Ʒ";
		if (��е.equals(type)) reapAction = "�ջ�ȫ����е��Ʒ";
	}
	
	public String getType() {
		return type;
	}

	public String getReapAction() {
		return reapAction;
	}
}
