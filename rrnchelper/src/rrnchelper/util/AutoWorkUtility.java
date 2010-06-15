package rrnchelper.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.Transaction;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;

import rrnchelper.db.dao.UserDao;
import rrnchelper.model.Crop;
import rrnchelper.model.Event;
import rrnchelper.model.Log;
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
			checkReapResult();
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
				System.out.println("Error Occured: " + e.getClass().getName());
			} finally {
				if (tx.isActive()) {
					tx.rollback();
					System.out.println("Transaction Rollback");
				}
			}
		}
	}

	//////////////////////////////////////////////////////
	//Feed
	/////////////////////////////////////////////////////
	public void feedFriend(Link friendLink) {
		feedAnimalForFriend(friendLink);
		feedMachineForFriend(friendLink);
	}
	
	private void feedAnimalForFriend(Link friendLink) {
		if (friendLink.go() && webControl.goByLinkName("【畜牧】★")) {
			List<Link> links = new LinkedList<Link>();
			do {
				links.addAll(webControl.getLinksByPartialName("喂食"));
			} while (webControl.goByLinkName("下一页"));
			
			//start feed animals;
			for(Link l : links){
				feedByFeedLink(l);
			}
		}
	}
	
	
	private void feedMachineForFriend(Link friendLink) {
		if (friendLink.go() && webControl.goByLinkName("【机械】★")) {
			List<Link> links = new LinkedList<Link>();
			do {
				links.addAll(webControl.getLinksByPartialName("添加"));
			} while (webControl.goByLinkName("下一页"));
			
			//start feed animals;
			for(Link l : links){
				feedByFeedLink(l);
			}
		}
	}
	
	private void feedByFeedLink(Link feedLink){
		feedLink.go();
		String addr = CropUtility.constructFeedAddress(webControl);
		System.out.println("Feed Address: "+ addr);
		webControl.go(addr);
		checkFeedResult(feedLink.getName());
	}
	
	
	private void checkFeedResult(String location) {
		Parser parser = Parser.createParser(webControl.getCurrentContent(),
				webControl.getCharset());
		try {
			NodeList nodeList = parser.parse(new HasAttributeFilter("class",
					"farm_white orange"));
			for (Node node : nodeList.toNodeArray()) {
				if (node.getChildren().size() >= 1
						&& node.getChildren().elementAt(0) instanceof TextNode) {
					String content = ((TextNode)node.getChildren().elementAt(0)).getText();
					if (content.contains("成功")) {
						LoggingUtility.logging(user, LogType.Feed,"在" + location + "喂食或添加原料成功，具体消息： "
								+ content);
						
					} else {
						LoggingUtility.logging(user, LogType.Feed,"在" + location + "喂食或添加原料失败，具体消息： "
								+ content);
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//////////////////////////////////////////////////////
	//Steal
	/////////////////////////////////////////////////////
	public void stealFriend(Link friendLink) {
		friendLink.go();
		List<Link> links = new LinkedList<Link>();
		for (Product product : user.getMyFarm().getProducts()) {
			Link link = webControl
					.getLinkByName("【" + product.getType() + "】★");
			if (link != null) {
				links.add(link);
			}
		}
		for (Link link : links) {
			stealFriendOnThisLink(friendLink.getName(), link);
		}
	}

	private void stealFriendOnThisLink(String name, Link link) {
		if (link.go()) {
			do {
				Link reapLink = webControl.getLinkByName("采摘");
				if (reapLink != null && reapLink.go()) {
					checkStealResult(name);
					while ((reapLink = webControl.getLinkByName("采摘")) != null
							&& reapLink.go()) {
						checkStealResult(name);
					}
					break;
				}
			} while (webControl.goByLinkName("下一页"));
		}
	}

	private void checkStealResult(String location) {
		Parser parser = Parser.createParser(webControl.getCurrentContent(),
				webControl.getCharset());
		try {
			NodeList nodeList = parser.parse(new HasAttributeFilter("class",
					"farm_white orange"));
			for (Node node : nodeList.toNodeArray()) {
				if (node.getChildren().size() >= 1
						&& node.getChildren().elementAt(0) instanceof TextNode) {
					String content = ((TextNode)node.getChildren().elementAt(0)).getText();
					if (content.contains("采摘成功")) {
						LoggingUtility.logging(user, LogType.Steal,"在" + location + "偷菜成功，具体消息： "
								+ content);
						
					} else {
						LoggingUtility.logging(user, LogType.Steal,"在" + location + "偷菜失败，具体消息： "
								+ content);

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkReapResult() {
		Parser parser = Parser.createParser(webControl.getCurrentContent(),
				webControl.getCharset());
		try {
			NodeList nodeList = parser.parse(new HasAttributeFilter("class",
					"farm_white orange"));
			for (Node node : nodeList.toNodeArray()) {
				if (node.getChildren().size() >= 1
						&& node.getChildren().elementAt(0) instanceof TextNode) {
					String content = ((TextNode)node.getChildren().elementAt(0)).getText();
					if (content.contains("成功")) {

						LoggingUtility.logging(user, LogType.Farm, "收获成功，具体消息： "+content);
						
					} else {
						LoggingUtility.logging(user, LogType.Farm, "收获失败，具体消息： "+content);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		Link link = new Link(webControl, event.getDescription(), event
				.getFriendUrl());
		// 更新此好友的偷菜事件
		this.refreshFriendStealEvent(link);
		// 更新此好友的定期更新事件
		this.refreshFriendUpdateEvent(link);
	}

	
	public void stealZYP() {
		Link link = new Link(
				this.webControl,
				"张云鹏的农场",
				"http://mapps.renren.com/rr_farm/farm/action/wap,friendsFarmAction.php?fid=228842897&sid="+user.getSid());
		this.stealFriend(link);
	}
	

	public void feedComicLee() {
		Link link = new Link(
				this.webControl,
				"陈励的农场",
				"http://mapps.renren.com/rr_farm/farm/action/wap,friendsFarmAction.php?fid=47366&sid="+user.getSid());
		System.out.println(link.getFullUrl());
		this.feedFriend(link);
	}
	

	public void feedDevin() {
		Link link = new Link(
				this.webControl,
				"徐寅斐的农场",
				"http://mapps.renren.com/rr_farm/farm/action/wap,friendsFarmAction.php?fid=221142194&sid="+user.getSid());
		this.feedFriend(link);
	}
}
