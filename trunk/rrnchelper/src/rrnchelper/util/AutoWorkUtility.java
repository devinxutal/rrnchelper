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
		if (friendLink.go() && webControl.goByLinkName("����������")) {
			List<Link> links = new LinkedList<Link>();
			do {
				links.addAll(webControl.getLinksByPartialName("ιʳ"));
			} while (webControl.goByLinkName("��һҳ"));
			
			//start feed animals;
			for(Link l : links){
				feedByFeedLink(l);
			}
		}
	}
	
	
	private void feedMachineForFriend(Link friendLink) {
		if (friendLink.go() && webControl.goByLinkName("����е����")) {
			List<Link> links = new LinkedList<Link>();
			do {
				links.addAll(webControl.getLinksByPartialName("���"));
			} while (webControl.goByLinkName("��һҳ"));
			
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
					if (content.contains("�ɹ�")) {
						LoggingUtility.logging(user, LogType.Feed,"��" + location + "ιʳ�����ԭ�ϳɹ���������Ϣ�� "
								+ content);
						
					} else {
						LoggingUtility.logging(user, LogType.Feed,"��" + location + "ιʳ�����ԭ��ʧ�ܣ�������Ϣ�� "
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
					.getLinkByName("��" + product.getType() + "����");
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
				Link reapLink = webControl.getLinkByName("��ժ");
				if (reapLink != null && reapLink.go()) {
					checkStealResult(name);
					while ((reapLink = webControl.getLinkByName("��ժ")) != null
							&& reapLink.go()) {
						checkStealResult(name);
					}
					break;
				}
			} while (webControl.goByLinkName("��һҳ"));
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
					if (content.contains("��ժ�ɹ�")) {
						LoggingUtility.logging(user, LogType.Steal,"��" + location + "͵�˳ɹ���������Ϣ�� "
								+ content);
						
					} else {
						LoggingUtility.logging(user, LogType.Steal,"��" + location + "͵��ʧ�ܣ�������Ϣ�� "
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
					if (content.contains("�ɹ�")) {

						LoggingUtility.logging(user, LogType.Farm, "�ջ�ɹ���������Ϣ�� "+content);
						
					} else {
						LoggingUtility.logging(user, LogType.Farm, "�ջ�ʧ�ܣ�������Ϣ�� "+content);
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
		if (webControl.goByLinkName(StringUtility.makeStaredLinkName("����ũ��"))) {
			List<Link> links = new LinkedList<Link>();
			// ������к�������
			do {
				links.addAll(webControl.getLinksByPartialName("��ũ��"));
			} while (webControl.goByLinkName("��һҳ"));
			// ����ÿ������
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
		// ����Steal�¼�
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

	public void regenerateFriendUpdateEvents() {
		gotoFarm();
		if (webControl.goByLinkName(StringUtility.makeStaredLinkName("����ũ��"))) {
			List<Link> links = new LinkedList<Link>();
			// ������к�������
			do {
				links.addAll(webControl.getLinksByPartialName("��ũ��"));
			} while (webControl.goByLinkName("��һҳ"));
			// ɾ�����еĸ����¼�
			LinkedList<Event> eventsToRemove = new LinkedList<Event>();
			for (Event e : user.getEvents()) {
				if (e.getEventType().equals(EventType.UpdateFriendEvent)) {
					eventsToRemove.add(e);
				}
			}
			user.getEvents().removeAll(eventsToRemove);
			// ����ÿ������
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
	 * ����"���ڸ��¸ú���͵���¼�"���¼�
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
		Link link = new Link(webControl, event.getDescription(), event
				.getFriendUrl());
		this.refreshFriendStealEvent(link);
		// ���´˺��ѵĶ��ڸ����¼�
		refreshFriendUpdateEvent(link);
	}

	private void processStealEvent(Event event) {
		Link link = new Link(webControl, event.getDescription(), event
				.getFriendUrl());
		// ���´˺��ѵ�͵���¼�
		this.refreshFriendStealEvent(link);
		// ���´˺��ѵĶ��ڸ����¼�
		this.refreshFriendUpdateEvent(link);
	}

	
	public void stealZYP() {
		Link link = new Link(
				this.webControl,
				"��������ũ��",
				"http://mapps.renren.com/rr_farm/farm/action/wap,friendsFarmAction.php?fid=228842897&sid="+user.getSid());
		this.stealFriend(link);
	}
	

	public void feedComicLee() {
		Link link = new Link(
				this.webControl,
				"������ũ��",
				"http://mapps.renren.com/rr_farm/farm/action/wap,friendsFarmAction.php?fid=47366&sid="+user.getSid());
		System.out.println(link.getFullUrl());
		this.feedFriend(link);
	}
	

	public void feedDevin() {
		Link link = new Link(
				this.webControl,
				"����쳵�ũ��",
				"http://mapps.renren.com/rr_farm/farm/action/wap,friendsFarmAction.php?fid=221142194&sid="+user.getSid());
		this.feedFriend(link);
	}
}
