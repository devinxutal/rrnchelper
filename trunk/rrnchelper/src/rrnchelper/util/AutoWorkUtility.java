package rrnchelper.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.Transaction;

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
	private int eventProcessCountPerRequest = 2;

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
					.getLinkByName("【" + product.getType() + "】★");
			if (link != null && link.go()) {
				reapAllProducts(product);
			}
			gotoFarm();
		}
	}

	public void reapAllProducts(Product product) {
		Link link = webControl.getLinkByName(product.getReapAction());
		if (link != null && link.go()) {
			LoggingUtility.logging(user, LogType.Farm, "成功收获"
					+ product.getType() + "中的作物");
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
				if (eventsToBeProcessed.size() == this.eventProcessCountPerRequest) {
					break;
				}
			}
		}

		for (Event event : eventsToBeProcessed) {
			Transaction tx = UserDao.getPersistenceManager()
					.currentTransaction();
			try {
				tx.begin();
				user.getEvents().remove(event);
				if (event.getEventType().equals(
						EventType.UpdateFriendEvent.toString())) {
					processFriendUpdateEvent(event);
				} else if (event.getEventType().equals(
						EventType.StealEvent.toString())) {
					processStealEvent(event);
				}
				tx.commit();
			} catch (Exception e) {
				System.out.println("Error Occured: "
						+ e.getClass().getName());
			}finally{
				if(tx.isActive()){
					tx.rollback();
					System.out.println("Transaction Rollback");
				}
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
		if (webControl.goByLinkName(StringUtility.makeStaredLinkName("好友农场"))) {
			List<Link> links = new LinkedList<Link>();
			// 获得所有好友链接
			do {
				links.addAll(webControl.getLinksByPartialName("的农场"));
			} while (webControl.goByLinkName("下一页"));
			// 处理每个好友
			for (Link link : links) {
				refreshFriendStealEvent(link);
				UserDao.saveOrUpdateUser(user);
			}
		}
	}

	private void refreshFriendStealEvent(Link friendlink) {
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
					} while (webControl.goByLinkName("下一页"));
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
		// 更新Steal事件
		if (tempCrop != null) {
			event.setTime(tempCrop.getRipeTime());
			// 删除已经存在的事件
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
			// 添加现在的
			user.getEvents().add(event);
		}
	}

	public void regenerateFriendUpdateEvents() {
		gotoFarm();
		if (webControl.goByLinkName(StringUtility.makeStaredLinkName("好友农场"))) {
			List<Link> links = new LinkedList<Link>();
			// 获得所有好友链接
			do {
				links.addAll(webControl.getLinksByPartialName("的农场"));
			} while (webControl.goByLinkName("下一页"));
			// 删除已有的更新事件
			LinkedList<Event> eventsToRemove = new LinkedList<Event>();
			for (Event e : user.getEvents()) {
				if (e.getEventType().equals(EventType.UpdateFriendEvent)) {
					eventsToRemove.add(e);
				}
			}
			user.getEvents().removeAll(eventsToRemove);
			// 处理每个好友
			int index = 0;
			for (Link link : links) {
				Event event = new Event();
				event.setEventType(EventType.UpdateFriendEvent.toString());
				event.setDescription(link.getName());
				event
						.setTime(new Date(new Date().getTime() + 60 * 1000
								* index));
				event.setUrl(link.getFullUrl());
				event.setFriendUrl(link.getFullUrl());
				user.getEvents().add(event);
				index++;
			}
		}
	}

	/**
	 * 更新"定期更新该好友偷菜事件"的事件
	 * 
	 * @param friendUrl
	 */
	private void refreshFriendUpdateEvent(Link friendlink) {
		Event updateEvent = new Event();
		updateEvent.setEventType(EventType.UpdateFriendEvent.toString());
		updateEvent.setTime(new Date(new Date().getTime() + 3600 * 1000));
		updateEvent.setFriendUrl(friendlink.getFullUrl());
		updateEvent.setUrl(friendlink.getFullUrl());
		updateEvent.setDescription(friendlink.getName());

		Event exist = null;
		for (Event e : user.getEvents()) {
			if (e.getEventType().equals(EventType.UpdateFriendEvent.toString())
					&& e.getFriendUrl().equals(friendlink.getFullUrl())) {
				exist = e;
			}
		}
		if (exist != null) {
			user.getEvents().remove(exist);
		}
		// 添加现在的
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
		Link link = new Link(webControl, event.getDescription(), event
				.getFriendUrl());
		this.refreshFriendStealEvent(link);
		// 更新此好友的定期更新事件
		refreshFriendUpdateEvent(link);
	}

	private void processStealEvent(Event event) {
		Link link = new Link(webControl, event.getDescription(), event.getFriendUrl());
		// 更新此好友的偷菜事件
		this.refreshFriendStealEvent(link);
		// 更新此好友的定期更新事件
		this.refreshFriendUpdateEvent(link);
	}
}
