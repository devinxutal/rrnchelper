package rrnchelper.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

public class WebControl implements Serializable {
	private String currentContent;
	private String domain;
	private String charset = "UTF-8";

	public WebControl() {
	}

	public WebControl(String domain) {
		this.domain = domain;
	}

	public boolean doGet(String url) {
		currentContent = getWebContent(url, charset);
		return true;
	}

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

	public String getWebContent(String urlstr, String charset) {
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

	public String getMatchStr(String pattern, String source) {
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
}
