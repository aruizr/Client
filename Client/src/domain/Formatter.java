package domain;

public class Formatter {
	
	public static final String RED = "#D17575";
	public static final String BROWN = "#D1A375";
	public static final String YELLOW = "#D1D075";
	public static final String GREEN = "#85D175";
	public static final String CYAN = "#75D1C2";
	public static final String BLUE = "#7580D1";
	public static final String PURPLE = "#9475D1";
	public static final String PINK = "#D075D1";
	public static final String GREY = "#808080";
	public static final String LIGHT_BLACK = "#2B2B2B";

	public static String italic(String text) {
		return tag("i")+text+closeTag("i");
	}
	
	public static String bold(String text) {
		return tag("b")+text+closeTag("b");
	}
	
	public static String lineBreak() {
		return tag("br");
	}
	
	public static String colorText(String color, String text) {
		return tag("span style=\"color:"+color+";\"")+text+closeTag("span");
	}
	
	public static String space(int amount) {
		String s = "";
		for (int i = 0; i < amount; i++) {
			s += "&nbsp;";
		}
		return s;
	}
	
	private static String tag(String tagName) {
		return "<"+tagName+">";
	}
	
	private static String closeTag(String tagName) {
		return "</"+tagName+">";
	}
}
