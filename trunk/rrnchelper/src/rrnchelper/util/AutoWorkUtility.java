package rrnchelper.util;

import java.util.LinkedList;
import java.util.List;

import rrnchelper.model.Event;
import rrnchelper.model.Product;
import rrnchelper.model.User;
import rrnchelper.web.Link;
import rrnchelper.web.WebControl;

public class AutoWorkUtility {
	private User user;
	private WebControl webControl;

	public AutoWorkUtility() {
		this(null);
	}

	public AutoWorkUtility(User user) {
		this.user = user;
		this.webControl = new WebControl();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// //////////////

	public void gotoFarm() {
		webControl.go(user.getMyFarm().getFarmAddress());
	}

	public void checkEveryType() {
		for (Product product : user.getMyFarm().getProducts()) {
			Link link = webControl
					.getLinkByName("��" + product.getType() + "����");
			if (link != null && link.go()) {
				reapAllProducts(product);
			}
			gotoFarm();
		}
	}

	public void reapAllProducts(Product product) {
		Link link = webControl.getLinkByName(product.getReapAction());
		if (link != null && link.go()) {
			LoggingUtility.logging(user, LogType.Farm, "�ɹ��ջ�"
					+ product.getType() + "�е�����");
		}
	}

	public void feed() {

	}

	public void reapTheProduct() {

	}

	public void refreshEvents() {

	}

	public void refreshAllFriendEvents() {
		gotoFarm();
		if (webControl.goByLinkName(StringUtility.makeStaredLinkName("����ũ��"))) {
			List<Link> links = new LinkedList<Link>();
			// ������к�������
			do {
				links.addAll(webControl.getLinksByPartialName("��ũ��"));
			} while (webControl.goByLinkName("��һҳ"));
			// ����ÿ������
			for (Link link : links) {
				refreshFriendEvent(link);
			}
		}
	}

	private void refreshFriendEvent(Link friendlink) {
		Event event = new Event();
		event.setDescription(friendlink.getName());
		event.setUrl(friendlink.getFullUrl());
		event.setEventType(EventType.Steal.toString());
		for (Product product : user.getMyFarm().getProducts()) {
			if (friendlink.go()) {
				Link link = this.getStaredOrUnStaredLink(product.getType());
				if (link != null && link.go()) {
					do{
						//TODO
					}while(webControl.goByLinkName("��һҳ"));
				}
			}
		}
	}

	private Link getStaredOrUnStaredLink(String content) {
		Link link = webControl.getLinkByName(StringUtility
				.makeStaredLinkName(content));
		if (link == null) {
			link = webControl.getLinkByName(StringUtility
					.makeUnstaredLinkName(content));
		}
		return link;
	}
}
