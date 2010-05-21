package rrnchelper.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

public class WebControl {
	private String currentContent;
	private String domain;
	private String charset = "UTF-8";

	public WebControl() {
	}

	public WebControl(String domain) {
		this.domain = domain;
	}

	
	public boolean go(Link link){
		return go(link.getUrl());
	}
	
	public boolean go(String url){
		return doGet(url);
	}
	
	public boolean goByLinkName(String linkName){
		Link link = getLinkByName(linkName);
		if(link != null){
			return go(link);
		}
		return false;
	}
	
	/**
	 * @deprecated use go(String url) instead
	 * @param url
	 * @return
	 */
	public boolean doGet(String url) {
		currentContent = getWebContent(url, charset);
		return true;
	}

	public Link getLinkByName(String name){
		Parser parser = Parser.createParser(currentContent, charset);
		try {
			NodeList linkNodeList = parser.parse(new NodeClassFilter(LinkTag.class));
			LinkTag linkNode = null;
			for (Node node : linkNodeList.toNodeArray()) {
				if (((LinkTag) node).getLinkText().equals(name)) {
					linkNode = (LinkTag) node;
					return newLink(linkNode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Link> getLinksByPartialName(String partialname){
		Parser parser = Parser.createParser(currentContent, charset);
		List<Link> links = new LinkedList<Link>();
		try {
			NodeList linkNodeList = parser.parse(new NodeClassFilter(LinkTag.class));
			LinkTag linkNode = null;
			for (Node node : linkNodeList.toNodeArray()) {
				linkNode = (LinkTag) node;
				if (linkNode.getLinkText().contains(partialname)) {
					
					links.add(newLink(linkNode));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return links;
	}
	
	
	/**
	 * @deprecated
	 * @param name
	 * @return
	 */
	public boolean doGetByName(String name) {
		System.out.println("click " + name);
		Parser parser = Parser.createParser(currentContent, charset);
		try {
			NodeList linkNodeList = parser.parse(new NodeClassFilter(LinkTag.class));
			LinkTag linkNode = null;
			for (Node node : linkNodeList.toNodeArray()) {
				if (((LinkTag) node).getLinkText().equals(name)) {
					linkNode = (LinkTag) node;
					break;
				}
			}
			if (linkNode != null) {
				String url = domain + linkNode.getLink();
				System.out.println("跳转到" + url);
				currentContent = getWebContent(url, charset);
				return true;
			} else {
				System.out.println("无此链接");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private String getWebContent(String urlstr, String charset) {
		StringBuffer sb = new StringBuffer("");
		try {
			URL url = new URL(urlstr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setInstanceFollowRedirects(false);
			con.connect();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s).append("\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String getMatchStr(String pattern, String source) {
		Pattern ptn = Pattern.compile(pattern);
		Matcher m = ptn.matcher(source);
		// System.out.println(source);
		if (m.find()) {
			return m.group();
		} else {
			return null;
		}
	}

	public String getCurrentContent() {
		return currentContent;
	}

	public void setCurrentContent(String currentContent) {
		this.currentContent = currentContent;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	
	
	///////////////////////////////////////
	//private methods//////////////////////
	///////////////////////////////////////
	
	private Link newLink(LinkTag link){
		String name = link.getLinkText();
		String url = link.getLink();
		return new Link(this, name ,url);
	}
}
