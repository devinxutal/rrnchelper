package rrnchelper.util;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import rrnchelper.model.Crop;
import rrnchelper.web.WebControl;

public class CropUtility {
	public static List<Crop> parseCrops(WebControl webControl) {
		List<Crop> crops = new LinkedList<Crop>();
		Parser parser = Parser.createParser(webControl.getCurrentContent(),
				webControl.getCharset());
		try {
			NodeList nodeList = parser.parse(new HasAttributeFilter("class",
					"farm_white"));
			for (Node node : nodeList.toNodeArray()) {
				Crop crop = parseCrop(webControl, node);
				if (crop != null) {
					crops.add(crop);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return crops;
	}

	private static Crop parseCrop(WebControl webControl, Node node) {
		if (node.getChildren().size() <= 1) {
			return null;
		}
		Crop crop = new Crop();
		int index = 0;
		for (Node n : node.getChildren().toNodeArray()) {
			String[] keyValuePair = null;
			if (n instanceof TextNode
					&& (keyValuePair = parseKeyValuePair(n.getText())) != null) {
				setCrop(crop, keyValuePair[0], keyValuePair[1], index);
			} else if (n instanceof LinkTag) {
				LinkTag ln = (LinkTag) n;
				if (ln.getLinkText().equals("采摘")) {
					crop.setReapUrl(webControl.getDomain() + ln.getLink());
					crop.setReapable(true);
				}

			}
			index++;
		}
		if (crop.getRipeTime() == null) {
			crop
					.setRipeTime(new Date(
							new Date().getTime() + 3600 * 100 * 1000));
		}
		if (crop.isReapable()) {
			crop.setRipeTime(new Date());
		}
		return crop;
	}

	private static String[] parseKeyValuePair(String text) {
		text = text.replaceAll("&nbsp;", "");
		text = text.replaceAll("\\s", "");
		Pattern p = Pattern.compile("【(.+)】(.+)$");
		Matcher m = p.matcher(text);
		if (m.find() && m.groupCount() == 2) {
			for (int i = 1; i <= m.groupCount(); i++) {
				String[] values = new String[2];
				values[0] = m.group(1);
				values[1] = m.group(2);
				return values;
			}
		}
		return null;
	}

	private static void setCrop(Crop crop, String key, String value, int index) {
		if (index == 2) {
			crop.setName(key);
			crop.setStatus(value);
		}
		if (key.equals("完成时间") || key.equals("成熟时间") || key.equals("生产时间")) {
			crop.setRipeTime(parseRipeTime(value));
		}
	}

	private static Date parseRipeTime(String str) {
		Date time = new Date();
		int hour = 0;
		int minute = 0;
		int second = 0;
		Pattern p = Pattern.compile(".*\\D(\\d+)小时.*$");
		Matcher m = p.matcher(str);
		if (m.find()) {
			hour = Integer.parseInt(m.group(1));
		}
		p = Pattern.compile(".*\\D(\\d+)分.*");
		m = p.matcher(str);
		if (m.find()) {

			minute = Integer.parseInt(m.group(1));
		}
		p = Pattern.compile(".*\\D(\\d+)秒.*");
		m = p.matcher(str);
		if (m.find()) {
			second = Integer.parseInt(m.group(1));
		}
		time.setTime(time.getTime() + 1000
				* (3600 * hour + 60 * minute + second));
		return time;
	}
}
