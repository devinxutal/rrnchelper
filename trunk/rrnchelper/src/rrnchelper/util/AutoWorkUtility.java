package rrnchelper.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import rrnchelper.db.dao.UserDao;
import rrnchelper.model.Crop;
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
		if (user != null) {
			this.webControl = user.getWebControl();
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		this.webControl = user.getWebControl();
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

	public void checkEvent() {
		List<Event> eventsToBeProcessed = new LinkedList<Event>();
		for (Event event : user.getEvents()) {
			if (event.getTime().before(new Date())) {
				eventsToBeProcessed.add(event);
			}
		}

		for (Event event : eventsToBeProcessed) {
			user.getEvents().remove(event);
			if (event.getEventType().equals(
					EventType.UpdateFriendEvent.toString())) {
				processFriendUpdateEvent(event);
			} else if (event.getEventType().equals(
					EventType.StealEvent.toString())) {
				processStealEvent(event);
			}
		}
	}

	public void stealFriend(Link friendLink) {

	}

	public void refreshEvents() {
		refreshAllFriendEvents();
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
				UserDao.saveOrUpdateUser(user);
			}
		}
	}

	private void refreshFriendEvent(Link friendlink) {
		Event event = new Event();
		event.setDescription(friendlink.getName());
		event.setUrl(friendlink.getFullUrl());
		event.setFriendUrl(friendlink.getFullUrl());
		event.setEventType(EventType.StealEvent.toString());
		List<Crop> crops = new LinkedList<Crop>();
		for (Product product : user.getMyFarm().getProducts()) {
			if (friendlink.go()) {
				Link link = this.getStaredOrUnStaredLink(product.getType());
				if (link != null && link.go()) {
					do {
						crops.addAll(CropUtility.parseCrops(webControl));
					} while (webControl.goByLinkName("��һҳ"));
				}
			}
		}
		Crop tempCrop = null;
		for (Crop crop : crops) {
			if (tempCrop == null) {
				tempCrop = crop;
			} else {
				if (tempCrop.getRipeTime().after(crop.getRipeTime())) {
					tempCrop = crop;
				}
			}
		}
		if (tempCrop != null) {
			event.setTime(tempCrop.getRipeTime());
			// ɾ���Ѿ����ڵ��¼�
			Event exist = null;
			for (Event e : user.getEvents()) {
				if (e.getEventType().equals(EventType.StealEvent.toString())
						&& e.getFriendUrl().equals(event.getFriendUrl())) {
					exist = e;
				}
			}
			if (exist != null) {
				user.getEvents().remove(exist);
			}
			// ������ڵ�
			user.getEvents().add(event);
		}

	}

	/**
	 * ����"���ڸ��¸ú���͵���¼�"���¼�
	 * 
	 * @param friendUrl
	 */
	private void refreshFriendUpdateEvent(String friendUrl) {
		Event updateEvent = new Event();
		updateEvent.setEventType(EventType.UpdateFriendEvent.toString());
		updateEvent.setTime(new Date(new Date().getTime() + 3600 * 1000));
		updateEvent.setFriendUrl(friendUrl);
		updateEvent.setUrl(friendUrl);

		Event exist = null;
		for (Event e : user.getEvents()) {
			if (e.getEventType().equals(EventType.UpdateFriendEvent.toString())
					&& e.getFriendUrl().equals(friendUrl)) {
				exist = e;
			}
		}
		if (exist != null) {
			user.getEvents().remove(exist);
		}
		// ������ڵ�
		user.getEvents().add(updateEvent);
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

	private void processFriendUpdateEvent(Event event) {
		Link link = new Link(webControl, "friend link", event.getFriendUrl());
		this.refreshFriendEvent(link);
	}

	private void processStealEvent(Event event) {
		Link link = new Link(webControl, "friend link", event.getFriendUrl());

		// ���´˺��ѵ�͵���¼�
		this.refreshFriendEvent(link);
		// ���´˺��ѵĶ��ڸ����¼�
		this.refreshFriendUpdateEvent(event.getFriendUrl());
	}
}
