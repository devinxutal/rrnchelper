package rrnchelper.web;

public class Link {
	private String name;
	private String url;
	private WebControl control;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Link(WebControl control, String name, String url) {
		this.control = control;
		this.name = name;
		this.url = url;
	}

	public String getFullUrl() {
		if (getUrl().startsWith("http")) {
			return getUrl();
		} else {
			return control.getDomain() + getUrl();
		}
	}

	public boolean go() {
		return control.go(this);
	}

}
